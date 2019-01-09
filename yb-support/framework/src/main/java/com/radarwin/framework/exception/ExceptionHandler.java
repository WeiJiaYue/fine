package com.radarwin.framework.exception;

import com.radarwin.framework.security.AbstractCurrentUser;
import com.radarwin.framework.util.SecurityUtil;
import com.radarwin.framework.util.StringUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by josh on 15/7/11.
 */
public class ExceptionHandler implements HandlerExceptionResolver {

    private static Logger logger = LogManager.getLogger(ExceptionHandler.class);

    private static final String errorMsg = "系统错误";

    private String defaultErrorPage = "500.html";

    private int errorStatusCode;

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object o, Exception e) {

        AbstractCurrentUser.removeUserLocal();

        logger.error(ExceptionUtils.getStackTrace(e));

        if (SecurityUtil.isAjaxRequest(request)) {
            try {
                response.setContentType("application/json;charset=utf-8");
                if (errorStatusCode != 0) {
                    response.setStatus(errorStatusCode);
                }
                PrintWriter printWriter = response.getWriter();
                if (e instanceof BusinessException) {
                    printWriter.print(e.getMessage());
                } else {
                    printWriter.print(errorMsg);
                }
                printWriter.flush();
                printWriter.close();
            } catch (Exception ex) {
                logger.error(ExceptionUtils.getStackTrace(ex));
            }
            return null;
        } else {
            String path = StringUtil.EMPTY;
            if (defaultErrorPage.indexOf("/") == 0) {
                path = path + defaultErrorPage;
            } else {
                path = path + "/" + defaultErrorPage;
            }
            return new ModelAndView(path);
        }
    }

    public void setDefaultErrorPage(String defaultErrorPage) {
        this.defaultErrorPage = defaultErrorPage;
    }

    public void setErrorStatusCode(int errorStatusCode) {
        this.errorStatusCode = errorStatusCode;
    }
}
