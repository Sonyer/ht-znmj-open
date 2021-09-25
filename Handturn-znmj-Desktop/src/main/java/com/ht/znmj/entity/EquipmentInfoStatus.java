package com.ht.znmj.entity;

/**
 * 设备状态
 */
public enum EquipmentInfoStatus {
    UN_ACTIVE("0","无效"), ACTIVE("1","有效");

    private String code;
    private String name;

    EquipmentInfoStatus(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        EquipmentInfoStatus[] equipmentInfoStatuses = values();
        for (EquipmentInfoStatus equipmentInfoStatus : equipmentInfoStatuses) {
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
