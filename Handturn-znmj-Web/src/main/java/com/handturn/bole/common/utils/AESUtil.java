package com.handturn.bole.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    /**  密钥算法 */
    private static final String ALGORITHM = "AES";
    /**  加解密算法/工作模式/填充方式  */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";
    /** AES加密  */
    public static String encryptData(String data,String password) throws Exception {
        // 创建密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        SecretKeySpec key = new SecretKeySpec(MD5Utils.MD5(password).toLowerCase().getBytes(), ALGORITHM);
        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64Util.encode(cipher.doFinal(data.getBytes()));
    }

    /** AES解密 */
    public static String decryptData(String base64Data,String password) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        SecretKeySpec key = new SecretKeySpec(MD5Utils.MD5(password).toLowerCase().getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decode = Base64Util.decode(base64Data);
        byte[] doFinal = cipher.doFinal(decode);
        return new String(doFinal,"utf-8");
    }
}
