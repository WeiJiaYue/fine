package com.radarwin.framework.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by josh on 15/9/11.
 */
public interface AuthImageService {

    boolean showAuthImage(HttpServletRequest request, String loginName);

    void generateAuthImage(HttpServletRequest request, HttpServletResponse response);
}
