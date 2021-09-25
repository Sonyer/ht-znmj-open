package com.handturn.bole.main.visitor.entity;

import lombok.Data;

/**
 * 日志类型
 *
 * @author Eric
 * @date 2020-12-06 17:24:27
 */
@Data
public class OcVisiteLogFlag{
    public static final String LINE_OFF = "0";   //离线数据
    public static final String LINE_ON = "1";   //在线数据
}
