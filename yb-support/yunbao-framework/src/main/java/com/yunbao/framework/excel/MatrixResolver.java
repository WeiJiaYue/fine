package com.yunbao.framework.excel;

import com.yunbao.framework.util.StringUtil;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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
    protected abstract void resolveHeader(Row row, String cellValue, Matrix matrix);

    /**
     * 如果解析失败，抛出异常
     *
     * @param headerRange
     * @throws ExcelResolveException
     */
    protected abstract void headerRangeHasResolvedSuccessfully(HeaderRange headerRange);


    /**
     * 判断解析的header是否正确
     * 如果解析失败，抛出异常
     *
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
        SpreadsheetVersion version = workbook.getSpreadsheetVersion();
        this.workbook = workbook;
        this.sheet = sheet;
        //第几个sheet
        Iterator<Row> rows = sheet.rowIterator();
        Matrix matrix = new Matrix();
        int blankRowCount = 0;
        int blankRowIndex = -1;
        while (rows.hasNext()) {
            Row row = rows.next();
            boolean wholeRowAreNull = true; //标识整行都为空
            for (int i = row.getFirstCellNum(); i <= row.getLastCellNum() && i < version.getMaxColumns(); i++) {
                if(row.getFirstCellNum()==-1||row.getLastCellNum()==-1){
                    break;
                }
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell == null) {
                    continue;
                }
                //判断当matrix的lastCellNum已被解析时，不要超过实际数据的最后一个列
                if (cell.getColumnIndex() > matrix.getLastCellNum() &&
                        matrix.getLastCellNum() != 0) {
                    break;
                }
                String value = getCellValue(cell);
                if (StringUtil.isNotEmpty(value)) {
                    wholeRowAreNull = false;
                    resolveHeader(row, value, matrix);
                }
            }
            if (wholeRowAreNull) {
                if (blankRowCount == 5) {  //fixme 连续5行空行视为往下的都没数据了
                    matrix.setLastRowNum(blankRowIndex - blankRowCount);
                    break;
                }

                if (row.getRowNum() - blankRowIndex == 1) { //表示是连续空行
                    blankRowCount++;
                } else {
                    blankRowCount = 0;
                }
                blankRowIndex = row.getRowNum();


            } else {
                matrix.setLastRowNum(sheet.getLastRowNum());

            }
        }
        headerRangeHasResolvedSuccessfully(matrix.getHeaderRange());
        this.matrix = matrix;
        return matrix;
    }

    protected void headerCellRange(Sheet sheet, Row headerRow, Matrix matrix) {
        HeaderRange headerRange = matrix.getHeaderRange();
        //在header row 范围内第一个有值的列
        int firstCellNum = -1;
        int lastCellNum = 0;
        if (headerRange.endRow == headerRange.startRow) {
            Iterator<Cell> cells = headerRow.cellIterator();
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
        } else {
            Iterator<Row> headerRowRangeIterator = sheet.rowIterator();
            while (headerRowRangeIterator.hasNext()) {
                Row row = headerRowRangeIterator.next();
                if (row.getRowNum() < headerRange.startRow
                        || row.getRowNum() > headerRange.endRow) {
                    continue;
                }
                Iterator<Cell> cells = row.cellIterator();
                //当前行第一个有值得cell num
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    if (cell == null) {
                        continue;
                    }
                    String value = getCellValue(cell);
                    if (StringUtil.isNotEmpty(value)) {
                        int temp = cell.getColumnIndex();
                        //获取合并单元格中最小的一个cellColumnIndex座位firstCellNum
                        if (temp < firstCellNum || firstCellNum == -1) {
                            firstCellNum = temp;
                            break;
                        }
                        //提早结束
                        if (cell.getColumnIndex() > firstCellNum) {
                            break;
                        }
                    }

                }

            }
        }
        if(firstCellNum==-1){
            firstCellNum=0;
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


}
