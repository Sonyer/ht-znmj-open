package com.handturn.bole.main.equipment.service;

import com.handturn.bole.main.equipment.entity.EquipmentInfo;

import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 基础资料-设备管理 Service接口
 *
 * @author Eric
 * @date 2020-09-11 08:20:12
 */
public interface IEquipmentInfoService extends IService<EquipmentInfo> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param equipmentInfo equipmentInfo
     * @return ICustomPage<EquipmentInfo>
     */
    ICustomPage<EquipmentInfo> findEquipmentInfos(QueryRequest request, EquipmentInfo equipmentInfo);

    /**
     * 修改
     *
     * @param equipmentInfo equipmentInfo
     */
    EquipmentInfo saveEquipmentInfo(EquipmentInfo equipmentInfo);

    /**
     * 安装
     *
     * @param equipmentInfo equipmentInfo
     */
    EquipmentInfo erect(EquipmentInfo equipmentInfo);

    /**
     * 拆卸
     *
     * @param equipmentInfoIds equipmentInfoIds
     */
    void down(String[] equipmentInfoIds);

    /**
     * 远程开门
     *
     * @param equipmentInfoIds equipmentInfoIds
     */
    void remoteOpen(String[] equipmentInfoIds);


    /**
    * 通过Id获取信息
    * @param equipmentInfoId
    * @return
    */
    EquipmentInfo findEquipmentInfoById(Long equipmentInfoId);

    /**
     * 通过mac获取信息
     * @param mac
     * @return
     */
    EquipmentInfo findEquipmentInfoByMac(String mac,String ocCode);

    /**
     * 通过mac获取信息
     * @param macs
     * @return
     */
    List<EquipmentInfo> findEquipmentInfoByMacs(Set<String> macs, String ocCode);


    String findEquipmentAuthCodeByEquipmentIds(String[] equipmentInfoIdArr);
}
