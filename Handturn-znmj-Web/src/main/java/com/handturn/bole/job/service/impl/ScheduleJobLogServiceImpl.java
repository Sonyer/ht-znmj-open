package com.handturn.bole.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.job.entity.ScheduleJobLog;
import com.handturn.bole.job.mapper.ScheduleJobLogMapper;
import com.handturn.bole.job.service.IScheduleJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author Eric
 */
@Slf4j
@Service("ScheduleJobLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper, ScheduleJobLog> implements IScheduleJobLogService {

    @Override
    public ICustomPage<ScheduleJobLog> findJobLogs(QueryRequest request, ScheduleJobLog jobLog) {
        LambdaQueryWrapper<ScheduleJobLog> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(jobLog.getBeanName())) {
            queryWrapper.eq(ScheduleJobLog::getBeanName, jobLog.getBeanName());
        }
        if (StringUtils.isNotBlank(jobLog.getMethodName())) {
            queryWrapper.eq(ScheduleJobLog::getMethodName, jobLog.getMethodName());
        }
        if (StringUtils.isNotBlank(jobLog.getStatus())) {
            queryWrapper.eq(ScheduleJobLog::getStatus, jobLog.getStatus());
        }
        CustomPage<ScheduleJobLog> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createDate", FebsConstant.ORDER_DESC, true);
        return (ICustomPage<ScheduleJobLog>) this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void saveJobLog(ScheduleJobLog log) {
        this.save(log);
    }

    @Override
    @Transactional
    public void deleteJobLogs(String[] jobLogIds) {
        List<String> list = Arrays.asList(jobLogIds);
        this.baseMapper.deleteBatchIds(list);
    }

}
