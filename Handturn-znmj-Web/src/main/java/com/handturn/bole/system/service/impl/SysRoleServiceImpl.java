package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleService;
import com.handturn.bole.sysconf.service.SysconfCodeRuleContant;
import com.handturn.bole.system.entity.SysRole;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.mapper.SysRoleMapper;
import com.handturn.bole.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 系统-角色 Service实现
 *
 * @author Eric
 * @date 2019-12-01 10:35:21
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;

    @Override
    public List<SysRole> findUserRole(String userCode) {
        return this.baseMapper.findUserRole(userCode);
    }

    @Override
    public ICustomPage<SysRole> findSysRoles(QueryRequest request, SysRole sysRole) {
        sysRole.setOcId(UserInfoHolder.getUserInfo().getCurrentOc().getId());

        CustomPage<SysUser> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysRole);
    }

    @Override
    @Transactional
    public SysRole saveSysRole(SysRole sysRole,boolean isSystem) {
        if(sysRole.getId() == null){
            //编码生成
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.SYS_ROLE_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);

            sysRole.setRoleCode(code);
            sysRole.setStatus(BaseStatus.ENABLED);
            this.save(sysRole);
            return sysRole;
        }else{
            SysRole sysRoleOld = this.baseMapper.selectById(sysRole.getId());
            if(!isSystem && sysRoleOld.getIsSysCreate().equals(BaseBoolean.TRUE)){
                throw new FebsException("不允许对系统数据进行调整!");
            }
            CopyUtils.copyProperties(sysRole,sysRoleOld);
            this.updateById(sysRoleOld);
            return sysRoleOld;
        }
    }

    @Override
    @Transactional
    public void enableSysRole(String[] sysRoleIds) {
        Arrays.stream(sysRoleIds).forEach(sysRoleId -> {
            SysRole sysRole = this.baseMapper.selectById(sysRoleId);
            sysRole.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysRole);
        });
    }

    @Override
    @Transactional
    public void disableSysRole(String[] sysRoleIds) {
        Arrays.stream(sysRoleIds).forEach(sysRoleId -> {
            SysRole sysRole = this.baseMapper.selectById(sysRoleId);
            sysRole.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysRole);
        });
    }

    @Override
    public SysRole findSysRoleById(Long sysRoleId){
        return this.baseMapper.selectOne(new QueryWrapper<SysRole>().lambda().eq(SysRole::getId, sysRoleId));
    }
}
