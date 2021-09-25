package com.handturn.bole.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.CommonTreeSelect;
import com.handturn.bole.system.entity.SysOrganizationDep;

import java.util.List;

/**
 * 系统-公司部门 Service接口
 *
 * @author Eric
 * @date 2019-12-01 10:22:59
 */
public interface ISysOrganizationDepService extends IService<SysOrganizationDep> {
    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    List<CommonTreeSelect<SysOrganizationDep>> findSysOrganizationDeps();

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    CommonTree<SysOrganizationDep> findSysOrganizationDeps(SysOrganizationDep sysOrganizationDep);

    /**
     * 按照条件查询table展示
     * @param sysOrganizationDep
     * @return
     */
    List<SysOrganizationDep> findSysOrganizationDep4Table(SysOrganizationDep sysOrganizationDep);

    /**
     * 编辑
     *
     * @param sysOrganizationDep sysOrganizationDep
     */
    SysOrganizationDep saveSysOrganizationDep(SysOrganizationDep sysOrganizationDep,boolean isSystem);

    /**
     * 删除
     *
     * @param sysOrganizationDepIds resourceIds
     */
    void deleteSysOrganizationDep(String sysOrganizationDepIds);

    /**
     * @param sysOrganizationDepId
     * @return
     */
    SysOrganizationDep findById(Long sysOrganizationDepId);
}
