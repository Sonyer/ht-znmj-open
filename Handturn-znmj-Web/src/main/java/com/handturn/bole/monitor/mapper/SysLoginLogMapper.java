package com.handturn.bole.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.monitor.entity.SysLoginLog;
import com.handturn.bole.system.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @author Eric
 */
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long findTotalVisitCount();

    /**
     * 获取系统今日访问次数
     *
     * @return Long
     */
    Long findTodayVisitCount();

    /**
     * 获取系统今日访问 IP数
     *
     * @return Long
     */
    Long findTodayIp();

    /**
     * 获取系统近七天来的访问记录
     *
     * @param sysUser 用户
     * @return 系统近七天来的访问记录
     */
    List<Map<String, Object>> findLastSevenDaysVisitCount(SysUser sysUser);
}