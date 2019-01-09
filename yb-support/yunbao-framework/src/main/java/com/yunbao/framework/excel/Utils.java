package com.yunbao.framework.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    /**
     * cell value 为空数据时的占位符
     */
    final static String placeholder_on_blank_cell_value = "--";

    final static String concat = "-";

    final static String placeholder_on_blank_header_cell = ":)";

    public static String getCellValue(Cell cell) {
        return getCellValue(cell, null);
    }


    public static String getCellValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null
                || (cell.getCellType() == CellType.STRING && StringUtils.isBlank(cell
                .getStringCellValue()))) {
            return null;
        }
        CellType cellType = cell.getCellType();
        if (cellType == CellType.BLANK) {
            return null;

        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cellType == CellType.ERROR) {
            return String.valueOf(cell.getErrorCellValue());
        } else if (cellType == CellType.FORMULA) {
            try {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return String.valueOf(cell.getDateCellValue());
                } else {
                    if (evaluator != null) {
                        CellValue cellValue = evaluator.evaluate(cell);
                        if (cellValue.getCellType() == CellType.STRING) {
                            return cellValue.getStringValue();
                        } else if (cellValue.getCellType() == CellType.NUMERIC) {
                            return getNumericVal(String.valueOf(evaluator.evaluate(cell).getNumberValue()));
                        } else {
                            return cellValue.getStringValue();
                        }
                    } else {
                        return getNumericVal(String.valueOf(cell.getNumericCellValue()));
                    }
                }
            } catch (IllegalStateException e) {
                return String.valueOf(cell.getRichStringCellValue());
            }
        } else if (cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return com.yunbao.framework.util.DateUtil.convertToString(cell.getDateCellValue(), com.yunbao.framework.util.DateUtil.YYYY_MM_DD_HH_MM_SS);
            } else {
                DecimalFormat df = new DecimalFormat();
                String s = String.valueOf(df.format(cell.getNumericCellValue()));
                if (s.contains(",")) {
                    s = s.replace(",", "");
                }
                return getNumericVal(s);
            }
        } else if (cellType == CellType.STRING)
            return cell.getStringCellValue();
        else {
            return null;
        }

    }


    //fixme 暂时的法子
    private static String getNumericVal(String val) {
        int point = val.indexOf(".");
        if (point == -1) {
            return val;
        }
        String decimal = val.substring(point + 1);

        if (decimal.length() == 1) {
            if ("0".equals(decimal)) {
                return val.substring(0, point);
            } else {
                return val;
            }
        } else if (decimal.length() == 2) {
            return val;
        } else if (decimal.length() > 2) {
            return new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        } else {
            return val;
        }

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

            if (!range.containsRow(row)) {
                continue;
            }
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();

            if (range.containsColumn(column)) {
                return new MergedRegion(true, firstRow, lastRow, firstColumn, lastColumn);
            }


        }
        return new MergedRegion(false, 0, 0, 0, 0);
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
            MergedRegion r = isMergeCell(sheet, row.getRowNum(), cell.getColumnIndex());
            if (r.merged) {
                mergedCells.add(r);
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


    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


}
