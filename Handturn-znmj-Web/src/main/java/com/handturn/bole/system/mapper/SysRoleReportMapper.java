package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.system.entity.SysRoleReport;
import org.springframework.data.repository.query.Param;

/**
 * 系统-角色报表 Mapper
 *
 * @author Eric
 * @date 2020-02-16 20:13:20
 */
public interface SysRoleReportMapper extends BaseMapper<SysRoleReport> {

    ICustomPage<SysRoleReport> findForPage(CustomPage page, @Param("sysRoleReport") SysRoleReport sysRoleReport);

}
