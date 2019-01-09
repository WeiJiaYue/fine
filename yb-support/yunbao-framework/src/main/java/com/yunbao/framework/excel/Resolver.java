package com.yunbao.framework.excel;


import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface Resolver {


    /**
     * 解析excel数据范围
     * @param workbook
     * @param sheet
     * @return
     */
    Matrix resolveMatrix(Workbook workbook, Sheet sheet);


    /**
     *
     * @return 返回matrix解析结果
     */
    Matrix getResolveMatrixResult();


    /**
     * 检测是否是有效的一行数据
     * @param invalidCellValCountOnRow 在内容部分一行中失效单元格数据值得数量
     * @param headerSize 标题的长度
     * @return 是否是有效的一行数据
     */
    boolean invalidBodyRow(int invalidCellValCountOnRow,int headerSize);


    /**
     * 判断解析的header是否正确
     * 如果解析失败，抛出异常
     * @param headers
     * @throws ExcelResolveException
     */
    void isInvalidHeader(List<String> headers);



}
