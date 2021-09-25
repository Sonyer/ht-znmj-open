package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.system.entity.SysOcResource;
import org.springframework.data.repository.query.Param;

/**
 * 系统-网点资源 Mapper
 *
 * @author MrBird
 * @date 2019-12-13 10:00:36
 */
public interface SysOcResourceMapper extends BaseMapper<SysOcResource> {

    ICustomPage<SysOcResource> findForPage(CustomPage page, @Param("sysOcResource") SysOcResource sysOcResource);

}
