package com.handturn.bole.sysconf.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfQueryReportColumn;

import java.util.List;
import java.util.Map;

/**
 * 系统-报表查询列 Service接口
 *
 * @author Eric
 * @date 2020-02-12 09:14:32
 */
public interface ISysconfQueryReportColumnService extends IService<SysconfQueryReportColumn> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysconfQueryReportColumn sysconfQueryReportColumn
     * @return ICustomPage<SysconfQueryReportColumn>
     */
    ICustomPage<SysconfQueryReportColumn> findSysconfQueryReportColumns(QueryRequest request, SysconfQueryReportColumn sysconfQueryReportColumn);

    /**
     * 修改
     *
     * @param sysconfQueryReportColumn sysconfQueryReportColumn
     */
    SysconfQueryReportColumn saveSysconfQueryReportColumn(SysconfQueryReportColumn sysconfQueryReportColumn);

    /**
     * 启用
     *
     * @param sysconfQueryReportColumnIds sysconfQueryReportColumnIds
     */
    void enableSysconfQueryReportColumn(String[] sysconfQueryReportColumnIds);

    /**
    * 禁用
    *
    * @param sysconfQueryReportColumnIds sysconfQueryReportColumnIds
    */
    void disableSysconfQueryReportColumn(String[] sysconfQueryReportColumnIds);


    /**
     * 通过Id获取信息
     * @param sysconfQueryReportColumnId
     * @return
     */
    SysconfQueryReportColumn findSysconfQueryReportColumnById(Long sysconfQueryReportColumnId);

    /**
     * 通过报表ID获取字段
     * @param reportId
     * @return
     */
    List<SysconfQueryReportColumn> findColumnsByReportId(Long reportId);

    /**
     * 通过报表ID获取字段
     * @param reportId
     * @return
     */
    Map<String,SysconfQueryReportColumn> findColumnsMapByReportId(Long reportId);
}
