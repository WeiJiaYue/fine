package com.radarwin.framework.security.passwordencoder;

import com.radarwin.framework.util.AlgorithmUtil;
import org.springframework.security.authentication.encoding.BaseDigestPasswordEncoder;

/**
 * ¬
 * Created by josh on 15/7/28.
 */
public class Md5PasswordEncoder extends BaseDigestPasswordEncoder {

    @Override
    public String encodePassword(String rawPass, Object salt) {
        return AlgorithmUtil.md5(rawPass);
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String pass1 = "" + encPass;
        String pass2 = this.encodePassword(rawPass, salt);
        return PasswordEncoderUtils.equals(pass1, pass2);
    }
}
