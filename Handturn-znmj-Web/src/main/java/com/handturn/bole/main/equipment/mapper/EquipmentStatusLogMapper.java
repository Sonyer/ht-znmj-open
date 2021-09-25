package com.handturn.bole.main.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.equipment.entity.EquipmentStatusLog;
import org.springframework.data.repository.query.Param;

/**
 * 基础资料-设备状态日志 Mapper
 *
 * @author Eric
 * @date 2020-09-11 08:20:34
 */
public interface EquipmentStatusLogMapper extends BaseMapper<EquipmentStatusLog> {

    ICustomPage<EquipmentStatusLog> findForPage(CustomPage page, @Param("equipmentStatusLog") EquipmentStatusLog equipmentStatusLog);

}
