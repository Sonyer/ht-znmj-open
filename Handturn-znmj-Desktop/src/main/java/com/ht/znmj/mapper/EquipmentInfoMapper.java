package com.ht.znmj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ht.znmj.common.customPage.CustomPage;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.entity.EquipmentInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 设备信息 Mapper
 *
 * @author Eric
 * @date 2019-12-17 08:35:14
 */
public interface EquipmentInfoMapper extends BaseMapper<EquipmentInfo> {

    ICustomPage<EquipmentInfo> findForPage(CustomPage page, @Param("equipmentInfo") EquipmentInfo equipmentInfo);


}
