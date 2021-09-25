package com.ht.znmj.entity;

/**
 * 人员授权类型
 */
public enum VisitorInfoAuthType {
    ALL("0","按全量人员"), QUERY("1","按查询人员"),CHECK("2","按选择人员");

    private String code;
    private String name;

    VisitorInfoAuthType(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        VisitorInfoAuthType[] equipmentInfoInOuts = values();
        for (VisitorInfoAuthType equipmentInfoInOut : equipmentInfoInOuts) {
            if (equipmentInfoInOut.getCode().equals(value)) {
                return equipmentInfoInOut.getName();
            }
        }
        return null;
    }

    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name;
    }
}
