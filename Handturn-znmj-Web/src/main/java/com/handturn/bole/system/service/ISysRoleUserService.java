package com.handturn.bole.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.system.entity.SysRoleUser;

import java.util.Set;

/**
 * 系统-角色用户 Service接口
 *
 * @author MrBird
 * @date 2019-12-13 15:14:41
 */
public interface ISysRoleUserService extends IService<SysRoleUser> {
    /**
     * 修改
     *
     * @param sysRoleUser sysRoleUser
     */
    SysRoleUser saveSysRoleUser(SysRoleUser sysRoleUser);

    /**
     * 通过角色获取网点权限
     * @param roleId
     * @return
     */
    Set<Long> findUserIds4Role(Long roleId);

    void saveRoleUser(Long roleId,String[] userIds);

}
