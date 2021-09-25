package com.handturn.bole.sysconf.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统-报表 Service接口
 *
 * @author Eric
 * @date 2020-02-12 09:06:44
 */
public interface ISysconfQueryReportService extends IService<SysconfQueryReport> {

    /**
     * 查找所有的报表/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    CommonTree<SysconfQueryReport> findReportTree(SysconfQueryReport sysconfQueryReport);

    /**
     * 查找用户的报表/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    CommonTree<SysconfQueryReport> findUserReport(String rootReportId,String userCode);

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    CommonTree<SysconfQueryReport> findReportsByRootId(String rootReportId, Set<Long> permReportIds);

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    CommonTree<SysconfQueryReport> findReportsByRootIdOrgId(String rootReportId,String orgId, Set<Long> permReportIds);

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    CommonTree<SysconfQueryReport> findReportsByRootIdOcId(String rootReportId,String ocId, Set<Long> permReportIds);

    /**
     * 按照条件查询table展示
     * @param sysconfQueryReport
     * @return
     */
    List<SysconfQueryReport> findSysconfQueryReport4Table(SysconfQueryReport sysconfQueryReport);

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysconfQueryReport sysconfQueryReport
     * @return ICustomPage<SysconfQueryReport>
     */
    ICustomPage<SysconfQueryReport> findSysconfQueryReports(QueryRequest request, SysconfQueryReport sysconfQueryReport);

    /**
     * 修改
     *
     * @param sysconfQueryReport sysconfQueryReport
     */
    SysconfQueryReport saveSysconfQueryReport(SysconfQueryReport sysconfQueryReport);


    /**
     * 删除
     *
     * @param sysconfQueryReportIds sysconfQueryReportIds
     */
    void deleteSysconfQueryReport(String[] sysconfQueryReportIds);


    /**
     * 启用
     *
     * @param sysconfQueryReportIds sysconfQueryReportIds
     */
    void enableSysconfQueryReport(String[] sysconfQueryReportIds);

    /**
    * 禁用
    *
    * @param sysconfQueryReportIds sysconfQueryReportIds
    */
    void disableSysconfQueryReport(String[] sysconfQueryReportIds);


    /**
    * 通过Id获取信息
    * @param sysconfQueryReportId
    * @return
    */
    SysconfQueryReport findSysconfQueryReportById(Long sysconfQueryReportId);

    /**
     * 查找所有根节点
     *
     * @return 用户菜单集合
     */
    List<SysconfQueryReport> findAllRootSysReports();

    /**
     * 通过根节点查询报表
     * @param orgId
     * @return
     */
    List<SysconfQueryReport> findAllRootSysReportsByOrgId(Long orgId);

    /**
     * 查找所有根节点
     *
     * @return 用户菜单集合
     */
    List<SysconfQueryReport> findAllRootSysReportsByOcId(Long ocId);

    /**
     * 通过根节点查询报表
     * @param rootReportId
     * @return
     */
    List<SysconfQueryReport> findSysconfReportsByRootId(String rootReportId);

    /**
     * 报表查询
     * @param request
     * @param reportId
     * @return
     */
    ICustomPage reportList(String reportId, QueryRequest queryRequest, HttpServletRequest request, HttpServletResponse response,Map<String,String> exitQueryParams);

    /**
     * 报表导出
     * @param request
     * @param reportId
     * @return
     */
    void reportExport(String reportId, QueryRequest queryRequest, HttpServletRequest request, HttpServletResponse response,Map<String,String> exitQueryParams);
}
