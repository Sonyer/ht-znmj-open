package com.ht.znmj.websocket.constant;

/**
 * WEBSOCKET常量
 */
public class WebSocketConstant {
    //连接常量
    public final static String CONNECT_PARAM_OCTOKEN = "ocToken";  //网点参数
    public final static String CONNECT_PARAM_MAC = "mac";  //客户端mac
    public final static String CONNECT_PARAM_LANIP = "lanIp";  //IP地址
    public final static String CONNECT_PARAM_ACCOUNTCODE = "accountCode";  //账号
    public final static String CONNECT_PARAM_PASSWORD = "password";  //密码

    public final static String CONNECT_PARAM_APPVERSION = "appVersion"; //应用版本号
    public final static String CONNECT_PARAM_SYSTEMNAME = "systemName";  //系统名称
    public final static String CONNECT_PARAM_SYSTEMVERSION = "systemVersion";  //系统版本


    //接口类型
    public final static String INTERFACE_TYPE_HEARTBEAT = "INTERFACE_TYPE_HEARTBEAT";  //心跳
    public final static String INTERFACE_TYPE_ASY_APPLY = "INTERFACE_TYPE_ASY_APPLY";  //同步申请
    public final static String INTERFACE_TYPE_ASY_APPLY_EQUIPMENT_RETURN = "INTERFACE_TYPE_ASY_APPLY_EQUIPMENT_RETURN";  //同步申请设备返回
    public final static String INTERFACE_TYPE_ASY_APPLY_VISITOR_RETURN = "INTERFACE_TYPE_ASY_APPLY_VISITOR_RETURN";  //同步申请人员返回
    public final static String INTERFACE_TYPE_VISITORLOG_REQUEST = "INTERFACE_TYPE_VISITORLOG_REQUEST";  //日志请求

    public final static String INTERFACE_TYPE_EQUIPMENT_REMOTEOPEN = "INTERFACE_TYPE_EQUIPMENT_REMOTEOPEN"; //远程开门

}
