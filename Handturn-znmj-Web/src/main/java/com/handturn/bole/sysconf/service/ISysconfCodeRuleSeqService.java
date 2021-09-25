package com.handturn.bole.sysconf.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfCodeRuleSeq;

/**
 * 系统配置-编码流水 Service接口
 *
 * @author MrBird
 * @date 2019-12-08 21:44:27
 */
public interface ISysconfCodeRuleSeqService extends IService<SysconfCodeRuleSeq> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysconfCodeRuleSeq sysconfCodeRuleSeq
     * @return ICustomPage<SysconfCodeRuleSeq>
     */
    ICustomPage<SysconfCodeRuleSeq> findSysconfCodeRuleSeqs(QueryRequest request, SysconfCodeRuleSeq sysconfCodeRuleSeq);

    /**
     * 修改
     *
     * @param sysconfCodeRuleSeq sysconfCodeRuleSeq
     */
    void saveSysconfCodeRuleSeq(SysconfCodeRuleSeq sysconfCodeRuleSeq);

    /**
     * 启用
     *
     * @param sysconfCodeRuleSeqIds sysconfCodeRuleSeqIds
     */
    void enableSysconfCodeRuleSeq(String[] sysconfCodeRuleSeqIds);

    /**
    * 禁用
    *
    * @param sysconfCodeRuleSeqIds sysconfCodeRuleSeqIds
    */
    void disableSysconfCodeRuleSeq(String[] sysconfCodeRuleSeqIds);


    /**
    * 通过Id获取信息
    * @param sysconfCodeRuleSeqId
    * @return
    */
    SysconfCodeRuleSeq findSysconfCodeRuleSeqById(Long sysconfCodeRuleSeqId);

    /**
     * 生成编码
     * @param ruleCode
     * @param ruleCodeStr
     * @return
     */
    Long generateSeqNum(String ruleCode,String ruleCodeStr,Long initSeq);
}
