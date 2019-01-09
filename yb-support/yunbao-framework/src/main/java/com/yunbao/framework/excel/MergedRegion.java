package com.yunbao.framework.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class MergedRegion {


    boolean merged;
    int startRow;
    int endRow;
    int startCol;
    int endCol;


    /**
     * 合并单元格的值是在合并区域的第一行第一列
     *
     * @param sheet
     * @return
     */
    public String getMergedRegionValue(Sheet sheet) {
        if (merged) {
            Row row = sheet.getRow(startRow);
            Cell cell = row.getCell(startCol);
            return Utils.getCellValue(cell);
        }
        return null;
    }

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
