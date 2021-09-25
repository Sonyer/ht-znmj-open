package com.handturn.bole.sysconf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.entity.SysconfCodeRule;
import com.handturn.bole.sysconf.mapper.SysconfCodeRuleMapper;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleSeqService;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleService;
import com.handturn.bole.sysconf.service.SysconfCodeRuleContant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统配置-编码规则 Service实现
 *
 * @author MrBird
 * @date 2019-12-08 21:25:21
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysconfCodeRuleServiceImpl extends ServiceImpl<SysconfCodeRuleMapper, SysconfCodeRule> implements ISysconfCodeRuleService {

    @Autowired
    private ISysconfCodeRuleSeqService sysconfCodeRuleSeqService;

    @Override
    public ICustomPage<SysconfCodeRule> findSysconfCodeRules(QueryRequest request, SysconfCodeRule sysconfCodeRule) {
        CustomPage<SysconfCodeRule> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysconfCodeRule);
    }

    @Override
    @Transactional
    public void saveSysconfCodeRule(SysconfCodeRule sysconfCodeRule) {
        if(sysconfCodeRule.getId() == null){
            sysconfCodeRule.setStatus(BaseStatus.ENABLED);
            this.save(sysconfCodeRule);
        }else{
            SysconfCodeRule sysconfCodeRuleOld = this.baseMapper.selectById(sysconfCodeRule.getId());
            CopyUtils.copyProperties(sysconfCodeRule,sysconfCodeRuleOld);
            this.updateById(sysconfCodeRuleOld);
        }
    }

    @Override
    @Transactional
    public void enableSysconfCodeRule(String[] sysconfCodeRuleIds) {
        Arrays.stream(sysconfCodeRuleIds).forEach(sysconfCodeRuleId -> {
            SysconfCodeRule sysconfCodeRule = this.baseMapper.selectById(sysconfCodeRuleId);
            sysconfCodeRule.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysconfCodeRule);
        });
	}

    @Override
    @Transactional
    public void disableSysconfCodeRule(String[] sysconfCodeRuleIds) {
        Arrays.stream(sysconfCodeRuleIds).forEach(sysconfCodeRuleId -> {
            SysconfCodeRule sysconfCodeRule = this.baseMapper.selectById(sysconfCodeRuleId);
            sysconfCodeRule.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysconfCodeRule);
        });
    }

    @Override
    public SysconfCodeRule findSysconfCodeRuleById(Long sysconfCodeRuleId){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfCodeRule>().lambda().eq(SysconfCodeRule::getId, sysconfCodeRuleId));
    }


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
    @Override
    public String generateCode(String ruleCode, Map<String,String> params){
        SysconfCodeRule sysconfCodeRule = this.baseMapper.selectOne(new QueryWrapper<SysconfCodeRule>().lambda().eq(SysconfCodeRule::getRuleCode, ruleCode));
        String ruleCodeStr = sysconfCodeRule.getRuleCodeStr();

        String ruleStr = ruleCodeStr;

        //获取规则参数
        Pattern ruleParamP=Pattern.compile("\\[.*?\\]");
        Matcher ruleParamM=ruleParamP.matcher(ruleCodeStr);

        String seqStr = "";
        while(ruleParamM.find()){
            String param = ruleParamM.group(0).substring(0,ruleParamM.group(0).length());
            if(params != null && params.get(param) != null){
                ruleStr = ruleStr.replace(param,params.get(param));
            }

            if(param.contains(SysconfCodeRuleContant.CHECK_$SEQ)){
                seqStr = param;
            }
        }

        //日期时间
        if(ruleStr.contains(SysconfCodeRuleContant.$DATETIME)) {
            SimpleDateFormat simpleDateFormatDateTime = new SimpleDateFormat("yyMMddHHmmss");
            String dateTime = simpleDateFormatDateTime.format(new Date());
            ruleStr = ruleStr.replace(SysconfCodeRuleContant.$DATETIME,dateTime);
        }

        //日期
        if(ruleStr.contains(SysconfCodeRuleContant.$DATE)) {
            SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("yyMMdd");
            String date = simpleDateFormatDate.format(new Date());
            ruleStr = ruleStr.replace(SysconfCodeRuleContant.$DATE,date);
        }

        //时间
        if(ruleStr.contains(SysconfCodeRuleContant.$TIME)) {
            SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HHmmss");
            String time = simpleDateFormatTime.format(new Date());
            ruleStr = ruleStr.replace(SysconfCodeRuleContant.$TIME,time);
        }

        Long seqNum = sysconfCodeRuleSeqService.generateSeqNum(sysconfCodeRule.getRuleCode(),ruleStr,sysconfCodeRule.getRuleSeqInit());

        //流水
        if(StringUtils.isNotEmpty(seqStr)){
            String seqNumStr = "";
            seqNumStr = convertSeqNum(seqNum,seqStr);
            ruleStr = ruleStr.replace(seqStr,seqNumStr);
        }

        return ruleStr;

    }

    /**
     * 流水号转字符串
     * @param seqNum
     * @param seqStr
     * @return
     */
    private String convertSeqNum(Long seqNum,String seqStr){
        Pattern ruleParamP=Pattern.compile("\\{.*?\\}");
        Matcher ruleParamM=ruleParamP.matcher(seqStr);

        String seqNumStr = "";

        int length = 0;
        while(ruleParamM.find()){
            length = Integer.valueOf(ruleParamM.group(0).substring(1,ruleParamM.group(0).length()-1));
        }
        if (length == 0){
            seqNumStr = seqNum+"";
        }else{
            seqNumStr = autoGenericCode(seqNum,length);
        }

        return seqNumStr;
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     * @param code
     * @return
     */
    private String autoGenericCode(Long code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", code + 1);

        return result;
    }
}
