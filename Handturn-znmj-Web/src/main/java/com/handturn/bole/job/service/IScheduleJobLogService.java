package com.handturn.bole.job.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.job.entity.ScheduleJobLog;

/**
 * @author Eric
 */
public interface IScheduleJobLogService extends IService<ScheduleJobLog> {

    ICustomPage<ScheduleJobLog> findJobLogs(QueryRequest request, ScheduleJobLog jobLog);

    void saveJobLog(ScheduleJobLog log);

    void deleteJobLogs(String[] jobLogIds);
}
