package com.radarwin.framework.web.editor;

import com.radarwin.framework.util.StringUtil;
import org.springframework.beans.propertyeditors.PropertiesEditor;

/**
 * Created by josh on 15/7/17.
 */
public class IntegerEditor extends PropertiesEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtil.isBlank(text) || StringUtil.NULL.equalsIgnoreCase(text)) {
            setValue(null);
            return;
        }
        setValue(Integer.valueOf(text));
    }
}
