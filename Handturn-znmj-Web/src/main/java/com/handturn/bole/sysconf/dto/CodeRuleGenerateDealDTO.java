package com.handturn.bole.sysconf.dto;

import lombok.Data;

import java.util.Map;

/**
 * įæįžå·
 */
@Data
public class CodeRuleGenerateDealDTO {
    public String ruleCode;

    public Map<String, String> params;
}
