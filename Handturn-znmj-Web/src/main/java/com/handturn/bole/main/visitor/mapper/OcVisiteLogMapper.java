package com.handturn.bole.main.visitor.mapper;

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.visitor.entity.OcVisiteLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

/**
 * 网点-会员访客日志 Mapper
 *
 * @author Eric
 * @date 2020-12-06 17:24:27
 */
public interface OcVisiteLogMapper extends BaseMapper<OcVisiteLog> {

    ICustomPage<OcVisiteLog> findForPage(CustomPage page, @Param("ocVisiteLog") OcVisiteLog ocVisiteLog);

}
