package com.radarwin.framework.web.editor;

import com.radarwin.framework.util.StringUtil;
import org.springframework.beans.propertyeditors.PropertiesEditor;

import java.math.BigDecimal;

/**
 * Created by josh on 15/7/17.
 */
public class BigDecimalEditor extends PropertiesEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtil.isBlank(text) || StringUtil.NULL.equalsIgnoreCase(text)) {
            setValue(null);
            return;
        }
        setValue(new BigDecimal(text));
    }
}
