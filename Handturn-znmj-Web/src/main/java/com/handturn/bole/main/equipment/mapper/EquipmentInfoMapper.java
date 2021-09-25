package com.handturn.bole.main.equipment.mapper;

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.equipment.entity.EquipmentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

/**
 * 基础资料-设备管理 Mapper
 *
 * @author Eric
 * @date 2020-09-11 08:20:12
 */
public interface EquipmentInfoMapper extends BaseMapper<EquipmentInfo> {

    ICustomPage<EquipmentInfo> findForPage(CustomPage page, @Param("equipmentInfo") EquipmentInfo equipmentInfo);

}
