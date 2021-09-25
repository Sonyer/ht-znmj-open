package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.system.entity.SysOganizationResource;
import org.springframework.data.repository.query.Param;

/**
 * 系统-组织资源 Mapper
 *
 * @author MrBird
 * @date 2019-12-12 19:27:53
 */
public interface SysOganizationResourceMapper extends BaseMapper<SysOganizationResource> {

    ICustomPage<SysOganizationResource> findForPage(CustomPage page, @Param("sysOganizationResource") SysOganizationResource sysOganizationResource);

}
