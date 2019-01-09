package com.yunbao.framework.excel;

import com.yunbao.framework.util.StringUtil;
import com.yunbao.framework.util.ValidateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

import static com.yunbao.framework.excel.Utils.*;


/**
 * 基于给定的关键字推算出标题的占用行
 * 并求出matrix的范围
 */
public class KeywordPolicy extends MatrixResolver {


    private String matchingKeyword;


    public KeywordPolicy(String matchingKeyword) {
        this.matchingKeyword = matchingKeyword;
    }


    @Override
    public void resolveHeader(Row row, String cellValue, Matrix matrix) {
        if (cellValue.contains(matchingKeyword)) {
            if (matrix.isResolved()) {
                return;
            }
            matrix.setLastRowNum(sheet.getLastRowNum());
            matrix.setFirstRowNum(sheet.getFirstRowNum());
            matrix.setLastCellNum(row.getLastCellNum());
            matrix.setFirstCellNum(row.getFirstCellNum());

            /**
             * 定位标题的行数范围，根据给定的关键字
             */
            headerRowRange(row, matrix);
            /**
             * 解析标题的列数范围
             */
            headerCellRange(sheet, row, matrix);


            //header row 偏移量计算
            HeaderRange range = matrix.getHeaderRange();
            int headerSize = matrix.getLastCellNum();
            int endRow = -1;
            //从startRow扫描到endRow，解析哪一行为正确的标题行
            for (int i = range.startRow; i <= range.endRow; i++) {
                int invalidCellValOnHeaderRowCount = 0;
                int mayInvalidCellValOnHeaderRowCount = 0;
                Row mayHeaderRow = sheet.getRow(i);
                for (int j = matrix.getFirstCellNum(); j <= matrix.getLastCellNum(); j++) {
                    Cell cell = mayHeaderRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value = getCellValue(cell);
                    if (StringUtil.isEmpty(value)) {
                        invalidCellValOnHeaderRowCount++;
                    } else {
                        if (ValidateUtil.isDecimalOrNumeric(value)) {
                            mayInvalidCellValOnHeaderRowCount++;
                        }
                    }
                }
                if (invalidCellValOnHeaderRowCount > headerSize / 2 || mayInvalidCellValOnHeaderRowCount > headerSize / 5) {
                    continue;
                }
                endRow = i;
                if (endRow != -1) {
                    break;
                }
            }
            if (endRow == -1) {
                return;
            }
            headerRowRange(row, matrix);

            secondaryHeaderCellRangeResolve(sheet, row, matrix);
            matrix.setFirstRowNum(matrix.getHeaderRange().endRow + 1);
            matrix.setResolved(true);
        }
    }


    @Override
    protected void headerRangeHasResolvedSuccessfully(HeaderRange headerRange) {
        if (headerRange == null) {
            throw new ExcelResolveException("被解析的excel文本标题行缺少" + matchingKeyword + "列");
        }

    }


    @Override
    public void isInvalidHeader(List<String> headers) {
        if (headers.isEmpty()) {
            throw new ExcelResolveException("被解析的excel文本标题行没有值");
        }
        int invalidCellValOnHeaderRowCount = 0;
        int mayInvalidCellValOnHeaderRowCount = 0;
        for (String header : headers) {
            if (placeholder_on_blank_cell_value.equals(header)) {
                invalidCellValOnHeaderRowCount++;
            }
            if (ValidateUtil.isDecimalOrNumeric(header)) {
                mayInvalidCellValOnHeaderRowCount++;
            }
        }
        if (invalidCellValOnHeaderRowCount > headers.size() / 2
                || mayInvalidCellValOnHeaderRowCount > headers.size() / 5) {//fixme 暂时默认一个header行中失效数据超过一半为失效header行
            throw new ExcelResolveException("标题错误，请确认" + matchingKeyword + "在正确的标题行中");
        }

    }


    @Override
    public String getMatchingKeyword() {
        return matchingKeyword;
    }


    private void secondaryHeaderCellRangeResolve(Sheet sheet, Row headerRow, Matrix matrix) {
        headerCellRange(sheet, headerRow, matrix);
    }

    private void headerRowRange(Row row, Matrix matrix) {
        List<MergedRegion> mergedCells = getMergedCells(sheet, row, matrix);
        if (mergedCells.isEmpty()) {
            matrix.setHeaderRange(new HeaderRange(row.getRowNum(), row.getRowNum()));
        } else {
            HeaderRange headerRange = parseHeaderRangeByMergedCells(mergedCells);
            matrix.setHeaderRange(headerRange);
        }
    }


}
