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
import com.handturn.bole.master.member.entity.MemberVisitorInfo;
import com.handturn.bole.master.member.mapper.MemberVisitorInfoMapper;
import com.handturn.bole.master.member.service.IMemberVisitorInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 会员-会员访客信息 Service实现
 *
 * @author Eric
 * @date 2020-09-22 08:57:18
 */
@Service("MemberVisitorInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MemberVisitorInfoServiceImpl extends ServiceImpl<MemberVisitorInfoMapper, MemberVisitorInfo> implements IMemberVisitorInfoService {

    @Override
    public ICustomPage<MemberVisitorInfo> findMemberVisitorInfos(QueryRequest request, MemberVisitorInfo memberVisitorInfo) {
        CustomPage<MemberVisitorInfo> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, memberVisitorInfo);
    }

    @Override
    @Transactional
    public MemberVisitorInfo saveMemberVisitorInfo(MemberVisitorInfo memberVisitorInfo) {
        if(memberVisitorInfo.getId() == null){
            this.save(memberVisitorInfo);
            return memberVisitorInfo;
        }else{
            MemberVisitorInfo memberVisitorInfoOld = this.baseMapper.selectById(memberVisitorInfo.getId());
            CopyUtils.copyProperties(memberVisitorInfo,memberVisitorInfoOld);
            this.updateById(memberVisitorInfoOld);
            return memberVisitorInfoOld;
        }
    }

    @Override
    @Transactional
    public void enableMemberVisitorInfo(String[] memberVisitorInfoIds) {
        Arrays.stream(memberVisitorInfoIds).forEach(memberVisitorInfoId -> {
            MemberVisitorInfo memberVisitorInfo = this.baseMapper.selectById(memberVisitorInfoId);
            memberVisitorInfo.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(memberVisitorInfo);
        });
	}

    @Override
    @Transactional
    public void disableMemberVisitorInfo(String[] memberVisitorInfoIds) {
        Arrays.stream(memberVisitorInfoIds).forEach(memberVisitorInfoId -> {
            MemberVisitorInfo memberVisitorInfo = this.baseMapper.selectById(memberVisitorInfoId);
            memberVisitorInfo.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(memberVisitorInfo);
        });
    }

    @Override
    public MemberVisitorInfo findMemberVisitorInfoById(Long memberVisitorInfoId){
        return this.baseMapper.selectOne(new QueryWrapper<MemberVisitorInfo>().lambda().eq(MemberVisitorInfo::getId, memberVisitorInfoId));
    }

    /**
     * 通过Id获取信息
     * @param memeberAccountCode
     * @return
     */
    @Override
    public Set<Long> findByAccountCode(String memeberAccountCode){
        QueryWrapper<MemberVisitorInfo> queryWrapper = new QueryWrapper<MemberVisitorInfo>();
        queryWrapper.lambda().eq(MemberVisitorInfo::getMemberAccountCode,memeberAccountCode);
        queryWrapper.lambda().select(MemberVisitorInfo::getId);
        List<MemberVisitorInfo> visitorInfos = this.baseMapper.selectList(queryWrapper);

        Set<Long> visitorIds = new HashSet<>();
        visitorInfos.forEach(visitorInfo ->{
            visitorIds.add(visitorInfo.getId());
        });
        return visitorIds;
    }
}
