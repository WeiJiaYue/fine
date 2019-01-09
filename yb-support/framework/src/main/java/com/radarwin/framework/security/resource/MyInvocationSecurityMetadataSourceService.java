package com.radarwin.framework.security.resource;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.sql.DataSource;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 13-10-30
 * Time: 下午6:05
 * To change this template use File | Settings | File Templates.
 */

public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
    private String queryUrl = "select re.resource_str,r.role_code"
            + "                      from roles r"
            + "                      join resource_role rr"
            + "                        on r.id=rr.role_id"
            + "                      join resources re"
            + "                        on re.id=rr.resource_id"
            + "                     where re.resource_type='url'"
            + "                  order by re.priority";
    private DataSource dataSource;
    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
    private RequestMatcher requestMatcher;

    public MyInvocationSecurityMetadataSourceService(DataSource dataSource, String queryUrl) {
        this.dataSource = dataSource;
        this.queryUrl = queryUrl;
        loadResourceDefine(queryUrl);
    }

    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Iterator<String> it = resourceMap.keySet().iterator();
        while (it.hasNext()) {
            String resURL = it.next();
            requestMatcher = new AntPathRequestMatcher(resURL);
            if (requestMatcher.matches(((FilterInvocation) object).getRequest())) {
                return resourceMap.get(resURL);
            }
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean supports(Class<?> aClass) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void loadResourceDefine(String query) {
        ResourceDetailsMapping resourceDetailsMapping = new ResourceDetailsMapping(dataSource, query);
        List<ResourceDetails> resources = resourceDetailsMapping.execute();
        resourceMap = new HashMap<>();
        Map<String, ConfigAttribute> authMap = new HashMap<String, ConfigAttribute>();
        for (ResourceDetails resourceDetails : resources) {
            if (!authMap.containsKey(resourceDetails.getRole())) {
                ConfigAttribute ca = new SecurityConfig(resourceDetails.getRole());
                authMap.put(resourceDetails.getRole(), ca);
            }
            if (resourceMap.containsKey(resourceDetails.getName())) {
                Collection<ConfigAttribute> value = resourceMap.get(resourceDetails.getName());
                value.add(authMap.get(resourceDetails.getRole()));
                resourceMap.put(resourceDetails.getName(), value);
            } else {
                Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
                configAttributes.add(authMap.get(resourceDetails.getRole()));
                resourceMap.put(resourceDetails.getName(), configAttributes);
            }
        }
    }

    public void refresh() {
        this.resourceMap = null;
        loadResourceDefine(queryUrl);
    }
}
