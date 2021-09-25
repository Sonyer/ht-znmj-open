package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.system.entity.SysOrganization;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 系统-组织 Mapper
 *
 * @author MrBird
 * @date 2019-12-08 10:00:07
 */
public interface SysOrganizationMapper extends BaseMapper<SysOrganization> {

    ICustomPage<SysOrganization> findForPage(CustomPage page, @Param("sysOrganization") SysOrganization sysOrganization);

    List<SysOrganization> findForAll();

}
