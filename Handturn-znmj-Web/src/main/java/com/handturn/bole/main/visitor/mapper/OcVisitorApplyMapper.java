package com.handturn.bole.main.visitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.visitor.entity.OcVisitorApply;
import org.springframework.data.repository.query.Param;

/**
 * 网点-会员访客审核 Mapper
 *
 * @author Eric
 * @date 2020-09-22 08:57:57
 */
public interface OcVisitorApplyMapper extends BaseMapper<OcVisitorApply> {

    ICustomPage<OcVisitorApply> findForPage(CustomPage page, @Param("ocVisitorApply") OcVisitorApply ocVisitorApply);

}
