package com.handturn.bole.system.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.entity.SysRole;
import com.handturn.bole.system.service.ISysResourceService;
import com.handturn.bole.system.service.ISysRoleService;
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
* 系统-角色 View Controller
*
* @author MrBird
* @date 2019-12-07 21:32:50
*/
@Slf4j
@Validated
@Controller
public class SysRoleViewController extends BaseController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysResourceService sysResourceService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;
    @Autowired
    ISysconfQueryReportService sysconfQueryReportService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysRole")
    @RequiresPermissions("sysRole:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        return FebsUtil.view("system/sysRole/sysRoleList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysRole/add")
    @RequiresPermissions("sysRole:add")
    public String add(Model model) {
        //组装新增对象
        SysRole sysRole = new SysRole();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysRole",sysRole);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("system/sysRole/sysRoleEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysRole/update/{sysRoleId}")
    @RequiresPermissions("sysRole:update")
    public String update(@PathVariable Long sysRoleId, Model model) {
        SysRole sysRole = sysRoleService.findSysRoleById(sysRoleId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysRole",sysRole);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("system/sysRole/sysRoleEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysRole/auth/{sysRoleId}")
    @RequiresPermissions("sysRole:auth")
    public String auth(@PathVariable Long sysRoleId, Model model) {
        SysRole sysRole = sysRoleService.findSysRoleById(sysRoleId);

        List<SysResource> rootModules = sysResourceService.findAllRootSysResourcesByOcId(sysRole.getOcId());

        //传参
        model.addAttribute("rootModules",rootModules);
        model.addAttribute("sysRole",sysRole);
        return FebsUtil.view("system/sysRole/sysRoleAuth");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysRole/reportAuth/{sysRoleId}")
    @RequiresPermissions("sysRole:report")
    public String reportAuth(@PathVariable Long sysRoleId, Model model) {
        SysRole sysRole = sysRoleService.findSysRoleById(sysRoleId);

        List<SysconfQueryReport> rootReports = sysconfQueryReportService.findAllRootSysReportsByOcId(sysRole.getOcId());

        //传参
        model.addAttribute("rootReports",rootReports);
        model.addAttribute("sysRole",sysRole);
        return FebsUtil.view("system/sysRole/sysRoleReport");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysRole/dataAuth/{sysRoleId}")
    @RequiresPermissions("sysRole:dataAuth")
    public String dataAuth(@PathVariable Long sysRoleId, Model model) {
        SysRole sysRole = sysRoleService.findSysRoleById(sysRoleId);

        //传参
        model.addAttribute("sysRole",sysRole);
        return FebsUtil.view("system/sysRole/sysRoleDataAuth");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysRole/bindUser/{sysRoleId}")
    @RequiresPermissions("sysRole:bindUser")
    public String bindUser(@PathVariable Long sysRoleId, Model model) {
        SysRole sysRole = sysRoleService.findSysRoleById(sysRoleId);

        //传参
        model.addAttribute("sysRole",sysRole);
        return FebsUtil.view("system/sysRole/sysRoleBindUser");
    }
}
