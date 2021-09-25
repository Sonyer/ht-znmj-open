package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import com.handturn.bole.system.entity.SysGroupReport;
import com.handturn.bole.system.entity.SysRoleReport;
import com.handturn.bole.system.mapper.SysGroupReportMapper;
import com.handturn.bole.system.service.ISysGroupReportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.utils.CopyUtils;

import java.util.*;

/**
 * 系统-组报表 Service实现
 *
 * @author Eric
 * @date 2020-05-23 14:55:39
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysGroupReportServiceImpl extends ServiceImpl<SysGroupReportMapper, SysGroupReport> implements ISysGroupReportService {

    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;

    @Override
    public ICustomPage<SysGroupReport> findSysGroupReports(QueryRequest request, SysGroupReport sysGroupReport) {
        CustomPage<SysGroupReport> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysGroupReport);
    }

    @Override
    @Transactional
    public SysGroupReport saveSysGroupReport(SysGroupReport sysGroupReport) {
        if(sysGroupReport.getId() == null){
            this.save(sysGroupReport);
            return sysGroupReport;
        }else{
            SysGroupReport sysGroupReportOld = this.baseMapper.selectById(sysGroupReport.getId());
            CopyUtils.copyProperties(sysGroupReport,sysGroupReportOld);
            this.updateById(sysGroupReportOld);
            return sysGroupReportOld;
        }
    }

    @Override
    @Transactional
    public void enableSysGroupReport(String[] sysGroupReportIds) {
        Arrays.stream(sysGroupReportIds).forEach(sysGroupReportId -> {
            SysGroupReport sysGroupReport = this.baseMapper.selectById(sysGroupReportId);
            //sysGroupReport.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysGroupReport);
        });
	}

    @Override
    @Transactional
    public void disableSysGroupReport(String[] sysGroupReportIds) {
        Arrays.stream(sysGroupReportIds).forEach(sysGroupReportId -> {
            SysGroupReport sysGroupReport = this.baseMapper.selectById(sysGroupReportId);
            //sysGroupReport.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysGroupReport);
        });
    }

    @Override
    public SysGroupReport findSysGroupReportById(Long sysGroupReportId){
        return this.baseMapper.selectOne(new QueryWrapper<SysGroupReport>().lambda().eq(SysGroupReport::getId, sysGroupReportId));
    }

    /**
     * 获取分组报表
     * @param groupId
     * @return
     */
    @Override
    public Set<Long> findReportIds4Group(Long groupId){
        Set<Long> reportIdSet = new HashSet<Long>();
        List<SysGroupReport> groupReports = this.baseMapper.selectList(new QueryWrapper<SysGroupReport>().lambda().eq(SysGroupReport::getGroupId, groupId));
        for(SysGroupReport groupReport : groupReports){
            reportIdSet.add(groupReport.getReportId());
        }

        return reportIdSet;
    }

    /**
     * 保存分组报表
     * @param groupId
     * @param rootReportId
     * @param reportIds
     */
    @Override
    @Transactional
    public void saveGroupReport(Long groupId,Long rootReportId,String[] reportIds){
        List<SysconfQueryReport> sysReports = sysconfQueryReportService.findSysconfReportsByRootId(String.valueOf(rootReportId));
        //删除模块资源
        List<Long> ids = new ArrayList<Long>();
        sysReports.forEach(sysReport -> {
            ids.add(sysReport.getId());
        });
        this.baseMapper.delete(new QueryWrapper<SysGroupReport>().lambda()
                .in(SysGroupReport::getReportId, ids)
                .eq(SysGroupReport::getGroupId,groupId));

        //保存模块资源
        for(String reportId : reportIds){
            if(StringUtils.isEmpty(reportId)){
                continue;
            }
            SysGroupReport sysGroupReport = new SysGroupReport();
            sysGroupReport.setGroupId(groupId);
            sysGroupReport.setReportId(Long.valueOf(reportId));
            this.save(sysGroupReport);
        }
    }
}
