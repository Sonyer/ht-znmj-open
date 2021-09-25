package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.system.entity.SysGroupReport;
import org.springframework.data.repository.query.Param;

/**
 * 系统-组报表 Mapper
 *
 * @author Eric
 * @date 2020-05-23 14:55:39
 */
public interface SysGroupReportMapper extends BaseMapper<SysGroupReport> {

    ICustomPage<SysGroupReport> findForPage(CustomPage page, @Param("sysGroupReport") SysGroupReport sysGroupReport);

}
