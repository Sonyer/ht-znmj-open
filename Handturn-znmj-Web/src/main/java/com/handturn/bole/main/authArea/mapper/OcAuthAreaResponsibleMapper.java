package com.handturn.bole.main.authArea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.authArea.entity.OcAuthAreaResponsible;
import org.springframework.data.repository.query.Param;

/**
 * 网点-访客区域责任人 Mapper
 *
 * @author Eric
 * @date 2020-09-30 08:32:35
 */
public interface OcAuthAreaResponsibleMapper extends BaseMapper<OcAuthAreaResponsible> {

    ICustomPage<OcAuthAreaResponsible> findForPage(CustomPage page, @Param("ocAuthAreaResponsible") OcAuthAreaResponsible ocAuthAreaResponsible);

}
