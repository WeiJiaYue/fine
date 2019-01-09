package com.radarwin.framework.web.controller;

import com.radarwin.framework.security.AbstractCurrentUser;
import com.radarwin.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by josh on 15/7/9.
 */
@Controller
public class SecurityController extends BaseController {


    @Autowired
    private AbstractCurrentUser abstractCurrentUser;


    protected Map<String, Object> convertParamToMap(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String, Object> map = new HashMap<>();
        while (enumeration.hasMoreElements()) {
            String paramName = enumeration.nextElement();
            map.put(paramName, request.getParameter(paramName));
        }

        //获取当前用户id
        String userId = abstractCurrentUser.getCurrentUserName();
        if (StringUtil.isNotEmpty(userId)) {
            map.put("userId", userId);
        }
        return map;
    }

}
