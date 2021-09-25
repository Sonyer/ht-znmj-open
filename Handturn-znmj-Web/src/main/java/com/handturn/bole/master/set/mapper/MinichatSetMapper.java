package com.handturn.bole.master.set.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.master.set.entity.MinichatSet;
import org.springframework.data.repository.query.Param;

/**
 * 智能门禁-小程序参数配置 Mapper
 *
 * @author Eric
 * @date 2020-02-28 11:05:13
 */
public interface MinichatSetMapper extends BaseMapper<MinichatSet> {

    ICustomPage<MinichatSet> findForPage(CustomPage page, @Param("minichatSet") MinichatSet minichatSet);

}
