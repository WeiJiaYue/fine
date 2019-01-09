package com.radarwin.framework.jsonhelp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.radarwin.framework.util.DateUtil;

import java.io.IOException;
import java.util.Date;

/**
 * Created by josh on 15/6/23.
 */
public class DateTimeJsonSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(DateUtil.convertToString(date, DateUtil.YYYY_MM_DD_HH_MM_SS));
    }
}
