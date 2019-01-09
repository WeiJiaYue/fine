package com.asiahr;

import java.sql.Connection;

/**
 * Created by louis on 17-3-10.
 */
public interface ConnectionCreator extends DatabaseDialect{



    Connection getConnection()throws Exception;
}
