package com.handturn.bole.monitor.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.monitor.entity.SysLoginLog;
import com.handturn.bole.system.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @author Eric
 */
public interface ISysLoginLogService extends IService<SysLoginLog> {

    /**
     * 获取登录日志分页信息
     *
     * @param loginLog 传参
     * @param request  request
     * @return ICustomPage<SysLoginLog>
     */
    ICustomPage<SysLoginLog> findSysLoginLogs(SysLoginLog loginLog, QueryRequest request);

    /**
     * 保存登录日志
     *
     * @param loginLog 登录日志
     */
    void saveSysLoginLog(SysLoginLog loginLog);

    /**
     * 删除登录日志
     *
     * @param ids 日志 id集合
     */
    void deleteSysLoginLogs(String[] ids);

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
