package com.yunbao.framework.excel;

import com.yunbao.framework.util.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import static com.yunbao.framework.excel.Utils.getCellValue;

public class ExcelReader extends HeaderReader {


    private Resolver invalidBodyRowResolver;

    public final static Reader defaultInstance = new ExcelReader();


    /**
     * 如果不能解析出正确的excel，可以自定义解析器
     *
     * @param matrixResolver
     * @param invalidBodyRowResolver
     */
    public ExcelReader(Resolver matrixResolver, Resolver invalidBodyRowResolver) {
        super(matrixResolver);
        this.invalidBodyRowResolver = invalidBodyRowResolver;
    }


    /**
     * 按照默认解析规则的实列
     */
    public ExcelReader() {
        this(new KeywordPolicy(SalaryTemplateNecessaryColumn.手机.name()), new InvalidCellValueCountPolicy());
    }


    @Override
    public ExcelDatum read(Workbook workbook, int sheetNo) {
        this.readHeader(workbook, sheetNo);
        Map<Integer, List<String>> bodies = new HashMap<>();
        Sheet sheet = workbook.getSheetAt(sheetNo);
        Iterator<Row> rows = sheet.rowIterator();
        Matrix matrix = matrixResolver.getResolveMatrixResult();
        while (rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() <= matrix.getHeaderRange().endRow) {
                continue;
            }
            List<String> datum = new ArrayList<>();
            int invalidCellValCountOnRow = 0;
            boolean wholeRowIsBlank = true; //标识整行都为空
            for (int i = 0; i <= matrix.getLastCellNum(); i++) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell.getColumnIndex() < matrix.getFirstCellNum()) {
                    continue;
                }

                String value = getCellValue(cell);
                if (StringUtil.isEmpty(value)) {
                    invalidCellValCountOnRow++;
                } else {
                    wholeRowIsBlank = false;
                }
                datum.add(StringUtil.isEmpty(value) ? placeholder_on_blank_cell_value : value);
            }

            if (datum.size() != 0) {
                if(wholeRowIsBlank){//去除空行
                    continue;
                }
                if (invalidBodyRowResolver.invalidBodyRow(invalidCellValCountOnRow, headers.size())) { //去除一行中有效数据太少的行
                    continue;
                }
                bodies.put(row.getRowNum(), datum);
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


    public static void main(String[] args) throws Exception {
        String path = "/Users/lewis/Downloads/real data/201803云保科技工资表03.09的副本.xlsx";
//        String path = "/Users/lewis/Downloads/real data/唯一视觉线上工资发放 4.xlsx";
//        String path = "/Users/lewis/Downloads/real data/代发明细.xlsx";
//        String path = "/Users/lewis/Downloads/real data/副本 手工单接收表（二工厂）.xlsx";
//        String path = "/Users/lewis/Downloads/real data/工资条1.xlsx";
//        String path = "/Users/lewis/Downloads/real data/工资条.xlsx";
//        String path = "/Users/lewis/Downloads/real data/测试测试工资模版.xlsx";
//         path = "/Users/lewis/Downloads/excel 导入模板/导入工资条模板.xlsx";

        File f = new File(path);
        InputStream inputStream = new FileInputStream(f);
        ExcelDatum excelDatum = defaultInstance.read(inputStream, 0);

        for (String header : excelDatum.getHeaders()) {
            System.out.print(header);
            System.out.print(",");
        }




    }
}
