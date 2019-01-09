package com.asiahr;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by louis on 17-3-10.
 */
public class DatabaseConnectionCreator implements ConnectionCreator, DatabaseDialect {


    private static final String ORACLE = "oracle";
    private static final String MYSQL = "mysql";

    private String host;
    private String port;
    private String username;
    private String password;
    private String database;
    private String dialect;


    public DatabaseConnectionCreator(String host, String port, String username, String password, String database) {
        this(host, port, username, password, database, ORACLE);

    }

    public DatabaseConnectionCreator(String host, String port, String username, String password, String database, String dialect) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.dialect = dialect;
    }

    @Override
    public Connection getConnection() throws Exception {
        Args.isNull(getDialect(), "database dialect must not be empty");
        String url;
        Connection connection = null;
        switch (getDialect()) {
            case ORACLE:
                String driver = "oracle.jdbc.OracleDriver";
                url = "jdbc:oracle:thin:@" + host + ":" + port + "/" + database;

                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);

                break;
            case MYSQL:
                url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?user=" + username +
                        "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8";
                connection = DriverManager.getConnection(url);
                break;
            default:
                Args.isNotIllegal("illegal dialect parameter");

        }
        return connection;
    }

    @Override
    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
}
