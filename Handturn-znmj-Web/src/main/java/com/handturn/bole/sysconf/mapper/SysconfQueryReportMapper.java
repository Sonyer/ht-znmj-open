package com.handturn.bole.sysconf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.system.entity.SysResource;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 系统-报表 Mapper
 *
 * @author Eric
 * @date 2020-02-12 09:06:44
 */
public interface SysconfQueryReportMapper extends BaseMapper<SysconfQueryReport> {

    ICustomPage<SysconfQueryReport> findForPage(CustomPage page, @Param("sysconfQueryReport") SysconfQueryReport sysconfQueryReport);

    List<SysconfQueryReport> findAllRootReports();

    List<SysconfQueryReport> findAllRootReportsByOrgId(String orgId);

    List<SysconfQueryReport> findAllRootReportsByOcId(String ocId);

    List<SysconfQueryReport> findReportsByRootIdOrgId(String rootReportId, String orgId);

    List<SysconfQueryReport> findReportsByRootIdOcId(String rootReportId,String ocId);

    List<SysconfQueryReport> findUserReport(String rootReportId,String userCode);
}
