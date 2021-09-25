package com.ht.znmj.entity;

/**
 * 人员是否过期
 */
public enum VisitorInfoIsActive {
    UNACTIVE("0","无效"), ACTIVE("1","有效");

    private String code;
    private String name;

    VisitorInfoIsActive(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        VisitorInfoIsActive[] equipmentInfoInOuts = values();
        for (VisitorInfoIsActive equipmentInfoInOut : equipmentInfoInOuts) {
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
