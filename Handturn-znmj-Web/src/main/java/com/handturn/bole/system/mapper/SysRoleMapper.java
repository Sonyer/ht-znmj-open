package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.system.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统-角色 Mapper
 *
 * @author Eric
 * @date 2019-12-01 10:35:21
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 通过用户名查找用户角色
     *
     * @param userCode 用户编号
     * @return 用户角色集合
     */
    List<SysRole> findUserRole(String userCode);


    ICustomPage<SysRole> findForPage(CustomPage page, @Param("sysRole") SysRole sysRole);
}
