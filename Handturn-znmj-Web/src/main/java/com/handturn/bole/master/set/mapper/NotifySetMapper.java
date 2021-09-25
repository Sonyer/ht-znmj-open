package com.handturn.bole.master.set.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.master.set.entity.NotifySet;
import org.springframework.data.repository.query.Param;

/**
 * 智能门禁-通知设置 Mapper
 *
 * @author Eric
 * @date 2020-06-09 18:43:12
 */
public interface NotifySetMapper extends BaseMapper<NotifySet> {

    ICustomPage<NotifySet> findForPage(CustomPage page, @Param("notifySet") NotifySet notifySet);

}
