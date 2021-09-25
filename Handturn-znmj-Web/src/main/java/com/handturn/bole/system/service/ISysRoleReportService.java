package com.handturn.bole.system.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysRoleReport;

import java.util.Set;

/**
 * 系统-角色报表 Service接口
 *
 * @author Eric
 * @date 2020-02-16 20:13:20
 */
public interface ISysRoleReportService extends IService<SysRoleReport> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysRoleReport sysRoleReport
     * @return ICustomPage<SysRoleReport>
     */
    ICustomPage<SysRoleReport> findSysRoleReports(QueryRequest request, SysRoleReport sysRoleReport);

    /**
     * 修改
     *
     * @param sysRoleReport sysRoleReport
     */
    SysRoleReport saveSysRoleReport(SysRoleReport sysRoleReport);

    /**
     * 启用
     *
     * @param sysRoleReportIds sysRoleReportIds
     */
    void enableSysRoleReport(String[] sysRoleReportIds);

    /**
    * 禁用
    *
    * @param sysRoleReportIds sysRoleReportIds
    */
    void disableSysRoleReport(String[] sysRoleReportIds);


    /**
    * 通过Id获取信息
    * @param sysRoleReportId
    * @return
    */
    SysRoleReport findSysRoleReportById(Long sysRoleReportId);

    /**
     * 获取组织报表
     * @param roleId
     * @return
     */
    Set<Long> findReportIds4Role(Long roleId);

    /**
     * 保存组织报表
     * @param roleId
     * @param rootReportId
     * @param reportIds
     */
    void saveRoleReport(Long roleId,Long rootReportId,String[] reportIds);

    /**
     * 通过角色ID删除
     * @param roleId
     */
    void deleteRoleReportByRole(Long roleId);
}
