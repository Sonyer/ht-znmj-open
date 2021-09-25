package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.system.entity.SysOcResource;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.mapper.SysOcResourceMapper;
import com.handturn.bole.system.service.ISysOcResourceService;
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
 * 系统-网点资源 Service实现
 *
 * @author MrBird
 * @date 2019-12-13 10:00:36
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysOcResourceServiceImpl extends ServiceImpl<SysOcResourceMapper, SysOcResource> implements ISysOcResourceService {

    @Autowired
    ISysResourceService sysResourceService;

    @Override
    @Transactional
    public SysOcResource saveSysOcResource(SysOcResource sysOcResource) {
        if(sysOcResource.getId() == null){
            this.save(sysOcResource);
            return sysOcResource;
        }else{
            SysOcResource sysOcResourceOld = this.baseMapper.selectById(sysOcResource.getId());
            CopyUtils.copyProperties(sysOcResource,sysOcResourceOld);
            this.updateById(sysOcResourceOld);
            return sysOcResourceOld;
        }
    }

    /**
     * 获取组织菜单
     * @param ocId
     * @return
     */
    @Override
    public Set<Long> findSysResourceIds4OrgOc(Long ocId){
        Set<Long> resourceIdSet = new HashSet<Long>();
        List<SysOcResource> ocResources = this.baseMapper.selectList(new QueryWrapper<SysOcResource>().lambda().eq(SysOcResource::getOcId, ocId));
        for(SysOcResource ocResource : ocResources){
            resourceIdSet.add(ocResource.getResourceId());
        }

        return resourceIdSet;
    }

    /**
     * 保存组织资源
     * @param ocId
     * @param rootModuleId
     * @param resourceIds
     */
    @Override
    @Transactional
    public void saveOrgOcResource(Long ocId,Long rootModuleId,String[] resourceIds){
        List<SysResource> sysResources = sysResourceService.findSysResourcesByRootId(String.valueOf(rootModuleId));
        //删除模块资源
        List<Long> ids = new ArrayList<Long>();
        sysResources.forEach(sysResource -> {
            ids.add(sysResource.getId());
        });
        this.baseMapper.delete(new QueryWrapper<SysOcResource>().lambda()
                .in(SysOcResource::getResourceId, ids)
                .eq(SysOcResource::getOcId,ocId));

        //保存模块资源
        for(String resourceId : resourceIds){
            if(StringUtils.isEmpty(resourceId)){
                continue;
            }
            SysOcResource sysOcResource = new SysOcResource();
            sysOcResource.setOcId(ocId);
            sysOcResource.setResourceId(Long.valueOf(resourceId));
            this.save(sysOcResource);
        }
    }

    @Override
    @Transactional
    public void deleteOcResourceByOc(Long ocId){
        this.baseMapper.delete(new QueryWrapper<SysOcResource>().lambda()
                .eq(SysOcResource::getOcId,ocId));
    }
}
