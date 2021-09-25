package com.handturn.bole.main.area.mapper;

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.area.entity.AreaInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

/**
 * 基础资料-区域管理 Mapper
 *
 * @author Eric
 * @date 2020-09-11 08:20:03
 */
public interface AreaInfoMapper extends BaseMapper<AreaInfo> {

    ICustomPage<AreaInfo> findForPage(CustomPage page, @Param("areaInfo") AreaInfo areaInfo);

}
