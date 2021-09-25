package com.handturn.bole.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import com.handturn.bole.system.entity.*;
import com.handturn.bole.system.service.*;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统-角色 Controller
 *
 * @author Eric
 * @date 2019-12-01 10:35:21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("system/sysRole")
public class SysRoleController extends BaseController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysResourceService sysResourceService;

    @Autowired
    private ISysRoleResourceService sysRoleResourceService;

    @Autowired
    private ISysRoleOcService sysRoleOcService;

    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleUserService sysRoleUserService;

    @Autowired
    private ISysRoleReportService sysRoleReportService;

    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;

    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysRole:view")
    public FebsResponse sysRoleList(QueryRequest request, SysRole sysRole) {
        Map<String, Object> dataTable = getDataTable(this.sysRoleService.findSysRoles(request, sysRole));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑角色/按钮", exceptionMessage = "编辑角色失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysRole:add","sysRole:update"})
    public FebsResponse addSysRoler(@Valid SysRole sysRole) {
        sysRole.setOcId(UserInfoHolder.getUserInfo().getCurrentOc().getId());
        sysRole.setOrgId(UserInfoHolder.getUserInfo().getCurrentOrg().getId());
        this.sysRoleService.saveSysRole(sysRole,false);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{sysRoleIds}")
    @RequiresPermissions("sysRole:enable")
    @ControllerEndpoint(exceptionMessage = "启用角色失败")
    public FebsResponse enableSysRole(@NotBlank(message = "{required}") @PathVariable String sysRoleIds) {
        String[] sysRoleIdArr = sysRoleIds.split(StringPool.COMMA);
        this.sysRoleService.enableSysRole(sysRoleIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysRoleIds}")
    @RequiresPermissions("sysRole:disable")
    @ControllerEndpoint(exceptionMessage = "禁用角色失败")
    public FebsResponse disableSysRole(@NotBlank(message = "{required}") @PathVariable String sysRoleIds) {
        String[] sysRoleIdArr = sysRoleIds.split(StringPool.COMMA);
        this.sysRoleService.disableSysRole(sysRoleIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改角色", exceptionMessage = "导出Excel失败")
    @PostMapping("sysRole/excel")
    @ResponseBody
    @RequiresPermissions("sysRole:export")
    public void export(QueryRequest queryRequest, SysRole sysRole, HttpServletResponse response) {
        List<SysRole> sysRoles = this.sysRoleService.findSysRoles(queryRequest, sysRole).getRecords();
        ExcelKit.$Export(SysRole.class, response).downXlsx(sysRoles, false);
    }

    @PostMapping("resourceTree")
    @RequiresPermissions("sysRole:auth")
    @ControllerEndpoint(exceptionMessage = "获取菜单树失败")
    public FebsResponse getResourceTree(String roleId,String rootRourceId) {

        SysRole sysRole = sysRoleService.findSysRoleById(Long.valueOf(roleId));
        Set<Long> resourceIds = sysRoleResourceService.findSysResourceIds4Role(Long.valueOf(roleId));
        CommonTree<SysResource> sysResources = this.sysResourceService.findSysResourcesByRootIdOcId(rootRourceId,String.valueOf(sysRole.getOcId()),resourceIds);

        return new FebsResponse().success().data(sysResources.getChilds());
    }

    @PostMapping("saveOrgResource")
    @RequiresPermissions("sysRole:auth")
    @ControllerEndpoint(exceptionMessage = "获取菜单树失败")
    public FebsResponse saveOrgResource(String roleId,String rootModuleId,String resourceIds) {
        String[] resourceIdArr = resourceIds.split(",");
        sysRoleResourceService.saveRoleResource(Long.valueOf(roleId),Long.valueOf(rootModuleId),resourceIdArr);
        return new FebsResponse().success().data(null);
    }

    @PostMapping("reportTree")
    @RequiresPermissions("sysRole:auth")
    @ControllerEndpoint(exceptionMessage = "获取报表树失败")
    public FebsResponse reportTree(String roleId,String rootReportId) {

        SysRole sysRole = sysRoleService.findSysRoleById(Long.valueOf(roleId));
        Set<Long> reportIds = sysRoleReportService.findReportIds4Role(Long.valueOf(roleId));
        CommonTree<SysconfQueryReport> sysResources = this.sysconfQueryReportService.findReportsByRootIdOcId(rootReportId,String.valueOf(sysRole.getOcId()),reportIds);

        return new FebsResponse().success().data(sysResources.getChilds());
    }

    @PostMapping("saveRoleReport")
    @RequiresPermissions("sysRole:auth")
    @ControllerEndpoint(exceptionMessage = "获取报表树失败")
    public FebsResponse saveRoleReport(String roleId,String rootReportId,String reportIds) {
        String[] reportIdArr = reportIds.split(",");
        sysRoleReportService.saveRoleReport(Long.valueOf(roleId),Long.valueOf(rootReportId),reportIdArr);
        return new FebsResponse().success().data(null);
    }


    @GetMapping("ocTree/{sysRoleId}")
    @RequiresPermissions("sysRole:dataAuth")
    @ControllerEndpoint(exceptionMessage = "获取网点树失败")
    public FebsResponse getOcTree(@NotBlank(message = "{required}") @PathVariable String sysRoleId) {

        SysRole sysRole = sysRoleService.findSysRoleById(Long.valueOf(sysRoleId));
        Set<Long> ocIds = sysRoleOcService.findOcIds4Role(Long.valueOf(sysRoleId));
        CommonTree<SysOrganizationOc> sysOcs = null;
        if(UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)
                || UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)){
            sysOcs = this.sysOrganizationOcService.findSysOcByOrgId(sysRole.getOrgId(),ocIds);
        }else{
            sysOcs = this.sysOrganizationOcService.findSysOcByOcId(sysRole.getOcId(),ocIds);
        }

        return new FebsResponse().success().data(sysOcs.getChilds());
    }

    @PostMapping("saveRoleOc")
    @RequiresPermissions("sysRole:dataAuth")
    @ControllerEndpoint(exceptionMessage = "保存角色网点失败")
    public FebsResponse saveRoleOc(String roleId,String ocIds) {
        String[] ocIdArr = ocIds.split(",");
        sysRoleOcService.saveRoleOc(Long.valueOf(roleId),ocIdArr);

        return new FebsResponse().success().data(null);
    }

    @GetMapping("userTree/{sysRoleId}")
    @RequiresPermissions("sysRole:bindUser")
    @ControllerEndpoint(exceptionMessage = "获取用户树失败")
    public FebsResponse userTree(@NotBlank(message = "{required}") @PathVariable String sysRoleId) {

        SysRole sysRole = sysRoleService.findSysRoleById(Long.valueOf(sysRoleId));
        Set<Long> userIds = sysRoleUserService.findUserIds4Role(Long.valueOf(sysRoleId));

        CommonTree<SysUser> sysUsers = this.sysUserService.findSysUserByOcId(sysRole.getOcId(),userIds);

        return new FebsResponse().success().data(sysUsers.getChilds());

    }

    @PostMapping("saveRoleUser")
    @RequiresPermissions("sysRole:bindUser")
    @ControllerEndpoint(exceptionMessage = "保存角色用户失败")
    public FebsResponse saveRoleUser(String roleId,String userIds) {
        String[] userIdArr = userIds.split(",");
        sysRoleUserService.saveRoleUser(Long.valueOf(roleId),userIdArr);
        return new FebsResponse().success().data(null);
    }

}
