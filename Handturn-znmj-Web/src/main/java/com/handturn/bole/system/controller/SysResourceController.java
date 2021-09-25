package com.handturn.bole.system.controller;

import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.system.entity.OrgType;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.service.ISysResourceService;
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
 * 系统-资源 Controller
 *
 * @author Eric
 * @date 2019-12-01 10:31:46
 */
@Slf4j
@Validated
@RestController
@RequestMapping("system/sysResource")
public class SysResourceController extends BaseController {

    @Autowired
    private ISysResourceService sysResourceService;
    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping("tree")
    @RequiresPermissions("sysResource:view")
    @ControllerEndpoint(exceptionMessage = "获取菜单树失败")
    public FebsResponse getResourceTree(SysResource sysResource) {
        CommonTree<SysResource> sysResources = this.sysResourceService.findSysResources(sysResource);
        return new FebsResponse().success().data(sysResources.getChilds());
    }

    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysResource:view")
    public FebsResponse getAllSysResources(SysResource sysResource) {
        List<SysResource> dataTable = this.sysResourceService.findSysResources4Table(sysResource);
        Map<String, Object> data = new HashMap<>();
        data.put("rows", dataTable);
        data.put("total", CollectionUtils.size(dataTable));
        return new FebsResponse().success().data(data);
    }

    @PostMapping("save")
    @RequiresPermissions({"sysResource:add","sysResource:update"})
    @ControllerEndpoint(operation = "编辑菜单/按钮", exceptionMessage = "编辑菜单/按钮失败")
    public FebsResponse save(@Valid SysResource sysResource) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        this.sysResourceService.saveSysResource(sysResource);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{sysResourceIds}")
    @RequiresPermissions("sysResource:delete")
    @ControllerEndpoint(operation = "删除菜单/按钮", exceptionMessage = "删除菜单/按钮失败")
    public FebsResponse delete(@NotBlank(message = "{required}") @PathVariable String sysResourceIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            throw new FebsException("非系统组织不允许操作!");
        }
        this.sysResourceService.deleteSysResource(sysResourceIds);
        return new FebsResponse().success();
    }


    @GetMapping("excel")
    @RequiresPermissions("sysResource:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败")
    public void export(SysResource sysResource, HttpServletResponse response) {
        List<SysResource> sysResources = this.sysResourceService.findSysResources4Table(sysResource);
        ExcelKit.$Export(SysResource.class, response).downXlsx(sysResources, false);
    }
}
