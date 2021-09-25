package com.ht.znmj.entity;

/**
 * 人员备份恢复类型
 */
public enum VisitorInfoBakRestType {
    CLEAN("0","覆盖恢复"), ADD("1","增量恢复");

    private String code;
    private String name;

    VisitorInfoBakRestType(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        VisitorInfoBakRestType[] equipmentInfoInOuts = values();
        for (VisitorInfoBakRestType equipmentInfoInOut : equipmentInfoInOuts) {
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
