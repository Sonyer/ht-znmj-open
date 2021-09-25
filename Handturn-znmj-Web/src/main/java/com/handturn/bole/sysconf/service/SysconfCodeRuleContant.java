package com.handturn.bole.sysconf.service;

public class SysconfCodeRuleContant {
    public static final String $CU = "[$CU]";  //客户编号
    public static final String $ORG = "[$ORG]";  //组织编号
    public static final String $OC = "[$OC]";  //网点编号
    public static final String $DATETIME = "[$DATETIME]";   //日期时间
    public static final String $DATE = "[$DATE]";  //日期yyMMdd
    public static final String $TIME = "[$TIME]";  //时间HH24mmss
    public static final String $SEQ = "[$SEQ{6}]";  //流水号“{}”内位数

    public static final String CHECK_$SEQ = "$SEQ";  //流水号“{}”内位数

    public static final String SYSCONF_QUERY_REPORT_CODE = "SYSCONF_QUERY_REPORT_CODE"; //报表编码规则
    public static final String SYS_RESOURCE_CODE = "SYS_RESOURCE_CODE"; //资源管理编码规则
    public static final String SYS_ORGANIZATION_CODE = "SYS_ORGANIZATION_CODE"; //组织编码规则
    public static final String SYS_ORGANIZATION_OC_CODE = "SYS_ORGANIZATION_OC_CODE"; //网点编码规则
    public static final String SYS_ROLE_CODE = "SYS_ROLE_CODE"; //角色编码规则
    public static final String SYS_ORGANIZATION_DEP_CODE = "SYS_ORGANIZATION_DEP_CODE"; //部门编码规则
    public static final String BAS_ITEM_CODE = "BAS_ITEM_CODE"; //商品编码规则
    public static final String BAS_VENDOR_CODE = "BAS_VENDOR_CODE"; //供应商编码规则
    public static final String BAS_DELIVERY_LOCATION_CODE = "BAS_DELIVERY_LOCATION_CODE"; //配送点编码规则

    public static final String INB_RECEIPT_CODE = "INB_RECEIPT_CODE"; //入库单管理编码规则
    public static final String OUB_SHIPMENT_CODE = "OUB_SHIPMENT_CODE"; //出库单管理编码规则

    public static final String INV_WORK_PICK_GROUP_CODE = "INV_WORK_PICK_GROUP_CODE"; //批次组编号规则
    public static final String INV_WORK_PICK_HEADER_CODE = "INV_WORK_PICK_HEADER_CODE"; //批次组头规则
    public static final String INV_WORK_PICK_CONTAINER_CODE = "INV_WORK_PICK_CONTAINER_CODE"; //批次周转箱规则

    public static final String OUB_DELIVERY_BATCH_CODE = "OUB_DELIVERY_BATCH_CODE"; //批次周转箱规则

    public static final String LINLINGO_BAS_CATEGORY_CODE = "LINLINGO_BAS_CATEGORY_CODE"; //商品分类
    public static final String LINLINGO_BAS_CHANNEL_CODE = "LINLINGO_BAS_CHANNEL_CODE"; //频道栏目

    public static final String LINLINGO_MEMBER_ACOUNT_CODE = "LINLINGO_MEMBER_ACOUNT_CODE"; //会员账号编码
    public static final String LINLINGO_PUBLISH_LIST_CODE = "LINLINGO_PUBLISH_LIST_CODE"; //频道栏目
    public static final String LINLINGO_BUYER_ORDER_CODE = "LINLINGO_BUYER_ORDER_CODE"; //订单编码
    public static final String LINLINGO_MEMBER_AMOUNT_RECORD_CODE = "LINLINGO_MEMBER_AMOUNT_RECORD_CODE"; //交易记录编码
    public static final String LINLINGO_MEMBER_PURSE_TRANSFERS_CODE = "LINLINGO_MEMBER_PURSE_TRANSFERS_CODE"; //提现记录编码


    public static final String BUSINESS_BAS_CATEGORY_CODE = "BUSINESS_BAS_CATEGORY_CODE";  //商户商品分类编号
    public static final String BUSINESS_BAS_ITEM_CODE = "BUSINESS_BAS_ITEM_CODE";  //商户商品编号


    //--------------------智能门禁-编码规则---------------------
    public static final String ZNMJ_AREAINFO_CODE = "ZNMJ_AREAINFO_CODE";  //智能门禁-区域编码
    public static final String ZNMJ_EQUIPMENTINFO_CODE = "ZNMJ_EQUIPMENTINFO_CODE";  //智能门禁-设备系统编码

    public static final String ZNMJ_AUTH_AREA_CODE = "ZNMJ_AUTH_AREA_CODE";  //智能门禁-权限区域编码


}
