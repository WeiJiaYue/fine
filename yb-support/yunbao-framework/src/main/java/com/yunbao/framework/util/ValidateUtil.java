package com.yunbao.framework.util;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lewis on 2017/8/4.
 */
public class ValidateUtil {

    private static final String number_regex = "-?[0-9]*";

    private static final String decimal_regex = "[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+";

    private static Pattern mobilePattern = Pattern.compile("^1\\d{10}$");



    public static boolean isDecimal(String shouldBeValidated) {
        return isMatch(decimal_regex, shouldBeValidated);
    }

    public static boolean isNumeric(String shouldBeValidated) {
        return isMatch(number_regex, shouldBeValidated);
    }

    public static boolean isDecimalOrNumeric(String value) {
        return isDecimal(value) || isNumeric(value);
    }




    private static boolean isMatch(String regex, String shouldBeValidated) {
        Pattern pattern = Pattern.compile(regex);
        Matcher isIllegal = pattern.matcher(shouldBeValidated);
        if (isIllegal.matches()) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean validateMobile(String s) {
        if (StringUtil.isBlank(s)) {
            return false;
        }
        Matcher matcher = mobilePattern.matcher(s);
        return matcher.matches();
    }


    public static void main(String[] args) {
        System.out.println(isDecimalOrNumeric("0.9"));
        System.out.println(isDecimalOrNumeric("0"));
        System.out.println(isDecimalOrNumeric("0.0"));
        System.out.println(isDecimalOrNumeric("0.00"));
        System.out.println(isDecimalOrNumeric("10"));
        System.out.println(isDecimalOrNumeric("-1"));
    }



}
