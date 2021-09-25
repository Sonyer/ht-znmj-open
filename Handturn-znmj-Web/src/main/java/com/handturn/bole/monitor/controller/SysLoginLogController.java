package com.handturn.bole.monitor.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.monitor.entity.SysLoginLog;
import com.handturn.bole.monitor.service.ISysLoginLogService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author Eric
 */
@Slf4j
@RestController
@RequestMapping("loginLog")
public class SysLoginLogController extends BaseController {

    @Autowired
    private ISysLoginLogService loginLogService;

    @GetMapping("list")
    @RequiresPermissions("loginlog:view")
    public FebsResponse loginLogList(SysLoginLog loginLog, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.loginLogService.findSysLoginLogs(loginLog, request));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("delete/{ids}")
    @RequiresPermissions("loginlog:delete")
    @ControllerEndpoint(exceptionMessage = "删除日志失败")
    public FebsResponse deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) {
        String[] loginLogIds = ids.split(StringPool.COMMA);
        this.loginLogService.deleteSysLoginLogs(loginLogIds);
        return new FebsResponse().success();
    }

    @GetMapping("excel")
    @RequiresPermissions("loginlog:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败")
    public void export(QueryRequest request, SysLoginLog loginLog, HttpServletResponse response) {
        List<SysLoginLog> loginLogs = this.loginLogService.findSysLoginLogs(loginLog, request).getRecords();
        ExcelKit.$Export(SysLoginLog.class, response).downXlsx(loginLogs, false);
    }
}
