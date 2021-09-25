package com.handturn.bole.sysconf.constant;

/**
 * 系统配置参数
 */
public class SysconfGlobalConstant {
    public static class SysconfGlobalGroupCode{
        public final static String TEMPLET_FILE_PATH = "templet_file_path";
        public final static String TEMPLET_PRINTE_FILE_PATH = "templet_printe_file_path";
        public final static String TEMPLET_LOGO_FILE_PATH = "templet_logo_file_path";

        public final static String QINIU_STORE_SECRET = "QINIU_STORE_SECRET";
        public final static String LOCATION_STORE_CONF = "LOCATION_STORE_CONF";
        public final static String FILE_STORE_METHOD = "FILE_STORE_METHOD";
    }

    public static class TempletFilePathKey{
        public final static String linux = "linux";
        public final static String window = "window";
    }

    public static class TempletPrinteFilePathKey{
        public final static String linux = "linux";
        public final static String window = "window";
    }

    public static class LOCATION_STORE_CONF{
        public final static String physical_path = "physical_path";
        public final static String request_url = "request_url";
    }

    public static class QINIU_STORE_SECRET{
        public final static String access_key = "access_key";
        public final static String secret_key = "secret_key";
        public final static String bucket = "bucket";
    }

    public static class FILE_STORE_METHOD{
        public final static String FILE_STORE_METHOD = "FILE_STORE_METHOD";
    }
}
