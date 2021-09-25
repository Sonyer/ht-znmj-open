package com.handturn.bole.system.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import com.handturn.bole.system.entity.SysOrganization;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.service.ISysOrganizationService;
import com.handturn.bole.system.service.ISysResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
* 系统-组织 View Controller
*
* @author MrBird
* @date 2019-12-08 10:00:07
*/
@Slf4j
@Validated
@Controller
public class SysOrganizationViewController extends BaseController {

    @Autowired
    private ISysOrganizationService sysOrganizationService;

    @Autowired
    private ISysconfDictCodeService sysconfDictCodeService;
    @Autowired
    private ISysResourceService sysResourceService;
    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganization")
    @RequiresPermissions("sysOrganization:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> orgTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OrgType");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("orgTypeOption",orgTypeOption);
        return FebsUtil.view("system/sysOrganization/sysOrganizationList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganization/add")
    @RequiresPermissions("sysOrganization:add")
    public String add(Model model) {
        //组装新增对象
        SysOrganization sysOrganization = new SysOrganization();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> orgTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OrgType");

        //传参
        model.addAttribute("sysOrganization",sysOrganization);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("orgTypeOption",orgTypeOption);
        return FebsUtil.view("system/sysOrganization/sysOrganizationEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganization/update/{sysOrganizationId}")
    @RequiresPermissions("sysOrganization:update")
    public String update(@PathVariable Long sysOrganizationId, Model model) {
        SysOrganization sysOrganization = sysOrganizationService.findSysOrganizationById(sysOrganizationId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> orgTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OrgType");

        //传参
        model.addAttribute("sysOrganization",sysOrganization);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("orgTypeOption",orgTypeOption);
        return FebsUtil.view("system/sysOrganization/sysOrganizationEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganization/auth/{sysOrganizationId}")
    @RequiresPermissions("sysOrganization:auth")
    public String auth(@PathVariable Long sysOrganizationId, Model model) {
        SysOrganization sysOrganization = sysOrganizationService.findSysOrganizationById(sysOrganizationId);

        List<SysResource> rootModules = sysResourceService.findAllRootSysResources();

        //传参
        model.addAttribute("rootModules",rootModules);
        model.addAttribute("sysOrganization",sysOrganization);
        return FebsUtil.view("system/sysOrganization/sysOrganizationAuth");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganization/reportAuth/{sysOrganizationId}")
    @RequiresPermissions("sysOrganization:report")
    public String reportAuth(@PathVariable Long sysOrganizationId, Model model) {
        SysOrganization sysOrganization = sysOrganizationService.findSysOrganizationById(sysOrganizationId);

        List<SysconfQueryReport> rootReports = sysconfQueryReportService.findAllRootSysReports();

        //传参
        model.addAttribute("rootReports",rootReports);
        model.addAttribute("sysOrganization",sysOrganization);
        return FebsUtil.view("system/sysOrganization/sysOrganizationReport");
    }

}
