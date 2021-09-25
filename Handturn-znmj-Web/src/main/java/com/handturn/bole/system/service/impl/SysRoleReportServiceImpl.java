package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.entity.SysRoleReport;
import com.handturn.bole.system.entity.SysRoleResource;
import com.handturn.bole.system.mapper.SysRoleReportMapper;
import com.handturn.bole.system.service.ISysRoleReportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统-角色报表 Service实现
 *
 * @author Eric
 * @date 2020-02-16 20:13:20
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysRoleReportServiceImpl extends ServiceImpl<SysRoleReportMapper, SysRoleReport> implements ISysRoleReportService {

    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;

    @Override
    public ICustomPage<SysRoleReport> findSysRoleReports(QueryRequest request, SysRoleReport sysRoleReport) {
        CustomPage<SysRoleReport> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysRoleReport);
    }

    @Override
    @Transactional
    public SysRoleReport saveSysRoleReport(SysRoleReport sysRoleReport) {
        if(sysRoleReport.getId() == null){
            this.save(sysRoleReport);
            return sysRoleReport;
        }else{
            SysRoleReport sysRoleReportOld = this.baseMapper.selectById(sysRoleReport.getId());
            CopyUtils.copyProperties(sysRoleReport,sysRoleReportOld);
            this.updateById(sysRoleReportOld);
            return sysRoleReportOld;
        }
    }

    @Override
    @Transactional
    public void enableSysRoleReport(String[] sysRoleReportIds) {
        Arrays.stream(sysRoleReportIds).forEach(sysRoleReportId -> {
            SysRoleReport sysRoleReport = this.baseMapper.selectById(sysRoleReportId);
            //sysRoleReport.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysRoleReport);
        });
	}

    @Override
    @Transactional
    public void disableSysRoleReport(String[] sysRoleReportIds) {
        Arrays.stream(sysRoleReportIds).forEach(sysRoleReportId -> {
            SysRoleReport sysRoleReport = this.baseMapper.selectById(sysRoleReportId);
            //sysRoleReport.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysRoleReport);
        });
    }

    @Override
    public SysRoleReport findSysRoleReportById(Long sysRoleReportId){
        return this.baseMapper.selectOne(new QueryWrapper<SysRoleReport>().lambda().eq(SysRoleReport::getId, sysRoleReportId));
    }

    /**
     * 获取组织报表
     * @param roleId
     * @return
     */
    @Override
    public Set<Long> findReportIds4Role(Long roleId){
        Set<Long> reportIdSet = new HashSet<Long>();
        List<SysRoleReport> roleReports = this.baseMapper.selectList(new QueryWrapper<SysRoleReport>().lambda().eq(SysRoleReport::getRoleId, roleId));
        for(SysRoleReport roleReport : roleReports){
            reportIdSet.add(roleReport.getReportId());
        }

        return reportIdSet;
    }

    /**
     * 保存组织报表
     * @param roleId
     * @param rootReportId
     * @param reportIds
     */
    @Override
    @Transactional
    public void saveRoleReport(Long roleId,Long rootReportId,String[] reportIds){
        List<SysconfQueryReport> sysReports = sysconfQueryReportService.findSysconfReportsByRootId(String.valueOf(rootReportId));
        //删除模块资源
        List<Long> ids = new ArrayList<Long>();
        sysReports.forEach(sysReport -> {
            ids.add(sysReport.getId());
        });
        this.baseMapper.delete(new QueryWrapper<SysRoleReport>().lambda()
                .in(SysRoleReport::getReportId, ids)
                .eq(SysRoleReport::getRoleId,roleId));

        //保存模块资源
        for(String reportId : reportIds){
            if(StringUtils.isEmpty(reportId)){
                continue;
            }
            SysRoleReport sysRoleReport = new SysRoleReport();
            sysRoleReport.setRoleId(roleId);
            sysRoleReport.setReportId(Long.valueOf(reportId));
            this.save(sysRoleReport);
        }
    }

    @Override
    @Transactional
    public void deleteRoleReportByRole(Long roleId){
        this.baseMapper.delete(new QueryWrapper<SysRoleReport>().lambda()
                .eq(SysRoleReport::getRoleId,roleId));
    }
}
