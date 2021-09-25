package com.handturn.bole.job.util;

import com.handturn.bole.common.utils.SpringContextUtil;
import com.handturn.bole.job.entity.ScheduleJob;
import com.handturn.bole.job.entity.ScheduleJobLog;
import com.handturn.bole.job.entity.ScheduleLogStatus;
import com.handturn.bole.job.service.IScheduleJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 定时任务
 *
 * @author Eric
 */
@Slf4j
public class ScheduleJobUtil extends QuartzJobBean {

    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get(ScheduleJob.JOB_PARAM_KEY);

        // 获取spring bean
        IScheduleJobLogService scheduleJobLogService = SpringContextUtil.getBean(IScheduleJobLogService.class);

        ScheduleJobLog jobLog = new ScheduleJobLog();
        jobLog.setJobId(scheduleJob.getId());
        jobLog.setBeanName(scheduleJob.getBeanName());
        jobLog.setMethodName(scheduleJob.getMethodName());
        jobLog.setParams(scheduleJob.getParams());
        jobLog.setCreateDate(new Date());

        long startTime = System.currentTimeMillis();

        try {
            // 执行任务
            log.info("任务准备执行，任务ID:{}", scheduleJob.getId());
            ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(), scheduleJob.getMethodName(),
                    scheduleJob.getParams());
            Future<?> future = service.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            jobLog.setTimes(times);
            // 任务状态 0:成功 1:失败
            jobLog.setStatus(ScheduleLogStatus.SUCCESS.getValue());

            log.info("任务执行完毕，任务ID:{} 总共耗时:{} 毫秒", scheduleJob.getId(), times);
        } catch (Exception e) {
            log.error("任务执行失败，任务ID:" + scheduleJob.getId(), e);
            long times = System.currentTimeMillis() - startTime;
            jobLog.setTimes(times);
            // 任务状态 0:成功 1:失败
            jobLog.setStatus(ScheduleLogStatus.ERROR.getValue());
            jobLog.setError(StringUtils.substring(e.toString(), 0, 2000));
        } finally {
            scheduleJobLogService.saveJobLog(jobLog);
        }
    }
}
