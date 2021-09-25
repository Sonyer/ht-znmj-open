package com.ht.znmj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.VisitorLog;

/**
 * 人员信息 Service接口
 *
 * @author Eric
 * @date 2020-01-29 14:34:06
 */
public interface IVisitorLogService extends IService<VisitorLog> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param visitorLog visitorLog
     * @return ICustomPage<BasRoute>
     */
    ICustomPage<VisitorLog> findVisitorLogs(QueryRequest request, VisitorLog visitorLog);

    /**
     * 通过ID查询
     */
    VisitorLog findVisitorLogById(String id);

    /**
     * 保存设备信息
     * @param visitorLog
     */
    VisitorLog saveVisitorLog(VisitorLog visitorLog);
}
