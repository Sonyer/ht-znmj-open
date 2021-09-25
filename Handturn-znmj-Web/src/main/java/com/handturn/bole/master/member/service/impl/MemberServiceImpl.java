package com.handturn.bole.master.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.common.utils.StringRandom;
import com.handturn.bole.master.member.entity.Member;
import com.handturn.bole.master.member.mapper.MemberMapper;
import com.handturn.bole.master.member.service.IMemberService;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.sysconf.service.SysconfCodeRuleContant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

/**
 * 智能门禁-平台会员 Service实现
 *
 * @author Eric
 * @date 2020-03-13 20:42:35
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {
    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;

    @Override
    public ICustomPage<Member> findMembers(QueryRequest request, Member member) {
        CustomPage<Member> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, member);
    }

    @Override
    @Transactional
    public Member saveMember(Member member) {
        if(member.getId() == null){
            //编码生成
            String ucode = StringRandom.getStringRandom(20);
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.LINLINGO_MEMBER_ACOUNT_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String accountName = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);

            member.setAccountCode(ucode);
            member.setNickName("用户-"+accountName);
            this.save(member);
            return member;
        }else{
            Member memberOld = this.baseMapper.selectById(member.getId());
            CopyUtils.copyProperties(member,memberOld);
            this.updateById(memberOld);
            return memberOld;
        }
    }

    /**
     * 特约会员
     *
     * @param memberIds memberIds
     */
    @Override
    @Transactional
    public void specialUserMember(String[] memberIds){
        Arrays.stream(memberIds).forEach(memberId -> {
            Member member = this.baseMapper.selectById(memberId);
            if(member.getSpecialUser().equals(BaseBoolean.TRUE)){
                throw new FebsException("会员:"+member.getAccountCode()+"已是特约会员,不需要重复操作!");
            }
            member.setSpecialUser(BaseBoolean.TRUE);
            member.setSpecialUserTime(new Date());
            this.baseMapper.updateById(member);
        });
    }

    /**
     * 取消特约会员
     *
     * @param memberIds memberIds
     */
    @Override
    @Transactional
    public void cancelSpecialUserMember(String[] memberIds){
        Arrays.stream(memberIds).forEach(memberId -> {
            Member member = this.baseMapper.selectById(memberId);
            if(member.getSpecialUser().equals(BaseBoolean.FALSE)){
                throw new FebsException("会员:"+member.getAccountCode()+"不是特约会员,不需要操作!");
            }
            member.setSpecialUser(BaseBoolean.FALSE);
            this.baseMapper.updateById(member);
        });
    }

    @Override
    @Transactional
    public void enableMember(String[] memberIds) {
        Arrays.stream(memberIds).forEach(memberId -> {
            Member member = this.baseMapper.selectById(memberId);
            member.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(member);
        });
	}

    @Override
    @Transactional
    public void disableMember(String[] memberIds) {
        Arrays.stream(memberIds).forEach(memberId -> {
            Member member = this.baseMapper.selectById(memberId);
            member.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(member);
        });
    }

    @Override
    public Member findMemberById(Long memberId){
        return this.baseMapper.selectOne(new QueryWrapper<Member>().lambda().eq(Member::getId, memberId));
    }

    /**
     * 账号编码获取
     * @param accountCode
     * @return
     */
    @Override
    public Member findMemberByAccountCode(String accountCode){
        return this.baseMapper.selectOne(new QueryWrapper<Member>().lambda().eq(Member::getAccountCode, accountCode));
    }

}
