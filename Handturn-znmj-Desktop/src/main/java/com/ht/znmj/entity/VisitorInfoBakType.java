package com.ht.znmj.entity;

/**
 * 人员备份类型
 */
public enum VisitorInfoBakType {
    ALL("0","全量备份"), QUERY("1","查询备份");

    private String code;
    private String name;

    VisitorInfoBakType(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        VisitorInfoBakType[] equipmentInfoInOuts = values();
        for (VisitorInfoBakType equipmentInfoInOut : equipmentInfoInOuts) {
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
