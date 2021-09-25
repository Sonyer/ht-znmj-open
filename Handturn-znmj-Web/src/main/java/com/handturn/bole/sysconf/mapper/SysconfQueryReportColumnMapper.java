package com.handturn.bole.sysconf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.sysconf.entity.SysconfQueryReportColumn;
import org.springframework.data.repository.query.Param;

/**
 * 系统-报表查询列 Mapper
 *
 * @author Eric
 * @date 2020-02-12 09:14:32
 */
public interface SysconfQueryReportColumnMapper extends BaseMapper<SysconfQueryReportColumn> {

    ICustomPage<SysconfQueryReportColumn> findForPage(CustomPage page, @Param("sysconfQueryReportColumn") SysconfQueryReportColumn sysconfQueryReportColumn);

}
