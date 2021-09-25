package com.handturn.bole.job.mapper;

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.job.entity.ScheduleJobLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

/**
 * 调度日志表 Mapper
 *
 * @author Eric
 * @date 2019-12-13 20:20:24
 */
public interface ScheduleJobLogMapper extends BaseMapper<ScheduleJobLog> {

    ICustomPage<ScheduleJobLog> findForPage(CustomPage page, @Param("scheduleJobLog") ScheduleJobLog scheduleJobLog);

}
