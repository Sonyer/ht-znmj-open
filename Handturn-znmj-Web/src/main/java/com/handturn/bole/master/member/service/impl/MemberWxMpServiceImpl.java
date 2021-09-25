package com.handturn.bole.master.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.master.member.entity.MemberWxMp;
import com.handturn.bole.master.member.mapper.MemberWxMpMapper;
import com.handturn.bole.master.member.service.IMemberWxMpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 智能门禁-微信小程序会员 Service实现
 *
 * @author Eric
 * @date 2020-03-16 13:19:19
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MemberWxMpServiceImpl extends ServiceImpl<MemberWxMpMapper, MemberWxMp> implements IMemberWxMpService {

    @Override
    public ICustomPage<MemberWxMp> findMemberWxMps(QueryRequest request, MemberWxMp memberWxMp) {
        CustomPage<MemberWxMp> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, memberWxMp);
    }

    @Override
    @Transactional
    public MemberWxMp saveMemberWxMp(MemberWxMp memberWxMp) {
        if(memberWxMp.getId() == null){
            this.save(memberWxMp);
            return memberWxMp;
        }else{
            MemberWxMp memberWxMpOld = this.baseMapper.selectById(memberWxMp.getId());
            CopyUtils.copyProperties(memberWxMp,memberWxMpOld);
            this.updateById(memberWxMpOld);
            return memberWxMpOld;
        }
    }

    @Override
    @Transactional
    public void enableMemberWxMp(String[] memberWxMpIds) {
        Arrays.stream(memberWxMpIds).forEach(memberWxMpId -> {
            MemberWxMp memberWxMp = this.baseMapper.selectById(memberWxMpId);
            memberWxMp.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(memberWxMp);
        });
	}

    @Override
    @Transactional
    public void disableMemberWxMp(String[] memberWxMpIds) {
        Arrays.stream(memberWxMpIds).forEach(memberWxMpId -> {
            MemberWxMp memberWxMp = this.baseMapper.selectById(memberWxMpId);
            memberWxMp.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(memberWxMp);
        });
    }

    @Override
    public MemberWxMp findMemberWxMpById(Long memberWxMpId){
        return this.baseMapper.selectOne(new QueryWrapper<MemberWxMp>().lambda().eq(MemberWxMp::getId, memberWxMpId));
    }

    /**
     * 通过Id获取信息
     * @param memberId
     * @return
     */
    @Override
    public  MemberWxMp findMemberWxMpByMemberId(Long memberId){
        return this.baseMapper.selectOne(new QueryWrapper<MemberWxMp>().lambda().eq(MemberWxMp::getBindMemberId, memberId));
    }

    /**
     * 通过微信OPENID获取微信用户信息
     * @param openId
     * @return
     */
    @Override
    public MemberWxMp findMemberWxMpById(String openId){
        return this.baseMapper.selectOne(new QueryWrapper<MemberWxMp>().lambda().eq(MemberWxMp::getOpenid, openId));
    }
}
