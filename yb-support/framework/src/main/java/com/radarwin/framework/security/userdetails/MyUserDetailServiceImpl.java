package com.radarwin.framework.security.userdetails;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 13-10-29
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
public class MyUserDetailServiceImpl extends JdbcDaoSupport implements UserDetailsService {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private String authoritiesByUsernameQuery;
    private String usersByUsernameQuery;

    private MappingSqlQuery usersByUsernameMapping;
    private MappingSqlQuery authoritiesByUsernameMapping;

    public String getAuthoritiesByUsernameQuery() {
        return authoritiesByUsernameQuery;
    }

    public void setAuthoritiesByUsernameQuery(String authoritiesByUsernameQuery) {
        this.authoritiesByUsernameQuery = authoritiesByUsernameQuery;
    }

    public String getUsersByUsernameQuery() {
        return usersByUsernameQuery;
    }

    public void setUsersByUsernameQuery(String usersByUsernameQuery) {
        this.usersByUsernameQuery = usersByUsernameQuery;
    }

    @Override
    protected void initDao() throws ApplicationContextException {
        initMappingSqlQueries();
    }

    private void initMappingSqlQueries() {
        this.usersByUsernameMapping = new UsersByUsernameMapping(getDataSource());
        this.authoritiesByUsernameMapping = new AuthoritiesByUsernameMapping(getDataSource());
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List users = loadUsersByUsername(username);

        if (users.size() == 0) {
            throw new UsernameNotFoundException(
                    messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found"));
        }

        UserDetails user = (UserDetails) users.get(0); // contains no GrantedAuthority[]

        Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();
        dbAuthsSet.addAll(loadUserAuthorities(user.getUsername()));

        if (dbAuthsSet.size() == 0) {
            throw new UsernameNotFoundException(
                    messages.getMessage("JdbcDaoImpl.noAuthority",
                            new Object[]{username}, "User {0} has no GrantedAuthority"));
        }

        return createUserDetails(username, user, dbAuthsSet);
    }

    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery,
                                            Collection<GrantedAuthority> combinedAuthorities) {

        UserInfo user = new UserInfo(username, userFromUserQuery.getPassword(), userFromUserQuery.isEnabled(),
                true, true, true, combinedAuthorities);

        user.setAttributes(((UserInfo) userFromUserQuery).getAttributes());
        return user;
    }

    protected List loadUsersByUsername(String username) {
        return usersByUsernameMapping.execute(username);
    }

    protected List loadUserAuthorities(String username) {
        return authoritiesByUsernameMapping.execute(username);
    }

    private class UsersByUsernameMapping extends MappingSqlQuery {
        protected UsersByUsernameMapping(DataSource ds) {
            super(ds, usersByUsernameQuery);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        @Override
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            String username = rs.getString(1);
            String password = rs.getString(2);
            boolean enabled = rs.getBoolean(3);
            Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
            authSet.add(new SimpleGrantedAuthority("HOLDER"));

            UserInfo user = new UserInfo(username, password, enabled, true, true, true, authSet);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = WordUtils.uncapitalize(
                        StringUtils.replace(
                                WordUtils.capitalizeFully(
                                        StringUtils.replace(rsmd.getColumnName(i), "_", " ")), " ", ""));
                user.setAttribute(columnName, rs.getObject(i));
            }
            return user;
        }
    }

    private class AuthoritiesByUsernameMapping extends MappingSqlQuery {
        protected AuthoritiesByUsernameMapping(DataSource ds) {
            super(ds, authoritiesByUsernameQuery);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        @Override
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            String roleName = rs.getString(2);
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

            return authority;
        }
    }
}
