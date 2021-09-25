package com.ht.znmj.view.service;

import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.service.IEquipmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 页面调用接口
 */
@Component
public class HomeEquipmentUtil {
    @Autowired
    private IEquipmentInfoService equipmentService;

    private static HomeEquipmentUtil equipmentUtil;

    @PostConstruct
    public void init(){
        equipmentUtil = this;
        equipmentUtil.equipmentService = this.equipmentService;
    }

    public static List<EquipmentInfo> findEquipmentInfosByStatus(String status){
        return equipmentUtil.equipmentService.findEquipmentInfosByStatus(status);
    }

    public static List<EquipmentInfo> findAllEquipmentInfos(){
        return equipmentUtil.equipmentService.findAllEquipmentInfos();
    }

    public static Map<String,EquipmentInfo> findMapEquipmentInfos(){
        return equipmentUtil.equipmentService.findMapEquipmentInfos();
    }

    public static EquipmentInfo findEquipmentInfoById(String id){
        return equipmentUtil.equipmentService.findEquipmentInfoById(id);
    }

    public static String registerEquipmentInfo(String license){
        return equipmentUtil.equipmentService.registerEquipmentInfo(license);
    }

    public static EquipmentInfo saveEquipmentInfo(EquipmentInfo equipmentInfo){
        return equipmentUtil.equipmentService.saveEquipmentInfo(equipmentInfo);
    }

    public static EquipmentInfo updateConnStatus(String sn,String status){
        return equipmentUtil.equipmentService.updateConnStatus(sn,status);
    }
}
