package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.system.entity.SysOganizationResource;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.mapper.SysOganizationResourceMapper;
import com.handturn.bole.system.service.ISysOganizationResourceService;
import com.handturn.bole.system.service.ISysResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 系统-组织资源 Service实现
 *
 * @author MrBird
 * @date 2019-12-12 19:27:53
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysOganizationResourceServiceImpl extends ServiceImpl<SysOganizationResourceMapper, SysOganizationResource> implements ISysOganizationResourceService {

    @Autowired
    ISysResourceService sysResourceService;

    @Override
    @Transactional
    public SysOganizationResource saveSysOganizationResource(SysOganizationResource sysOganizationResource) {
        if(sysOganizationResource.getId() == null){
            this.save(sysOganizationResource);
            return sysOganizationResource;
        }else{
            SysOganizationResource sysOganizationResourceOld = this.baseMapper.selectById(sysOganizationResource.getId());
            CopyUtils.copyProperties(sysOganizationResource,sysOganizationResourceOld);
            this.updateById(sysOganizationResourceOld);
            return sysOganizationResourceOld;
        }
    }

    /**
     * 获取组织菜单
     * @param orgId
     * @return
     */
    @Override
    public Set<Long> findSysResourceIds4Org(Long orgId){
        Set<Long> resourceIdSet = new HashSet<Long>();
        List<SysOganizationResource> orgResources = this.baseMapper.selectList(new QueryWrapper<SysOganizationResource>().lambda().eq(SysOganizationResource::getOrgId, orgId));
        for(SysOganizationResource orgResource : orgResources){
            resourceIdSet.add(orgResource.getResourceId());
        }
        return resourceIdSet;
    }

    /**
     * 保存组织资源
     * @param orgId
     * @param rootModuleId
     * @param resourceIds
     */
    @Override
    @Transactional
    public void saveOrgResource(Long orgId,Long rootModuleId,String[] resourceIds){
        List<SysResource> sysResources = sysResourceService.findSysResourcesByRootId(String.valueOf(rootModuleId));
        //删除模块资源
        List<Long> ids = new ArrayList<Long>();
        sysResources.forEach(sysResource -> {
            ids.add(sysResource.getId());
        });
        this.baseMapper.delete(new QueryWrapper<SysOganizationResource>().lambda()
                .in(SysOganizationResource::getResourceId, ids)
                .eq(SysOganizationResource::getOrgId,orgId));

        //保存模块资源
        for(String resourceId : resourceIds){
            if(StringUtils.isEmpty(resourceId)){
                continue;
            }
            SysOganizationResource sysOganizationResource = new SysOganizationResource();
            sysOganizationResource.setOrgId(orgId);
            sysOganizationResource.setResourceId(Long.valueOf(resourceId));
            this.save(sysOganizationResource);
        }
    }
}
