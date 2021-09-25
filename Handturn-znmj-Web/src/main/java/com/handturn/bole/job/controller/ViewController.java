package com.handturn.bole.job.controller;

import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.job.entity.ScheduleJob;
import com.handturn.bole.job.service.IScheduleJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotBlank;

/**
 * @author Eric
 */
@Controller("jobView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "job")
public class ViewController {

    @Autowired
    private IScheduleJobService jobService;

    @GetMapping("job")
    @RequiresPermissions("job:view")
    public String online() {
        return FebsUtil.view("job/scheduleJob");
    }

    @GetMapping("job/add")
    @RequiresPermissions("job:add")
    public String jobAdd() {
        return FebsUtil.view("job/scheduleJobAdd");
    }

    @GetMapping("job/update/{jobId}")
    @RequiresPermissions("job:update")
    public String jobUpdate(@NotBlank(message = "{required}") @PathVariable Long jobId, Model model) {
        ScheduleJob job = jobService.findJob(jobId);
        model.addAttribute("job", job);
        return FebsUtil.view("job/scheduleJobUpdate");
    }

    @GetMapping("log")
    @RequiresPermissions("job:log:view")
    public String log() {
        return FebsUtil.view("job/scheduleJobLog");
    }

}
