package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.system.entity.SysRoleOc;
import com.handturn.bole.system.mapper.SysRoleOcMapper;
import com.handturn.bole.system.service.ISysRoleOcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 系统-角色网点 Service实现
 *
 * @author MrBird
 * @date 2019-12-13 15:07:51
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysRoleOcServiceImpl extends ServiceImpl<SysRoleOcMapper, SysRoleOc> implements ISysRoleOcService {

    @Override
    @Transactional
    public SysRoleOc saveSysRoleOc(SysRoleOc sysRoleOc) {
        if(sysRoleOc.getId() == null){
            this.save(sysRoleOc);
            return sysRoleOc;
        }else{
            SysRoleOc sysRoleOcOld = this.baseMapper.selectById(sysRoleOc.getId());
            CopyUtils.copyProperties(sysRoleOc,sysRoleOcOld);
            this.updateById(sysRoleOcOld);
            return sysRoleOcOld;
        }
    }

    /**
     * 通过角色获取网点权限
     * @param roleId
     * @return
     */
    @Override
    public Set<Long> findOcIds4Role(Long roleId){
        Set<Long> ocIdSet = new HashSet<Long>();
        List<SysRoleOc> roleOcs = this.baseMapper.selectList(new QueryWrapper<SysRoleOc>().lambda().eq(SysRoleOc::getRoleId, roleId));
        for(SysRoleOc roleOc : roleOcs){
            ocIdSet.add(roleOc.getOcId());
        }
        return ocIdSet;
    }

    @Override
    @Transactional
    public void saveRoleOc(Long roleId,String[] ocIds){
        this.baseMapper.delete(new QueryWrapper<SysRoleOc>().lambda()
                .eq(SysRoleOc::getRoleId,roleId));

        //保存模块资源
        for(String ocId : ocIds){
            if(StringUtils.isEmpty(ocId)){
                continue;
            }
            SysRoleOc roleOc = new SysRoleOc();
            roleOc.setOcId(Long.valueOf(ocId));
            roleOc.setRoleId(roleId);
            this.save(roleOc);
        }
    }

}
