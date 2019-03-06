package com.yunbao.framework.excel;


import static com.yunbao.framework.excel.SalarySlipRequiredColumn.getValidateHeaders;
import static com.yunbao.framework.excel.SalarySlipRequiredColumn.*;

public class ReaderFactory {


    private final static Reader readerByMobile = new ExcelReader(new DefaultValidator(getValidateHeaders(邮箱.name())), new KeywordPolicy(手机.name()));

    private final static Reader readerByEmail = new ExcelReader(new DefaultValidator(getValidateHeaders(手机.name())), new KeywordPolicy(邮箱.name()));


    public static Reader create(NotificationType type) {
        Reader reader;
        switch (type) {
            case mobile:
                reader = readerByMobile;
                break;
            case email:
                reader = readerByEmail;

                break;
            default:
                throw new ExcelResolveException("没有该通知方式");
        }
        return reader;
    }


    public enum NotificationType {
        mobile, email
    }
}
