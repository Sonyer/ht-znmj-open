package com.handturn.bole.main.visitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.authArea.service.IOcAuthAreaVisitorService;
import com.handturn.bole.main.visitor.entity.OcVisitorAuditStatus;
import com.handturn.bole.main.visitor.entity.OcVisitorFreezeStatus;
import com.handturn.bole.main.visitor.entity.OcVisitorInfo;
import com.handturn.bole.main.visitor.mapper.OcVisitorInfoMapper;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 网点-会员访客信息 Service实现
 *
 * @author Eric
 * @date 2020-09-22 08:57:38
 */
@Service("OcVisitorInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OcVisitorInfoServiceImpl extends ServiceImpl<OcVisitorInfoMapper, OcVisitorInfo> implements IOcVisitorInfoService {

    @Autowired
    private IOcAuthAreaVisitorService ocAuthAreaVisitorService;

    @Override
    public ICustomPage<OcVisitorInfo> findOcVisitorInfos(QueryRequest request, OcVisitorInfo ocVisitorInfo) {
        CustomPage<OcVisitorInfo> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, ocVisitorInfo);
    }

    @Override
    @Transactional
    public OcVisitorInfo saveOcVisitorInfo(OcVisitorInfo ocVisitorInfo) {
        if(ocVisitorInfo.getId() == null){
            this.save(ocVisitorInfo);
            return ocVisitorInfo;
        }else{
            OcVisitorInfo ocVisitorInfoOld = this.baseMapper.selectById(ocVisitorInfo.getId());
            CopyUtils.copyProperties(ocVisitorInfo,ocVisitorInfoOld);
            this.updateById(ocVisitorInfoOld);

            ocAuthAreaVisitorService.updateAuthOcVisitors(ocVisitorInfoOld.getId().toString(),ocVisitorInfoOld.getOrgCode(),ocVisitorInfoOld.getOcCode(),ocVisitorInfoOld.getOrgName(),ocVisitorInfoOld.getOcName());
            return ocVisitorInfoOld;
        }
    }

    @Override
    @Transactional
    public void freeze(String[] ocVisitorInfoIds) {
        Arrays.stream(ocVisitorInfoIds).forEach(ocVisitorInfoId -> {
            OcVisitorInfo ocVisitorInfo = this.baseMapper.selectById(ocVisitorInfoId);
            if(!ocVisitorInfo.getFreezeStatus().equals(OcVisitorFreezeStatus.FREEZE)){
                ocVisitorInfo.setFreezeStatus(OcVisitorFreezeStatus.FREEZE);
                this.baseMapper.updateById(ocVisitorInfo);
            }
        });
    }

    @Override
    @Transactional
    public void normal(String[] ocVisitorInfoIds) {
        Arrays.stream(ocVisitorInfoIds).forEach(ocVisitorInfoId -> {
            OcVisitorInfo ocVisitorInfo = this.baseMapper.selectById(ocVisitorInfoId);
            if(!ocVisitorInfo.getFreezeStatus().equals(OcVisitorFreezeStatus.NORMAL)){
                ocVisitorInfo.setFreezeStatus(OcVisitorFreezeStatus.NORMAL);
                this.baseMapper.updateById(ocVisitorInfo);
            }
        });
    }

    @Override
    public OcVisitorInfo findOcVisitorInfoById(Long ocVisitorInfoId){
        return this.baseMapper.selectOne(new QueryWrapper<OcVisitorInfo>().lambda().eq(OcVisitorInfo::getId, ocVisitorInfoId));
    }

    /**
     * 通过Id获取信息
     * @param ocVisitorInfoIds
     * @return
     */
    @Override
    public List<OcVisitorInfo> findOcVisitorInfoByIds(Set<String> ocVisitorInfoIds){
        QueryWrapper<OcVisitorInfo> queryWrapper = new QueryWrapper<OcVisitorInfo>();
        queryWrapper.lambda().in(OcVisitorInfo::getId,ocVisitorInfoIds);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 通过Id获取信息
     * @param idCardName
     * @return
     */
    @Override
    public OcVisitorInfo findOcVisitorInfoByIdCard(String idCardName,String phoneNumber,String ocCode,String orgCode){
        QueryWrapper<OcVisitorInfo> queryWrapper = new QueryWrapper<OcVisitorInfo>();
        queryWrapper.lambda().eq(OcVisitorInfo::getIdCardName,idCardName);
        queryWrapper.lambda().eq(OcVisitorInfo::getPhoneNumber,phoneNumber);
        queryWrapper.lambda().eq(OcVisitorInfo::getOcCode,ocCode);
        queryWrapper.lambda().eq(OcVisitorInfo::getOrgCode,orgCode);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 通过Id获取信息
     * @param memeberAccountCode
     * @return
     */
    @Override
    public Set<Long> findByAccountCode(String memeberAccountCode){
        QueryWrapper<OcVisitorInfo> queryWrapper = new QueryWrapper<OcVisitorInfo>();
        queryWrapper.lambda().eq(OcVisitorInfo::getMemberAccountCode,memeberAccountCode);
        queryWrapper.lambda().select(OcVisitorInfo::getId);
        List<OcVisitorInfo> visitorInfos = this.baseMapper.selectList(queryWrapper);

        Set<Long> visitorIds = new HashSet<>();
        visitorInfos.forEach(visitorInfo ->{
            visitorIds.add(visitorInfo.getId());
        });
        return visitorIds;
    }
}
