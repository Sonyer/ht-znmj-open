package com.handturn.bole.common.utils;

import java.util.Base64;

public class Base64Util {
    /** 解码  */
    public static byte[] decode(String encodedText){
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(encodedText);
    }
    /** 编码 */
    public static String encode(byte[] data){
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }
}
