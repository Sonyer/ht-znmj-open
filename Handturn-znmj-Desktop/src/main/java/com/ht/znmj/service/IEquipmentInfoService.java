package com.ht.znmj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.EquipmentInfo;

import java.util.List;
import java.util.Map;

/**
 * 设备信息 Service接口
 *
 * @author Eric
 * @date 2020-01-29 14:34:06
 */
public interface IEquipmentInfoService extends IService<EquipmentInfo> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param equipmentInfo equipmentInfo
     * @return ICustomPage<BasRoute>
     */
    ICustomPage<EquipmentInfo> findEquipmentInfos(QueryRequest request, EquipmentInfo equipmentInfo);

    /**
     * 通过状态查询
     */
    List<EquipmentInfo> findEquipmentInfosByStatus(String status);

    /**
     * 通过连接状态查询
     */
    List<EquipmentInfo> findEquipmentInfosByConnStatus(String connStatus);

    /**
     * 查询所有
     */
    List<EquipmentInfo> findAllEquipmentInfos();

    /**
     * 查询Map
     */
    Map<String,EquipmentInfo> findMapEquipmentInfos();

    /**
     * 通过ID查询
     */
    EquipmentInfo findEquipmentInfoById(String id);

    /**
     * 通过SN获取设备
     */
    EquipmentInfo findEquipmentInfoBySn(String sn);

    /**
     * 保存设备信息
     * @param equipmentInfo
     */
    EquipmentInfo saveEquipmentInfo(EquipmentInfo equipmentInfo);

    /**
     * 修改状态
     */
    EquipmentInfo updateConnStatus(String sn,String connStatus);

    /**
     * 修改日志最大
     */
    void updateMaxLogSeq(String sn,Integer maxLogSeq);

    /**
     * 注册
     */
    String registerEquipmentInfo(String license);

}
