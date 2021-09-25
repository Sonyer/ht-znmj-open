package com.handturn.bole.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.system.entity.SysResource;

import java.util.List;
import java.util.Set;

/**
 * 系统-资源 Service接口
 *
 * @author Eric
 * @date 2019-12-01 10:31:46
 */
public interface ISysResourceService extends IService<SysResource> {

    /**
     * 查找用户菜单集合
     *
     * @param userCode 用户名
     * @return 用户菜单集合
     */
    CommonTree<SysResource> findUserSysResources(String userCode,Long rootResourceId);

    /**
     * 查找用户菜单集合
     *
     * @param userId 用户Id
     * @return 用户菜单集合
     */
    List<SysResource> findUserRootSysResources(Long userId);

    /**
     * 查找所有根节点
     *
     * @return 用户菜单集合
     */
    List<SysResource> findAllRootSysResources();

    /**
     * 查找所有根节点
     *
     * @return 用户菜单集合
     */
    List<SysResource> findAllRootSysResourcesByOrgId(Long orgId);


    /**
     * 查找所有根节点
     *
     * @return 用户菜单集合
     */
    List<SysResource> findAllRootSysResourcesByOcId(Long ocId);

    /**
     * 查找用户权限集
     *
     * @param userCode 用户编号
     * @return 用户权限集合
     */
    List<SysResource> findUserPermissions(String userCode);

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    CommonTree<SysResource> findSysResources(SysResource sysResource);

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    CommonTree<SysResource> findSysResourcesByRootId(String rootResourceId, Set<Long> permResourceIds);

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    CommonTree<SysResource> findSysResourcesByRootIdOrgId(String rootResourceId,String orgId, Set<Long> permResourceIds);

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    CommonTree<SysResource> findSysResourcesByRootIdOcId(String rootResourceId,String ocId, Set<Long> permResourceIds);

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    List<SysResource> findSysResourcesByRootId(String rootResourceId);

    /**
     * 按照条件查询table展示
     * @param sysResource
     * @return
     */
    List<SysResource> findSysResources4Table(SysResource sysResource);

    /**
     * 编辑
     *
     * @param sysResource sysResource
     */
    void saveSysResource(SysResource sysResource);

    /**
     * 删除
     *
     * @param resourceIds resourceIds
     */
    void deleteSysResource(String resourceIds);

    /**
     * 通过Id获取菜单信息
     * @param resourceId
     * @return
     */
    SysResource findById(Long resourceId);

}
