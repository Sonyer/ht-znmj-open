package com.handturn.bole.system.mapper;

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.system.entity.SysOcReport;
import org.springframework.data.repository.query.Param;

/**
 * 系统-网点报表 Mapper
 *
 * @author Eric
 * @date 2020-02-16 20:11:11
 */
public interface SysOcReportMapper extends BaseMapper<SysOcReport> {

    ICustomPage<SysOcReport> findForPage(CustomPage page, @Param("sysOcReport") SysOcReport sysOcReport);

}
