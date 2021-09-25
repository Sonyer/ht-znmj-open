package com.ht.znmj.entity;

/**
 * 设备状态
 */
public enum CloudInterfaceConnStatus {
    NO_CONNECT("0","未连接"), CONNECTED("1","已连接");

    private String code;
    private String name;

    CloudInterfaceConnStatus(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        CloudInterfaceConnStatus[] equipmentInfoStatuses = values();
        for (CloudInterfaceConnStatus equipmentInfoStatus : equipmentInfoStatuses) {
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
