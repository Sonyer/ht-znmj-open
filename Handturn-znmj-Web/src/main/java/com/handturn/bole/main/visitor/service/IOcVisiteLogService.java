package com.handturn.bole.main.visitor.service;

import com.handturn.bole.main.visitor.entity.OcVisiteLog;

import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 网点-会员访客日志 Service接口
 *
 * @author Eric
 * @date 2020-12-06 17:24:27
 */
public interface IOcVisiteLogService extends IService<OcVisiteLog> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param ocVisiteLog ocVisiteLog
     * @return ICustomPage<OcVisiteLog>
     */
    ICustomPage<OcVisiteLog> findOcVisiteLogs(QueryRequest request, OcVisiteLog ocVisiteLog);

    /**
     * 修改
     *
     * @param ocVisiteLog ocVisiteLog
     */
    OcVisiteLog saveOcVisiteLog(OcVisiteLog ocVisiteLog);

    /**
     * 启用
     *
     * @param ocVisiteLogIds ocVisiteLogIds
     */
    void enableOcVisiteLog(String[] ocVisiteLogIds);

    /**
    * 禁用
    *
    * @param ocVisiteLogIds ocVisiteLogIds
    */
    void disableOcVisiteLog(String[] ocVisiteLogIds);


    /**
    * 通过Id获取信息
    * @param ocVisiteLogId
    * @return
    */
    OcVisiteLog findOcVisiteLogById(Long ocVisiteLogId);
}
