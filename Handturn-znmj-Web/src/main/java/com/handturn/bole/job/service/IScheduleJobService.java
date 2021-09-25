package com.handturn.bole.job.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.job.entity.ScheduleJob;

/**
 * @author Eric
 */
public interface IScheduleJobService extends IService<ScheduleJob> {

    ScheduleJob findJob(Long jobId);

    ICustomPage<ScheduleJob> findJobs(QueryRequest request, ScheduleJob job);

    void createJob(ScheduleJob job);

    void updateJob(ScheduleJob job);

    void deleteJobs(String[] jobIds);

    int updateBatch(String jobIds, String status);

    void run(String jobIds);

    void pause(String jobIds);

    void resume(String jobIds);

}
