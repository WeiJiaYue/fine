package com.yunbao.framework.excel;

import com.yunbao.framework.util.StringUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.yunbao.framework.excel.Utils.getCellValue;
import static com.yunbao.framework.excel.Utils.isMergeCell;

/**
 * 标题reader
 */
public abstract class HeaderReader implements Reader {


    protected List<String> headers;

    /**
     * matrix resolver
     */
    protected Resolver matrixResolver;


    public HeaderReader(Resolver matrixResolver) {
        this.matrixResolver = matrixResolver;
    }


    @Override
    public List<String> readHeader(Workbook workbook, int sheetNo) {
        List<String> headers = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(sheetNo);
        Matrix matrix = matrixResolver.resolveMatrix(workbook, sheet);
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() > matrix.getHeaderRange().endRow) {
                break;
            }
            if (row.getRowNum() == matrix.getHeaderRange().startRow) { //从header开始的行解析header
                for (int i = 0; i <= matrix.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (cell.getColumnIndex() < matrix.getFirstCellNum()) {
                        continue;
                    }
                    String value = getCellValue(cell);
                    MergedRegion result = isMergeCell(sheet, row.getRowNum(), cell.getColumnIndex());
                    if (result.merged) {
                        /**
                         * merged=true; startRow=2; endRow=3; startCol=4; endCol=4
                         *
                         * ------------
                         * | 绩效工资  |
                         * ------------
                         * |   金额    |
                         * ------------
                         */
                        if (result.startCol == result.endCol && result.endRow > result.startRow) { //行合并 列没有合并
                            for (int j = result.startRow + 1; j <= result.endRow; j++) {
                                Row row0 = sheet.getRow(j);
                                Cell cell0 = row0.getCell(result.startCol);
                                String value0 = getCellValue(cell0);
                                headers.add(concat(value, value0));
                            }
                        }
                        /**
                         *
                         * merged=true; startRow=2; endRow=2; startCol=14; endCol=15
                         * 1：
                         * -----------
                         * | 绩效工资  |
                         * -----------
                         * |金额|增加额|
                         * -----------
                         * or
                         * 2：
                         * ---------------
                         * |金额 增加额   |
                         * ---------------
                         */
                        if (result.startRow == result.endRow && result.endCol > result.startCol) { //第一行列合并  第二行分开
                            if (StringUtil.isEmpty(value)) { //如果这里的cellValue 是空值，那么认为循环到第一行合并单元格的第二列
                                continue;
                            }
                            for (int j = result.startCol; j <= result.endCol; j++) {
                                if (matrix.getHeaderRange().endRow == matrix.getHeaderRange().startRow) { //标识title在一行，header列存在行内合并单元格
                                    Cell cell0 = row.getCell(j);
                                    String value0 = getCellValue(cell0);
                                    headers.add(concat(value0, null));
                                } else {//标题在多行，如格式1
                                    for (int k = matrix.getHeaderRange().startRow + 1; k <= matrix.getHeaderRange().endRow; k++) {
                                        Row row0 = sheet.getRow(k);
                                        Cell cell0 = row0.getCell(j);
                                        String value0 = getCellValue(cell0);
                                        headers.add(concat(value, value0));
                                    }
                                }

                            }
                        }
                    } else {//没有合并单元格的
                        if (matrix.getHeaderRange().startRow == matrix.getHeaderRange().endRow) {//标识title在一行，所有header列不存在合并单元格
                            headers.add(concat(value, null));
                        } else {
                            for (int j = matrix.getHeaderRange().startRow + 1; j <= matrix.getHeaderRange().endRow; j++) {
                                Row row0 = sheet.getRow(j);
                                Cell cell0 = row0.getCell(cell.getColumnIndex());
                                String value0 = getCellValue(cell0);
                                headers.add(concat(value, value0));
                            }
                        }
                    }
                }
            }
        }
        matrixResolver.isInvalidHeader(headers);
        this.headers = headers;
        return headers;
    }


    protected String concat(String value, String value0) {
        if (StringUtil.isEmpty(value) && StringUtil.isEmpty(value0)) {
            return placeholder_on_blank_cell_value;
        }
        if (StringUtil.isEmpty(value) && StringUtil.isNotEmpty(value0)) {
            return value0;
        }
        if (StringUtil.isNotEmpty(value) && StringUtil.isEmpty(value0)) {
            return value;
        }
        if (StringUtil.isNotEmpty(value) && StringUtil.isNotEmpty(value0)) {
            return value + concat + value0;
        }
        return placeholder_on_blank_cell_value;
    }

    protected Workbook newWorkbook(InputStream in) throws Exception {
        return WorkbookFactory.create(in);
    }
}
