package com.handturn.bole.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.system.entity.SysOcResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 系统-网点资源 Service接口
 *
 * @author MrBird
 * @date 2019-12-13 10:00:36
 */
public interface ISysOcResourceService extends IService<SysOcResource> {

    /**
     * 修改
     *
     * @param sysOcResource sysOcResource
     */
    SysOcResource saveSysOcResource(SysOcResource sysOcResource);

    /**
     * 获取组织菜单
     * @param ocId
     * @return
     */
    Set<Long> findSysResourceIds4OrgOc(Long ocId);

    /**
     * 保存组织资源
     * @param ocId
     * @param rootModuleId
     * @param resourceIds
     */
    void saveOrgOcResource(Long ocId,Long rootModuleId,String[] resourceIds);

    /**
     * 通过OC 删除
     * @param ocId
     */
    void deleteOcResourceByOc(Long ocId);

}
