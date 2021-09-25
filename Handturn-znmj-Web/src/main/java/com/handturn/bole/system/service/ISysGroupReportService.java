package com.handturn.bole.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysGroupReport;

import java.util.Set;

/**
 * 系统-组报表 Service接口
 *
 * @author Eric
 * @date 2020-05-23 14:55:39
 */
public interface ISysGroupReportService extends IService<SysGroupReport> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysGroupReport sysGroupReport
     * @return ICustomPage<SysGroupReport>
     */
    ICustomPage<SysGroupReport> findSysGroupReports(QueryRequest request, SysGroupReport sysGroupReport);

    /**
     * 修改
     *
     * @param sysGroupReport sysGroupReport
     */
    SysGroupReport saveSysGroupReport(SysGroupReport sysGroupReport);

    /**
     * 启用
     *
     * @param sysGroupReportIds sysGroupReportIds
     */
    void enableSysGroupReport(String[] sysGroupReportIds);

    /**
    * 禁用
    *
    * @param sysGroupReportIds sysGroupReportIds
    */
    void disableSysGroupReport(String[] sysGroupReportIds);


    /**
    * 通过Id获取信息
    * @param sysGroupReportId
    * @return
    */
    SysGroupReport findSysGroupReportById(Long sysGroupReportId);

    /**
     * 获取分组报表
     * @param groupId
     * @return
     */
    Set<Long> findReportIds4Group(Long groupId);

    /**
     * 保存分组报表
     * @param groupId
     * @param rootReportId
     * @param reportIds
     */
    void saveGroupReport(Long groupId,Long rootReportId,String[] reportIds);
}
