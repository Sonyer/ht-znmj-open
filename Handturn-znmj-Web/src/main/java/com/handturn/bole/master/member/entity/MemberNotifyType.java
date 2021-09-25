package com.handturn.bole.master.member.entity;

/**
 * 会员通知 类型
 */
public class MemberNotifyType {
    public static String VALIDATE_CODE_NOTIFY = "VALIDATE_CODE_NOTIFY";   //验证码通知

    public static String VISITOR_APPLY_NOTIFY = "VISITOR_APPLY_NOTIFY";   //访客申请通知
    public static String VISITOR_AUDIT_NOTIFY = "VISITOR_AUDIT_NOTIFY";   //负责人审核完成


    public static String getRedisInnerNotifyTypesKey(){
        return "INNER_NOTIFY_TYPES_KEY";
    }

    public static String getRedisKey(String notifyType,String accountCode){
        return accountCode+"|"+notifyType;
    }

    //统一语术
    /*public static String toMessage(String notifyType,String defindMessage,String[] params){
        if(!StringUtils.isEmpty(defindMessage)){
            return defindMessage;
        }

        String message = "";
        if(notifyType.equals(VISITOR_APPLY_NOTIFY)){
            message = "收到客户订单,请及时发货，单号:{0}。";
        }
        if(notifyType.equals(VISITOR_AUDIT_NOTIFY)){
            message = "收到客户退货申请,请及时处理，单号:{0}。";
        }

        if(params != null){
            for(int i = 0; i < params.length; i++){
                message = message.replace("{"+i+"}",params[i]);
            }
        }

        return message;
    }*/
}
