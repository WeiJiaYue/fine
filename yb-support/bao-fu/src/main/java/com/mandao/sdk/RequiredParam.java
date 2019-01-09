package com.mandao.sdk;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标识注解，标识该参数是必须有值
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredParam {
}
