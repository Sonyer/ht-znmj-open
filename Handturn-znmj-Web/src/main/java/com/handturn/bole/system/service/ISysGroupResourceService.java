package com.handturn.bole.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysGroupResource;

import java.util.Set;

/**
 * 系统-组资源 Service接口
 *
 * @author Eric
 * @date 2020-05-23 14:55:50
 */
public interface ISysGroupResourceService extends IService<SysGroupResource> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysGroupResource sysGroupResource
     * @return ICustomPage<SysGroupResource>
     */
    ICustomPage<SysGroupResource> findSysGroupResources(QueryRequest request, SysGroupResource sysGroupResource);

    /**
     * 修改
     *
     * @param sysGroupResource sysGroupResource
     */
    SysGroupResource saveSysGroupResource(SysGroupResource sysGroupResource);

    /**
     * 启用
     *
     * @param sysGroupResourceIds sysGroupResourceIds
     */
    void enableSysGroupResource(String[] sysGroupResourceIds);

    /**
    * 禁用
    *
    * @param sysGroupResourceIds sysGroupResourceIds
    */
    void disableSysGroupResource(String[] sysGroupResourceIds);


    /**
    * 通过Id获取信息
    * @param sysGroupResourceId
    * @return
    */
    SysGroupResource findSysGroupResourceById(Long sysGroupResourceId);

    /**
     * 获取分组菜单
     * @param groupId
     * @return
     */
    Set<Long> findSysResourceIds4Group(Long groupId);

    /**
     * 保存分组资源
     * @param groupId
     * @param rootModuleId
     * @param resourceIds
     */
    void saveGroupResource(Long groupId,Long rootModuleId,String[] resourceIds);
}
