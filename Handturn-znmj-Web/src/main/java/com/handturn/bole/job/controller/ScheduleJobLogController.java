package com.handturn.bole.job.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.job.entity.ScheduleJobLog;
import com.handturn.bole.job.service.IScheduleJobLogService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RestController
@RequestMapping("jobLog")
public class ScheduleJobLogController extends BaseController {

    @Autowired
    private IScheduleJobLogService jobLogService;

    @GetMapping
    @RequiresPermissions("job:log:view")
    public FebsResponse jobLogList(QueryRequest request, ScheduleJobLog log) {
        Map<String, Object> dataTable = getDataTable(this.jobLogService.findJobLogs(request, log));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("delete/{jobIds}")
    @RequiresPermissions("job:log:delete")
    @ControllerEndpoint(exceptionMessage = "删除调度日志失败")
    public FebsResponse deleteJobLog(@NotBlank(message = "{required}") @PathVariable String jobIds) {
        String[] ids = jobIds.split(StringPool.COMMA);
        this.jobLogService.deleteJobLogs(ids);
        return new FebsResponse().success();
    }

    @GetMapping("excel")
    @RequiresPermissions("job:log:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败")
    public void export(QueryRequest request, ScheduleJobLog jobLog, HttpServletResponse response) {
        List<ScheduleJobLog> jobLogs = this.jobLogService.findJobLogs(request, jobLog).getRecords();
        ExcelKit.$Export(ScheduleJobLog.class, response).downXlsx(jobLogs, false);
    }
}
