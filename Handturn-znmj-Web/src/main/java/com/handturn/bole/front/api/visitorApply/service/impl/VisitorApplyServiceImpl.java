package com.handturn.bole.front.api.visitorApply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.front.api.visitorApply.service.IVisitorApplyService;
import com.handturn.bole.front.api.visitorApply.vo.VisitorApplyPageQueryResponseVo;
import com.handturn.bole.front.api.visitorApply.vo.VisitorApplyResponseVo;
import com.handturn.bole.front.api.visitorApply.vo.VisitorApplySubmitRequestVo;
import com.handturn.bole.main.authArea.entity.OcAuthArea;
import com.handturn.bole.main.authArea.entity.OcAuthAreaResponsible;
import com.handturn.bole.main.authArea.service.IOcAuthAreaResponsibleService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaVisitorService;
import com.handturn.bole.main.visitor.entity.*;
import com.handturn.bole.main.visitor.service.IOcVisitorApplyService;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import com.handturn.bole.master.member.entity.Member;
import com.handturn.bole.master.member.entity.MemberNotifyType;
import com.handturn.bole.master.member.entity.MemberVisitorInfo;
import com.handturn.bole.master.member.service.IMemberNotifyService;
import com.handturn.bole.master.member.service.IMemberService;
import com.handturn.bole.master.member.service.IMemberVisitorInfoService;
import com.handturn.bole.master.set.entity.NotifySet;
import com.handturn.bole.master.set.service.IImgStoreSetService;
import com.handturn.bole.master.set.service.INotifySetService;
import com.handturn.bole.monitor.service.IRedisService;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.service.ISysOrganizationService;
import com.handturn.bole.system.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VisitorApplyServiceImpl implements IVisitorApplyService {
    @Autowired
    private IRedisService redisService;

    @Autowired
    private IOcAuthAreaService ocAuthAreaService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IMemberNotifyService memberNotifyService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IOcVisitorInfoService ocVisitorInfoService;

    @Autowired
    private IOcVisitorApplyService ocVisitorApplyService;

    @Autowired
    private IOcAuthAreaVisitorService ocAuthAreaVisitorService;

    @Autowired
    private IOcAuthAreaResponsibleService ocAuthAreaResponsibleService;
    @Autowired
    private ISysOrganizationService sysOrganizationService;

    @Autowired
    private IImgStoreSetService imgStoreSetService;

    @Autowired
    private INotifySetService notifySetService;

    /**
     * 获取区域权限分页查询
     * @return
     */
    @Override
    public VisitorApplyPageQueryResponseVo initVisitorApply(String curAccountCode,String orgCode,String ocCode,String authAreaCode){
        Member member = memberService.findMemberByAccountCode(curAccountCode);
        if(member == null){
            throw new FebsException("您还未登陆!");
        }

        //获取审核权限
        QueryWrapper<OcVisitorApply> ocVisitorApplyQueryWrapper = new QueryWrapper<OcVisitorApply>();
        ocVisitorApplyQueryWrapper.lambda().eq(OcVisitorApply::getMemberAccountCode,curAccountCode);
        ocVisitorApplyQueryWrapper.lambda().eq(OcVisitorApply::getOcAuthAreaCode,authAreaCode);
        ocVisitorApplyQueryWrapper.lambda().eq(OcVisitorApply::getOrgCode,orgCode);
        ocVisitorApplyQueryWrapper.lambda().eq(OcVisitorApply::getOcCode,ocCode);
        ocVisitorApplyQueryWrapper.lambda().select(OcVisitorApply::getOcVisitorId,OcVisitorApply::getPositionName,OcVisitorApply::getAuditStatus,OcVisitorApply::getAuditMessage,OcVisitorApply::getAbstractMsg);
        List<OcVisitorApply> ocVisitorApplys = ocVisitorApplyService.getBaseMapper().selectList(ocVisitorApplyQueryWrapper);
        Map<Long,OcVisitorApply> ocVisitorApplyMap = new HashMap<>();
        ocVisitorApplys.forEach(ocVisitorApply ->{
            ocVisitorApplyMap.put(ocVisitorApply.getOcVisitorId(),ocVisitorApply);
        });


        //查询用户访客信息
        QueryWrapper<OcVisitorInfo> ocVisitorInfoQueryWrapper = new QueryWrapper<OcVisitorInfo>();
        ocVisitorInfoQueryWrapper.lambda().in(OcVisitorInfo::getMemberAccountCode,curAccountCode);
        ocVisitorInfoQueryWrapper.lambda().in(OcVisitorInfo::getOcCode,ocCode);
        ocVisitorInfoQueryWrapper.lambda().in(OcVisitorInfo::getOrgCode,orgCode);
        List<OcVisitorInfo> ocVisitorInfos = ocVisitorInfoService.getBaseMapper().selectList(ocVisitorInfoQueryWrapper);

        VisitorApplyPageQueryResponseVo visitorApplyPageQueryResponseVo = new VisitorApplyPageQueryResponseVo();
        List<VisitorApplyResponseVo> visitorApplyResponseVos = new ArrayList<>();
        ocVisitorInfos.forEach(ocVisitorInfo ->{
            VisitorApplyResponseVo visitorApplyResponseVo = new VisitorApplyResponseVo();
            visitorApplyResponseVo.setId(ocVisitorInfo.getId().toString());
            visitorApplyResponseVo.setIdCardName(ocVisitorInfo.getIdCardName());
            visitorApplyResponseVo.setIdCard(ocVisitorInfo.getIdCard());
            visitorApplyResponseVo.setPhoneNumber(ocVisitorInfo.getPhoneNumber());
            visitorApplyResponseVo.setFaceImgRequest(ocVisitorInfo.getFaceImgRequest());
            visitorApplyResponseVo.setPositionName(ocVisitorInfo.getPositionName());
            OcVisitorApply ocVisitorApply = ocVisitorApplyMap.get(ocVisitorInfo.getId());
            if(ocVisitorApply != null){
                String auditStatus = StringUtils.isEmpty(ocVisitorApply.getAuditStatus())?"未审核":ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.UNAUDIT)?"未审核":
                        ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITING)?"审核中":  ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITED_SUCCESS)?"已审核":
                        ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITED_FAIL)?"审核失败":ocVisitorApply.getAuditStatus();
                visitorApplyResponseVo.setAuditStatus(auditStatus);
                visitorApplyResponseVo.setRemark(ocVisitorApply.getAbstractMsg());
            }else{
                visitorApplyResponseVo.setAuditStatus("未审核");
            }

            visitorApplyResponseVos.add(visitorApplyResponseVo);
        });

        visitorApplyPageQueryResponseVo.setPageDatas(visitorApplyResponseVos);

        //消息通知模板
        Set<String> notifyTypeSet = new HashSet<>();
        notifyTypeSet.add(MemberNotifyType.VISITOR_APPLY_NOTIFY);

        Set<String> statuses = new HashSet<>();
        statuses.add(BaseStatus.ENABLED);
        List<NotifySet> linlingoSetNotifys = notifySetService.findNotifySetByIs(notifyTypeSet,null, BaseBoolean.TRUE,null,statuses);
        List<String> notifyTemplateIds = new ArrayList<String>();
        linlingoSetNotifys.forEach(linlingoSetNotify ->{
            notifyTemplateIds.add(linlingoSetNotify.getWxNotifyTemplateId());
        });
        visitorApplyPageQueryResponseVo.setWxNotifyTemplatIds(notifyTemplateIds);

        return visitorApplyPageQueryResponseVo;
    }

    /**
     * 上传图片
     * @param file
     * @param accountCode
     * @param token
     * @return
     */
    @Override
    public String uploadVisitorImg(MultipartFile file, String accountCode, String token){
        ImportResultBean resultBean = null;
        try {
            resultBean = imgStoreSetService.importUpload(file, IImgStoreSetService.IMGS_VISITOR_APPLY_FACE);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(resultBean == null){
            throw new FebsException("图片上传失败!");
        }

        return resultBean.getFileRequestUrl();
    }

    /**
     * 提交审核
     * @param request
     * @param requestVo
     * @return
     */
    @Override
    @Transactional
    public Boolean submitVisitorApply(HttpServletRequest request, VisitorApplySubmitRequestVo requestVo){
        //验证码的KEY
        String key = requestVo.getPhoneNumber()+"validateCode";
        try {
            String validateCode = redisService.get(key);
            if(StringUtils.isEmpty(validateCode)){
                throw new FebsException("验证码已失效,请重新获取后再提交!");
            }
            if(!validateCode.equals(requestVo.getValidateCode())){
                throw new FebsException("验证码已失效,请重新获取后再提交!");
            }
        } catch (RedisConnectException e) {
            e.printStackTrace();
            throw new FebsException("验证码已失效,请重新获取后再提交!");
        }

        if(StringUtils.isEmpty(requestVo.getFaceImgRequest())){
            throw new FebsException("请上传人脸照片!");
        }
        if(StringUtils.isEmpty(requestVo.getIdCardName())
                || StringUtils.isEmpty(requestVo.getIdCard())
                || StringUtils.isEmpty(requestVo.getPhoneNumber())
                || StringUtils.isEmpty(requestVo.getPositionName())){
            throw new FebsException("必须填写真实姓名、身份证号、职位名称、手机号码!");
        }

        //获取token
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            throw new FebsException("您还未登陆!");
        }

        String accountCode = null;
        try {
            accountCode = redisService.get(token);
        } catch (RedisConnectException e) {
            e.printStackTrace();
            throw new FebsException("您还未登陆!");
        }

        Member member = memberService.findMemberByAccountCode(accountCode);
        if(member == null){
            throw new FebsException("您还未登陆!");
        }

        OcAuthArea ocAuthArea = null;
        if(StringUtils.isEmpty(requestVo.getOrgCode())
            || StringUtils.isEmpty(requestVo.getOcCode())
                || StringUtils.isEmpty(requestVo.getAuthAreaCode())){
            throw new FebsException("参数错误!");
        }else{
            ocAuthArea = ocAuthAreaService.findByAuthAreaCode(requestVo.getAuthAreaCode(),requestVo.getOrgCode(),requestVo.getOcCode());
            if(ocAuthArea == null){
                throw new FebsException("参数错误!");
            }
        }

        //用户访客信息
        OcVisitorInfo ocVisitorInfo = null;
        if(StringUtils.isEmpty(requestVo.getId()) || requestVo.getId().equals("0")){
            ocVisitorInfo = new OcVisitorInfo();
            ocVisitorInfo.setMemberId(member.getId());
            ocVisitorInfo.setMemberAccountCode(member.getAccountCode());
            ocVisitorInfo.setIdCard(requestVo.getIdCard());
            ocVisitorInfo.setIdCardName(requestVo.getIdCardName());
            ocVisitorInfo.setFaceImgRequest(requestVo.getFaceImgRequest());
            ocVisitorInfo.setFaceImgAttchment(requestVo.getFaceImgRequest());
            ocVisitorInfo.setPhoneNumber(requestVo.getPhoneNumber());
            ocVisitorInfo.setOrgCode(ocAuthArea.getOrgCode());
            ocVisitorInfo.setOrgName(ocAuthArea.getOrgName());
            ocVisitorInfo.setOcCode(ocAuthArea.getOcCode());
            ocVisitorInfo.setOcName(ocAuthArea.getOcName());
            ocVisitorInfo.setVisitorType(OcVisitorType.OUTER_USER);
            ocVisitorInfo.setPositionName(requestVo.getPositionName());
            ocVisitorInfo.setAbstractMsg(requestVo.getRemark());
            ocVisitorInfo.setCreateType(OcVisitorCreateType.WX_INVITE);
            ocVisitorInfoService.getBaseMapper().insert(ocVisitorInfo);
        }else{
            ocVisitorInfo = ocVisitorInfoService.findOcVisitorInfoById(Long.valueOf(requestVo.getId()));
        }

        QueryWrapper<OcVisitorApply> ocVisitorApplyQueryWrapper = new QueryWrapper<OcVisitorApply>();
        ocVisitorApplyQueryWrapper.lambda().eq(OcVisitorApply::getMemberAccountCode,member.getAccountCode());
        ocVisitorApplyQueryWrapper.lambda().eq(OcVisitorApply::getOcAuthAreaCode,requestVo.getAuthAreaCode());
        ocVisitorApplyQueryWrapper.lambda().eq(OcVisitorApply::getOcVisitorId,ocVisitorInfo.getId());
        ocVisitorApplyQueryWrapper.lambda().eq(OcVisitorApply::getOrgCode,requestVo.getOrgCode());
        ocVisitorApplyQueryWrapper.lambda().eq(OcVisitorApply::getOcCode,requestVo.getOcCode());
        List<OcVisitorApply> ocVisitorApplys = ocVisitorApplyService.getBaseMapper().selectList(ocVisitorApplyQueryWrapper);

        OcVisitorApply ocVisitorApply = null;
        if(ocVisitorApplys == null || ocVisitorApplys.size() <= 0){
            ocVisitorApply = new OcVisitorApply();
        }else{
            ocVisitorApply = ocVisitorApplys.get(0);
        }
        ocVisitorApply.setOcAuthAreaCode(ocAuthArea.getAreaCode());
        ocVisitorApply.setOcAuthAreaName(ocAuthArea.getAreaName());
        ocVisitorApply.setOrgCode(ocAuthArea.getOrgCode());
        ocVisitorApply.setOcCode(ocAuthArea.getOcCode());
        ocVisitorApply.setOrgName(ocAuthArea.getOrgName());
        ocVisitorApply.setOcName(ocAuthArea.getOcName());
        ocVisitorApply.setAuditStatus(OcVisitorAuditStatus.AUDITING);
        ocVisitorApply.setAuditMessage("");
        ocVisitorApply.setAbstractMsg(requestVo.getRemark());
        ocVisitorApply.setFaceImgRequest(ocVisitorInfo.getFaceImgRequest());
        ocVisitorApply.setFaceImgAttchment(ocVisitorInfo.getFaceImgAttchment());
        ocVisitorApply.setIdCard(ocVisitorInfo.getIdCard());
        ocVisitorApply.setIdCardName(ocVisitorInfo.getIdCardName());
        ocVisitorApply.setMemberAccountCode(ocVisitorInfo.getMemberAccountCode());
        ocVisitorApply.setMemberId(ocVisitorInfo.getMemberId());
        ocVisitorApply.setOcVisitorId(ocVisitorInfo.getId());
        ocVisitorApply.setPhoneNumber(ocVisitorInfo.getPhoneNumber());
        ocVisitorApply.setPositionName(requestVo.getPositionName());
        ocVisitorApply.setVisitorType(OcVisitorType.OUTER_USER);
        if(ocVisitorApply.getId() == null || ocVisitorApply.getId() == 0l){
            ocVisitorApplyService.getBaseMapper().insert(ocVisitorApply);
        }else{
            ocVisitorApplyService.getBaseMapper().updateById(ocVisitorApply);
        }

        //通知消息
        QueryWrapper<OcAuthAreaResponsible> responsibleQueryWrapper = new QueryWrapper<OcAuthAreaResponsible>();
        responsibleQueryWrapper.lambda().eq(OcAuthAreaResponsible::getAuthAreaId,ocAuthArea.getId());
        responsibleQueryWrapper.lambda().select(OcAuthAreaResponsible::getUserId);
        List<OcAuthAreaResponsible> areaResponsibles = ocAuthAreaResponsibleService.getBaseMapper().selectList(responsibleQueryWrapper);
        Set<Long> userIds = new HashSet<Long>();
        areaResponsibles.forEach(areaResponsible ->{
            userIds.add(areaResponsible.getUserId());
        });

        Set<Long> nullSet = new HashSet<>();
        nullSet.add(-1L);
        List<SysUser> users = sysUserService.findByIds(userIds== null|| userIds.size() <= 0?nullSet:userIds);
        OcVisitorApply finalOcVisitorApply = ocVisitorApply;
        users.forEach(user ->{
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("auditContent", finalOcVisitorApply.getOcName()+"-门禁申请");
            params.put("idCardName", finalOcVisitorApply.getIdCardName());
            params.put("positionName", finalOcVisitorApply.getPositionName());
            memberNotifyService.notifyMessage(user.getMemberId(), user.getMemberAccountCode(), MemberNotifyType.VISITOR_APPLY_NOTIFY,params);
        });

        //是否自动审核
        if(ocAuthArea.getIsAutoAudit().equals(BaseBoolean.TRUE)){
            ocVisitorApply.setAuditStatus(OcVisitorAuditStatus.AUDITED_SUCCESS);
            ocVisitorApply.setAuditMessage("审核成功!");
            ocVisitorApplyService.auditSave(ocVisitorApply);
        }

        return true;
    }
}
