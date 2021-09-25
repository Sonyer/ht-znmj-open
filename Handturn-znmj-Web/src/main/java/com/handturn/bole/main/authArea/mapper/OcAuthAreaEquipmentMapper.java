package com.handturn.bole.main.authArea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.authArea.entity.OcAuthAreaEquipment;
import org.springframework.data.repository.query.Param;

/**
 * 网点-设备区域权限 Mapper
 *
 * @author Eric
 * @date 2020-09-22 08:58:18
 */
public interface OcAuthAreaEquipmentMapper extends BaseMapper<OcAuthAreaEquipment> {

    ICustomPage<OcAuthAreaEquipment> findForPage(CustomPage page, @Param("ocAuthAreaEquipment") OcAuthAreaEquipment ocAuthAreaEquipment);

}
