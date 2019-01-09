package com.yunbao.framework.excel;

import java.util.ArrayList;
import java.util.List;


/**
 * 工资条必须的字段
 */
public enum SalarySlipRequiredColumn {

    手机, 实发, 姓名, 邮箱;


    public static List<String> getValidateHeaders(String excludeKeyword) {
        List<String> cols = new ArrayList<>();
        for (SalarySlipRequiredColumn val : values()) {
            if (excludeKeyword.equals(val.name())) {
                continue;
            }
            cols.add(val.name());
        }
        return cols;
    }


}
