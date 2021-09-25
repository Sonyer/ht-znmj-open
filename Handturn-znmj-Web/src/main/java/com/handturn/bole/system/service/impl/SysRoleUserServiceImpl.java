package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.system.entity.SysRoleUser;
import com.handturn.bole.system.mapper.SysRoleUserMapper;
import com.handturn.bole.system.service.ISysRoleUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 系统-角色用户 Service实现
 *
 * @author MrBird
 * @date 2019-12-13 15:14:41
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUser> implements ISysRoleUserService {

    @Override
    @Transactional
    public SysRoleUser saveSysRoleUser(SysRoleUser sysRoleUser) {
        if(sysRoleUser.getId() == null){
            this.save(sysRoleUser);
            return sysRoleUser;
        }else{
            SysRoleUser sysRoleUserOld = this.baseMapper.selectById(sysRoleUser.getId());
            CopyUtils.copyProperties(sysRoleUser,sysRoleUserOld);
            this.updateById(sysRoleUserOld);
            return sysRoleUserOld;
        }
    }

    /**
     * 通过角色获取网点权限
     * @param roleId
     * @return
     */
    @Override
    public Set<Long> findUserIds4Role(Long roleId){
        Set<Long> userIdSet = new HashSet<Long>();
        List<SysRoleUser> roleUserIds = this.baseMapper.selectList(new QueryWrapper<SysRoleUser>().lambda()
                .eq(SysRoleUser::getRoleId, roleId));
        for(SysRoleUser sysRoleUser : roleUserIds){
            userIdSet.add(sysRoleUser.getUserId());
        }
        return userIdSet;
    }


    @Override
    @Transactional
    public void saveRoleUser(Long roleId,String[] userIds){
        this.baseMapper.delete(new QueryWrapper<SysRoleUser>().lambda()
                .eq(SysRoleUser::getRoleId,roleId));

        //保存模块资源
        for(String userId : userIds){
            if(StringUtils.isEmpty(userId)){
                continue;
            }
            SysRoleUser roleUser = new SysRoleUser();
            roleUser.setUserId(Long.valueOf(userId));
            roleUser.setRoleId(roleId);
            this.save(roleUser);
        }
    }

}
