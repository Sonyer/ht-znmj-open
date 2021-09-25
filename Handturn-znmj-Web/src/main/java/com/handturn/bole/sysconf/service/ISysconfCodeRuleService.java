package com.handturn.bole.sysconf.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfCodeRule;

import java.util.Map;

/**
 * 系统配置-编码规则 Service接口
 *
 * @author MrBird
 * @date 2019-12-08 21:25:21
 */
public interface ISysconfCodeRuleService extends IService<SysconfCodeRule> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysconfCodeRule sysconfCodeRule
     * @return ICustomPage<SysconfCodeRule>
     */
    ICustomPage<SysconfCodeRule> findSysconfCodeRules(QueryRequest request, SysconfCodeRule sysconfCodeRule);

    /**
     * 修改
     *
     * @param sysconfCodeRule sysconfCodeRule
     */
    void saveSysconfCodeRule(SysconfCodeRule sysconfCodeRule);

    /**
     * 启用
     *
     * @param sysconfCodeRuleIds sysconfCodeRuleIds
     */
    void enableSysconfCodeRule(String[] sysconfCodeRuleIds);

    /**
    * 禁用
    *
    * @param sysconfCodeRuleIds sysconfCodeRuleIds
    */
    void disableSysconfCodeRule(String[] sysconfCodeRuleIds);


    /**
    * 通过Id获取信息
    * @param sysconfCodeRuleId
    * @return
    */
    SysconfCodeRule findSysconfCodeRuleById(Long sysconfCodeRuleId);

    /**
     * 编号生成
     * * params
     *  * [$CU]    --客户编号
     *  * [$ORG]   --组织编号
     *  * [$OC]    --网点编号
     *  * [$DATE]  --日期yyMMdd
     *  * [$TIME]  --时间HH24mmss
     *  * [$SEQ{6}]  --流水号“{}”内位数
     * @param ruleCode
     * @return
     */
    String generateCode(String ruleCode, Map<String,String> params);


}
