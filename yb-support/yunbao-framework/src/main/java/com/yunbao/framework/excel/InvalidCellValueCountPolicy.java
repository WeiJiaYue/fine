package com.yunbao.framework.excel;


/**
 * 错误数据行解析规则
 * 按照在一行中错误数据的数量确定
 */
public class InvalidCellValueCountPolicy extends AbstractResolver {

    private float factor;


    public InvalidCellValueCountPolicy(float factor) {
        this.factor = factor;
    }

    public InvalidCellValueCountPolicy() {
        factor = 1.5f;
    }

    @Override
    public boolean invalidBodyRow(int invalidCellValCountOnRow, int headerSize) {
        if (invalidCellValCountOnRow > headerSize / factor) {
            return true;
        }
        return false;
    }


}