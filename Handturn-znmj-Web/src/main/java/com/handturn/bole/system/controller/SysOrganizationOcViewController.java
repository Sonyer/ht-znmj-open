package com.handturn.bole.system.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import com.handturn.bole.system.entity.OcType;
import com.handturn.bole.system.entity.SysOrganizationOc;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* 系统-公司操作运营中心 View Controller
*
* @author MrBird
* @date 2019-12-08 19:36:28
*/
@Slf4j
@Validated
@Controller
public class SysOrganizationOcViewController extends BaseController {

    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @Autowired
    ISysResourceService sysResourceService;
    @Autowired
    ISysconfQueryReportService sysconfQueryReportService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganizationOc")
    @RequiresPermissions("sysOrganizationOc:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> ocTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcType");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("ocTypeOption",ocTypeOption);
        return FebsUtil.view("system/sysOrganizationOc/sysOrganizationOcList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganizationOc/add")
    @RequiresPermissions("sysOrganizationOc:add")
    public String add(Model model) {
        //组装新增对象
        SysOrganizationOc sysOrganizationOc = new SysOrganizationOc();

        List<OptionVo> ocTypeOption = null;
        if(UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
            ocTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcType");
        }else if(UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)){
            Set<String> contains = new HashSet<String>();
            contains.add(OcType.ORG_OC);
            contains.add(OcType.ORG_3TH);
            ocTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeContains("OcType",contains);
        }else {
            Set<String> contains = new HashSet<String>();
            contains.add(UserInfoHolder.getUserInfo().getCurrentOc().getOcType());
            ocTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeContains("OcType",contains);
        }
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysOrganizationOc",sysOrganizationOc);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("ocTypeOption",ocTypeOption);
        return FebsUtil.view("system/sysOrganizationOc/sysOrganizationOcEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganizationOc/update/{sysOrganizationOcId}")
    @RequiresPermissions("sysOrganizationOc:update")
    public String update(@PathVariable Long sysOrganizationOcId, Model model) {
        SysOrganizationOc sysOrganizationOc = sysOrganizationOcService.findSysOrganizationOcById(sysOrganizationOcId);

        List<OptionVo> ocTypeOption = null;
        Set<String> contains = new HashSet<String>();
        contains.add(sysOrganizationOc.getOcType());
        ocTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeContains("OcType",contains);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysOrganizationOc",sysOrganizationOc);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("ocTypeOption",ocTypeOption);
        return FebsUtil.view("system/sysOrganizationOc/sysOrganizationOcEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganizationOc/auth/{sysOrganizationOcId}")
    @RequiresPermissions("sysOrganizationOc:auth")
    public String auth(@PathVariable Long sysOrganizationOcId, Model model) {
        SysOrganizationOc sysOrganizationOc = sysOrganizationOcService.findSysOrganizationOcById(sysOrganizationOcId);

        List<SysResource> rootModules = sysResourceService.findAllRootSysResourcesByOrgId(sysOrganizationOc.getOrgId());

        //传参
        model.addAttribute("rootModules",rootModules);
        model.addAttribute("sysOrganizationOc",sysOrganizationOc);
        return FebsUtil.view("system/sysOrganizationOc/sysOrganizationOcAuth");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganizationOc/reportAuth/{sysOrganizationOcId}")
    @RequiresPermissions("sysOrganizationOc:auth")
    public String reportAuth(@PathVariable Long sysOrganizationOcId, Model model) {
        SysOrganizationOc sysOrganizationOc = sysOrganizationOcService.findSysOrganizationOcById(sysOrganizationOcId);

        List<SysconfQueryReport> rootReports = sysconfQueryReportService.findAllRootSysReportsByOrgId(sysOrganizationOc.getOrgId());

        //传参
        model.addAttribute("rootReports",rootReports);
        model.addAttribute("sysOrganizationOc",sysOrganizationOc);
        return FebsUtil.view("system/sysOrganizationOc/sysOrganizationOcReport");
    }

}
