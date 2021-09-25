package com.handturn.bole.sysconf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.entity.SysconfCodeRuleSeq;
import com.handturn.bole.sysconf.mapper.SysconfCodeRuleSeqMapper;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 系统配置-编码流水 Service实现
 *
 * @author MrBird
 * @date 2019-12-08 21:44:27
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysconfCodeRuleSeqServiceImpl extends ServiceImpl<SysconfCodeRuleSeqMapper, SysconfCodeRuleSeq> implements ISysconfCodeRuleSeqService {

    @Override
    public ICustomPage<SysconfCodeRuleSeq> findSysconfCodeRuleSeqs(QueryRequest request, SysconfCodeRuleSeq sysconfCodeRuleSeq) {
        CustomPage<SysconfCodeRuleSeq> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysconfCodeRuleSeq);
    }

    @Override
    @Transactional
    public void saveSysconfCodeRuleSeq(SysconfCodeRuleSeq sysconfCodeRuleSeq) {
        if(sysconfCodeRuleSeq.getId() == null){
            this.save(sysconfCodeRuleSeq);
        }else{
            SysconfCodeRuleSeq sysconfCodeRuleSeqOld = this.baseMapper.selectById(sysconfCodeRuleSeq.getId());
            CopyUtils.copyProperties(sysconfCodeRuleSeq,sysconfCodeRuleSeqOld);
            this.updateById(sysconfCodeRuleSeqOld);
        }
    }

    @Override
    @Transactional
    public void enableSysconfCodeRuleSeq(String[] sysconfCodeRuleSeqIds) {
        Arrays.stream(sysconfCodeRuleSeqIds).forEach(sysconfCodeRuleSeqId -> {
            SysconfCodeRuleSeq sysconfCodeRuleSeq = this.baseMapper.selectById(sysconfCodeRuleSeqId);
            sysconfCodeRuleSeq.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysconfCodeRuleSeq);
        });
	}

    @Override
    @Transactional
    public void disableSysconfCodeRuleSeq(String[] sysconfCodeRuleSeqIds) {
        Arrays.stream(sysconfCodeRuleSeqIds).forEach(sysconfCodeRuleSeqId -> {
            SysconfCodeRuleSeq sysconfCodeRuleSeq = this.baseMapper.selectById(sysconfCodeRuleSeqId);
            sysconfCodeRuleSeq.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysconfCodeRuleSeq);
        });
    }

    @Override
    public SysconfCodeRuleSeq findSysconfCodeRuleSeqById(Long sysconfCodeRuleSeqId){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfCodeRuleSeq>().lambda().eq(SysconfCodeRuleSeq::getId, sysconfCodeRuleSeqId));
    }

    /**
     * 生成编码
     * @param ruleCode
     * @param ruleCodeStr
     * @return
     */
    @Override
    @Transactional
    public synchronized Long generateSeqNum(String ruleCode,String ruleCodeStr,Long seqInit){
        SysconfCodeRuleSeq sysconfCodeRuleSeq = this.baseMapper.selectOne(
                new QueryWrapper<SysconfCodeRuleSeq>().lambda()
                        .eq(SysconfCodeRuleSeq::getRuleCode, ruleCode)
                        .eq(SysconfCodeRuleSeq::getRuleCodeKey, ruleCodeStr));
        if(sysconfCodeRuleSeq == null){
            sysconfCodeRuleSeq = new SysconfCodeRuleSeq();
            sysconfCodeRuleSeq.setRuleCode(ruleCode);
            sysconfCodeRuleSeq.setRuleName(ruleCode);
            sysconfCodeRuleSeq.setRuleCodeKey(ruleCodeStr);
            sysconfCodeRuleSeq.setRuleCodeInit(seqInit);
            sysconfCodeRuleSeq.setSeqNum(seqInit);
            sysconfCodeRuleSeq.setStatus(BaseStatus.ENABLED);
            sysconfCodeRuleSeq.setIsSysCreated(BaseBoolean.TRUE);
            sysconfCodeRuleSeq.setSeqNum(sysconfCodeRuleSeq.getSeqNum()+1);
            this.save(sysconfCodeRuleSeq);
        }else{
            sysconfCodeRuleSeq.setSeqNum(sysconfCodeRuleSeq.getSeqNum()+1);
            this.updateById(sysconfCodeRuleSeq);
        }

        return sysconfCodeRuleSeq.getSeqNum();
    }
}
