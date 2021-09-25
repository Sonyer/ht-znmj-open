package com.handturn.bole.system.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.system.entity.SysOrganizationDep;
import com.handturn.bole.system.service.ISysOrganizationDepService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统-公司部门 Controller
 *
 * @author Eric
 * @date 2019-12-01 10:22:59
 */
@Slf4j
@Validated
@Controller
public class SysOrganizationDepViewController extends BaseController {

    @Autowired
    private ISysOrganizationDepService sysOrganizationDepService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganizationDep")
    @RequiresPermissions("sysOrganizationDep:view")
    public String view(Model model) {
        return FebsUtil.view("system/sysOrganizationDep/sysOrganizationDepList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganizationDep/add/{parentSysOrganizationDepId}")
    @RequiresPermissions("sysOrganizationDep:add")
    public String add(@PathVariable Long parentSysOrganizationDepId, Model model) {
        SysOrganizationDep parentOrganizationDep = sysOrganizationDepService.findById(parentSysOrganizationDepId);

        //组装新增对象
        SysOrganizationDep sysOrganizationDep = new SysOrganizationDep();
        sysOrganizationDep.setParentDepId(parentSysOrganizationDepId);
        sysOrganizationDep.setParentDepName(parentOrganizationDep == null?"":parentOrganizationDep.getDepName());

        //传参
        model.addAttribute("sysOrganizationDep",sysOrganizationDep);
        return FebsUtil.view("system/sysOrganizationDep/sysOrganizationDepEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysOrganizationDep/update/{sysOrganizationDepId}")
    @RequiresPermissions("sysOrganizationDep:update")
    public String update(@PathVariable Long sysOrganizationDepId, Model model) {
        SysOrganizationDep sysOrganizationDep = sysOrganizationDepService.findById(sysOrganizationDepId);
        SysOrganizationDep parentSysOrganizationDep = sysOrganizationDepService.findById(sysOrganizationDep.getParentDepId());
        sysOrganizationDep.setParentDepName(parentSysOrganizationDep == null?"":parentSysOrganizationDep.getDepName());

        //传参
        model.addAttribute("sysOrganizationDep",sysOrganizationDep);
        return FebsUtil.view("system/sysOrganizationDep/sysOrganizationDepEdit");
    }
}
