package com.handturn.bole.sysconf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.sysconf.entity.SysconfGlobalParam;
import org.springframework.data.repository.query.Param;

/**
 * 系统-系统配置参数 Mapper
 *
 * @author Eric
 * @date 2019-12-18 20:31:06
 */
public interface SysconfGlobalParamMapper extends BaseMapper<SysconfGlobalParam> {

    ICustomPage<SysconfGlobalParam> findForPage(CustomPage page, @Param("sysconfGlobalParam") SysconfGlobalParam sysconfGlobalParam);

}
