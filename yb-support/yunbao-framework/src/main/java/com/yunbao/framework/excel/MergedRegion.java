package com.yunbao.framework.excel;

public class MergedRegion {


    boolean merged;
    int startRow;
    int endRow;
    int startCol;
    int endCol;


    public MergedRegion(boolean merged, int startRow, int endRow, int startCol, int endCol) {
        this.merged = merged;
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    @Override
    public String toString() {
        return "merged=" + merged + "; startRow=" + startRow + "; endRow=" + endRow
                + "; startCol=" + startCol + "; endCol=" + endCol;
    }
}
