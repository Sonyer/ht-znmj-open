package com.ht.znmj.view.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.VisitorInfo;
import com.ht.znmj.service.IVisitorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 页面调用接口
 */
@Component
public class ManagerVisitorUtil {
    @Autowired
    private IVisitorInfoService visitorInfoService;

    private static ManagerVisitorUtil visitorUtil;

    @PostConstruct
    public void init(){
        visitorUtil = this;
        visitorUtil.visitorInfoService = this.visitorInfoService;
    }

    public static IPage<VisitorInfo> findVisitorInfos(QueryRequest request, VisitorInfo visitorInfo){
        return visitorUtil.visitorInfoService.findVisitorInfos(request,visitorInfo);
    }

    public static VisitorInfo findVisitorInfoById(String id){
        return visitorUtil.visitorInfoService.findVisitorInfoById(id);
    }

    public static VisitorInfo findVisitorInfoByUK(String name,String phoneNumber,String id){
        return visitorUtil.visitorInfoService.findVisitorInfoByUK(name,phoneNumber,id);
    }

    public static VisitorInfo saveVisitorInfo(VisitorInfo visitorInfo){
        return visitorUtil.visitorInfoService.saveVisitorInfo(visitorInfo);
    }

    public static void deleteVisitor(Set<String> visitorIds){
        visitorUtil.visitorInfoService.deleteVisitors(visitorIds);
    }

    public static void deleteAllVisitor(){
        visitorUtil.visitorInfoService.deleteAllVisitors();
    }
}
