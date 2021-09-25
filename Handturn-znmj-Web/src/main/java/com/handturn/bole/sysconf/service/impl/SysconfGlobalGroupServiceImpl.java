package com.handturn.bole.sysconf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.entity.SysconfGlobalGroup;
import com.handturn.bole.sysconf.entity.SysconfGlobalParam;
import com.handturn.bole.sysconf.mapper.SysconfGlobalGroupMapper;
import com.handturn.bole.sysconf.service.ISysconfGlobalGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 系统-系统配置 Service实现
 *
 * @author Eric
 * @date 2019-12-18 20:25:27
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysconfGlobalGroupServiceImpl extends ServiceImpl<SysconfGlobalGroupMapper, SysconfGlobalGroup> implements ISysconfGlobalGroupService {

    @Override
    public ICustomPage<SysconfGlobalGroup> findSysconfGlobalGroups(QueryRequest request, SysconfGlobalGroup sysconfGlobalGroup) {
        CustomPage<SysconfGlobalGroup> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysconfGlobalGroup);
    }

    @Override
    @Transactional
    public SysconfGlobalGroup saveSysconfGlobalGroup(SysconfGlobalGroup sysconfGlobalGroup) {
        if(sysconfGlobalGroup.getId() == null){
            sysconfGlobalGroup.setStatus(BaseStatus.ENABLED);
            this.save(sysconfGlobalGroup);
            return sysconfGlobalGroup;
        }else{
            SysconfGlobalGroup sysconfGlobalGroupOld = this.baseMapper.selectById(sysconfGlobalGroup.getId());
            CopyUtils.copyProperties(sysconfGlobalGroup,sysconfGlobalGroupOld);
            this.updateById(sysconfGlobalGroupOld);
            return sysconfGlobalGroupOld;
        }
    }

    /**
     * 同步
     *
     * @param sysconfGlobalGroupIds sysconfGlobalGroupIds
     */
    @Override
    @Transactional
    public void synchroSysconfGlobalGroup(String[] sysconfGlobalGroupIds){
        Arrays.stream(sysconfGlobalGroupIds).forEach(sysconfGlobalGroupId -> {
            SysconfGlobalGroup sysconfGlobalGroup = this.baseMapper.selectById(sysconfGlobalGroupId);
            SysconfGlobalGroup sysconfGlobalGroupNew = new SysconfGlobalGroup();
            CopyUtils.copyProperties(sysconfGlobalGroup,sysconfGlobalGroupNew);

            sysconfGlobalGroupNew.setId(null);
            sysconfGlobalGroupNew.setIsSysCreated(BaseBoolean.FALSE);
            sysconfGlobalGroupNew.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
            sysconfGlobalGroupNew.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());

            sysconfGlobalGroupNew.setStatus(BaseStatus.ENABLED);
            this.baseMapper.insert(sysconfGlobalGroupNew);
        });
    }

    @Override
    @Transactional
    public void enableSysconfGlobalGroup(String[] sysconfGlobalGroupIds) {
        Arrays.stream(sysconfGlobalGroupIds).forEach(sysconfGlobalGroupId -> {
            SysconfGlobalGroup sysconfGlobalGroup = this.baseMapper.selectById(sysconfGlobalGroupId);
            sysconfGlobalGroup.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysconfGlobalGroup);
        });
	}

    @Override
    @Transactional
    public void disableSysconfGlobalGroup(String[] sysconfGlobalGroupIds) {
        Arrays.stream(sysconfGlobalGroupIds).forEach(sysconfGlobalGroupId -> {
            SysconfGlobalGroup sysconfGlobalGroup = this.baseMapper.selectById(sysconfGlobalGroupId);
            sysconfGlobalGroup.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysconfGlobalGroup);
        });
    }

    @Override
    public SysconfGlobalGroup findSysconfGlobalGroupById(Long sysconfGlobalGroupId){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfGlobalGroup>().lambda().eq(SysconfGlobalGroup::getId, sysconfGlobalGroupId));
    }

    @Override
    public SysconfGlobalGroup findSysconfGlobalGroupByGroupCode(String groupCode){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfGlobalGroup>().lambda().eq(SysconfGlobalGroup::getGroupCode, groupCode));
    }
}
