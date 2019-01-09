package com.yunbao.framework.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

public class Utils {

    public static String getCellValue(Cell cell) {
        if (cell == null
                || (cell.getCellType() == CellType.STRING && StringUtils.isBlank(cell
                .getStringCellValue()))) {
            return null;
        }
        CellType cellType = cell.getCellType();
        if (cellType == CellType.BLANK)
            return null;
        else if (cellType == CellType.BOOLEAN)
            return String.valueOf(cell.getBooleanCellValue());
        else if (cellType == CellType.ERROR)
            return String.valueOf(cell.getErrorCellValue());
        else if (cellType == CellType.FORMULA) {
            try {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return String.valueOf(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            } catch (IllegalStateException e) {
                return String.valueOf(cell.getRichStringCellValue());
            }
        } else if (cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return String.valueOf(cell.getDateCellValue());
            } else {
                return String.valueOf(cell.getNumericCellValue());
            }
        } else if (cellType == CellType.STRING)
            return cell.getStringCellValue();
        else
            return null;
    }


    /**
     * 查看对应行对应列是否是合并单元格
     *
     * @param sheet
     * @param row
     * @param column
     * @return 合并单元格的矩阵
     */
    public static MergedRegion isMergeCell(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return new MergedRegion(true, firstRow, lastRow, firstColumn, lastColumn);
                }
            }
        }
        return new MergedRegion(false, 0, 0, 0, 0);
    }


    /**
     * 获取整行合并的单元格
     *
     * @param sheet
     * @param row
     * @return 整行合并的单元格 如果没有合并单元格返回size为0的集合
     */
    public static List<MergedRegion> getMergedCells(Sheet sheet, Row row) {
        Iterator<Cell> cells = row.cellIterator();
        List<MergedRegion> mergedCells = new ArrayList<>();
        while (cells.hasNext()) {
            Cell cell = cells.next();
            if (cell == null) {
                continue;
            }
            MergedRegion result = isMergeCell(sheet, row.getRowNum(), cell.getColumnIndex());
            if (result.merged) {
                mergedCells.add(result);
            }
        }
        return mergedCells;
    }


    /**
     * 获取整行合并的单元格 并且在firstCellNum与lastCellNum之间
     *
     * @param sheet
     * @param row
     * @param matrix
     * @return 整行合并的单元格 如果没有合并单元格返回size为0的集合
     */
    public static List<MergedRegion> getMergedCells(Sheet sheet, Row row, Matrix matrix) {
        Iterator<Cell> cells = row.cellIterator();
        List<MergedRegion> mergedCells = new ArrayList<>();
        while (cells.hasNext()) {
            Cell cell = cells.next();
            if (cell == null) {
                continue;
            }

            if (cell.getColumnIndex() < matrix.getFirstCellNum() || cell.getColumnIndex() > matrix.getLastCellNum()) {
                continue;
            }

            MergedRegion result = isMergeCell(sheet, row.getRowNum(), cell.getColumnIndex());
            if (result.merged) {
                mergedCells.add(result);
            }
        }
        return mergedCells;
    }


    /**
     * 根据合并的单元格集合解析header所占用的开始行和结束行
     *
     * @param mergedCells
     * @return
     */
    public static HeaderRange parseHeaderRangeByMergedCells(List<MergedRegion> mergedCells) {

        List<Integer> startRows = new ArrayList<>();
        List<Integer> endRows = new ArrayList<>();

        for (MergedRegion region : mergedCells) {
            startRows.add(region.startRow);
            endRows.add(region.endRow);
        }
        Collections.sort(startRows);
        Collections.sort(endRows);
        return new HeaderRange(startRows.get(0), endRows.get(endRows.size() - 1));


    }
}
