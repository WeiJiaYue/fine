package com.radarwin.framework.web.controller;

import com.radarwin.framework.web.editor.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by josh on 15-6-17.
 */
@Controller
public class BaseController {



    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new XssEditor());
        binder.registerCustomEditor(Date.class, new DateEditor());
        binder.registerCustomEditor(BigDecimal.class, new BigDecimalEditor());
        binder.registerCustomEditor(Long.class, new LongEditor());
        binder.registerCustomEditor(Integer.class, new IntegerEditor());
    }

    protected Map<String, Object> convertParamToMap(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String, Object> map = new HashMap<>();
        while (enumeration.hasMoreElements()) {
            String paramName = enumeration.nextElement();
            map.put(paramName, request.getParameter(paramName));
        }
        return map;
    }

    protected String redirect(String result) {
        return "redirect:" + result;
    }

    protected String forward(String result) {
        return "forward:" + result;
    }
}
