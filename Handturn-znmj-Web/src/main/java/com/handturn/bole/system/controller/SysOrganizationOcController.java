package com.handturn.bole.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import com.handturn.bole.system.entity.OcType;
import com.handturn.bole.system.entity.SysOrganizationOc;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.service.ISysOcReportService;
import com.handturn.bole.system.service.ISysOcResourceService;
import com.handturn.bole.system.service.ISysOrganizationOcService;
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
 * 系统-公司操作运营中心 Controller
 *
 * @author MrBird
 * @date 2019-12-08 19:36:28
 */
@Slf4j
@Validated
@RestController
@RequestMapping("system/sysOrganizationOc")
public class SysOrganizationOcController extends BaseController {

    @Autowired
    private ISysResourceService sysResourceService;

    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;

    @Autowired
    private ISysOcResourceService sysOcResourceService;

    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;

    @Autowired
    private ISysOcReportService sysOcReportService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysOrganizationOc:view")
    public FebsResponse sysOrganizationOcList(QueryRequest request, SysOrganizationOc sysOrganizationOc) {
        Map<String, Object> dataTable = getDataTable(this.sysOrganizationOcService.findSysOrganizationOcs(request, sysOrganizationOc));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑网点按钮", exceptionMessage = "编辑网点失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysOrganizationOc:add","sysOrganizationOc:update"})
    public FebsResponse addSysOrganizationOcr(@Valid SysOrganizationOc sysOrganizationOc) {
        if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
            throw new FebsException("非系统管理不允许操作!");
        }
        sysOrganizationOc.setOrgId(UserInfoHolder.getUserInfo().getCurrentOrg().getId());
        this.sysOrganizationOcService.saveSysOrganizationOc(sysOrganizationOc,false);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{sysOrganizationOcIds}")
    @RequiresPermissions("sysOrganizationOc:enable")
    @ControllerEndpoint(exceptionMessage = "启用网点失败")
    public FebsResponse enableSysOrganizationOc(@NotBlank(message = "{required}") @PathVariable String sysOrganizationOcIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
            throw new FebsException("非系统管理不允许操作!");
        }
        String[] sysOrganizationOcIdArr = sysOrganizationOcIds.split(StringPool.COMMA);
        this.sysOrganizationOcService.enableSysOrganizationOc(sysOrganizationOcIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysOrganizationOcIds}")
    @RequiresPermissions("sysOrganizationOc:disable")
    @ControllerEndpoint(exceptionMessage = "禁用网点失败")
    public FebsResponse disableSysOrganizationOc(@NotBlank(message = "{required}") @PathVariable String sysOrganizationOcIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
            throw new FebsException("非系统管理不允许操作!");
        }
        String[] sysOrganizationOcIdArr = sysOrganizationOcIds.split(StringPool.COMMA);
        this.sysOrganizationOcService.disableSysOrganizationOc(sysOrganizationOcIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出网点", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("sysOrganizationOc:export")
    public void exportSysOrganizationOc(QueryRequest queryRequest, SysOrganizationOc sysOrganizationOc, HttpServletResponse response) {
        List<SysOrganizationOc> sysOrganizationOcs = this.sysOrganizationOcService.findSysOrganizationOcs(queryRequest, sysOrganizationOc).getRecords();
        ExcelKit.$Export(SysOrganizationOc.class, response).downXlsx(sysOrganizationOcs, false);
    }

    @PostMapping("resourceTree")
    @RequiresPermissions("sysOrganizationOc:auth")
    @ControllerEndpoint(exceptionMessage = "获取菜单树失败")
    public FebsResponse getResourceTree(String ocId,String rootRourceId) {

        SysOrganizationOc sysOrganizationOc = sysOrganizationOcService.findSysOrganizationOcById(Long.valueOf(ocId));
        Set<Long> resourceIds = sysOcResourceService.findSysResourceIds4OrgOc(Long.valueOf(ocId));
        CommonTree<SysResource> sysResources = this.sysResourceService.findSysResourcesByRootIdOrgId(rootRourceId,String.valueOf(sysOrganizationOc.getOrgId()),resourceIds);

        return new FebsResponse().success().data(sysResources.getChilds());
    }

    @PostMapping("saveOrgResource")
    @RequiresPermissions("sysOrganizationOc:auth")
    @ControllerEndpoint(exceptionMessage = "获取菜单树失败")
    public FebsResponse saveOrgResource(String ocId,String rootModuleId,String resourceIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
            throw new FebsException("非系统管理不允许操作!");
        }
        String[] resourceIdArr = resourceIds.split(",");
        sysOcResourceService.saveOrgOcResource(Long.valueOf(ocId),Long.valueOf(rootModuleId),resourceIdArr);
        return new FebsResponse().success().data(null);
    }

    @PostMapping("reportTree")
    @RequiresPermissions("sysOrganizationOc:report")
    @ControllerEndpoint(exceptionMessage = "获取报表树失败")
    public FebsResponse reportTree(String ocId,String rootReportId) {

        SysOrganizationOc sysOrganizationOc = sysOrganizationOcService.findSysOrganizationOcById(Long.valueOf(ocId));
        Set<Long> reportIds = sysOcReportService.findReportIds4OrgOc(Long.valueOf(ocId));
        CommonTree<SysconfQueryReport> sysResources = this.sysconfQueryReportService.findReportsByRootIdOrgId(rootReportId,String.valueOf(sysOrganizationOc.getOrgId()),reportIds);

        return new FebsResponse().success().data(sysResources.getChilds());
    }

    @PostMapping("saveOrgReport")
    @RequiresPermissions("sysOrganizationOc:report")
    @ControllerEndpoint(exceptionMessage = "获取报表树失败")
    public FebsResponse saveOrgReport(String ocId,String rootReportId,String reportIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
            throw new FebsException("非系统管理不允许操作!");
        }
        String[] reportIdArr = reportIds.split(",");
        sysOcReportService.saveOrgOcReport(Long.valueOf(ocId),Long.valueOf(rootReportId),reportIdArr);
        return new FebsResponse().success().data(null);
    }
}
