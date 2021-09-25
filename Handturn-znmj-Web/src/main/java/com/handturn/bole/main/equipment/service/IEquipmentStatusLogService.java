package com.handturn.bole.main.equipment.service;

import com.handturn.bole.main.equipment.entity.EquipmentStatusLog;

import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基础资料-设备状态日志 Service接口
 *
 * @author Eric
 * @date 2020-09-11 08:20:34
 */
public interface IEquipmentStatusLogService extends IService<EquipmentStatusLog> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param equipmentStatusLog equipmentStatusLog
     * @return ICustomPage<EquipmentStatusLog>
     */
    ICustomPage<EquipmentStatusLog> findEquipmentStatusLogs(QueryRequest request, EquipmentStatusLog equipmentStatusLog);

    /**
     * 修改
     *
     * @param equipmentStatusLog equipmentStatusLog
     */
    EquipmentStatusLog saveEquipmentStatusLog(EquipmentStatusLog equipmentStatusLog);

    /**
     * 启用
     *
     * @param equipmentStatusLogIds equipmentStatusLogIds
     */
    void enableEquipmentStatusLog(String[] equipmentStatusLogIds);

    /**
    * 禁用
    *
    * @param equipmentStatusLogIds equipmentStatusLogIds
    */
    void disableEquipmentStatusLog(String[] equipmentStatusLogIds);


    /**
    * 通过Id获取信息
    * @param equipmentStatusLogId
    * @return
    */
    EquipmentStatusLog findEquipmentStatusLogById(Long equipmentStatusLogId);
}
