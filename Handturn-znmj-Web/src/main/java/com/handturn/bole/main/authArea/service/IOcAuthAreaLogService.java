package com.handturn.bole.main.authArea.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.main.authArea.entity.OcAuthAreaLog;

/**
 * 网点-访客区域权限日志 Service接口
 *
 * @author Eric
 * @date 2020-12-05 20:04:29
 */
public interface IOcAuthAreaLogService extends IService<OcAuthAreaLog> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param ocAuthAreaLog ocAuthAreaLog
     * @return ICustomPage<OcAuthAreaLog>
     */
    ICustomPage<OcAuthAreaLog> findOcAuthAreaLogs(QueryRequest request, OcAuthAreaLog ocAuthAreaLog);

    /**
     * 修改
     *
     * @param ocAuthAreaLog ocAuthAreaLog
     */
    OcAuthAreaLog saveOcAuthAreaLog(OcAuthAreaLog ocAuthAreaLog);

    /**
     * 启用
     *
     * @param ocAuthAreaLogIds ocAuthAreaLogIds
     */
    void enableOcAuthAreaLog(String[] ocAuthAreaLogIds);

    /**
    * 禁用
    *
    * @param ocAuthAreaLogIds ocAuthAreaLogIds
    */
    void disableOcAuthAreaLog(String[] ocAuthAreaLogIds);


    /**
    * 通过Id获取信息
    * @param ocAuthAreaLogId
    * @return
    */
    OcAuthAreaLog findOcAuthAreaLogById(Long ocAuthAreaLogId);
}
