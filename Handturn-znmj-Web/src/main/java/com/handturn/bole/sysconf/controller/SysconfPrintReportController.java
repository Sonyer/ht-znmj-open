package com.handturn.bole.sysconf.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.entity.SysconfPrintReport;
import com.handturn.bole.sysconf.service.ISysconfPrintReportService;
import com.handturn.bole.system.entity.OcType;
import com.handturn.bole.system.entity.OrgType;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 系统配置-打印模板 Controller
 *
 * @author Eric
 * @date 2020-01-31 09:19:11
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sysconf/sysconfPrintReport")
public class SysconfPrintReportController extends BaseController {

    @Autowired
    private ISysconfPrintReportService sysconfPrintReportService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysconfPrint:view")
    public FebsResponse sysconfPrintReportList(QueryRequest request, SysconfPrintReport sysconfPrintReport) {
        Map<String, Object> dataTable = getDataTable(this.sysconfPrintReportService.findSysconfPrintReports(request, sysconfPrintReport));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑打印模板/按钮", exceptionMessage = "编辑打印模板失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysconfPrint:add","sysconfPrint:update"})
    public FebsResponse addSysconfPrintReportr(@Valid SysconfPrintReport sysconfPrintReport) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
            sysconfPrintReport.setIsSysCreated(BaseBoolean.TRUE);
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        this.sysconfPrintReportService.saveSysconfPrintReport(sysconfPrintReport);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{sysconfPrintReportIds}")
    @RequiresPermissions("sysconfPrint:enable")
    @ControllerEndpoint(exceptionMessage = "启用打印模板失败")
    public FebsResponse enableSysconfPrintReport(@NotBlank(message = "{required}") @PathVariable String sysconfPrintReportIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfPrintReportIdArr = sysconfPrintReportIds.split(StringPool.COMMA);
        this.sysconfPrintReportService.enableSysconfPrintReport(sysconfPrintReportIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysconfPrintReportIds}")
    @RequiresPermissions("sysconfPrint:disable")
    @ControllerEndpoint(exceptionMessage = "禁用打印模板失败")
    public FebsResponse disableSysconfPrintReport(@NotBlank(message = "{required}") @PathVariable String sysconfPrintReportIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfPrintReportIdArr = sysconfPrintReportIds.split(StringPool.COMMA);
        this.sysconfPrintReportService.disableSysconfPrintReport(sysconfPrintReportIdArr);
        return new FebsResponse().success();
    }
}
