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
import com.handturn.bole.system.entity.SysOcReport;
import com.handturn.bole.system.entity.SysOcResource;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.mapper.SysOcReportMapper;
import com.handturn.bole.system.service.ISysOcReportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统-网点报表 Service实现
 *
 * @author Eric
 * @date 2020-02-16 20:11:11
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysOcReportServiceImpl extends ServiceImpl<SysOcReportMapper, SysOcReport> implements ISysOcReportService {

    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;

    @Override
    public ICustomPage<SysOcReport> findSysOcReports(QueryRequest request, SysOcReport sysOcReport) {
        CustomPage<SysOcReport> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysOcReport);
    }

    @Override
    @Transactional
    public SysOcReport saveSysOcReport(SysOcReport sysOcReport) {
        if(sysOcReport.getId() == null){
            this.save(sysOcReport);
            return sysOcReport;
        }else{
            SysOcReport sysOcReportOld = this.baseMapper.selectById(sysOcReport.getId());
            CopyUtils.copyProperties(sysOcReport,sysOcReportOld);
            this.updateById(sysOcReportOld);
            return sysOcReportOld;
        }
    }

    @Override
    @Transactional
    public void enableSysOcReport(String[] sysOcReportIds) {
        Arrays.stream(sysOcReportIds).forEach(sysOcReportId -> {
            SysOcReport sysOcReport = this.baseMapper.selectById(sysOcReportId);
            //sysOcReport.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysOcReport);
        });
	}

    @Override
    @Transactional
    public void disableSysOcReport(String[] sysOcReportIds) {
        Arrays.stream(sysOcReportIds).forEach(sysOcReportId -> {
            SysOcReport sysOcReport = this.baseMapper.selectById(sysOcReportId);
            //sysOcReport.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysOcReport);
        });
    }

    @Override
    public SysOcReport findSysOcReportById(Long sysOcReportId){
        return this.baseMapper.selectOne(new QueryWrapper<SysOcReport>().lambda().eq(SysOcReport::getId, sysOcReportId));
    }

    /**
     * 获取组织报表
     * @param ocId
     * @return
     */
    @Override
    public Set<Long> findReportIds4OrgOc(Long ocId){
        Set<Long> reportIdSet = new HashSet<Long>();
        List<SysOcReport> ocReports = this.baseMapper.selectList(new QueryWrapper<SysOcReport>().lambda().eq(SysOcReport::getOcId, ocId));
        for(SysOcReport ocReport : ocReports){
            reportIdSet.add(ocReport.getReportId());
        }

        return reportIdSet;
    }

    /**
     * 保存组织报表
     * @param ocId
     * @param rootReportId
     * @param reportIds
     */
    @Override
    @Transactional
    public void saveOrgOcReport(Long ocId,Long rootReportId,String[] reportIds){
        List<SysconfQueryReport> sysReports = sysconfQueryReportService.findSysconfReportsByRootId(String.valueOf(rootReportId));
        //删除模块资源
        List<Long> ids = new ArrayList<Long>();
        sysReports.forEach(sysReport -> {
            ids.add(sysReport.getId());
        });
        this.baseMapper.delete(new QueryWrapper<SysOcReport>().lambda()
                .in(SysOcReport::getReportId, ids)
                .eq(SysOcReport::getOcId,ocId));

        //保存模块资源
        for(String reportId : reportIds){
            if(StringUtils.isEmpty(reportId)){
                continue;
            }
            SysOcReport sysOcReport = new SysOcReport();
            sysOcReport.setOcId(ocId);
            sysOcReport.setReportId(Long.valueOf(reportId));
            this.save(sysOcReport);
        }
    }

    /**
     * 通过OCID 删除
     * @param ocId
     */
    @Override
    @Transactional
    public void deleteOcReportByOc(Long ocId){
        this.baseMapper.delete(new QueryWrapper<SysOcReport>().lambda()
                .eq(SysOcReport::getOcId,ocId));
    }
}
