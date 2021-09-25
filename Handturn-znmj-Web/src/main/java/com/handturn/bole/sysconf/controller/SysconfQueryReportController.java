package com.handturn.bole.sysconf.controller;

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
import com.handturn.bole.system.entity.OrgType;
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
 * 系统-报表 Controller
 *
 * @author Eric
 * @date 2020-02-12 09:06:44
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sysconf/sysconfQueryReport")
public class SysconfQueryReportController extends BaseController {

    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;

    @GetMapping("tree")
    @RequiresPermissions("sysconfQueryReport:view")
    @ControllerEndpoint(exceptionMessage = "获取报表树失败")
    public FebsResponse getReportTree(SysconfQueryReport sysconfQueryReport) {
        CommonTree<SysconfQueryReport> sysconfQueryReports = this.sysconfQueryReportService.findReportTree(sysconfQueryReport);
        return new FebsResponse().success().data(sysconfQueryReports.getChilds());
    }

    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysconfQueryReport:view")
    public FebsResponse getAllSysResources(SysconfQueryReport sysconfQueryReport) {
        List<SysconfQueryReport> dataTable = this.sysconfQueryReportService.findSysconfQueryReport4Table(sysconfQueryReport);
        Map<String, Object> data = new HashMap<>();
        data.put("rows", dataTable);
        data.put("total", CollectionUtils.size(dataTable));
        return new FebsResponse().success().data(data);
    }

    @ControllerEndpoint(operation = "编辑报表/按钮", exceptionMessage = "编辑报表失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysconfQueryReport:add","sysconfQueryReport:update"})
    public FebsResponse addSysconfQueryReportr(@Valid SysconfQueryReport sysconfQueryReport) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        return new FebsResponse().success().data(this.sysconfQueryReportService.saveSysconfQueryReport(sysconfQueryReport));
    }

    @GetMapping("delete/{sysconfQueryReportIds}")
    @RequiresPermissions("sysconfQueryReport:delete")
    @ControllerEndpoint(operation = "删除报表/按钮", exceptionMessage = "删除报表/按钮失败")
    public FebsResponse delete(@NotBlank(message = "{required}") @PathVariable String sysconfQueryReportIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfQueryReportIdArr = sysconfQueryReportIds.split(StringPool.COMMA);
        this.sysconfQueryReportService.deleteSysconfQueryReport(sysconfQueryReportIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("enable/{sysconfQueryReportIds}")
    @RequiresPermissions("sysconfQueryReport:enable")
    @ControllerEndpoint(exceptionMessage = "启用报表失败")
    public FebsResponse enableSysconfQueryReport(@NotBlank(message = "{required}") @PathVariable String sysconfQueryReportIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfQueryReportIdArr = sysconfQueryReportIds.split(StringPool.COMMA);
        this.sysconfQueryReportService.enableSysconfQueryReport(sysconfQueryReportIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysconfQueryReportIds}")
    @RequiresPermissions("sysconfQueryReport:disable")
    @ControllerEndpoint(exceptionMessage = "禁用报表失败")
    public FebsResponse disableSysconfQueryReport(@NotBlank(message = "{required}") @PathVariable String sysconfQueryReportIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfQueryReportIdArr = sysconfQueryReportIds.split(StringPool.COMMA);
        this.sysconfQueryReportService.disableSysconfQueryReport(sysconfQueryReportIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出报表", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("sysconfQueryReport:export")
    public void exportSysconfQueryReport(QueryRequest queryRequest, SysconfQueryReport sysconfQueryReport, HttpServletResponse response) {
        List<SysconfQueryReport> sysconfQueryReports = this.sysconfQueryReportService.findSysconfQueryReports(queryRequest, sysconfQueryReport).getRecords();
        ExcelKit.$Export(SysconfQueryReport.class, response).downXlsx(sysconfQueryReports, false);
    }
}
