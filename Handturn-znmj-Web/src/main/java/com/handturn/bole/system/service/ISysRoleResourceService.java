package com.handturn.bole.system.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysRoleResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 系统-角色资源 Service接口
 *
 * @author Eric
 * @date 2019-12-01 10:39:10
 */
public interface ISysRoleResourceService extends IService<SysRoleResource> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysRoleResource sysRoleResource
     * @return ICustomPage<SysRoleResource>
     */
    ICustomPage<SysRoleResource> findSysRoleResources(QueryRequest request, SysRoleResource sysRoleResource);

    /**
     * 查询（所有）
     *
     * @param sysRoleResource sysRoleResource
     * @return List<SysRoleResource>
     */
    List<SysRoleResource> findSysRoleResources(SysRoleResource sysRoleResource);

    /**
     * 新增
     *
     * @param sysRoleResource sysRoleResource
     */
    void createSysRoleResource(SysRoleResource sysRoleResource);

    /**
     * 修改
     *
     * @param sysRoleResource sysRoleResource
     */
    void updateSysRoleResource(SysRoleResource sysRoleResource);

    /**
     * 删除
     *
     * @param sysRoleResource sysRoleResource
     */
    void deleteSysRoleResource(SysRoleResource sysRoleResource);

    /**
     * 获取组织菜单
     * @param roleId
     * @return
     */
    Set<Long> findSysResourceIds4Role(Long roleId);

    /**
     * 保存组织资源
     * @param roleId
     * @param rootModuleId
     * @param resourceIds
     */
    void saveRoleResource(Long roleId,Long rootModuleId,String[] resourceIds);

    /**
     * 通过角色ID删除
     * @param roleId
     */
    void deleteRoleResourceByRole(Long roleId);
}
