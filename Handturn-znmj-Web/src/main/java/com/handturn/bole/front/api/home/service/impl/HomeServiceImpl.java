package com.handturn.bole.front.api.home.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.front.api.home.service.IHomeService;
import com.handturn.bole.front.api.home.vo.AuthAreaPageQueryResponseVo;
import com.handturn.bole.front.api.home.vo.AuthAreaResponseVo;
import com.handturn.bole.main.authArea.entity.OcAuthArea;
import com.handturn.bole.main.authArea.entity.OcAuthAreaResponsible;
import com.handturn.bole.main.authArea.entity.OcAuthAreaVisitor;
import com.handturn.bole.main.authArea.service.IOcAuthAreaResponsibleService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaVisitorService;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import com.handturn.bole.master.member.entity.Member;
import com.handturn.bole.master.member.service.IMemberService;
import com.handturn.bole.master.member.service.IMemberVisitorInfoService;
import com.handturn.bole.monitor.service.IRedisService;
import com.handturn.bole.system.entity.SysOrganization;
import com.handturn.bole.system.service.ISysOrganizationService;
import com.handturn.bole.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class HomeServiceImpl implements IHomeService {
    @Autowired
    private IRedisService redisService;

    @Autowired
    private IOcAuthAreaService ocAuthAreaService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IOcVisitorInfoService ocVisitorInfoService;

    @Autowired
    private IOcAuthAreaVisitorService ocAuthAreaVisitorService;

    @Autowired
    private IOcAuthAreaResponsibleService ocAuthAreaResponsibleService;
    @Autowired
    private ISysOrganizationService sysOrganizationService;

    /**
     * 获取区域权限分页查询
     * @return
     */
    @Override
    public AuthAreaPageQueryResponseVo authAreaPageQuery(String pageIndex, String searchValue, String curAccountCode){
        Member member = memberService.findMemberByAccountCode(curAccountCode);
        if(member == null){
            throw new FebsException("您还未登陆!");
        }
        //获取账号访客信息
        Set<Long> visitorIds = ocVisitorInfoService.findByAccountCode(curAccountCode);
        Set<Long> userIds = sysUserService.findIdsByAccountCode(curAccountCode);
        Set<Long> nullSet = new HashSet<>();
        nullSet.add(-1L);
        //查询访问权限
        QueryWrapper<OcAuthAreaVisitor> visiteQueryWrapper = new QueryWrapper<OcAuthAreaVisitor>();
        visiteQueryWrapper.lambda().in(OcAuthAreaVisitor::getOcVisitorId,visitorIds == null||visitorIds.size() <= 0?nullSet:visitorIds);
        visiteQueryWrapper.lambda().select(OcAuthAreaVisitor::getAuthAreaId,OcAuthAreaVisitor::getOrgCode);
        List<OcAuthAreaVisitor> areaVisitors = ocAuthAreaVisitorService.getBaseMapper().selectList(visiteQueryWrapper);

        Set<Long> visiteAuthAreaIds = new HashSet<>();
        Set<String> orgCodes = new HashSet<>();
        areaVisitors.forEach(areaVisitor ->{
            visiteAuthAreaIds.add(areaVisitor.getAuthAreaId());
            orgCodes.add(areaVisitor.getOrgCode());
        });

        //查询管理权限
        QueryWrapper<OcAuthAreaResponsible> responsibleQueryWrapper = new QueryWrapper<OcAuthAreaResponsible>();
        responsibleQueryWrapper.lambda().in(OcAuthAreaResponsible::getUserId,userIds == null || userIds.size() <= 0?nullSet:userIds);
        responsibleQueryWrapper.lambda().select(OcAuthAreaResponsible::getAuthAreaId,OcAuthAreaResponsible::getOrgCode);
        List<OcAuthAreaResponsible> areaResponsibles = ocAuthAreaResponsibleService.getBaseMapper().selectList(responsibleQueryWrapper);

        Set<Long> responsibleAuthAreaIds = new HashSet<>();
        areaResponsibles.forEach(areaResponsible ->{
            responsibleAuthAreaIds.add(areaResponsible.getAuthAreaId());
            orgCodes.add(areaResponsible.getOrgCode());
        });

        //查询ORG
        QueryWrapper<SysOrganization> orgQueryWrapper = new QueryWrapper<SysOrganization>();
        orgQueryWrapper.lambda().in(SysOrganization::getOrgCode,orgCodes == null|| orgCodes.size() <=0 ?nullSet:orgCodes);
        orgQueryWrapper.lambda().select(SysOrganization::getOrgCode,SysOrganization::getOrgName,SysOrganization::getLogoFilePath,SysOrganization::getLogoRequestUrl);
        List<SysOrganization> orgs = sysOrganizationService.getBaseMapper().selectList(orgQueryWrapper);
        Map<String,SysOrganization> orgMap = new HashMap<>();
        orgs.forEach(org ->{
            orgMap.put(org.getOrgCode(),org);
        });

        //合并区域ID
        Set<Long> authAreaIds = new HashSet<>();
        authAreaIds.addAll(visiteAuthAreaIds);
        authAreaIds.addAll(responsibleAuthAreaIds);

        //分页查询
        List<OcAuthArea> ocAuthAreas = ocAuthAreaService.authAreaPageQuery(Integer.valueOf(pageIndex),searchValue,authAreaIds.size()<=0?null:authAreaIds);
        AuthAreaPageQueryResponseVo responseVo = new AuthAreaPageQueryResponseVo();
        List<AuthAreaResponseVo> pageDatas = new ArrayList<>();
        ocAuthAreas.forEach(ocAuthArea ->{
            SysOrganization org = orgMap.get(ocAuthArea.getOrgCode());
            AuthAreaResponseVo authAreaResponseVo = new AuthAreaResponseVo();
            authAreaResponseVo.setAuthAreaCode(ocAuthArea.getAreaCode());
            authAreaResponseVo.setAuthAreaName(ocAuthArea.getAreaName());
            authAreaResponseVo.setLogoRequest(org == null?"":org.getLogoRequestUrl());
            authAreaResponseVo.setOcCode(ocAuthArea.getOcCode());
            authAreaResponseVo.setOcName(ocAuthArea.getOcName());
            authAreaResponseVo.setOrgCode(ocAuthArea.getOrgCode());
            authAreaResponseVo.setOrgName(org == null?"":org.getOrgName());
            authAreaResponseVo.setRemark(ocAuthArea.getRemark());
            String authStr = "";
            if(responsibleAuthAreaIds.contains(ocAuthArea.getId())){
                authStr +="【管理权限】";
            }
            if(visiteAuthAreaIds.contains(ocAuthArea.getId())){
                authStr +="【访客权限】";
            }
            authAreaResponseVo.setAuthStr(authStr);
            pageDatas.add(authAreaResponseVo);
        });
        responseVo.setPageDatas(pageDatas);

        return responseVo;
    }
}
