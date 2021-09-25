package com.ht.znmj.view.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.VisitorLog;
import com.ht.znmj.service.IVisitorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 页面调用接口
 */
@Component
public class ManagerVisitorLogUtil {
    @Autowired
    private IVisitorLogService visitorLogService;

    private static ManagerVisitorLogUtil visitorLogUtil;

    @PostConstruct
    public void init(){
        visitorLogUtil = this;
        visitorLogUtil.visitorLogService = this.visitorLogService;
    }

    public static IPage<VisitorLog> findVisitorLogs(QueryRequest request, VisitorLog visitorLog){
        return visitorLogUtil.visitorLogService.findVisitorLogs(request,visitorLog);
    }

    public static VisitorLog findVisitorLogById(String id){
        return visitorLogUtil.visitorLogService.findVisitorLogById(id);
    }

    public static VisitorLog saveVisitorLog(VisitorLog visitorLog){
        return visitorLogUtil.visitorLogService.saveVisitorLog(visitorLog);
    }
}
