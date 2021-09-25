package com.handturn.bole.system.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import com.handturn.bole.system.entity.SysGroup;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.service.ISysGroupService;
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
* 系统-资源组 View Controller
*
* @author Eric
* @date 2020-05-23 14:55:18
*/
@Slf4j
@Validated
@Controller
public class SysGroupViewController extends BaseController {
    @Autowired
    private ISysGroupService sysGroupService;

    @Autowired
    private ISysResourceService sysResourceService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;
    
    @Autowired
    ISysconfQueryReportService sysconfQueryReportService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysGroup")
    @RequiresPermissions("sysGroup:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        return FebsUtil.view("system/sysGroup/sysGroupList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysGroup/add")
    @RequiresPermissions("sysGroup:add")
    public String add(Model model) {
        //组装新增对象
        SysGroup sysGroup = new SysGroup();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysGroup",sysGroup);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("system/sysGroup/sysGroupEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysGroup/update/{sysGroupId}")
    @RequiresPermissions("sysGroup:update")
    public String update(@PathVariable Long sysGroupId, Model model) {
        SysGroup sysGroup = sysGroupService.findSysGroupById(sysGroupId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysGroup",sysGroup);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("system/sysGroup/sysGroupEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysGroup/auth/{sysGroupId}")
    @RequiresPermissions("sysGroup:auth")
    public String auth(@PathVariable Long sysGroupId, Model model) {
        SysGroup sysGroup = sysGroupService.findSysGroupById(sysGroupId);

        List<SysResource> rootModules = sysResourceService.findAllRootSysResources();

        //传参
        model.addAttribute("rootModules",rootModules);
        model.addAttribute("sysGroup",sysGroup);
        return FebsUtil.view("system/sysGroup/sysGroupAuth");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysGroup/reportAuth/{sysGroupId}")
    @RequiresPermissions("sysGroup:report")
    public String reportAuth(@PathVariable Long sysGroupId, Model model) {
        SysGroup sysGroup = sysGroupService.findSysGroupById(sysGroupId);

        List<SysconfQueryReport> rootReports = sysconfQueryReportService.findAllRootSysReports();

        //传参
        model.addAttribute("rootReports",rootReports);
        model.addAttribute("sysGroup",sysGroup);
        return FebsUtil.view("system/sysGroup/sysGroupReport");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysGroup/dataAuth/{sysGroupId}")
    @RequiresPermissions("sysGroup:dataAuth")
    public String dataAuth(@PathVariable Long sysGroupId, Model model) {
        SysGroup sysGroup = sysGroupService.findSysGroupById(sysGroupId);

        //传参
        model.addAttribute("sysGroup",sysGroup);
        return FebsUtil.view("system/sysGroup/sysGroupDataAuth");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysGroup/bindUser/{sysGroupId}")
    @RequiresPermissions("sysGroup:bindUser")
    public String bindUser(@PathVariable Long sysGroupId, Model model) {
        SysGroup sysGroup = sysGroupService.findSysGroupById(sysGroupId);

        //传参
        model.addAttribute("sysGroup",sysGroup);
        return FebsUtil.view("system/sysGroup/sysGroupBindUser");
    }
}
