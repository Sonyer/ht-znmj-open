package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.system.entity.SysResource;

import java.util.List;

/**
 * 系统-资源 Mapper
 *
 * @author Eric
 * @date 2019-12-01 10:31:46
 */
public interface SysResourceMapper extends BaseMapper<SysResource> {

    /**
     * 查找用户权限集
     *
     * @param userCode 用户编号
     * @return 用户权限集合
     */
    List<SysResource> findUserPermissions(String userCode);


    /**
     * 查找用户菜单集合
     *
     * @param userCode 用户编号
     * @return 用户菜单集合
     */
    List<SysResource> findUserSysResources(String userCode,String rootResourceId);

    /**
     * 查找用户菜单集合
     *
     * @param userId 用户编号
     * @return 用户菜单集合
     */
    List<SysResource> findUserRootSysResources(String userId);

    /**
     * 查找所有菜单集合
     *
     * @return 用户菜单集合
     */
    List<SysResource> findAllRootSysResources();

    /**
     * 查找所有菜单集合
     *
     * @return 用户菜单集合
     */
    List<SysResource> findAllRootSysResourcesByOrgId(String orgId);

    /**
     * 查找所有菜单集合
     *
     * @return 用户菜单集合
     */
    List<SysResource> findAllRootSysResourcesByOcId(String ocId);

    /**
     * 查找所有菜单集合
     *
     * @return 用户菜单集合
     */
    List<SysResource> findSysResourcesByRootIdOrgId(String rootResourceId,String orgId);

    /**
     * 查找所有菜单集合
     *
     * @return 用户菜单集合
     */
    List<SysResource> findSysResourcesByRootIdOcId(String rootResourceId,String ocId);

}
