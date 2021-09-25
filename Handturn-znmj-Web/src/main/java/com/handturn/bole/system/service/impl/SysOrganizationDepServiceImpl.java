package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.CommonTreeSelect;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleService;
import com.handturn.bole.sysconf.service.SysconfCodeRuleContant;
import com.handturn.bole.system.entity.SysOrganizationDep;
import com.handturn.bole.system.mapper.SysOrganizationDepMapper;
import com.handturn.bole.system.service.ISysOrganizationDepService;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统-公司部门 Service实现
 *
 * @author Eric
 * @date 2019-12-01 10:22:59
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysOrganizationDepServiceImpl extends ServiceImpl<SysOrganizationDepMapper, SysOrganizationDep> implements ISysOrganizationDepService {

    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public List<CommonTreeSelect<SysOrganizationDep>> findSysOrganizationDeps(){
        List<SysOrganizationDep> depts = this.baseMapper.selectList(new QueryWrapper<SysOrganizationDep>().lambda()
                .eq(SysOrganizationDep::getOcId, UserInfoHolder.getUserInfo().getCurrentOc().getId()));
        List<CommonTreeSelect<SysOrganizationDep>> trees = this.convertOrganizationDepTreeSelect(depts);
        return CommonTreeSelect.buildTreeSelect(trees);
    }

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public CommonTree<SysOrganizationDep> findSysOrganizationDeps(SysOrganizationDep sysOrganizationDep){
        QueryWrapper<SysOrganizationDep> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysOrganizationDep::getOcId, UserInfoHolder.getUserInfo().getCurrentOc().getId());
        if (StringUtils.isNotBlank(sysOrganizationDep.getDepName())) {
            queryWrapper.lambda().like(SysOrganizationDep::getDepName, sysOrganizationDep.getDepName());
        }
        queryWrapper.lambda().orderByAsc(SysOrganizationDep::getId);
        List<SysOrganizationDep> sysOrganizationDeps = this.baseMapper.selectList(queryWrapper);
        List<CommonTree<SysOrganizationDep>> trees = this.convertOrganizationDepTree(sysOrganizationDeps);

        return CommonTree.buildTree(trees,false,null);
    }

    private List<CommonTree<SysOrganizationDep>> convertOrganizationDepTree(List<SysOrganizationDep> sysOrganizationDeps) {
        List<CommonTree<SysOrganizationDep>> trees = new ArrayList<>();
        sysOrganizationDeps.forEach(sysOrganizationDep -> {
            CommonTree<SysOrganizationDep> tree = new CommonTree<>();
            tree.setId(String.valueOf(sysOrganizationDep.getId()));
            tree.setParentId(String.valueOf(sysOrganizationDep.getParentDepId()));
            tree.setTitle(sysOrganizationDep.getDepName());
            tree.setData(sysOrganizationDep);
            trees.add(tree);
        });
        return trees;
    }

    private List<CommonTreeSelect<SysOrganizationDep>> convertOrganizationDepTreeSelect(List<SysOrganizationDep> sysOrganizationDeps) {
        List<CommonTreeSelect<SysOrganizationDep>> trees = new ArrayList<>();
        sysOrganizationDeps.forEach(sysOrganizationDep -> {
            CommonTreeSelect<SysOrganizationDep> tree = new CommonTreeSelect<>();
            tree.setId(String.valueOf(sysOrganizationDep.getId()));
            tree.setParentId(String.valueOf(sysOrganizationDep.getParentDepId()));
            tree.setName(sysOrganizationDep.getDepName());
            tree.setData(sysOrganizationDep);
            trees.add(tree);
        });
        return trees;
    }


    /**
     * 按照条件查询table展示
     * @param sysOrganizationDep
     * @return
     */
    @Override
    public List<SysOrganizationDep> findSysOrganizationDep4Table(SysOrganizationDep sysOrganizationDep){
        QueryWrapper<SysOrganizationDep> queryWrapper = new QueryWrapper<SysOrganizationDep>();
        queryWrapper.lambda().eq(SysOrganizationDep::getOcId, UserInfoHolder.getUserInfo().getCurrentOc().getId());
        if (sysOrganizationDep.getId() != null) {
            queryWrapper.lambda().and(wrapper -> wrapper.eq(SysOrganizationDep::getParentDepId, sysOrganizationDep.getId()).or().eq(SysOrganizationDep::getId, sysOrganizationDep.getId()));
        }
        if (StringUtils.isNotEmpty(sysOrganizationDep.getDepName())) {
            queryWrapper.lambda().like(SysOrganizationDep::getDepName, sysOrganizationDep.getDepName());
        }
        queryWrapper.lambda().orderByAsc(SysOrganizationDep::getParentDepId).orderByAsc(SysOrganizationDep::getId);
        List<SysOrganizationDep> listSysOrganizationDeps = this.baseMapper.selectList(queryWrapper);
        List<SysOrganizationDep> menus = convertSysOrganizationDeps4List(listSysOrganizationDeps);
        return menus;
    }

    private List<SysOrganizationDep> convertSysOrganizationDeps4List(List<SysOrganizationDep> listSysOrganizationDeps) {
        List<SysOrganizationDep> result = new ArrayList<SysOrganizationDep>();
        Map<Long, List<SysOrganizationDep>> parentIdMap = new HashMap<Long, List<SysOrganizationDep>>();
        for (SysOrganizationDep sysOrganizationDep : listSysOrganizationDeps) {
            if (sysOrganizationDep.getParentDepId() == null || sysOrganizationDep.getParentDepId() == 0) {
                List<SysOrganizationDep> sysOrganizationDeps = null;
                if (parentIdMap.get(0L) == null) {
                    sysOrganizationDeps = new ArrayList<SysOrganizationDep>();
                } else {
                    sysOrganizationDeps = parentIdMap.get(0L);
                }
                sysOrganizationDeps.add(sysOrganizationDep);

                parentIdMap.put(0L, sysOrganizationDeps);
            } else {
                List<SysOrganizationDep> sysOrganizationDeps = null;
                if (parentIdMap.get(sysOrganizationDep.getParentDepId()) == null) {
                    sysOrganizationDeps = new ArrayList<SysOrganizationDep>();
                } else {
                    sysOrganizationDeps = parentIdMap.get(sysOrganizationDep.getParentDepId());
                }
                sysOrganizationDeps.add(sysOrganizationDep);

                parentIdMap.put(sysOrganizationDep.getParentDepId(), sysOrganizationDeps);
            }
        }

        result = iterationSysOrganizationDep(listSysOrganizationDeps,parentIdMap,new ArrayList<SysOrganizationDep>(),new HashMap<>());

        return result;
    }

    private List<SysOrganizationDep> iterationSysOrganizationDep(List<SysOrganizationDep> sysOrganizationDeps,Map<Long,List<SysOrganizationDep>> parentMenuK,List<SysOrganizationDep> result,Map<Long,Long> exitMap){
        if(sysOrganizationDeps == null){
            return result;
        }
        for(SysOrganizationDep sysOrganizationDep : sysOrganizationDeps){
            if(exitMap.get(sysOrganizationDep.getId()) != null){
                continue;
            }else{
                exitMap.put(sysOrganizationDep.getId(),sysOrganizationDep.getId());
            }
            result.add(sysOrganizationDep);
            if(parentMenuK.get(sysOrganizationDep.getId()) != null){
                result = iterationSysOrganizationDep(parentMenuK.get(sysOrganizationDep.getId()),parentMenuK,result,exitMap);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public SysOrganizationDep saveSysOrganizationDep(SysOrganizationDep sysOrganizationDep,boolean isSystem) {
        if(sysOrganizationDep.getId() == null){
            //编码生成
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.SYS_ORGANIZATION_DEP_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);

            sysOrganizationDep.setDepCode(code);
            sysOrganizationDep.setStatus(BaseStatus.ENABLED);
            this.save(sysOrganizationDep);
            return sysOrganizationDep;
        }else{
            SysOrganizationDep sysOrganizationDepOld = this.baseMapper.selectById(sysOrganizationDep.getId());
            CopyUtils.copyProperties(sysOrganizationDep,sysOrganizationDepOld);
            this.updateById(sysOrganizationDepOld);
            return sysOrganizationDepOld;
        }
    }

    @Override
    @Transactional
    public void deleteSysOrganizationDep(String sysOrganizationDepIds) {
        String[] sysOrganizationDepIdsArray = sysOrganizationDepIds.split(StringPool.COMMA);
        this.delete(Arrays.asList(sysOrganizationDepIdsArray));
    }

    private void delete(List<String> sysOrganizationDepIds) {
        removeByIds(sysOrganizationDepIds);

        LambdaQueryWrapper<SysOrganizationDep> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysOrganizationDep::getParentDepId, sysOrganizationDepIds);
        List<SysOrganizationDep> menus = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(menus)) {
            List<String> menuIdList = new ArrayList<>();
            menus.forEach(m -> menuIdList.add(String.valueOf(m.getId())));
            this.delete(menuIdList);
        }
    }

    /**
     * @param sysOrganizationDepId
     * @return
     */
    @Override
    public SysOrganizationDep findById(Long sysOrganizationDepId){
        return this.baseMapper.selectOne(new QueryWrapper<SysOrganizationDep>().lambda().eq(SysOrganizationDep::getId, sysOrganizationDepId));
    }
}
