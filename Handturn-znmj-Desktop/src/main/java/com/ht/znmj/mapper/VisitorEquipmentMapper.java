package com.ht.znmj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ht.znmj.common.customPage.CustomPage;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.entity.VisitorEquipment;
import org.apache.ibatis.annotations.Param;

/**
 * 人员设备信息 Mapper
 *
 * @author Eric
 * @date 2019-12-17 08:35:14
 */
public interface VisitorEquipmentMapper extends BaseMapper<VisitorEquipment> {

    ICustomPage<VisitorEquipment> findForPage(CustomPage page, @Param("visitorEquipment") VisitorEquipment visitorEquipment);


}
