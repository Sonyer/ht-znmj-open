package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleService;
import com.handturn.bole.system.entity.*;
import com.handturn.bole.system.mapper.SysGroupMapper;
import com.handturn.bole.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 系统-资源组 Service实现
 *
 * @author Eric
 * @date 2020-05-23 14:55:18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements ISysGroupService {

    @Autowired
    private ISysOrganizationService sysOrganizationService;

    @Autowired
    private ISysGroupResourceService sysGroupResourceService;

    @Autowired
    private ISysGroupReportService sysGroupReportService;

    @Autowired
    private ISysRoleResourceService sysRoleResourceService;

    @Autowired
    private ISysRoleReportService sysRoleReportService;

    @Autowired
    private ISysOcResourceService sysOcResourceService;

    @Autowired
    private ISysOcReportService sysOcReportService;

    @Override
    public ICustomPage<SysGroup> findSysGroups(QueryRequest request, SysGroup sysGroup) {
        CustomPage<SysGroup> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysGroup);
    }

    @Override
    @Transactional
    public SysGroup saveSysGroup(SysGroup sysGroup) {
        if(sysGroup.getId() == null){
            checkByUk(sysGroup);

            sysGroup.setStatus(BaseStatus.ENABLED);
            this.save(sysGroup);
            return sysGroup;
        }else{
            checkByUk(sysGroup);

            SysGroup sysGroupOld = this.baseMapper.selectById(sysGroup.getId());

            CopyUtils.copyProperties(sysGroup,sysGroupOld);
            this.updateById(sysGroupOld);
            return sysGroupOld;
        }
    }

    @Override
    @Transactional
    public void enableSysGroup(String[] sysGroupIds) {
        Arrays.stream(sysGroupIds).forEach(sysGroupId -> {
            SysGroup sysGroup = this.baseMapper.selectById(sysGroupId);
            sysGroup.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysGroup);
        });
	}

    @Override
    @Transactional
    public void disableSysGroup(String[] sysGroupIds) {
        Arrays.stream(sysGroupIds).forEach(sysGroupId -> {
            SysGroup sysGroup = this.baseMapper.selectById(sysGroupId);
            sysGroup.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysGroup);
        });
    }

    /**
     * 一键刷新
     *
     * @param sysGroupIds sysGroupIds
     */
    @Override
    @Transactional
    public void oneTouchFlush(String[] sysGroupIds){
        Arrays.stream(sysGroupIds).forEach(sysGroupId -> {
            SysGroup sysGroup = this.baseMapper.selectById(sysGroupId);
            Set<Long> resourceIdSet = sysGroupResourceService.findSysResourceIds4Group(sysGroup.getId());
            Set<Long> reportIdSet = sysGroupReportService.findReportIds4Group(sysGroup.getId());

            if(sysGroup.getGroupCode().equals(GroupCode.BUSSINESS_GROUP)){
                Integer pageIndex = 1;
                while(true){
                    pageIndex++;

                    List<SysOrganization> clientOrgs = sysOrganizationService.findSysOrganizationsByOrgType(OrgType.ORG,pageIndex);
                    if(clientOrgs == null || clientOrgs.size() <= 0){
                        break;
                    }else{
                        clientOrgs.forEach(clientOrg ->{
                            //新增role 权限
                            if(clientOrg.getRelateRoleId() != null && clientOrg.getRelateRoleId() != 0L){
                                //删除role 权限
                                sysRoleResourceService.deleteRoleResourceByRole(clientOrg.getRelateRoleId());

                                //删除role 权限
                                sysRoleReportService.deleteRoleReportByRole(clientOrg.getRelateRoleId());

                                resourceIdSet.forEach(resourceId->{
                                    SysRoleResource roleResource = new SysRoleResource();
                                    roleResource.setRoleId(clientOrg.getRelateRoleId());
                                    roleResource.setResourceId(resourceId);
                                    sysRoleResourceService.createSysRoleResource(roleResource);
                                });
                                reportIdSet.forEach(reportId->{
                                    SysRoleReport roleReport = new SysRoleReport();
                                    roleReport.setRoleId(clientOrg.getRelateRoleId());
                                    roleReport.setReportId(reportId);
                                    sysRoleReportService.saveSysRoleReport(roleReport);
                                });
                            }
                            //新增oc 权限
                            if(clientOrg.getRelateOcId() != null && clientOrg.getRelateOcId() != 0L){
                                //删除role 权限
                                sysOcResourceService.deleteOcResourceByOc(clientOrg.getRelateOcId());

                                //删除role 权限
                                sysOcReportService.deleteOcReportByOc(clientOrg.getRelateOcId());

                                resourceIdSet.forEach(resourceId->{
                                    SysOcResource ocResource = new SysOcResource();
                                    ocResource.setOcId(clientOrg.getRelateOcId());
                                    ocResource.setResourceId(resourceId);
                                    sysOcResourceService.saveSysOcResource(ocResource);
                                });
                                reportIdSet.forEach(reportId->{
                                    SysOcReport ocReport = new SysOcReport();
                                    ocReport.setOcId(clientOrg.getRelateOcId());
                                    ocReport.setReportId(reportId);
                                    sysOcReportService.saveSysOcReport(ocReport);
                                });
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public SysGroup findSysGroupById(Long sysGroupId){
        return this.baseMapper.selectOne(new QueryWrapper<SysGroup>().lambda().eq(SysGroup::getId, sysGroupId));
    }

    /**
     * 通过Id获取信息
     * @param groupCode
     * @return
     */
    @Override
    public SysGroup findSysGroupByCode(String groupCode){
        return this.baseMapper.selectOne(new QueryWrapper<SysGroup>().lambda().eq(SysGroup::getGroupCode, groupCode));
    }

    public void checkByUk(SysGroup group){
        QueryWrapper<SysGroup> queryWrapper = new QueryWrapper<SysGroup>();
        queryWrapper.lambda().eq(SysGroup::getGroupCode, group.getGroupCode());
        if(group.getId() != null && group.getId() != 0){
            queryWrapper.lambda().ne(SysGroup::getId, group.getId());
        }

        SysGroup temp = this.baseMapper.selectOne(queryWrapper);

        if(temp != null){
            throw new FebsException("记录已经存在!请确认!");
        }
    }
}
