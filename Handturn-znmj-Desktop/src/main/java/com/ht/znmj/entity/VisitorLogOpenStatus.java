package com.ht.znmj.entity;

/**
 * 设备出入标记
 */
public enum VisitorLogOpenStatus {
    NONE("0","未识别人员"), OPEN("1","已识别人员");

    private String code;
    private String name;

    VisitorLogOpenStatus(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String value) {
        VisitorLogOpenStatus[] equipmentInfoInOuts = values();
        for (VisitorLogOpenStatus equipmentInfoInOut : equipmentInfoInOuts) {
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
