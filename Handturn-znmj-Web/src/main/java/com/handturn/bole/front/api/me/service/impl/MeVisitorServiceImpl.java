package com.handturn.bole.front.api.me.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.front.api.me.service.IMeVisitorAuditService;
import com.handturn.bole.front.api.me.vo.MeVisitorAuditResponseVo;
import com.handturn.bole.main.authArea.entity.OcAuthArea;
import com.handturn.bole.main.authArea.entity.OcAuthAreaResponsible;
import com.handturn.bole.main.authArea.service.IOcAuthAreaResponsibleService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaService;
import com.handturn.bole.main.visitor.entity.OcVisitorApply;
import com.handturn.bole.main.visitor.entity.OcVisitorAuditStatus;
import com.handturn.bole.main.visitor.service.IOcVisitorApplyService;
import com.handturn.bole.master.member.entity.Member;
import com.handturn.bole.master.member.entity.MemberNotifyType;
import com.handturn.bole.master.member.service.IMemberNotifyService;
import com.handturn.bole.master.member.service.IMemberService;
import com.handturn.bole.master.set.entity.NotifySet;
import com.handturn.bole.master.set.service.INotifySetService;
import com.handturn.bole.monitor.service.IRedisService;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.system.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MeVisitorServiceImpl implements IMeVisitorAuditService {
    @Autowired
    private IRedisService redisService;
    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IMemberNotifyService memberNotifyService;
    @Autowired
    private INotifySetService notifySetService;

    @Autowired
    private IOcVisitorApplyService ocVisitorApplyService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IOcAuthAreaResponsibleService ocAuthAreaResponsibleService;

    @Autowired
    private IOcAuthAreaService ocAuthAreaService;

    /**
     * 获取我的订单
     * @param request
     * @return
     */
    @Override
    public MeVisitorAuditResponseVo initVisitorAudit(HttpServletRequest request, Integer pageIndex, String currentStatusType){
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

        Set<String> statues = new HashSet<String>();
        if(StringUtils.isEmpty(currentStatusType) || currentStatusType.equals("0")){  //全部
            statues.add(OcVisitorAuditStatus.UNAUDIT);
            statues.add(OcVisitorAuditStatus.AUDITING);
            statues.add(OcVisitorAuditStatus.AUDITED_SUCCESS);
            statues.add(OcVisitorAuditStatus.AUDITED_FAIL);
        }else if(currentStatusType.equals("1")){  //待审核
            statues.add(OcVisitorAuditStatus.AUDITING);
        }else if(currentStatusType.equals("2")){  //已审核
            statues.add(OcVisitorAuditStatus.AUDITED_SUCCESS);
        }else if(currentStatusType.equals("3")){  //审核失败
            statues.add(OcVisitorAuditStatus.AUDITED_FAIL);
        }

        //获取账号访客信息
        Set<Long> userIds = sysUserService.findIdsByAccountCode(accountCode);
        Set<Long> nullSet = new HashSet<>();
        nullSet.add(-1L);

        //查询管理权限
        QueryWrapper<OcAuthAreaResponsible> responsibleQueryWrapper = new QueryWrapper<OcAuthAreaResponsible>();
        responsibleQueryWrapper.lambda().in(OcAuthAreaResponsible::getUserId,userIds == null || userIds.size() <= 0?nullSet:userIds);
        responsibleQueryWrapper.lambda().select(OcAuthAreaResponsible::getAuthAreaId,OcAuthAreaResponsible::getOrgCode);
        List<OcAuthAreaResponsible> areaResponsibles = ocAuthAreaResponsibleService.getBaseMapper().selectList(responsibleQueryWrapper);

        Set<Long> responsibleAuthAreaIds = new HashSet<>();
        areaResponsibles.forEach(areaResponsible ->{
            responsibleAuthAreaIds.add(areaResponsible.getAuthAreaId());
        });

        //查询区域编码
        QueryWrapper<OcAuthArea> ocAuthAreaQueryWrapper = new QueryWrapper<OcAuthArea>();
        ocAuthAreaQueryWrapper.lambda().in(OcAuthArea::getId,responsibleAuthAreaIds == null || responsibleAuthAreaIds.size() <= 0?nullSet:responsibleAuthAreaIds);
        ocAuthAreaQueryWrapper.lambda().select(OcAuthArea::getAreaCode,OcAuthArea::getAreaName,OcAuthArea::getOcCode);
        List<OcAuthArea> ocAuthAreas = ocAuthAreaService.getBaseMapper().selectList(ocAuthAreaQueryWrapper);
        Set<String> authAreaCodes = new HashSet<String>();
        Set<String> ocCodes = new HashSet<>();
        Map<String,String> ocAuthAreaMap = new HashMap<>();
        ocAuthAreas.forEach(ocAuthArea ->{
            authAreaCodes.add(ocAuthArea.getAreaCode());
            ocCodes.add(ocAuthArea.getOcCode());
            ocAuthAreaMap.put(ocAuthArea.getAreaCode(),ocAuthArea.getAreaName());
        });

        //查询申请
        CustomPage<OcVisitorApply> page = new CustomPage<>(pageIndex, 10);
        QueryWrapper<OcVisitorApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(OcVisitorApply::getOcAuthAreaCode,authAreaCodes == null ||authAreaCodes.size() <= 0? nullSet:authAreaCodes);
        queryWrapper.lambda().in(OcVisitorApply::getOcCode,ocCodes == null ||ocCodes.size() <= 0? nullSet:ocCodes);
        queryWrapper.lambda().in(OcVisitorApply::getAuditStatus,statues);
        IPage<OcVisitorApply> ocVisitorApplyPages = ocVisitorApplyService.getBaseMapper().selectPage(page,queryWrapper);
        List<OcVisitorApply> ocVisitorApplys = ocVisitorApplyPages.getRecords();

        MeVisitorAuditResponseVo meVisitorAuditResponseVo = new MeVisitorAuditResponseVo();
        List<MeVisitorAuditResponseVo.InitMeOrderDataResponseVo> pageDatas = new ArrayList<MeVisitorAuditResponseVo.InitMeOrderDataResponseVo>();
        ocVisitorApplys.forEach(ocVisitorApply ->{
            MeVisitorAuditResponseVo.InitMeOrderDataResponseVo data = new MeVisitorAuditResponseVo.InitMeOrderDataResponseVo();
            data.setId(ocVisitorApply.getId().toString());
            data.setAuthAreaCode(ocVisitorApply.getOcAuthAreaCode());
            data.setAuthAreaName(ocAuthAreaMap.get(ocVisitorApply.getOcAuthAreaCode()));
            data.setOcCode(ocVisitorApply.getOcCode());
            data.setOcName(ocVisitorApply.getOcName());
            data.setIdCardName(ocVisitorApply.getIdCardName());
            data.setIdCard(ocVisitorApply.getIdCard());
            data.setPhoneNumber(ocVisitorApply.getPhoneNumber());
            data.setFaceImgRequest(ocVisitorApply.getFaceImgRequest());
            data.setPositionName(ocVisitorApply.getPositionName());
            String auditStatus = StringUtils.isEmpty(ocVisitorApply.getAuditStatus())?"未审核":ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.UNAUDIT)?"未审核":
                    ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITING)?"审核中":  ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITED_SUCCESS)?"已审核":
                            ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITED_FAIL)?"审核失败":ocVisitorApply.getAuditStatus();
            data.setAuditStatus(auditStatus);
            data.setRemark(ocVisitorApply.getAbstractMsg());
            pageDatas.add(data);
        });

        meVisitorAuditResponseVo.setPageDatas(pageDatas);

        //消息通知模板
        Set<String> notifyTypeSet = new HashSet<>();
        notifyTypeSet.add(MemberNotifyType.VISITOR_AUDIT_NOTIFY);

        Set<String> statuses = new HashSet<>();
        statuses.add(BaseStatus.ENABLED);
        List<NotifySet> linlingoSetNotifys = notifySetService.findNotifySetByIs(notifyTypeSet,null, BaseBoolean.TRUE,null,statuses);
        List<String> notifyTemplateIds = new ArrayList<String>();
        linlingoSetNotifys.forEach(linlingoSetNotify ->{
            notifyTemplateIds.add(linlingoSetNotify.getWxNotifyTemplateId());
        });
        meVisitorAuditResponseVo.setWxNotifyTemplatIds(notifyTemplateIds);

        return meVisitorAuditResponseVo;
    }

    /**
     * 收货确认
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String auditVisitor(HttpServletRequest request, String visitorApplyId){
        OcVisitorApply ocVisitorApply = ocVisitorApplyService.findOcVisitorApplyById(Long.valueOf(visitorApplyId));
        if(ocVisitorApply == null){
            throw new FebsException("申请信息不存在!");
        }
        if(!ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITING)){
            throw new FebsException("审核信息无效!");
        }
        ocVisitorApply.setAuditStatus(OcVisitorAuditStatus.AUDITED_SUCCESS);
        ocVisitorApply.setAuditMessage("审核成功!");
        ocVisitorApplyService.getBaseMapper().updateById(ocVisitorApply);

        //审核
        ocVisitorApplyService.auditSave(ocVisitorApply);

        String auditStatus = StringUtils.isEmpty(ocVisitorApply.getAuditStatus())?"未审核":ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.UNAUDIT)?"未审核":
                ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITING)?"审核中":  ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITED_SUCCESS)?"已审核":
                        ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITED_FAIL)?"审核失败":ocVisitorApply.getAuditStatus();
        //通知消息
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("auditContent",ocVisitorApply.getOcName()+"-门禁审核");
        params.put("auditStatus",auditStatus);
        params.put("auditTime",new Date());
        memberNotifyService.notifyMessage(ocVisitorApply.getMemberId(),ocVisitorApply.getMemberAccountCode(), MemberNotifyType.VISITOR_AUDIT_NOTIFY,params);

        return auditStatus;
    }


    /**
     * 取消审核
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String cancelVisitor(HttpServletRequest request,String visitorApplyId){
        OcVisitorApply ocVisitorApply = ocVisitorApplyService.findOcVisitorApplyById(Long.valueOf(visitorApplyId));
        if(ocVisitorApply == null){
            throw new FebsException("申请信息不存在!");
        }
        if(!ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITING)){
            throw new FebsException("审核信息无效!");
        }
        ocVisitorApply.setAuditStatus(OcVisitorAuditStatus.AUDITED_FAIL);
        ocVisitorApply.setAuditMessage("审核失败!");
        ocVisitorApplyService.getBaseMapper().updateById(ocVisitorApply);

        //审核
        ocVisitorApplyService.auditSave(ocVisitorApply);

        String auditStatus = StringUtils.isEmpty(ocVisitorApply.getAuditStatus())?"未审核":ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.UNAUDIT)?"未审核":
                ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITING)?"审核中":  ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITED_SUCCESS)?"已审核":
                        ocVisitorApply.getAuditStatus().equals(OcVisitorAuditStatus.AUDITED_FAIL)?"审核失败":ocVisitorApply.getAuditStatus();

        //通知消息
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("auditContent",ocVisitorApply.getOcName()+"-门禁审核");
        params.put("auditStatus",auditStatus);
        params.put("auditTime",new Date());
        memberNotifyService.notifyMessage(ocVisitorApply.getMemberId(),ocVisitorApply.getMemberAccountCode(), MemberNotifyType.VISITOR_AUDIT_NOTIFY,params);

        return auditStatus;
    }

}
