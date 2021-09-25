package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.authentication.ShiroRealm;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleService;
import com.handturn.bole.sysconf.service.SysconfCodeRuleContant;
import com.handturn.bole.system.entity.ResourceNodeType;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.mapper.SysResourceMapper;
import com.handturn.bole.system.service.ISysResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统-资源 Service实现
 *
 * @author Eric
 * @date 2019-12-01 10:31:46
 */
@Service("SysResourceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements ISysResourceService {

    @Autowired
    private ShiroRealm shiroRealm;

    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;

    /**
     * 查找用户菜单集合
     *
     * @param userCode 用户名
     * @return 用户菜单集合
     */
    @Override
    public CommonTree<SysResource> findUserSysResources(String userCode,Long rootResourceId){
        List<SysResource> menus = this.baseMapper.findUserSysResources(userCode,String.valueOf(rootResourceId));
        List<CommonTree<SysResource>> trees = this.convertCommonTree(menus,new HashSet<Long>());
        return CommonTree.buildTree(trees,true,String.valueOf(rootResourceId));

    }


    /**
     * 查找用户菜单集合
     *
     * @param userId 用户Id
     * @return 用户菜单集合
     */
    @Override
    public List<SysResource> findUserRootSysResources(Long userId){
        List<SysResource> menus = this.baseMapper.findUserRootSysResources(String.valueOf(userId));
        return menus;
    }

    /**
     * 查找所有根节点
     *
     * @return 用户菜单集合
     */
    @Override
    public List<SysResource> findAllRootSysResources(){
        List<SysResource> rootModule = this.baseMapper.findAllRootSysResources();
        return rootModule;
    }

    /**
     * 查找所有根节点
     *
     * @return 用户菜单集合
     */
    @Override
    public List<SysResource> findAllRootSysResourcesByOrgId(Long orgId){
        List<SysResource> rootModule = this.baseMapper.findAllRootSysResourcesByOrgId(String.valueOf(orgId));
        return rootModule;
    }

    /**
     * 查找所有根节点
     *
     * @return 用户菜单集合
     */
    @Override
    public List<SysResource> findAllRootSysResourcesByOcId(Long ocId){
        List<SysResource> rootModule = this.baseMapper.findAllRootSysResourcesByOcId(String.valueOf(ocId));
        return rootModule;
    }


    @Override
    public List<SysResource> findUserPermissions(String userCode) {
        return this.baseMapper.findUserPermissions(userCode);
    }

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public CommonTree<SysResource> findSysResources(SysResource sysResource){
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(sysResource.getResourceName())) {
            queryWrapper.lambda().like(SysResource::getResourceName, sysResource.getResourceName());
        }
        queryWrapper.lambda().orderByAsc(SysResource::getSortNo);
        List<SysResource> sysResources = this.baseMapper.selectList(queryWrapper);
        List<CommonTree<SysResource>> trees = this.convertCommonTree(sysResources,new HashSet<Long>());

        return CommonTree.buildTree(trees,false,null);
    }

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public CommonTree<SysResource> findSysResourcesByRootId(String rootResourceId, Set<Long> permResourceIds){
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(rootResourceId)) {
            queryWrapper.lambda().like(SysResource::getRootResourceId, rootResourceId);
        }
        queryWrapper.lambda().orderByAsc(SysResource::getSortNo);
        List<SysResource> sysResources = this.baseMapper.selectList(queryWrapper);
        List<CommonTree<SysResource>> trees = this.convertCommonTree(sysResources,permResourceIds);

        return CommonTree.buildTree(trees,false,null);
    }

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public CommonTree<SysResource> findSysResourcesByRootIdOrgId(String rootResourceId,String orgId, Set<Long> permResourceIds){
        List<SysResource> sysResources = this.baseMapper.findSysResourcesByRootIdOrgId(rootResourceId,orgId);
        List<CommonTree<SysResource>> trees = this.convertCommonTree(sysResources,permResourceIds);
        return CommonTree.buildTree(trees,false,null);
    }

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public CommonTree<SysResource> findSysResourcesByRootIdOcId(String rootResourceId,String ocId, Set<Long> permResourceIds){
        List<SysResource> sysResources = this.baseMapper.findSysResourcesByRootIdOcId(rootResourceId,ocId);
        List<CommonTree<SysResource>> trees = this.convertCommonTree(sysResources,permResourceIds);
        return CommonTree.buildTree(trees,false,null);
    }

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public List<SysResource> findSysResourcesByRootId(String rootResourceId){
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(rootResourceId)) {
            queryWrapper.lambda().like(SysResource::getRootResourceId, rootResourceId);
        }
        queryWrapper.lambda().orderByAsc(SysResource::getSortNo);
        List<SysResource> sysResources = this.baseMapper.selectList(queryWrapper);
        return sysResources;
    }

    private List<CommonTree<SysResource>> convertCommonTree(List<SysResource> sysResources, Set<Long> permResourceIds) {
        List<CommonTree<SysResource>> trees = new ArrayList<>();
        sysResources.forEach(sysResource -> {  //除去存在有子节点的资源
            permResourceIds.remove(sysResource.getParentResourceId());
        });

        sysResources.forEach(sysResource -> {
            CommonTree<SysResource> tree = new CommonTree<>();
            tree.setId(String.valueOf(sysResource.getId()));
            tree.setParentId(String.valueOf(sysResource.getParentResourceId()));
            tree.setTitle(sysResource.getResourceName());
            tree.setIcon(sysResource.getIcon());
            tree.setHref(sysResource.getUrl());
            tree.setData(sysResource);
            if(permResourceIds.contains(sysResource.getId())){
                tree.setChecked(true);
            }
            trees.add(tree);
        });
        return trees;
    }

    /**
     * 按照条件查询table展示
     * @param sysResource
     * @return
     */
    @Override
    public List<SysResource> findSysResources4Table(SysResource sysResource){
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<SysResource>();
        if (sysResource.getId() != null) {
            queryWrapper.lambda().and(wrapper -> wrapper.eq(SysResource::getParentResourceId, sysResource.getId()).or().eq(SysResource::getId, sysResource.getId()));
        }
        if (StringUtils.isNotEmpty(sysResource.getResourceName())) {
            queryWrapper.lambda().like(SysResource::getResourceName, sysResource.getResourceName());
        }
        queryWrapper.lambda().orderByAsc(SysResource::getParentResourceId).orderByAsc(SysResource::getSortNo);
        List<SysResource> listSysResource = this.baseMapper.selectList(queryWrapper);
        List<SysResource> menus = convertMenus4List(listSysResource);
        return menus;
    }

    private List<SysResource> convertMenus4List(List<SysResource> listSysResource) {
        List<SysResource> result = new ArrayList<SysResource>();
        Map<Long, List<SysResource>> parentIdMap = new HashMap<Long, List<SysResource>>();
        for (SysResource sysResource : listSysResource) {
            if (sysResource.getParentResourceId() == null || sysResource.getParentResourceId() == 0) {
                List<SysResource> sysResources = null;
                if (parentIdMap.get(0L) == null) {
                    sysResources = new ArrayList<SysResource>();
                } else {
                    sysResources = parentIdMap.get(0L);
                }
                sysResources.add(sysResource);

                parentIdMap.put(0L, sysResources);
            } else {
                List<SysResource> sysResources = null;
                if (parentIdMap.get(sysResource.getParentResourceId()) == null) {
                    sysResources = new ArrayList<SysResource>();
                } else {
                    sysResources = parentIdMap.get(sysResource.getParentResourceId());
                }
                sysResources.add(sysResource);

                parentIdMap.put(sysResource.getParentResourceId(), sysResources);
            }
        }

        result = iterationMunue(listSysResource,parentIdMap,new ArrayList<SysResource>(),new HashMap<>());

        return result;
    }

    private List<SysResource> iterationMunue(List<SysResource> resources,Map<Long,List<SysResource>> parentMenuK,List<SysResource> result,Map<Long,Long> exitMap){
        if(resources == null){
            return result;
        }
        for(SysResource sysResource : resources){
            if(exitMap.get(sysResource.getId()) != null){
                continue;
            }else{
                exitMap.put(sysResource.getId(),sysResource.getId());
            }
            result.add(sysResource);
            if(parentMenuK.get(sysResource.getId()) != null){
                result = iterationMunue(parentMenuK.get(sysResource.getId()),parentMenuK,result,exitMap);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void saveSysResource(SysResource sysResource) {
        if(sysResource.getId() == null){
            //编码生成
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.SYS_RESOURCE_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);

            sysResource.setResourceCode(code);
            sysResource.setStatus(BaseStatus.ENABLED);
            this.save(sysResource);
            if(sysResource.getResourceNodeType().equals(ResourceNodeType.MENU_ROOT)){
                sysResource.setRootResourceId(sysResource.getId());
                this.updateById(sysResource);
            }
        }else{
            SysResource sysResourceOld = this.baseMapper.selectById(sysResource.getId());
            CopyUtils.copyProperties(sysResource,sysResourceOld);
            if(sysResource.getResourceNodeType().equals(ResourceNodeType.MENU_ROOT)){
                sysResourceOld.setRootResourceId(sysResourceOld.getId());
            }
            this.updateById(sysResourceOld);
        }
    }

    @Override
    @Transactional
    public void deleteSysResource(String resourceIds) {
        String[] resourceIdsArray = resourceIds.split(StringPool.COMMA);
        this.delete(Arrays.asList(resourceIdsArray));

        shiroRealm.clearCache();
	}

    private void delete(List<String> resourceIds) {
        removeByIds(resourceIds);

        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysResource::getParentResourceId, resourceIds);
        List<SysResource> menus = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(menus)) {
            List<String> menuIdList = new ArrayList<>();
            menus.forEach(m -> menuIdList.add(String.valueOf(m.getId())));
            this.delete(menuIdList);
        }
    }


    /**
     * 通过Id获取菜单信息
     * @param resourceId
     * @return
     */
    @Override
    public SysResource findById(Long resourceId){
        return this.baseMapper.selectOne(new QueryWrapper<SysResource>().lambda().eq(SysResource::getId, resourceId));
    }
}
