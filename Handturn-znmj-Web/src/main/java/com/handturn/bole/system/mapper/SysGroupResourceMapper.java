package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.system.entity.SysGroupResource;
import org.springframework.data.repository.query.Param;

/**
 * 系统-组资源 Mapper
 *
 * @author Eric
 * @date 2020-05-23 14:55:50
 */
public interface SysGroupResourceMapper extends BaseMapper<SysGroupResource> {

    ICustomPage<SysGroupResource> findForPage(CustomPage page, @Param("sysGroupResource") SysGroupResource sysGroupResource);

}
