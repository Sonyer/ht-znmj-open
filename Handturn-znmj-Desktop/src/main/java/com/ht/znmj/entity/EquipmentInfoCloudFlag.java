package com.ht.znmj.entity;

/**
 * 设备云端同步状态
 */
public enum EquipmentInfoCloudFlag {
    NO_ASY("0","未同步"), ASYED("1","已同步");

    private String code;
    private String name;

    EquipmentInfoCloudFlag(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        EquipmentInfoCloudFlag[] equipmentInfoCloudFlags = values();
        for (EquipmentInfoCloudFlag equipmentInfoCloudFlag : equipmentInfoCloudFlags) {
            if (equipmentInfoCloudFlag.getCode().equals(value)) {
                return equipmentInfoCloudFlag.getName();
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
