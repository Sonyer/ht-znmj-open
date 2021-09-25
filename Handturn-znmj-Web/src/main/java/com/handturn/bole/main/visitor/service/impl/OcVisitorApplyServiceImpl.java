package com.handturn.bole.main.visitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.authArea.entity.OcAuthArea;
import com.handturn.bole.main.authArea.service.IOcAuthAreaService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaVisitorService;
import com.handturn.bole.main.visitor.entity.*;
import com.handturn.bole.main.visitor.mapper.OcVisitorApplyMapper;
import com.handturn.bole.main.visitor.service.IOcVisitorApplyService;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import com.handturn.bole.main.visitor.service.IOcVisitorUploadImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 网点-会员访客审核 Service实现
 *
 * @author Eric
 * @date 2020-09-22 08:57:57
 */
@Service("OcVisitorApplyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OcVisitorApplyServiceImpl extends ServiceImpl<OcVisitorApplyMapper, OcVisitorApply> implements IOcVisitorApplyService {

    @Autowired
    private IOcVisitorInfoService ocVisitorInfoService;

    @Autowired
    private IOcAuthAreaVisitorService ocAuthAreaVisitorService;

    @Autowired
    private IOcAuthAreaService ocAuthAreaService;

    @Autowired
    private IOcVisitorUploadImgService ocVisitorUploadImgService;

    @Override
    public ICustomPage<OcVisitorApply> findOcVisitorApplys(QueryRequest request, OcVisitorApply ocVisitorApply) {
        CustomPage<OcVisitorApply> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, ocVisitorApply);
    }

    @Override
    @Transactional
    public OcVisitorApply saveOcVisitorApply(OcVisitorApply ocVisitorApply) {
        if(ocVisitorApply.getId() == null){
            this.save(ocVisitorApply);
            return ocVisitorApply;
        }else{
            OcVisitorApply ocVisitorApplyOld = this.baseMapper.selectById(ocVisitorApply.getId());
            CopyUtils.copyProperties(ocVisitorApply,ocVisitorApplyOld);
            this.updateById(ocVisitorApplyOld);
            return ocVisitorApplyOld;
        }
    }

    @Override
    @Transactional
    public void enableOcVisitorApply(String[] ocVisitorApplyIds) {
        Arrays.stream(ocVisitorApplyIds).forEach(ocVisitorApplyId -> {
            OcVisitorApply ocVisitorApply = this.baseMapper.selectById(ocVisitorApplyId);
            //ocVisitorApply.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(ocVisitorApply);
        });
	}

    @Override
    @Transactional
    public void disableOcVisitorApply(String[] ocVisitorApplyIds) {
        Arrays.stream(ocVisitorApplyIds).forEach(ocVisitorApplyId -> {
            OcVisitorApply ocVisitorApply = this.baseMapper.selectById(ocVisitorApplyId);
            //ocVisitorApply.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(ocVisitorApply);
        });
    }

    @Override
    public OcVisitorApply findOcVisitorApplyById(Long ocVisitorApplyId){
        return this.baseMapper.selectOne(new QueryWrapper<OcVisitorApply>().lambda().eq(OcVisitorApply::getId, ocVisitorApplyId));
    }

    /**
     * 审核保存
     * @param ocVisitorApply
     * @return
     */
    @Override
    @Transactional
    public void auditsSave(OcVisitorApply ocVisitorApply){
        String ids = ocVisitorApply.getIds();
        String[] ocVisitorApplyIdArr = ids.split(StringPool.COMMA);
        Arrays.stream(ocVisitorApplyIdArr).forEach(ocVisitorApplyId -> {
            OcVisitorApply ocVisitorApplyAction = new OcVisitorApply();
            ocVisitorApplyAction.setId(Long.valueOf(ocVisitorApplyId));
            ocVisitorApplyAction.setOcCode(ocVisitorApply.getOcCode());
            ocVisitorApplyAction.setOrgCode(ocVisitorApply.getOrgCode());
            ocVisitorApplyAction.setOcName(ocVisitorApply.getOcName());
            ocVisitorApplyAction.setOrgName(ocVisitorApply.getOrgName());
            ocVisitorApplyAction.setAuditStatus(ocVisitorApply.getAuditStatus());
            ocVisitorApplyAction.setAuditMessage(ocVisitorApply.getAuditMessage());

            //存储
            auditSave(ocVisitorApplyAction);
        });
    }
    /**
     * 审核保存
     * @param ocVisitorApply
     * @return
     */
    @Override
    @Transactional
    public OcVisitorApply auditSave(OcVisitorApply ocVisitorApply){
        if(ocVisitorApply.getId() == null || ocVisitorApply.getId() == 0l){
            throw new FebsException("审核信息无效!");
        }
        QueryWrapper<OcVisitorApply> queryWrapper = new QueryWrapper<OcVisitorApply>();
        queryWrapper.lambda().eq(OcVisitorApply::getId,ocVisitorApply.getId());
        queryWrapper.lambda().eq(OcVisitorApply::getOrgCode,ocVisitorApply.getOrgCode());
        queryWrapper.lambda().eq(OcVisitorApply::getOcCode,ocVisitorApply.getOcCode());
        OcVisitorApply oldApply = this.baseMapper.selectOne(queryWrapper);
        if(oldApply == null){
            throw new FebsException("未找到需审核的信息!");
        }
        oldApply.setAuditStatus(ocVisitorApply.getAuditStatus());
        oldApply.setAuditMessage(ocVisitorApply.getAuditMessage());


        OcVisitorInfo ocVisitorInfo = null;
        if(oldApply.getOcVisitorId() == null || oldApply.getOcVisitorId() == 0l){
            ocVisitorInfo = ocVisitorInfoService.findOcVisitorInfoByIdCard(oldApply.getIdCardName(),oldApply.getPhoneNumber(),oldApply.getOcCode(),oldApply.getOrgCode());
            if(ocVisitorInfo == null){
                ocVisitorInfo = new OcVisitorInfo();
            }
        }else{
            OcVisitorInfo visitorInfo = ocVisitorInfoService.findOcVisitorInfoById(oldApply.getOcVisitorId());
            if(visitorInfo == null){
                ocVisitorInfo = ocVisitorInfoService.findOcVisitorInfoByIdCard(oldApply.getIdCardName(),oldApply.getPhoneNumber(),oldApply.getOcCode(),oldApply.getOrgCode());
                if(ocVisitorInfo == null){
                    ocVisitorInfo = new OcVisitorInfo();
                }
            }else{
                ocVisitorInfo = visitorInfo;
            }
        }


        ocVisitorInfo.setOcVisitorId(oldApply.getOcVisitorId());
        ocVisitorInfo.setMemberVisitorId(oldApply.getMemberVisitorId());
        ocVisitorInfo.setMemberAccountCode(oldApply.getMemberAccountCode());
        ocVisitorInfo.setMemberId(oldApply.getMemberId());
        ocVisitorInfo.setOrgCode(oldApply.getOrgCode());
        ocVisitorInfo.setOcCode(oldApply.getOcCode());
        ocVisitorInfo.setOrgName(oldApply.getOrgName());
        ocVisitorInfo.setOcName(oldApply.getOcName());
        ocVisitorInfo.setVisitorType(oldApply.getVisitorType());
        ocVisitorInfo.setIdCardName(oldApply.getIdCardName());
        ocVisitorInfo.setIdCard(oldApply.getIdCard());
        ocVisitorInfo.setPhoneNumber(oldApply.getPhoneNumber());
        ocVisitorInfo.setDepName(oldApply.getDepName());
        ocVisitorInfo.setPositionName(oldApply.getPositionName());
        ocVisitorInfo.setFaceImgRequest(oldApply.getFaceImgRequest());
        ocVisitorInfo.setFaceImgAttchment(oldApply.getFaceImgAttchment());
        ocVisitorInfo.setAbstractMsg(oldApply.getAbstractMsg());
        ocVisitorInfo.setCreateType(OcVisitorCreateType.WX_INVITE);


        OcVisitorInfo ocVisitorInfoTemp = saveInfoByApply(ocVisitorInfo);
        oldApply.setOcVisitorId(ocVisitorInfoTemp.getId());

        this.baseMapper.updateById(oldApply);

        //区域权限
        OcAuthArea ocAuthArea = ocAuthAreaService.findOcAuthAreaByCode(oldApply.getOcAuthAreaCode(),oldApply.getOrgCode(),oldApply.getOcCode());
        if(ocAuthArea == null){
            throw new FebsException("授权区域无法找到!");
        }
        if(oldApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITED_SUCCESS)){
            ocAuthAreaVisitorService.authOcVisitors(ocAuthArea.getId().toString(),new String[]{ocVisitorInfo.getId().toString()},ocAuthArea.getOrgCode(),ocAuthArea.getOcCode(),ocAuthArea.getOrgName(),ocAuthArea.getOcName());
        }else{
            ocAuthAreaVisitorService.cancelAuthOcVisitors(ocAuthArea.getId().toString(),new String[]{ocVisitorInfo.getId().toString()},ocAuthArea.getOrgCode(),ocAuthArea.getOcCode(),ocAuthArea.getOrgName(),ocAuthArea.getOcName());
        }
        return oldApply;
    }

    @Transactional
    public OcVisitorInfo saveInfoByApply(OcVisitorInfo ocVisitorInfo){
        OcVisitorUploadImg ocVisitorUploadImg = null;
        if(ocVisitorInfo.getFaceUploadImgId() == null || ocVisitorInfo.getFaceUploadImgId() == 0L){
            ocVisitorUploadImg = new OcVisitorUploadImg();
            ocVisitorUploadImg.setOrgCode(ocVisitorInfo.getOrgCode());
            ocVisitorUploadImg.setOrgName(ocVisitorInfo.getOrgName());
            ocVisitorUploadImg.setOcCode(ocVisitorInfo.getOcCode());
            ocVisitorUploadImg.setOcName(ocVisitorInfo.getOcName());
            ocVisitorUploadImg.setVisitorType(ocVisitorInfo.getVisitorType());
            ocVisitorUploadImg.setIdCard(ocVisitorInfo.getIdCard());
            ocVisitorUploadImg.setIdCardName(ocVisitorInfo.getIdCardName());
            ocVisitorUploadImg.setPhoneNumber(ocVisitorInfo.getPhoneNumber());
            ocVisitorUploadImg.setDepName(ocVisitorInfo.getDepName());
            ocVisitorUploadImg.setPositionName(ocVisitorInfo.getPositionName());
            ocVisitorUploadImg.setFaceImgRequest(ocVisitorInfo.getFaceImgRequest());
            ocVisitorUploadImg.setFaceImgAttchment(ocVisitorInfo.getFaceImgAttchment());
            ocVisitorUploadImg.setStatus(OcVisitorUploadImgStatus.BINDED);
            ocVisitorUploadImgService.getBaseMapper().insert(ocVisitorUploadImg);

            ocVisitorInfo.setFaceUploadImgId(ocVisitorUploadImg.getId());
        }else{
            ocVisitorUploadImg = ocVisitorUploadImgService.findOcVisitorUploadImgById(ocVisitorInfo.getFaceUploadImgId());
            ocVisitorUploadImg.setOrgCode(ocVisitorInfo.getOrgCode());
            ocVisitorUploadImg.setOrgName(ocVisitorInfo.getOrgName());
            ocVisitorUploadImg.setOcCode(ocVisitorInfo.getOcCode());
            ocVisitorUploadImg.setOcName(ocVisitorInfo.getOcName());
            ocVisitorUploadImg.setVisitorType(ocVisitorInfo.getVisitorType());
            ocVisitorUploadImg.setIdCard(ocVisitorInfo.getIdCard());
            ocVisitorUploadImg.setIdCardName(ocVisitorInfo.getIdCardName());
            ocVisitorUploadImg.setPhoneNumber(ocVisitorInfo.getPhoneNumber());
            ocVisitorUploadImg.setDepName(ocVisitorInfo.getDepName());
            ocVisitorUploadImg.setPositionName(ocVisitorInfo.getPositionName());
            ocVisitorUploadImg.setFaceImgRequest(ocVisitorInfo.getFaceImgRequest());
            ocVisitorUploadImg.setFaceImgAttchment(ocVisitorInfo.getFaceImgAttchment());
            ocVisitorUploadImg.setStatus(OcVisitorUploadImgStatus.BINDED);
            ocVisitorUploadImg.setUploadErrorMessage("");
            ocVisitorUploadImgService.getBaseMapper().updateById(ocVisitorUploadImg);

            ocVisitorInfo.setFaceUploadImgId(ocVisitorUploadImg.getId());
            ocVisitorInfo.setFaceImgAttchment(ocVisitorUploadImg.getFaceImgAttchment());
            ocVisitorInfo.setFaceImgRequest(ocVisitorUploadImg.getFaceImgRequest());

        }
        this.ocVisitorInfoService.saveOcVisitorInfo(ocVisitorInfo);

        return ocVisitorInfo;
    }
}
