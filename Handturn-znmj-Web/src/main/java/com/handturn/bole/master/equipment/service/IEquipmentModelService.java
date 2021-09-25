package com.handturn.bole.master.equipment.service;

import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.master.equipment.entity.EquipmentModel;

import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 基础资料-设备型号管理 Service接口
 *
 * @author Eric
 * @date 2020-09-11 08:20:08
 */
public interface IEquipmentModelService extends IService<EquipmentModel> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param equipmentModel equipmentModel
     * @return ICustomPage<EquipmentModel>
     */
    ICustomPage<EquipmentModel> findEquipmentModels(QueryRequest request, EquipmentModel equipmentModel);

    /**
     * 修改
     *
     * @param equipmentModel equipmentModel
     */
    EquipmentModel saveEquipmentModel(EquipmentModel equipmentModel);

    /**
     * 启用
     *
     * @param equipmentModelIds equipmentModelIds
     */
    void enableEquipmentModel(String[] equipmentModelIds);

    /**
    * 禁用
    *
    * @param equipmentModelIds equipmentModelIds
    */
    void disableEquipmentModel(String[] equipmentModelIds);


    /**
    * 通过Id获取信息
    * @param equipmentModelId
    * @return
    */
    EquipmentModel findEquipmentModelById(Long equipmentModelId);

    /**
     * @param statuses
     * @return
     */
    List<OptionVo> findOptionVo(Set<String> statuses);
}
