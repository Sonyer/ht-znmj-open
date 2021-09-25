package com.ht.znmj.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端Select Option展现
 */
@Data
public class OptionVo implements Serializable {
    private String value;

    private String text;

    private Object obj;
}
