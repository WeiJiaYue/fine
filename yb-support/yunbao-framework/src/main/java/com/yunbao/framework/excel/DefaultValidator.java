package com.yunbao.framework.excel;


import com.yunbao.framework.util.StringUtil;
import com.yunbao.framework.util.ValidateUtil;

import java.util.List;

public class DefaultValidator implements Validator {

    private List<String> validateColumns;

    private boolean enableValidator;


    public DefaultValidator(List<String> validateColumns) {
        this.validateColumns = validateColumns;
        if (this.validateColumns != null && !this.validateColumns.isEmpty()) {
            this.enableValidator = true;
        }
    }

    @Override
    public List<String> getColumnsBeValidated() {
        return this.validateColumns;
    }

    @Override
    public boolean needToValidate() {
        return this.enableValidator;
    }


    @Override
    public boolean requiredHeaders(List<String> headers) {
        if (enableValidator) {
            for (String col : validateColumns) {
                boolean contains = false;
                for (String header : headers) {
                    if (header.contains(col)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    throw new ExcelResolveException("标题行必须包含：" + col + "项");
                }

            }
        }
        return true;
    }


    @Override
    public ExcelReader.CellVal requiredCellVal(ExcelReader.CellVal cellVal) {
        boolean validateCell = false;
        for (String col : validateColumns) {
            if (cellVal.getHeader().contains(col) && !validateCell) {
                validateCell = true;
            }
        }
        if (validateCell) {
            if (StringUtil.isEmpty(cellVal.getVal())) {
                return new ExcelReader.CellVal(cellVal.getHeader(), Utils.placeholder_on_blank_cell_value, cellVal.getHeader() + "字段不能为空");
            }
            if (cellVal.getHeader().contains(SalarySlipRequiredColumn.实发.name())) {
                if (!ValidateUtil.isDecimalOrNumeric(cellVal.getVal())) {
                    return new ExcelReader.CellVal(cellVal.getHeader(), cellVal.getVal(), cellVal.getHeader() + "格式错误");
                }
            }
            if (cellVal.getHeader().contains(SalarySlipRequiredColumn.手机.name())) {
                if (!ValidateUtil.validateMobile(cellVal.getVal())) {
                    return new ExcelReader.CellVal(cellVal.getHeader(), cellVal.getVal(), cellVal.getHeader() + "格式错误");
                }
            }
        }

        if (StringUtil.isEmpty(cellVal.getVal())) {
            return new ExcelReader.CellVal(cellVal.getHeader(), Utils.placeholder_on_blank_cell_value);
        }
        return cellVal;
    }

    @Override
    public void validateDuplicate(List<List<ExcelReader.CellVal>> bodies, String matchingKeyword) {
        for (int i = 0; i < bodies.size(); i++) {
            List<ExcelReader.CellVal> preRow = bodies.get(i);
            ExcelReader.CellVal compare = ExcelReader.CellVal.getVal(preRow, matchingKeyword);
            for (int j = i + 1; j < bodies.size(); j++) {
                List<ExcelReader.CellVal> row = bodies.get(j);
                ExcelReader.CellVal beCompared = ExcelReader.CellVal.getVal(row, matchingKeyword);
                if (compare != null && beCompared != null && beCompared.getHeader().equals(Utils.placeholder_on_blank_cell_value)) {
                    if (compare.getVal().equals(beCompared.getVal())) {
                        String hint = beCompared.getHeader() + "为" + beCompared.getVal() + "请勿重复存在";
                        beCompared.setHint(beCompared.getHint() == null ? "" : beCompared.getHint() + "," + hint);
                        break;
                    }
                }
            }

        }


    }
}
