package com.yunbao.framework.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.List;

public interface Reader {


    /**
     * cell value 为空数据时的占位符
     */
    String placeholder_on_blank_cell_value = "--";

    String concat = "-";

    String placeholder_on_blank_header_cell = ":)";

    List<String> readHeader(Workbook workbook, int sheetNo);

    List<String> readHeader(Workbook workbook, Sheet sheet);

    ExcelDatum read(Workbook workbook, int sheetNo) throws Exception;

    ExcelDatum read(InputStream inputStream, int sheetNo) throws Exception;

    ExcelDatum read(InputStream workbook, Sheet sheet) throws Exception;

    ExcelDatum read(InputStream inputStream, String sheetName) throws Exception;

    List<String> sheets(InputStream inputStream) ;


}
