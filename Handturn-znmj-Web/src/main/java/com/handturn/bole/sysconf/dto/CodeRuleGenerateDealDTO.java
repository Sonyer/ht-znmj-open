package com.handturn.bole.sysconf.dto;

import lombok.Data;

import java.util.Map;

/**
 * 生成编号
 */
@Data
public class CodeRuleGenerateDealDTO {
    public String ruleCode;

    public Map<String, String> params;
}
