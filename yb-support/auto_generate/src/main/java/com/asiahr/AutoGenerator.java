package com.asiahr;

/**
 * Created by louis on 17-3-22.
 */
public interface AutoGenerator {

    void generateCode(String... tableNames);


    AutoGenerator propertiesSet(AutoGenerateProperties autoGenerateProperties);
}
