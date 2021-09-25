package com.handturn.bole.system.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysOganizationReport;

import java.util.Set;

/**
 * 系统-组织报表 Service接口
 *
 * @author Eric
 * @date 2020-02-16 20:08:44
 */
public interface ISysOganizationReportService extends IService<SysOganizationReport> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysOganizationReport sysOganizationReport
     * @return ICustomPage<SysOganizationReport>
     */
    ICustomPage<SysOganizationReport> findSysOganizationReports(QueryRequest request, SysOganizationReport sysOganizationReport);

    /**
     * 修改
     *
     * @param sysOganizationReport sysOganizationReport
     */
    SysOganizationReport saveSysOganizationReport(SysOganizationReport sysOganizationReport);

    /**
     * 获取组报表
     * @param orgId
     * @return
     */
    Set<Long> findSysReportIds4Org(Long orgId);


    /**
     * 保存组织报表
     * @param orgId
     * @param rootReportId
     * @param reportIds
     */
    void saveOrgReport(Long orgId,Long rootReportId,String[] reportIds);


    /**
    * 通过Id获取信息
    * @param sysOganizationReportId
    * @return
    */
    SysOganizationReport findSysOganizationReportById(Long sysOganizationReportId);
}
