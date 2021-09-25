package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.system.entity.SysRoleOc;
import org.springframework.data.repository.query.Param;

/**
 * 系统-角色网点 Mapper
 *
 * @author MrBird
 * @date 2019-12-13 15:07:51
 */
public interface SysRoleOcMapper extends BaseMapper<SysRoleOc> {

    ICustomPage<SysRoleOc> findForPage(CustomPage page, @Param("sysRoleOc") SysRoleOc sysRoleOc);

}
