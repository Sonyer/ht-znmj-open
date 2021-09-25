package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.entity.SysRoleResource;
import com.handturn.bole.system.mapper.SysRoleResourceMapper;
import com.handturn.bole.system.service.ISysResourceService;
import com.handturn.bole.system.service.ISysRoleResourceService;
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
 * 系统-角色资源 Service实现
 *
 * @author Eric
 * @date 2019-12-01 10:39:10
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements ISysRoleResourceService {

    @Autowired
    private ISysResourceService sysResourceService;

    @Override
    public ICustomPage<SysRoleResource> findSysRoleResources(QueryRequest request, SysRoleResource sysRoleResource) {
        LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        CustomPage<SysRoleResource> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        return (ICustomPage<SysRoleResource>) this.page(page, queryWrapper);
    }

    @Override
    public List<SysRoleResource> findSysRoleResources(SysRoleResource sysRoleResource) {
	    LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createSysRoleResource(SysRoleResource sysRoleResource) {
        this.save(sysRoleResource);
    }

    @Override
    @Transactional
    public void updateSysRoleResource(SysRoleResource sysRoleResource) {
        this.saveOrUpdate(sysRoleResource);
    }

    @Override
    @Transactional
    public void deleteSysRoleResource(SysRoleResource sysRoleResource) {
        LambdaQueryWrapper<SysRoleResource> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}

    /**
     * 获取组织菜单
     * @param roleId
     * @return
     */
    @Override
    public Set<Long> findSysResourceIds4Role(Long roleId){
        Set<Long> resourceIdSet = new HashSet<Long>();
        List<SysRoleResource> roleResources = this.baseMapper.selectList(new QueryWrapper<SysRoleResource>().lambda().eq(SysRoleResource::getRoleId, roleId));
        for(SysRoleResource roleResource : roleResources){
            resourceIdSet.add(roleResource.getResourceId());
        }

        return resourceIdSet;
    }

    /**
     * 保存组织资源
     * @param roleId
     * @param rootModuleId
     * @param resourceIds
     */
    @Override
    @Transactional
    public void saveRoleResource(Long roleId,Long rootModuleId,String[] resourceIds){
        List<SysResource> sysResources = sysResourceService.findSysResourcesByRootId(String.valueOf(rootModuleId));
        //删除模块资源
        List<Long> ids = new ArrayList<Long>();
        sysResources.forEach(sysResource -> {
            ids.add(sysResource.getId());
        });
        this.baseMapper.delete(new QueryWrapper<SysRoleResource>().lambda()
                .in(SysRoleResource::getResourceId, ids)
                .eq(SysRoleResource::getRoleId,roleId));

        //保存模块资源
        for(String resourceId : resourceIds){
            if(StringUtils.isEmpty(resourceId)){
                continue;
            }
            SysRoleResource sysRoleResource = new SysRoleResource();
            sysRoleResource.setRoleId(roleId);
            sysRoleResource.setResourceId(Long.valueOf(resourceId));
            this.save(sysRoleResource);
        }
    }

    @Override
    @Transactional
    public void deleteRoleResourceByRole(Long roleId){
        this.baseMapper.delete(new QueryWrapper<SysRoleResource>().lambda()
                .eq(SysRoleResource::getRoleId,roleId));
    }
}
