package com.ht.znmj.view.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.VisitorEquipment;
import com.ht.znmj.entity.VisitorInfo;
import com.ht.znmj.service.IVisitorEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 页面调用接口
 */
@Component
public class ManagerVisitorEquipmentUtil {
    @Autowired
    private IVisitorEquipmentService visitorEquipmentService;

    private static ManagerVisitorEquipmentUtil visitorUtil;

    @PostConstruct
    public void init(){
        visitorUtil = this;
        visitorUtil.visitorEquipmentService = this.visitorEquipmentService;
    }

    public static IPage<VisitorEquipment> findVisitorInfos(QueryRequest request, VisitorEquipment visitorEquipment){
        return visitorUtil.visitorEquipmentService.findVisitorEquipments(request,visitorEquipment);
    }

    public static VisitorEquipment findVisitorEquipmentById(String id){
        return visitorUtil.visitorEquipmentService.findVisitorEquipmentById(id);
    }

    public static VisitorEquipment findVisitorEquipmentByUK(String visitorId,String equipmentId){
        return visitorUtil.visitorEquipmentService.findVisitorEquipmentByUK(visitorId,equipmentId);
    }

    public static VisitorEquipment saveVisitorEquipment(String visitorId,String equipmentId){
        return visitorUtil.visitorEquipmentService.saveVisitorEquipment(visitorId,equipmentId);
    }

    public static void deleteVisitorEquipments(Set<String> visitorIds, String equipmentId){
        visitorUtil.visitorEquipmentService.deleteVisitorEquipments(visitorIds,equipmentId);
    }

    public static void authAllVisitorEquipments(Set<String> equipmentIds){
        visitorUtil.visitorEquipmentService.authAllVisitorEquipments(equipmentIds);
    }

    public static void authQueryVisitorEquipments(VisitorInfo queryVisitorInfo, Set<String> equipmentIds){
        visitorUtil.visitorEquipmentService.authQueryVisitorEquipments(queryVisitorInfo,equipmentIds);
    }

    public static void authVisitorIdEquipments(Set<String> visitorIds, Set<String> equipmentIds){
        visitorUtil.visitorEquipmentService.authVisitorIdEquipments(visitorIds,equipmentIds);
    }

    public static void authCancelAllVisitorEquipments(Set<String> equipmentIds){
        visitorUtil.visitorEquipmentService.authCancelAllVisitorEquipments(equipmentIds);
    }

    public static void authCancelQueryVisitorEquipments(VisitorInfo queryVisitorInfo, Set<String> equipmentIds){
        visitorUtil.visitorEquipmentService.authCancelQueryVisitorEquipments(queryVisitorInfo,equipmentIds);
    }

    public static void authCancelVisitorIdEquipments(Set<String> visitorIds, Set<String> equipmentIds){
        visitorUtil.visitorEquipmentService.authCancelVisitorIdEquipments(visitorIds,equipmentIds);
    }
}
