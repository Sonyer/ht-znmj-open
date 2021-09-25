package com.handturn.bole.main.visitor.entity;

import lombok.Data;

/**
 * 抓拍状态
 *
 * @author Eric
 * @date 2020-12-06 17:24:27
 */
@Data
public class OcVisiteLogOpenStatus{
    public static final String NONE = "0";   //未识别
    public static final String OPEN = "1";   //已识别
}
