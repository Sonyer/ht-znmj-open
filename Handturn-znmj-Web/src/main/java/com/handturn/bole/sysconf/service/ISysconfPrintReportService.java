package com.handturn.bole.sysconf.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfPrintReport;

/**
 * 系统配置-打印模板 Service接口
 *
 * @author Eric
 * @date 2020-01-31 09:19:11
 */
public interface ISysconfPrintReportService extends IService<SysconfPrintReport> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysconfPrintReport sysconfPrintReport
     * @return ICustomPage<SysconfPrintReport>
     */
    ICustomPage<SysconfPrintReport> findSysconfPrintReports(QueryRequest request, SysconfPrintReport sysconfPrintReport);

    /**
     * 修改
     *
     * @param sysconfPrintReport sysconfPrintReport
     */
    SysconfPrintReport saveSysconfPrintReport(SysconfPrintReport sysconfPrintReport);

    /**
     * 启用
     *
     * @param sysconfPrintReportIds sysconfPrintReportIds
     */
    void enableSysconfPrintReport(String[] sysconfPrintReportIds);

    /**
    * 禁用
    *
    * @param sysconfPrintReportIds sysconfPrintReportIds
    */
    void disableSysconfPrintReport(String[] sysconfPrintReportIds);


    /**
    * 通过Id获取信息
    * @param sysconfPrintReportId
    * @return
    */
    SysconfPrintReport findSysconfPrintReportById(Long sysconfPrintReportId);

    /**
     * 通过相关信息获取报表模板
     * @param printCode
     * @return
     */
    SysconfPrintReport findSysconfPrintReportByPrintInfo(String printCode,String orgCode,String warehouseCode,String clientCode);
}
