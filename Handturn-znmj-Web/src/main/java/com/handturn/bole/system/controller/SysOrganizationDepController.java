package com.handturn.bole.system.controller;

import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.*;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.system.entity.SysOrganizationDep;
import com.handturn.bole.system.service.ISysOrganizationDepService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统-公司部门 Controller
 *
 * @author Eric
 * @date 2019-12-01 10:22:59
 */
@Slf4j
@Validated
@RestController
@RequestMapping("system/sysOrganizationDep")
public class SysOrganizationDepController extends BaseController {

    @Autowired
    private ISysOrganizationDepService sysOrganizationDepService;

    @GetMapping("select/tree")
    @ControllerEndpoint(exceptionMessage = "获取部门树失败")
    public List<CommonTreeSelect<SysOrganizationDep>> getDeptTree() throws FebsException {
        return this.sysOrganizationDepService.findSysOrganizationDeps();
    }

    @GetMapping("tree")
    @RequiresPermissions("sysOrganizationDep:view")
    @ControllerEndpoint(exceptionMessage = "获取部门树失败")
    public FebsResponse getSysOrganizationDepTree(SysOrganizationDep sysOrganizationDep) {
        CommonTree<SysOrganizationDep> sysResources = this.sysOrganizationDepService.findSysOrganizationDeps(sysOrganizationDep);
        return new FebsResponse().success().data(sysResources.getChilds());
    }

    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysOrganizationDep:view")
    public FebsResponse getAllSysOrganizationDeps(SysOrganizationDep sysOrganizationDep) {
        List<SysOrganizationDep> dataTable = this.sysOrganizationDepService.findSysOrganizationDep4Table(sysOrganizationDep);
        Map<String, Object> data = new HashMap<>();
        data.put("rows", dataTable);
        data.put("total", CollectionUtils.size(dataTable));
        return new FebsResponse().success().data(data);
    }

    @PostMapping("save")
    @RequiresPermissions({"sysOrganizationDep:add","sysOrganizationDep:update"})
    @ControllerEndpoint(operation = "编辑部门/按钮", exceptionMessage = "编辑部门/按钮失败")
    public FebsResponse save(@Valid SysOrganizationDep sysOrganizationDep) {
        sysOrganizationDep.setOrgId(UserInfoHolder.getUserInfo().getCurrentOrg().getId());
        sysOrganizationDep.setOcId(UserInfoHolder.getUserInfo().getCurrentOc().getId());
        this.sysOrganizationDepService.saveSysOrganizationDep(sysOrganizationDep,false);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{sysOrganizationDepIds}")
    @RequiresPermissions("sysOrganizationDep:delete")
    @ControllerEndpoint(operation = "删除部门/按钮", exceptionMessage = "删除部门/按钮失败")
    public FebsResponse delete(@NotBlank(message = "{required}") @PathVariable String sysOrganizationDepIds) {
        this.sysOrganizationDepService.deleteSysOrganizationDep(sysOrganizationDepIds);
        return new FebsResponse().success();
    }


    @GetMapping("excel")
    @RequiresPermissions("sysOrganizationDep:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败")
    public void export(SysOrganizationDep sysOrganizationDep, HttpServletResponse response) {
        List<SysOrganizationDep> sysOrganizationDeps = this.sysOrganizationDepService.findSysOrganizationDep4Table(sysOrganizationDep);
        ExcelKit.$Export(SysOrganizationDep.class, response).downXlsx(sysOrganizationDeps, false);
    }
}
