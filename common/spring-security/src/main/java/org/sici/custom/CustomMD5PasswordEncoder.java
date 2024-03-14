package org.sici.custom;

import util.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @projectName: oa-parent
 * @package: org.sici.custom
 * @className: CustomMD5PasswordEncoder
 * @author: 749291
 * @description: TODO
 * @date: 3/9/2024 4:38 PM
 * @version: 1.0
 */

@Component
public class CustomMD5PasswordEncoder implements PasswordEncoder {
    public String encode(CharSequence rawPassword) {
        return MD5.encrypt(rawPassword.toString());
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5.encrypt(rawPassword.toString()));
    }
}
