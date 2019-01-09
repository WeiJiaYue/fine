package com.yunbao.framework.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 解析出的excel数据
 */
public class ExcelDatum {
    private List<String> headers = new ArrayList<>(); //标题


    /**
     * key 为行数
     * value 为一行的内容
     */
    private Map<Integer, List<String>> bodies = new HashMap<>(); //内容


    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public Map<Integer, List<String>> getBodies() {
        return bodies;
    }

    public void setBodies(Map<Integer, List<String>> bodies) {
        this.bodies = bodies;
    }
}
