package com.yunbao.framework.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;


/**
 * 解析器适配器
 */
public abstract class AbstractResolver implements Resolver{


    @Override
    public Matrix resolveMatrix(Workbook workbook, Sheet sheet) {
        return null;
    }

    @Override
    public Matrix getResolveMatrixResult() {
        return null;
    }

    @Override
    public boolean invalidBodyRow(int invalidCellValCountOnRow, int headerSize) {
        return false;
    }


    @Override
    public void isInvalidHeader(List<String> headers) {

    }
}
