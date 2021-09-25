package com.ht.znmj.entity;

/**
 * 设备状态
 */
public enum EquipmentInfoConnStatus {
    NO_CONNECT("0","未连接"), CONNECTED("1","已连接");

    private String code;
    private String name;

    EquipmentInfoConnStatus(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        EquipmentInfoConnStatus[] equipmentInfoStatuses = values();
        for (EquipmentInfoConnStatus equipmentInfoStatus : equipmentInfoStatuses) {
            if (equipmentInfoStatus.getCode().equals(value)) {
                return equipmentInfoStatus.getName();
            }
        }
        return "";
    }

    public String getCode(){
        return code;
    }

    public String getName(){
        return name;
    }
}
