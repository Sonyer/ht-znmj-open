package com.handturn.bole.job.entity;

public enum ScheduleStatus {
    /**
     * 正常
     */
    NORMAL("NORMAL"),
    /**
     * 暂停
     */
    PAUSE("PAUSE");

    private String value;

    ScheduleStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
