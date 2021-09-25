package com.handturn.bole.common.utils;

import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

    /**
     * 关闭InputStream
     * @param inputStream
     */
    public static void closeInputStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭OutputStream
     * @param outputStream
     */
    public static void closeOutputStream(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
