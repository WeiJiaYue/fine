package com.asiahr;

/**
 * Created by louis on 17-3-10.
 */
public class Args {


    public static void isNull(Object o, String message) throws Exception {
        if (o == null) {
            throw new Exception(message);
        }
    }


    public static void isNotIllegal(String message) throws Exception {
        throw new Exception(message);


    }





}
