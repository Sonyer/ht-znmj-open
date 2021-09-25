package com.handturn.bole.sysconf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.sysconf.entity.SysconfGlobalGroup;
import org.springframework.data.repository.query.Param;

/**
 * 系统-系统配置 Mapper
 *
 * @author Eric
 * @date 2019-12-18 20:25:27
 */
public interface SysconfGlobalGroupMapper extends BaseMapper<SysconfGlobalGroup> {

    ICustomPage<SysconfGlobalGroup> findForPage(CustomPage page, @Param("sysconfGlobalGroup") SysconfGlobalGroup sysconfGlobalGroup);

}
