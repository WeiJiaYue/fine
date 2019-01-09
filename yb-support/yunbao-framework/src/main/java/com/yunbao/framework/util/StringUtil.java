package com.yunbao.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by josh on 15-6-17.
 */
public class StringUtil extends StringUtils {

    public static final String EMPTY = "";

    public static final String DOT = ".";

    public static final String COMMA = ",";

    public static final String COLON = ":";

    public static final String LT = "<";

    public static final String GT = ">";

    public static final String ULT = "\\u003c";

    public static final String UGT = "\\u003e";

    public static final String NULL = "null";

    public static final String PERCENT = "%";

    public static String string2Unicode(String s) {
        StringBuffer unicode = new StringBuffer();
        if (s == null) {
            return unicode.toString();
        }

        for (int i = 0; i < s.length(); i++) {

            // 取出每一个字符
            char c = s.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    public static String decodeXss(String s){
        if (s == null) {
            return s;
        }

        // < >
        s = s.replaceAll("&lt;", "<").replaceAll( "&gt;",">");

        // '
        s = s.replaceAll("&quot;", "'");
        return s;
    }

    public static String filterXss(String s) {
        if (s == null) {
            return s;
        }

        // < >
        s = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        // < 及 >的unicode字符
        s = s.replaceAll("\\u003c", "&lt;").replaceAll("\\u003e", "\"&gt;\"");

        // '
        s = s.replaceAll("'", "&quot;");

        // ' 的unicode字符
        s = s.replaceAll("\\u0027", "&quot;");

        // "
        s = s.replaceAll("\"", "&quot;");

        // " 的unicode字符
        s = s.replaceAll("\\u0022", "&quot;");

        // eval 表达式
        s = s.replaceAll("eval\\((.*)\\)", "");

        // javascript:( 表达式
        s = s.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        return s;
    }

    public static String getString(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String) o;
        }
        return o.toString();
    }

    public static void main(String[] args) {

    }
}
