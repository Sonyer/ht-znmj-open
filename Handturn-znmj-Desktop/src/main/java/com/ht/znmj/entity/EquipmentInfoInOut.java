package com.ht.znmj.entity;

/**
 * 设备出入标记
 */
public enum EquipmentInfoInOut {
    NONE("0","未知位置"), IN("1","入口"),OUT("2","出口");

    private String code;
    private String name;

    EquipmentInfoInOut(String code,String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        EquipmentInfoInOut[] equipmentInfoInOuts = values();
        for (EquipmentInfoInOut equipmentInfoInOut : equipmentInfoInOuts) {
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
