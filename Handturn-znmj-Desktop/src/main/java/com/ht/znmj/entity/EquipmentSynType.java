package com.ht.znmj.entity;

/**
 * 设备同步类型
 */
public enum EquipmentSynType {
    NOASY("0","手工授权"), ASY("1","一键授权");

    private String code;
    private String name;

    EquipmentSynType(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        EquipmentSynType[] equipmentInfoInOuts = values();
        for (EquipmentSynType equipmentInfoInOut : equipmentInfoInOuts) {
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
