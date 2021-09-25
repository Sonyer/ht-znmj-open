package com.handturn.bole.main.authArea.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.main.authArea.entity.OcAuthAreaEquipment;

import java.util.List;
import java.util.Set;

/**
 * 网点-设备区域权限 Service接口
 *
 * @author Eric
 * @date 2020-09-22 08:58:18
 */
public interface IOcAuthAreaEquipmentService extends IService<OcAuthAreaEquipment> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param ocAuthAreaEquipment ocAuthAreaEquipment
     * @return ICustomPage<OcAuthAreaEquipment>
     */
    ICustomPage<OcAuthAreaEquipment> findOcAuthAreaEquipments(QueryRequest request, OcAuthAreaEquipment ocAuthAreaEquipment);

    /**
     * 修改
     *
     * @param ocAuthAreaEquipment ocAuthAreaEquipment
     */
    OcAuthAreaEquipment saveOcAuthAreaEquipment(OcAuthAreaEquipment ocAuthAreaEquipment);

    /**
     * 启用
     *
     * @param ocAuthAreaEquipmentIds ocAuthAreaEquipmentIds
     */
    void enableOcAuthAreaEquipment(String[] ocAuthAreaEquipmentIds);

    /**
    * 禁用
    *
    * @param ocAuthAreaEquipmentIds ocAuthAreaEquipmentIds
    */
    void disableOcAuthAreaEquipment(String[] ocAuthAreaEquipmentIds);


    /**
    * 通过Id获取信息
    * @param ocAuthAreaEquipmentId
    * @return
    */
    OcAuthAreaEquipment findOcAuthAreaEquipmentById(Long ocAuthAreaEquipmentId);


    List<OcAuthAreaEquipment> findOcAuthAreaEquipmentByEquipmentIds(Set<String> equipmentIds);

    List<OcAuthAreaEquipment> findOcAuthAreaEquipmentByAreaIds(Set<String> authAreaIds);

    /**
     * 授权
     * @param oauthAreaId
     * @param ocEquipmentIdsArr
     */
    void authOcEquipments(String oauthAreaId,String[] ocEquipmentIdsArr,String orgCode,String ocCode,String orgName,String ocName);

    /**
     * 取消授权
     * @param oauthAreaId
     * @param ocEquipmentIdsArr
     */
    void cancelAuthOcEquipments(String oauthAreaId,String[] ocEquipmentIdsArr,String orgCode,String ocCode,String orgName,String ocName);
}
