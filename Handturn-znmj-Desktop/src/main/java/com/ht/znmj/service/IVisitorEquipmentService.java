package com.ht.znmj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.VisitorEquipment;
import com.ht.znmj.entity.VisitorInfo;

import java.util.Set;

/**
 * 人员设备信息 Service接口
 *
 * @author Eric
 * @date 2020-01-29 14:34:06
 */
public interface IVisitorEquipmentService extends IService<VisitorEquipment> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param visitorEquipment visitorEquipment
     * @return ICustomPage<BasRoute>
     */
    ICustomPage<VisitorEquipment> findVisitorEquipments(QueryRequest request, VisitorEquipment visitorEquipment);

    /**
     * 通过ID查询
     */
    VisitorEquipment findVisitorEquipmentById(String id);

    /**
     * 通过人员 设备ID查询
     */
    VisitorEquipment findVisitorEquipmentByUK(String visitorId,String equipmentId);

    /**
     * 保存信息
     * @param visitorId
     */
    VisitorEquipment saveVisitorEquipment(String visitorId,String equipmentId);

    /**
     * 删除信息
     * @param visitorIds
     */
    void deleteVisitorEquipments(Set<String> visitorIds, String equipmentId);


    /**
     * 授权所有人员
     */
    void authAllVisitorEquipments(Set<String> equipmentIds);

    /**
     * 查询授权
     */
    void authQueryVisitorEquipments(VisitorInfo queryVisitorInfo, Set<String> equipmentIds);

    /**
     * 选择授权
     */
    void authVisitorIdEquipments(Set<String> visitorIds, Set<String> equipmentIds);

    /**
     * 取消授权所有人员
     */
    void authCancelAllVisitorEquipments(Set<String> equipmentIds);

    /**
     * 取消查询授权
     */
    void authCancelQueryVisitorEquipments(VisitorInfo queryVisitorInfo, Set<String> equipmentIds);

    /**
     * 取消选择授权
     */
    void authCancelVisitorIdEquipments(Set<String> visitorIds, Set<String> equipmentIds);

}
