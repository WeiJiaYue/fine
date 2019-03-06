package com.yunbao.framework.excel;


import java.util.List;

/**
 * 验证器 包括header body合法性的校验
 */
public interface Validator {




    List<String> getColumnsBeValidated();

    boolean needToValidate();

    boolean requiredHeaders(List<String> headers);

    ExcelReader.CellVal requiredCellVal(ExcelReader.CellVal value);

    void validateDuplicate(List<List<ExcelReader.CellVal>> bodies, String matchingKeyword);
}
