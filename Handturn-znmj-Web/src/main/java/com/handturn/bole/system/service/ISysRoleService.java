package com.handturn.bole.system.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysRole;

import java.util.List;

/**
 * 系统-角色 Service接口
 *
 * @author Eric
 * @date 2019-12-01 10:35:21
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 通过用户名查找用户角色
     *
     * @param userCode 用户编号
     * @return 用户角色集合
     */
    List<SysRole> findUserRole(String userCode);

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysRole sysRole
     * @return ICustomPage<SysRole>
     */
    ICustomPage<SysRole> findSysRoles(QueryRequest request, SysRole sysRole);

    /**
     * 修改
     *
     * @param sysRole sysRole
     */
    SysRole saveSysRole(SysRole sysRole,boolean isSystem);



    /**
     * 启用
     *
     * @param sysRoleIds sysRoleIds
     */
    void enableSysRole(String[] sysRoleIds);

    /**
     * 禁用
     *
     * @param sysRoleIds sysRoleIds
     */
    void disableSysRole(String[] sysRoleIds);

    /**
     * 通过Id获取信息
     * @param sysRoleId
     * @return
     */
    SysRole findSysRoleById(Long sysRoleId);
}
