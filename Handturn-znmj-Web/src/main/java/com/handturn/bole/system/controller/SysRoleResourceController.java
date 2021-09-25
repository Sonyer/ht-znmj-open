package com.handturn.bole.system.controller;

import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.system.entity.SysRoleResource;
import com.handturn.bole.system.service.ISysRoleResourceService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 系统-角色资源 Controller
 *
 * @author Eric
 * @date 2019-12-01 10:39:10
 */
@Slf4j
@Validated
@RestController
public class SysRoleResourceController extends BaseController {

    @Autowired
    private ISysRoleResourceService sysRoleResourceService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysRoleResource")
    public String sysRoleResourceIndex(){
        return FebsUtil.view("sysRoleResource/sysRoleResource");
    }

    @GetMapping("sysRoleResource")
    @ResponseBody
    @RequiresPermissions("sysRoleResource:list")
    public FebsResponse getAllSysRoleResources(SysRoleResource sysRoleResource) {
        return new FebsResponse().success().data(sysRoleResourceService.findSysRoleResources(sysRoleResource));
    }

    @GetMapping("sysRoleResource/list")
    @ResponseBody
    @RequiresPermissions("sysRoleResource:list")
    public FebsResponse sysRoleResourceList(QueryRequest request, SysRoleResource sysRoleResource) {
        Map<String, Object> dataTable = getDataTable(this.sysRoleResourceService.findSysRoleResources(request, sysRoleResource));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增SysRoleResource", exceptionMessage = "新增SysRoleResource失败")
    @PostMapping("sysRoleResource")
    @ResponseBody
    @RequiresPermissions("sysRoleResource:add")
    public FebsResponse addSysRoleResource(@Valid SysRoleResource sysRoleResource) {
        this.sysRoleResourceService.createSysRoleResource(sysRoleResource);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除SysRoleResource", exceptionMessage = "删除SysRoleResource失败")
    @GetMapping("sysRoleResource/delete")
    @ResponseBody
    @RequiresPermissions("sysRoleResource:delete")
    public FebsResponse deleteSysRoleResource(SysRoleResource sysRoleResource) {
        this.sysRoleResourceService.deleteSysRoleResource(sysRoleResource);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改SysRoleResource", exceptionMessage = "修改SysRoleResource失败")
    @PostMapping("sysRoleResource/update")
    @ResponseBody
    @RequiresPermissions("sysRoleResource:update")
    public FebsResponse updateSysRoleResource(SysRoleResource sysRoleResource) {
        this.sysRoleResourceService.updateSysRoleResource(sysRoleResource);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改SysRoleResource", exceptionMessage = "导出Excel失败")
    @PostMapping("sysRoleResource/excel")
    @ResponseBody
    @RequiresPermissions("sysRoleResource:export")
    public void export(QueryRequest queryRequest, SysRoleResource sysRoleResource, HttpServletResponse response) {
        List<SysRoleResource> sysRoleResources = this.sysRoleResourceService.findSysRoleResources(queryRequest, sysRoleResource).getRecords();
        ExcelKit.$Export(SysRoleResource.class, response).downXlsx(sysRoleResources, false);
    }
}
