package com.handturn.bole.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.system.entity.SysOganizationResource;

import java.util.Set;

/**
 * 系统-组织资源 Service接口
 *
 * @author MrBird
 * @date 2019-12-12 19:27:53
 */
public interface ISysOganizationResourceService extends IService<SysOganizationResource> {
    /**
     * 修改
     *
     * @param sysOganizationResource sysOganizationResource
     */
    SysOganizationResource saveSysOganizationResource(SysOganizationResource sysOganizationResource);

    /**
     * 获取组织菜单
     * @param orgId
     * @return
     */
    Set<Long> findSysResourceIds4Org(Long orgId);

    /**
     * 保存组织资源
     * @param orgId
     * @param rootModuleId
     * @param resourceIds
     */
    void saveOrgResource(Long orgId,Long rootModuleId,String[] resourceIds);

}
