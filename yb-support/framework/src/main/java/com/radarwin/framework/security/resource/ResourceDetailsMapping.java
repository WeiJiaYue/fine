package com.radarwin.framework.security.resource;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ResourceDetailsMapping extends MappingSqlQuery {
    protected ResourceDetailsMapping(DataSource dataSource, String resourceQuery) {
        super(dataSource, resourceQuery);
        compile();
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        String name = rs.getString(1);
        String role = rs.getString(2);
        ResourceDetails resourceDetails = new ResourceDetails(name, role);
        return resourceDetails;
    }
}
