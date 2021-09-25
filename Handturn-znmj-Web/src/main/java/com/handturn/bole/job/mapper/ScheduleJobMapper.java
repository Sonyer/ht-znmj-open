package com.handturn.bole.job.mapper;

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.job.entity.ScheduleJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 定时任务表 Mapper
 *
 * @author Eric
 * @date 2019-12-13 20:20:20
 */
public interface ScheduleJobMapper extends BaseMapper<ScheduleJob> {

    List<ScheduleJob> queryList();

    ICustomPage<ScheduleJob> findForPage(CustomPage page, @Param("scheduleJob") ScheduleJob scheduleJob);

}
