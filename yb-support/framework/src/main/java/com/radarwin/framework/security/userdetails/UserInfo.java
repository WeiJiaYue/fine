package com.radarwin.framework.security.userdetails;

import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Josh
 */
public class UserInfo extends org.springframework.security.core.userdetails.User {

    // private static final long serialVersionUID = -8165305738818088471L;
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public UserInfo(String username, String password, boolean enabled,
                    boolean accountNonExpired, boolean credentialsNonExpired,
                    boolean accountNonLocked, java.util.Collection<GrantedAuthority> authorities)
            throws IllegalArgumentException {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public void setAttribute(String key, Object value) {
        if (getAttributes().containsKey(key)) {
            getAttributes().remove(key);
        } else {
            getAttributes().put(key, value);
        }
    }

    public void setAttributes(Map attribute) {
        getAttributes().putAll(attribute);
    }

    public Object getAttribute(String key) {
        return getAttributes().get(key);
    }

    public void removeAttribute(String key) {
        if (getAttributes().containsKey(key)) {
            getAttributes().remove(key);
        }
    }

    /**
     * @return the attributes
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

}
