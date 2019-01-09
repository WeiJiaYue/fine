package com.yunbao.framework.excel;

public class HeaderRange {


    int startRow;

    int endRow;

    public HeaderRange(int startRow, int endRow) {
        this.startRow = startRow;
        this.endRow = endRow;
    }


    public boolean isMergedCellHeader() {
        return endRow > startRow;
    }


    @Override
    public String toString() {
        return "header range : start at " + startRow + " end at " + endRow;
    }
}
