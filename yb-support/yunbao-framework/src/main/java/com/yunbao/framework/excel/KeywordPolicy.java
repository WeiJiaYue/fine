package com.yunbao.framework.excel;

import com.yunbao.framework.util.ValidateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

import static com.yunbao.framework.excel.Utils.getMergedCells;
import static com.yunbao.framework.excel.Utils.parseHeaderRangeByMergedCells;

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
    public boolean resolveHeader(String cellValue, Row row, Matrix matrix) {
        if (cellValue.contains(matchingKeyword)) {
            //先设置header行的范围
            List<MergedRegion> mergedCells = getMergedCells(sheet, row, matrix);
            if (mergedCells.isEmpty()) {
                matrix.setHeaderRange(new HeaderRange(row.getRowNum(), row.getRowNum()));
                cellRange(row, matrix);
            } else {
                HeaderRange headerRange = parseHeaderRangeByMergedCells(mergedCells);
                matrix.setHeaderRange(headerRange);
            }

            //在判断数据列的范围
            matrix.setLastCellNum(row.getLastCellNum());

            //数据第一行的位置
            matrix.setFirstRowNum(matrix.getHeaderRange().endRow + 1);
            return true;
        }
        return false;
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
            if (Reader.placeholder_on_blank_cell_value.equals(header)) {
                invalidCellValOnHeaderRowCount++;
            }
            if (ValidateUtil.isDecimalOrNumeric(header)) {
                mayInvalidCellValOnHeaderRowCount++;
            }

        }
        if (invalidCellValOnHeaderRowCount > headers.size() / 2
                || mayInvalidCellValOnHeaderRowCount > headers.size() / 5) {//todo 暂时默认一个header行中失效数据超过一半为失效header行
            throw new ExcelResolveException("标题错误，请确认" + matchingKeyword + "在正确的标题行中");
        }

    }
}
