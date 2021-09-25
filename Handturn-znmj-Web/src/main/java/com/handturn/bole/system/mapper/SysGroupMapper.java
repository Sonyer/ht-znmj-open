package com.handturn.bole.system.mapper;

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.system.entity.SysGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

/**
 * 系统-资源组 Mapper
 *
 * @author Eric
 * @date 2020-05-23 14:55:18
 */
public interface SysGroupMapper extends BaseMapper<SysGroup> {

    ICustomPage<SysGroup> findForPage(CustomPage page, @Param("sysGroup") SysGroup sysGroup);

}
