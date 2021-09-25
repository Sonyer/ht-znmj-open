package com.handturn.bole.job.entity;

public enum ScheduleLogStatus {
    /**
     * 成功
     */
    SUCCESS("SUCCESS"),
    /**
     * 失败
     */
    ERROR("ERROR");

    private String value;

    ScheduleLogStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
