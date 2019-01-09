package com.yunbao.framework.excel;

import com.yunbao.framework.util.StringUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.yunbao.framework.excel.Utils.*;


/**
 * 标题reader
 */
public abstract class HeaderReader implements Reader {

    protected boolean enableValidate;

    protected Validator validator;


    protected List<String> headers;

    /**
     * matrix resolver
     */
    protected Resolver matrixResolver;


    public HeaderReader(Resolver matrixResolver) {
        this.matrixResolver = matrixResolver;
    }

    public HeaderReader(Validator validator, Resolver matrixResolver) {
        this.validator = validator;
        this.matrixResolver = matrixResolver;
        this.enableValidate = true;
    }

    @Override
    public List<String> readHeader(Workbook workbook, int sheetNo) {
        return readHeader(workbook, workbook.getSheetAt(sheetNo));
    }


    @Override
    public List<String> readHeader(Workbook workbook, Sheet sheet) {
        List<String> headers = new ArrayList<>();
        Matrix matrix = matrixResolver.resolveMatrix(workbook, sheet);
        HeaderRange headerRange = matrix.getHeaderRange();
        Row row = sheet.getRow(headerRange.startRow);
        for (int i = matrix.getFirstCellNum(); i <= matrix.getLastCellNum(); ) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String value = getCellValue(cell);
            MergedRegion result = isMergeCell(sheet, row.getRowNum(), cell.getColumnIndex());
            int step = 1;
            String val;
            if (result.merged) {
                step = (result.endCol - result.startCol) + 1;

                /**
                 * 选定区域包含多重数值。合并到一个单元格后只能保留最左上角的数据。
                 */
                val = result.getMergedRegionValue(sheet);
            } else {
                val = value;
            }
            List<List<String>> headerChain = new ArrayList<>();
            List<String> tops = new ArrayList<>();  //合并单元格最上面的一层header
            for (int j = result.startCol; j <= result.endCol; j++) {
                tops.add(val);
            }
            /**
             * 读取第一行之后的header并且组装
             */
            for (int m = headerRange.startRow + 1; m <= headerRange.endRow; m++) {
                List<String> headersOnRow = new ArrayList<>();
                Row row0 = sheet.getRow(m);
                boolean wholeRowAreBlank = true;
                for (int j = result.startCol; j <= result.endCol; j++) {
                    if (!result.merged) {
                        //这个地方如果result不是合并单元格，那么startCol和endCol值为0、为避免读取错误的单元格列，所以j=当前的cellIndex
                        j = cell.getColumnIndex();
                    }
                    Cell cell0 = row0.getCell(j);
                    MergedRegion mergeCell = isMergeCell(sheet, row0.getRowNum(), cell0.getColumnIndex());
                    String value0 = getCellValue(cell0);
                    if (mergeCell.merged) {
                        value0 = mergeCell.getMergedRegionValue(sheet);
                        m += (mergeCell.endRow - mergeCell.startRow);
                    }
                    if (StringUtil.isNotEmpty(value0)) {
                        wholeRowAreBlank = false;
                    }
                    headersOnRow.add(value0);
                }
                if (wholeRowAreBlank) {
                    continue;
                }
                headerChain.add(headersOnRow);
            }
            if (headerChain.isEmpty()) {
                for (String top : tops) {
                    if (StringUtil.isEmpty(top)) {
                        top = placeholder_on_blank_cell_value;
                    }
                    headers.add(top);
                }
                i += step;
                continue;
            }
            for (List<String> headerOnRow : headerChain) {
                List<String> tempArr = new ArrayList<>();
                String temp;
                for (int j = 0; j < step; j++) {
                    String header = headerOnRow.get(j);
                    temp = replaceBlank(concat(tops.get(j), header));
                    tempArr.add(temp);
                }
                tops.clear();
                tops.addAll(tempArr);
            }
            headers.addAll(tops);
            i += step;
        }
        matrixResolver.isInvalidHeader(headers);
        if (enableValidate) {
            validator.requiredHeaders(headers);
        }
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
            if (value.equals(value0)) {  //去重
                return value;
            }
            return value + concat + value0;
        }
        return placeholder_on_blank_cell_value;
    }

    protected Workbook newWorkbook(InputStream in) throws Exception {
        return WorkbookFactory.create(in);
    }
}
