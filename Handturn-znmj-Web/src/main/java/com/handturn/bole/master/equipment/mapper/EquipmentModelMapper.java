package com.handturn.bole.master.equipment.mapper;

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.master.equipment.entity.EquipmentModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

/**
 * 基础资料-设备型号管理 Mapper
 *
 * @author Eric
 * @date 2020-09-11 08:20:08
 */
public interface EquipmentModelMapper extends BaseMapper<EquipmentModel> {

    ICustomPage<EquipmentModel> findForPage(CustomPage page, @Param("equipmentModel") EquipmentModel equipmentModel);

}
