package com.handturn.bole.sysconf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.sysconf.entity.SysconfPrintReport;
import org.springframework.data.repository.query.Param;

/**
 * 系统配置-打印模板 Mapper
 *
 * @author Eric
 * @date 2020-01-31 09:19:11
 */
public interface SysconfPrintReportMapper extends BaseMapper<SysconfPrintReport> {

    ICustomPage<SysconfPrintReport> findForPage(CustomPage page, @Param("sysconfPrintReport") SysconfPrintReport sysconfPrintReport);

}
