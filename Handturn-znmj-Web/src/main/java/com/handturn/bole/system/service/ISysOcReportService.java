package com.handturn.bole.system.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysOcReport;

import java.util.Set;

/**
 * 系统-网点报表 Service接口
 *
 * @author Eric
 * @date 2020-02-16 20:11:11
 */
public interface ISysOcReportService extends IService<SysOcReport> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysOcReport sysOcReport
     * @return ICustomPage<SysOcReport>
     */
    ICustomPage<SysOcReport> findSysOcReports(QueryRequest request, SysOcReport sysOcReport);

    /**
     * 修改
     *
     * @param sysOcReport sysOcReport
     */
    SysOcReport saveSysOcReport(SysOcReport sysOcReport);

    /**
     * 启用
     *
     * @param sysOcReportIds sysOcReportIds
     */
    void enableSysOcReport(String[] sysOcReportIds);

    /**
    * 禁用
    *
    * @param sysOcReportIds sysOcReportIds
    */
    void disableSysOcReport(String[] sysOcReportIds);


    /**
    * 通过Id获取信息
    * @param sysOcReportId
    * @return
    */
    SysOcReport findSysOcReportById(Long sysOcReportId);

    /**
     * 获取组织报表
     * @param ocId
     * @return
     */
    Set<Long> findReportIds4OrgOc(Long ocId);

    /**
     * 保存组织报表
     * @param ocId
     * @param rootReportId
     * @param reportIds
     */
    void saveOrgOcReport(Long ocId,Long rootReportId,String[] reportIds);

    /**
     * 通过OCID 删除
     * @param ocId
     */
    void deleteOcReportByOc(Long ocId);
}
