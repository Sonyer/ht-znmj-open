package com.handturn.bole.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import com.handturn.bole.system.entity.SysGroup;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.service.ISysGroupReportService;
import com.handturn.bole.system.service.ISysGroupResourceService;
import com.handturn.bole.system.service.ISysGroupService;
import com.handturn.bole.system.service.ISysResourceService;
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
 * 系统-资源组 Controller
 *
 * @author Eric
 * @date 2020-05-23 14:55:18
 */
@Slf4j
@Validated
@RestController
@RequestMapping("system/sysGroup")
public class SysGroupController extends BaseController {

    @Autowired
    private ISysGroupService sysGroupService;

    @Autowired
    private ISysResourceService sysResourceService;

    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;

    @Autowired
    private ISysGroupResourceService sysGroupResourceService;

    @Autowired
    private ISysGroupReportService sysGroupReportService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysGroup:view")
    public FebsResponse sysGroupList(QueryRequest request, SysGroup sysGroup) {
        Map<String, Object> dataTable = getDataTable(this.sysGroupService.findSysGroups(request, sysGroup));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑分组/按钮", exceptionMessage = "编辑分组失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysGroup:add","sysGroup:update"})
    public FebsResponse addSysGroupr(@Valid SysGroup sysGroup) {
        this.sysGroupService.saveSysGroup(sysGroup);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{sysGroupIds}")
    @RequiresPermissions("sysGroup:enable")
    @ControllerEndpoint(exceptionMessage = "启用分组失败")
    public FebsResponse enableSysGroup(@NotBlank(message = "{required}") @PathVariable String sysGroupIds) {
        String[] sysGroupIdArr = sysGroupIds.split(StringPool.COMMA);
        this.sysGroupService.enableSysGroup(sysGroupIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysGroupIds}")
    @RequiresPermissions("sysGroup:disable")
    @ControllerEndpoint(exceptionMessage = "禁用分组失败")
    public FebsResponse disableSysGroup(@NotBlank(message = "{required}") @PathVariable String sysGroupIds) {
        String[] sysGroupIdArr = sysGroupIds.split(StringPool.COMMA);
        this.sysGroupService.disableSysGroup(sysGroupIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("oneTouchFlush/{sysGroupIds}")
    @RequiresPermissions("sysGroup:oneTouchFlush")
    @ControllerEndpoint(exceptionMessage = "禁用分组失败")
    public FebsResponse oneTouchFlush(@NotBlank(message = "{required}") @PathVariable String sysGroupIds) {
        String[] sysGroupIdArr = sysGroupIds.split(StringPool.COMMA);
        this.sysGroupService.oneTouchFlush(sysGroupIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改分组", exceptionMessage = "导出Excel失败")
    @PostMapping("sysGroup/excel")
    @ResponseBody
    @RequiresPermissions("sysGroup:export")
    public void export(QueryRequest queryRequest, SysGroup sysGroup, HttpServletResponse response) {
        List<SysGroup> sysGroups = this.sysGroupService.findSysGroups(queryRequest, sysGroup).getRecords();
        ExcelKit.$Export(SysGroup.class, response).downXlsx(sysGroups, false);
    }

    @PostMapping("resourceTree")
    @RequiresPermissions("sysGroup:auth")
    @ControllerEndpoint(exceptionMessage = "获取菜单树失败")
    public FebsResponse getResourceTree(String groupId,String rootRourceId) {

        SysGroup sysGroup = sysGroupService.findSysGroupById(Long.valueOf(groupId));
        Set<Long> resourceIds = sysGroupResourceService.findSysResourceIds4Group(Long.valueOf(groupId));
        CommonTree<SysResource> sysResources = this.sysResourceService.findSysResourcesByRootId(rootRourceId,resourceIds);

        return new FebsResponse().success().data(sysResources.getChilds());
    }

    @PostMapping("saveOrgResource")
    @RequiresPermissions("sysGroup:auth")
    @ControllerEndpoint(exceptionMessage = "获取菜单树失败")
    public FebsResponse saveOrgResource(String groupId,String rootModuleId,String resourceIds) {
        String[] resourceIdArr = resourceIds.split(",");
        sysGroupResourceService.saveGroupResource(Long.valueOf(groupId),Long.valueOf(rootModuleId),resourceIdArr);
        return new FebsResponse().success().data(null);
    }

    @PostMapping("reportTree")
    @RequiresPermissions("sysGroup:auth")
    @ControllerEndpoint(exceptionMessage = "获取报表树失败")
    public FebsResponse reportTree(String groupId,String rootReportId) {

        SysGroup sysGroup = sysGroupService.findSysGroupById(Long.valueOf(groupId));
        Set<Long> reportIds = sysGroupReportService.findReportIds4Group(Long.valueOf(groupId));
        CommonTree<SysconfQueryReport> sysResources = this.sysconfQueryReportService.findReportsByRootId(rootReportId,reportIds);

        return new FebsResponse().success().data(sysResources.getChilds());
    }

    @PostMapping("saveGroupReport")
    @RequiresPermissions("sysGroup:auth")
    @ControllerEndpoint(exceptionMessage = "获取报表树失败")
    public FebsResponse saveGroupReport(String groupId,String rootReportId,String reportIds) {
        String[] reportIdArr = reportIds.split(",");
        sysGroupReportService.saveGroupReport(Long.valueOf(groupId),Long.valueOf(rootReportId),reportIdArr);
        return new FebsResponse().success().data(null);
    }

}
