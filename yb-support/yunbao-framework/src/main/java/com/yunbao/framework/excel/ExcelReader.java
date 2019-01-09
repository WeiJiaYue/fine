package com.yunbao.framework.excel;

import com.yunbao.framework.util.StringUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.yunbao.framework.excel.Utils.getCellValue;


public class ExcelReader extends HeaderReader {


    private Resolver invalidBodyRowResolver;


    public final static Reader defaultInstance = new ExcelReader();


    /**
     * 如果不能解析出正确的excel，可以自定义解析器
     *
     * @param matrixResolver
     */
    public ExcelReader(Resolver matrixResolver) {
        this(null, matrixResolver);
    }

    /**
     * @param validator      验证器
     * @param matrixResolver 解析器
     */
    public ExcelReader(Validator validator, Resolver matrixResolver) {
        super(validator, matrixResolver);
        this.invalidBodyRowResolver = new InvalidCellValueCountPolicy();
    }


    /**
     * 按照默认解析规则的实列
     */
    public ExcelReader() {
        this(new KeywordPolicy(SalarySlipRequiredColumn.手机.name()));
    }


    @Override
    public ExcelDatum read(Workbook workbook, int sheetNo) {
        this.readHeader(workbook, sheetNo);
        List<List<CellVal>> bodies = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(sheetNo);
        Iterator<Row> rows = sheet.rowIterator();
        Matrix matrix = matrixResolver.getResolveMatrixResult();

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        while (rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() <= matrix.getHeaderRange().endRow) {
                continue;
            }
            List<CellVal> datum = new ArrayList<>();
            int invalidCellValCountOnRow = 0;
            boolean wholeRowAreBlank = true; //标识整行都为空
            int offset = matrix.getFirstCellNum();
            for (int i = matrix.getFirstCellNum(); i <= matrix.getLastCellNum(); i++) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String value = getCellValue(cell);
                if (StringUtil.isEmpty(value)) {
                    invalidCellValCountOnRow++;
                } else {
                    wholeRowAreBlank = false;
                }
                String header = headers.get(i - offset);
                CellVal cellVal = new CellVal(header, value);
                if (enableValidate) {
                    cellVal = validator.requiredCellVal(cellVal);
                } else {
                    if (StringUtil.isEmpty(cellVal.getVal())) {
                        cellVal.setVal(placeholder_on_blank_cell_value);
                    }
                }
                datum.add(cellVal);
            }

            if (datum.size() != 0) {
                if (wholeRowAreBlank) {//去除空行
                    continue;
                }
                if (invalidBodyRowResolver.invalidBodyRow(invalidCellValCountOnRow, headers.size())) { //去除一行中有效数据太少的行
                    continue;
                }
                bodies.add(datum);
            }
        }
        ExcelDatum excelDatum = new ExcelDatum();
        excelDatum.setHeaders(headers);
        excelDatum.setBodies(bodies);
        return excelDatum;
    }


    @Override
    public ExcelDatum read(InputStream inputStream, int sheetNo) throws Exception {
        return read(newWorkbook(inputStream), sheetNo);
    }

    @Override
    public ExcelDatum read(InputStream inputStream, String sheetName) throws Exception {
        Workbook workbook = newWorkbook(inputStream);
        return read(workbook, workbook.getSheet(sheetName));
    }


    @Override
    public ExcelDatum read(InputStream in, Sheet sheet) throws Exception {
        return read(newWorkbook(in), sheet);
    }

    @Override
    public List<String> sheets(InputStream inputStream) {
        List<String> sheets = new ArrayList<>();
        Workbook workbook;
        try {
            workbook = newWorkbook(inputStream);
        } catch (Exception e) {
            return sheets;
        }
        if (workbook != null) {
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                sheets.add(sheet.getSheetName());
            }
        }
        return sheets;
    }


    private ExcelDatum read(Workbook workbook, Sheet sheet) {
        if (sheet == null) {
            throw new ExcelResolveException("sheet不能为空");
        }
        return read(workbook, workbook.getSheetIndex(sheet));
    }


    public static class CellVal {
        private String header;
        private String val;
        private String hint;


        public CellVal(String header, String val) {
            this.header = header;
            this.val = val;
        }

        public CellVal(String header, String val, String hint) {
            this.header = header;
            this.val = val;
            this.hint = hint;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }
    }
}
