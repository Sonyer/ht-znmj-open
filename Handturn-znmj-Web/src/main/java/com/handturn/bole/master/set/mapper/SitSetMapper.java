package com.handturn.bole.master.set.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.master.set.entity.SitSet;
import org.springframework.data.repository.query.Param;

/**
 * 智能门禁-站点配置 Mapper
 *
 * @author Eric
 * @date 2020-02-28 09:41:18
 */
public interface SitSetMapper extends BaseMapper<SitSet> {

    ICustomPage<SitSet> findForPage(CustomPage page, @Param("sitSet") SitSet sitSet);

}
