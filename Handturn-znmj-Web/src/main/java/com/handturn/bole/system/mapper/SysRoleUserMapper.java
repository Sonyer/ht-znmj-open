package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.system.entity.SysRoleUser;
import org.springframework.data.repository.query.Param;

/**
 * 系统-角色用户 Mapper
 *
 * @author MrBird
 * @date 2019-12-13 15:14:41
 */
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

    ICustomPage<SysRoleUser> findForPage(CustomPage page, @Param("sysRoleUser") SysRoleUser sysRoleUser);

}
