package com.yunbao.framework.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Test {


    public static void main(String[] args) throws Exception {
        String path = "/Users/lewis/Downloads/real data/201803云保科技工资表03.09.xlsx";
//        path = "/Users/lewis/Downloads/real data/201803云保科技工资表03.09的副本.xlsx";
//        path = "/Users/lewis/Downloads/real data/唯一视觉线上工资发放 4.xlsx";
//        path = "/Users/lewis/Downloads/real data/代发明细.xlsx";
//         path = "/Users/lewis/Downloads/real data/副本 手工单接收表（二工厂）.xlsx";
//         path = "/Users/lewis/Downloads/real data/工资条1.xlsx";
////         path = "/Users/lewis/Downloads/real data/工资条.xlsx";
//         path = "/Users/lewis/Downloads/real data/测试测试工资模版.xlsx";
        path = "/Users/lewis/Downloads/excel 导入模板/导入工资条模板.xlsx";
        path = "/Users/lewis/Downloads/excel 导入模板/导入工资条模板的副本.xlsx";


        List<String> validateColumns = new ArrayList<>();
        validateColumns.add("实发");
        validateColumns.add("姓名");

        File f = new File(path);
        InputStream inputStream = new FileInputStream(f);
        Reader reader = ReaderFactory.create(ReaderFactory.NotificationType.mobile);
        ExcelDatum excelDatum = reader.read(inputStream, 0);

        for (String header : excelDatum.getHeaders()) {
            System.out.print(header);
            System.out.print(",");
        }

    }
}
