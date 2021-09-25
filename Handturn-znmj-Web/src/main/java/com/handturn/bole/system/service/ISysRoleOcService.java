package com.handturn.bole.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.system.entity.SysRoleOc;

import java.util.Set;

/**
 * 系统-角色网点 Service接口
 *
 * @author MrBird
 * @date 2019-12-13 15:07:51
 */
public interface ISysRoleOcService extends IService<SysRoleOc> {

    /**
     * 修改
     *
     * @param sysRoleOc sysRoleOc
     */
    SysRoleOc saveSysRoleOc(SysRoleOc sysRoleOc);

    /**
     * 通过角色获取网点权限
     * @param roleId
     * @return
     */
    Set<Long> findOcIds4Role(Long roleId);


    void saveRoleOc(Long roleId,String[] ocIds);

}
