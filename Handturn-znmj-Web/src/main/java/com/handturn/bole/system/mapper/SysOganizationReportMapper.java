package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.system.entity.SysOganizationReport;
import org.springframework.data.repository.query.Param;

/**
 * 系统-组织报表 Mapper
 *
 * @author Eric
 * @date 2020-02-16 20:08:44
 */
public interface SysOganizationReportMapper extends BaseMapper<SysOganizationReport> {

    ICustomPage<SysOganizationReport> findForPage(CustomPage page, @Param("sysOganizationReport") SysOganizationReport sysOganizationReport);

}
