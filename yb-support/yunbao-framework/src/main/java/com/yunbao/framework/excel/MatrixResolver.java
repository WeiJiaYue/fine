package com.yunbao.framework.excel;

import com.yunbao.framework.util.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.yunbao.framework.excel.Utils.getCellValue;


/**
 * excel 数据范围解析器，可以理解为求出excel数据的矩阵大小
 * 包括解析header标题的范围
 */
public abstract class MatrixResolver extends AbstractResolver {

    protected Workbook workbook;

    protected Sheet sheet;

    protected Matrix matrix;


    /**
     * 根据header执行策略解析header出现的区域
     *
     * @param cellValue 单元格值
     * @param row       当前行
     * @param matrix    excel数据矩阵
     * @return if true 已经解析出header的区域
     */
    protected abstract boolean resolveHeader(String cellValue, Row row, Matrix matrix);

    /**
     * 如果解析失败，抛出异常
     * @param headerRange
     * @throws ExcelResolveException
     */
    protected abstract void headerRangeHasResolvedSuccessfully(HeaderRange headerRange);


    /**
     * 判断解析的header是否正确
     * 如果解析失败，抛出异常
     * @param headers
     * @throws ExcelResolveException
     */
    public abstract void isInvalidHeader(List<String> headers);


    @Override
    public Matrix getResolveMatrixResult() {
        return this.matrix;
    }

    @Override
    public Matrix resolveMatrix(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
        //第几个sheet
        Iterator<Row> rows = sheet.rowIterator();
        Matrix matrix = new Matrix();
        List<Integer> blankRows = new ArrayList<>();
        while (rows.hasNext()) {
            Row row = rows.next();
            Iterator<Cell> cells = row.cellIterator();
            boolean wholeRowIsNull = true; //标识整行都为空
            while (cells.hasNext()) {
                Cell cell = cells.next();
                if (cell == null) {
                    continue;
                }
                //判断不要超过实际数据的最后一个列
                if (cell.getColumnIndex() > matrix.getLastCellNum() &&
                        matrix.getLastCellNum() != 0) {
                    break;
                }
                String value = getCellValue(cell);
                if (StringUtil.isNotEmpty(value)) {
                    wholeRowIsNull = false;
                    resolveHeader(value, row, matrix);
                }
            }
            if (wholeRowIsNull) {
                blankRows.add(row.getRowNum());
                if (blankRows.size() >= 10) {
                    //todo 这边要取连续递增一行的10条记录，并且用第一条记录作为lastRow
                    int planingLastRowNum = blankRows.get(9);
                    matrix.setLastRowNum(planingLastRowNum);
                    break;
                }
            } else {
                matrix.setLastRowNum(sheet.getLastRowNum());

            }
        }
        headerRangeHasResolvedSuccessfully(matrix.getHeaderRange());
        this.matrix = matrix;
        return matrix;
    }

    protected void cellRange(Row headerRow, Matrix matrix) {
        Iterator<Cell> cells = headerRow.cellIterator();
        int firstCellNum = 0;
        int lastCellNum = 0;
        while (cells.hasNext()) {
            Cell cell = cells.next();
            if (cell == null) {
                continue;
            }

            String value = getCellValue(cell);
            if (StringUtil.isNotEmpty(value)) {
                firstCellNum = cell.getColumnIndex();
                break;

            }
        }
        for (int i = headerRow.getLastCellNum(); i > firstCellNum; i--) {
            Cell cell = headerRow.getCell(i);
            if (cell == null) {
                continue;
            }
            String value = getCellValue(cell);
            if (StringUtil.isNotEmpty(value)) {
                lastCellNum = cell.getColumnIndex();
                break;

            }
        }
        matrix.setFirstCellNum((short) firstCellNum);
        matrix.setLastCellNum((short) lastCellNum);
    }



//    protected void cellRange(Row headerRow, Matrix matrix) {
//        Iterator<Cell> cells = headerRow.cellIterator();
//        int firstCellNum = 0;
//        int lastCellNum = 0;
//        while (cells.hasNext()) {
//            Cell cell = cells.next();
//            if (cell == null) {
//                continue;
//            }
//
//            String value = getCellValue(cell);
//            if (StringUtil.isNotEmpty(value)) {
//                firstCellNum = cell.getColumnIndex();
//                break;
//
//            }
//        }
//        for (int i = headerRow.getLastCellNum(); i > firstCellNum; i--) {
//            Cell cell = headerRow.getCell(i);
//            if (cell == null) {
//                continue;
//            }
//            String value = getCellValue(cell);
//            if (StringUtil.isNotEmpty(value)) {
//                lastCellNum = cell.getColumnIndex();
//                break;
//
//            }
//        }
//        matrix.setFirstCellNum((short) firstCellNum);
//        matrix.setLastCellNum((short) lastCellNum);
//    }


}
