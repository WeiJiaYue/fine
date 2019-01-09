package com.yunbao.framework.excel;


/**
 * 数据矩阵
 */
public class Matrix {


    private boolean resolved;  //标识是否成功解析出数据矩阵

    private short lastCellNum;

    private short firstCellNum;

    private int firstRowNum;

    private int lastRowNum;

    private HeaderRange headerRange;


    public short getLastCellNum() {
        return lastCellNum;
    }

    public void setLastCellNum(short lastCellNum) {
        this.lastCellNum = lastCellNum;
    }

    public short getFirstCellNum() {
        return firstCellNum;
    }

    public void setFirstCellNum(short firstCellNum) {
        this.firstCellNum = firstCellNum;
    }

    public int getFirstRowNum() {
        return firstRowNum;
    }

    public void setFirstRowNum(int firstRowNum) {
        this.firstRowNum = firstRowNum;
    }

    public int getLastRowNum() {
        return lastRowNum;
    }

    public void setLastRowNum(int lastRowNum) {
        this.lastRowNum = lastRowNum;
    }

    public HeaderRange getHeaderRange() {
        return headerRange;
    }

    public void setHeaderRange(HeaderRange headerRange) {
        this.headerRange = headerRange;
    }


    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
