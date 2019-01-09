package com.radarwin.framework.web.editor;

import com.radarwin.framework.util.StringUtil;
import org.springframework.beans.propertyeditors.PropertiesEditor;

/**
 * Created by josh on 15-6-8.
 */
public class XssEditor extends PropertiesEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            return;
        }
        setValue(StringUtil.filterXss(text));
    }

    @Override
    public String getAsText() {
        Object v = getValue();
        if (v == null) {
            return null;
        }
        return v.toString();
    }
}
