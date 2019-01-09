package com.yunbao.framework.excel;

import java.util.ArrayList;
import java.util.List;


/**
 * 解析出的excel数据
 */
public class ExcelDatum {
    private List<String> headers = new ArrayList<>(); //标题


    private List<List<ExcelReader.CellVal>> bodies = new ArrayList<>(); //内容


    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }


    public List<List<ExcelReader.CellVal>> getBodies() {
        return bodies;
    }

    public void setBodies(List<List<ExcelReader.CellVal>> bodies) {
        this.bodies = bodies;
    }
}
