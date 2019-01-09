package com.radarwin.framework.web.editor;

import com.radarwin.framework.util.DateUtil;
import com.radarwin.framework.util.StringUtil;
import org.springframework.beans.propertyeditors.PropertiesEditor;

/**
 * Created by josh on 15/7/17.
 */
public class DateEditor extends PropertiesEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtil.isBlank(text) || StringUtil.NULL.equalsIgnoreCase(text)) {
            setValue(null);
            return;
        }

        if (text.length() == DateUtil.YYYY_MM_DD_HH_MM_SS.length()) {
            setValue(DateUtil.convertToDate(text, DateUtil.YYYY_MM_DD_HH_MM_SS));
        } else if (text.length() == DateUtil.YYYY_MM_DD.length()) {
            setValue(DateUtil.convertToDate(text, DateUtil.YYYY_MM_DD));
        }else if(text.length()==DateUtil.YYYY_MM_DD_HH_MM.length()){
            setValue(DateUtil.convertToDate(text, DateUtil.YYYY_MM_DD_HH_MM));
        }
    }
}
