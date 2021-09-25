package com.handturn.bole.sysconf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.sysconf.entity.SysconfPrint;
import org.springframework.data.repository.query.Param;

/**
 * 系统配置-打印配置 Mapper
 *
 * @author Eric
 * @date 2020-01-31 09:15:26
 */
public interface SysconfPrintMapper extends BaseMapper<SysconfPrint> {

    ICustomPage<SysconfPrint> findForPage(CustomPage page, @Param("sysconfPrint") SysconfPrint sysconfPrint);

}
