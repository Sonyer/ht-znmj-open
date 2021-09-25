package com.handturn.bole.sysconf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.entity.SysconfCodeRule;

/**
 * 系统配置-编码规则 Service接口
 *
 * @author MrBird
 * @date 2019-12-08 21:25:21
 */
public interface ISysconfCodeRuleOptionService extends IService<SysconfCodeRule> {
    /**
     * 是否完成处理 -线程锁
     * @param codeRuleGenerateDealDTO
     * @return
     */
    String generateCodeMain(CodeRuleGenerateDealDTO codeRuleGenerateDealDTO);


}
