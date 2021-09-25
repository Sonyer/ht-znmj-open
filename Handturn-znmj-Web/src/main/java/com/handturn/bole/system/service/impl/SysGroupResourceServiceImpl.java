package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.system.entity.SysGroupResource;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.entity.SysRoleResource;
import com.handturn.bole.system.mapper.SysGroupResourceMapper;
import com.handturn.bole.system.service.ISysGroupResourceService;
import com.handturn.bole.system.service.ISysResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统-组资源 Service实现
 *
 * @author Eric
 * @date 2020-05-23 14:55:50
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysGroupResourceServiceImpl extends ServiceImpl<SysGroupResourceMapper, SysGroupResource> implements ISysGroupResourceService {

    @Autowired
    private ISysResourceService sysResourceService;

    @Override
    public ICustomPage<SysGroupResource> findSysGroupResources(QueryRequest request, SysGroupResource sysGroupResource) {
        CustomPage<SysGroupResource> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysGroupResource);
    }

    @Override
    @Transactional
    public SysGroupResource saveSysGroupResource(SysGroupResource sysGroupResource) {
        if(sysGroupResource.getId() == null){
            this.save(sysGroupResource);
            return sysGroupResource;
        }else{
            SysGroupResource sysGroupResourceOld = this.baseMapper.selectById(sysGroupResource.getId());
            CopyUtils.copyProperties(sysGroupResource,sysGroupResourceOld);
            this.updateById(sysGroupResourceOld);
            return sysGroupResourceOld;
        }
    }

    @Override
    @Transactional
    public void enableSysGroupResource(String[] sysGroupResourceIds) {
        Arrays.stream(sysGroupResourceIds).forEach(sysGroupResourceId -> {
            SysGroupResource sysGroupResource = this.baseMapper.selectById(sysGroupResourceId);
            //sysGroupResource.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysGroupResource);
        });
	}

    @Override
    @Transactional
    public void disableSysGroupResource(String[] sysGroupResourceIds) {
        Arrays.stream(sysGroupResourceIds).forEach(sysGroupResourceId -> {
            SysGroupResource sysGroupResource = this.baseMapper.selectById(sysGroupResourceId);
            //sysGroupResource.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysGroupResource);
        });
    }

    @Override
    public SysGroupResource findSysGroupResourceById(Long sysGroupResourceId){
        return this.baseMapper.selectOne(new QueryWrapper<SysGroupResource>().lambda().eq(SysGroupResource::getId, sysGroupResourceId));
    }

    /**
     * 获取分组菜单
     * @param groupId
     * @return
     */
    @Override
    public Set<Long> findSysResourceIds4Group(Long groupId){
        Set<Long> resourceIdSet = new HashSet<Long>();
        List<SysGroupResource> groupResources = this.baseMapper.selectList(new QueryWrapper<SysGroupResource>().lambda().eq(SysGroupResource::getGroupId, groupId));
        for(SysGroupResource groupResource : groupResources){
            resourceIdSet.add(groupResource.getResourceId());
        }

        return resourceIdSet;
    }

    /**
     * 保存分组资源
     * @param groupId
     * @param rootModuleId
     * @param resourceIds
     */
    @Override
    @Transactional
    public void saveGroupResource(Long groupId,Long rootModuleId,String[] resourceIds){
        List<SysResource> sysResources = sysResourceService.findSysResourcesByRootId(String.valueOf(rootModuleId));
        //删除模块资源
        List<Long> ids = new ArrayList<Long>();
        sysResources.forEach(sysResource -> {
            ids.add(sysResource.getId());
        });
        this.baseMapper.delete(new QueryWrapper<SysGroupResource>().lambda()
                .in(SysGroupResource::getResourceId, ids)
                .eq(SysGroupResource::getGroupId,groupId));

        //保存模块资源
        for(String resourceId : resourceIds){
            if(StringUtils.isEmpty(resourceId)){
                continue;
            }
            SysGroupResource sysGroupResource = new SysGroupResource();
            sysGroupResource.setGroupId(groupId);
            sysGroupResource.setResourceId(Long.valueOf(resourceId));
            this.save(sysGroupResource);
        }
    }
}
