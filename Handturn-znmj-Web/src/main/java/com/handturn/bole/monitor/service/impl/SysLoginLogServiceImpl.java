package com.handturn.bole.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.AddressUtil;
import com.handturn.bole.common.utils.HttpContextUtil;
import com.handturn.bole.common.utils.IPUtil;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.monitor.entity.SysLoginLog;
import com.handturn.bole.monitor.mapper.SysLoginLogMapper;
import com.handturn.bole.monitor.service.ISysLoginLogService;
import com.handturn.bole.system.entity.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Eric
 */
@Service("loginLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

    @Override
    public ICustomPage<SysLoginLog> findSysLoginLogs(SysLoginLog loginLog, QueryRequest request) {
        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(loginLog.getUserCode())) {
            queryWrapper.lambda().eq(SysLoginLog::getUserCode, loginLog.getUserCode().toLowerCase());
        }
        if (StringUtils.isNotBlank(loginLog.getUserName())) {
            queryWrapper.lambda().eq(SysLoginLog::getUserName, loginLog.getUserName().toLowerCase());
        }
        if (StringUtils.isNotBlank(loginLog.getLoginTimeFrom()) && StringUtils.isNotBlank(loginLog.getLoginTimeTo())) {
            queryWrapper.lambda()
                    .ge(SysLoginLog::getLoginTime, loginLog.getLoginTimeFrom())
                    .le(SysLoginLog::getLoginTime, loginLog.getLoginTimeTo());
        }

        CustomPage<SysLoginLog> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "loginTime", FebsConstant.ORDER_DESC, true);

        return (ICustomPage<SysLoginLog>) this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void saveSysLoginLog(SysLoginLog loginLog) {
        loginLog.setLoginTime(new Date());
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        loginLog.setIp(ip);
        loginLog.setLocation(AddressUtil.getCityInfo(ip));
        this.save(loginLog);
    }

    @Override
    @Transactional
    public void deleteSysLoginLogs(String[] ids) {
        List<String> list = Arrays.asList(ids);
        baseMapper.deleteBatchIds(list);
    }

    @Override
    public Long findTotalVisitCount() {
        return this.baseMapper.findTotalVisitCount();
    }

    @Override
    public Long findTodayVisitCount() {
        return this.baseMapper.findTodayVisitCount();
    }

    @Override
    public Long findTodayIp() {
        return this.baseMapper.findTodayIp();
    }

    @Override
    public List<Map<String, Object>> findLastSevenDaysVisitCount(SysUser sysUser) {
        return this.baseMapper.findLastSevenDaysVisitCount(sysUser);
    }
}
