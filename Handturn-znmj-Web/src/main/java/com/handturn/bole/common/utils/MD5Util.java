package com.handturn.bole.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author Eric
 */
public class MD5Util {

    protected MD5Util() {

    }

    private static final String ALGORITH_NAME = "md5";

    private static final int HASH_ITERATIONS = 5;

    public static String encrypt(String username, String password) {
        String source = StringUtils.lowerCase(username);
        password = StringUtils.lowerCase(password);
        return new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(source), HASH_ITERATIONS).toHex();
    }

    public static void main(String[] args){
        System.out.println(encrypt("A1001101","123456"));
    }
}
