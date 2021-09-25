package com.handturn.bole.job.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.job.entity.ScheduleJob;
import com.handturn.bole.job.service.IScheduleJobService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author Eric
 */
@Slf4j
@Validated
@RestController
@RequestMapping("job")
public class ScheduleJobController extends BaseController {

    @Autowired
    private IScheduleJobService jobService;

    @GetMapping
    @RequiresPermissions("job:view")
    public FebsResponse jobList(QueryRequest request, ScheduleJob job) {
        Map<String, Object> dataTable = getDataTable(this.jobService.findJobs(request, job));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("cron/check")
    public boolean checkCron(String cron) {
        try {
            return CronExpression.isValidExpression(cron);
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping
    @RequiresPermissions("job:add")
    @ControllerEndpoint(operation = "新增定时任务", exceptionMessage = "新增定时任务失败")
    public FebsResponse addJob(@Valid ScheduleJob job) {
        this.jobService.createJob(job);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{jobIds}")
    @RequiresPermissions("job:delete")
    @ControllerEndpoint(operation = "删除定时任务", exceptionMessage = "删除定时任务失败")
    public FebsResponse deleteJob(@NotBlank(message = "{required}") @PathVariable String jobIds) {
        String[] ids = jobIds.split(StringPool.COMMA);
        this.jobService.deleteJobs(ids);
        return new FebsResponse().success();
    }

    @PostMapping("update")
    @ControllerEndpoint(operation = "修改定时任务", exceptionMessage = "修改定时任务失败")
    public FebsResponse updateJob(@Valid ScheduleJob job) {
        this.jobService.updateJob(job);
        return new FebsResponse().success();
    }

    @GetMapping("run/{jobIds}")
    @RequiresPermissions("job:run")
    @ControllerEndpoint(operation = "执行定时任务", exceptionMessage = "执行定时任务失败")
    public FebsResponse runJob(@NotBlank(message = "{required}") @PathVariable String jobIds) {
        this.jobService.run(jobIds);
        return new FebsResponse().success();
    }

    @GetMapping("pause/{jobIds}")
    @RequiresPermissions("job:pause")
    @ControllerEndpoint(operation = "暂停定时任务", exceptionMessage = "暂停定时任务失败")
    public FebsResponse pauseJob(@NotBlank(message = "{required}") @PathVariable String jobIds) {
        this.jobService.pause(jobIds);
        return new FebsResponse().success();
    }

    @GetMapping("resume/{jobIds}")
    @RequiresPermissions("job:resume")
    @ControllerEndpoint(operation = "恢复定时任务", exceptionMessage = "恢复定时任务失败")
    public FebsResponse resumeJob(@NotBlank(message = "{required}") @PathVariable String jobIds) {
        this.jobService.resume(jobIds);
        return new FebsResponse().success();
    }

    @GetMapping("excel")
    @RequiresPermissions("job:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败")
    public void export(QueryRequest request, ScheduleJob job, HttpServletResponse response) {
        List<ScheduleJob> jobs = this.jobService.findJobs(request, job).getRecords();
        ExcelKit.$Export(ScheduleJob.class, response).downXlsx(jobs, false);
    }
}
