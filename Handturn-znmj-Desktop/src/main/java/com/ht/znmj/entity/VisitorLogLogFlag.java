package com.ht.znmj.entity;

/**
 * 设备出入标记
 */
public enum VisitorLogLogFlag {
    LINE_OFF("0","离线"), LINE_ON("1","在线");

    private String code;
    private String name;

    VisitorLogLogFlag(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        VisitorLogLogFlag[] equipmentInfoInOuts = values();
        for (VisitorLogLogFlag equipmentInfoInOut : equipmentInfoInOuts) {
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
