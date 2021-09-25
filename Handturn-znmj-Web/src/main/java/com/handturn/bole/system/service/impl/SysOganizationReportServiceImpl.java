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
import com.handturn.bole.system.entity.SysOganizationReport;
import com.handturn.bole.system.entity.SysOganizationResource;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.mapper.SysOganizationReportMapper;
import com.handturn.bole.system.service.ISysOganizationReportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统-组织报表 Service实现
 *
 * @author Eric
 * @date 2020-02-16 20:08:44
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysOganizationReportServiceImpl extends ServiceImpl<SysOganizationReportMapper, SysOganizationReport> implements ISysOganizationReportService {

    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;

    @Override
    public ICustomPage<SysOganizationReport> findSysOganizationReports(QueryRequest request, SysOganizationReport sysOganizationReport) {
        CustomPage<SysOganizationReport> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysOganizationReport);
    }

    @Override
    @Transactional
    public SysOganizationReport saveSysOganizationReport(SysOganizationReport sysOganizationReport) {
        if(sysOganizationReport.getId() == null){
            this.save(sysOganizationReport);
            return sysOganizationReport;
        }else{
            SysOganizationReport sysOganizationReportOld = this.baseMapper.selectById(sysOganizationReport.getId());
            CopyUtils.copyProperties(sysOganizationReport,sysOganizationReportOld);
            this.updateById(sysOganizationReportOld);
            return sysOganizationReportOld;
        }
    }

    /**
     * 获取组报表
     * @param orgId
     * @return
     */
    @Override
    public Set<Long> findSysReportIds4Org(Long orgId){
        Set<Long> reportIdSet = new HashSet<Long>();
        List<SysOganizationReport> orgReports = this.baseMapper.selectList(new QueryWrapper<SysOganizationReport>().lambda().eq(SysOganizationReport::getOrgId, orgId));
        for(SysOganizationReport orgReport : orgReports){
            reportIdSet.add(orgReport.getReportId());
        }
        return reportIdSet;
    }

    /**
     * 保存组织报表
     * @param orgId
     * @param rootReportId
     * @param reportIds
     */
    @Override
    @Transactional
    public void saveOrgReport(Long orgId,Long rootReportId,String[] reportIds){
        List<SysconfQueryReport> sysReports = sysconfQueryReportService.findSysconfReportsByRootId(String.valueOf(rootReportId));
        //删除模块资源
        List<Long> ids = new ArrayList<Long>();
        sysReports.forEach(sysReport -> {
            ids.add(sysReport.getId());
        });
        this.baseMapper.delete(new QueryWrapper<SysOganizationReport>().lambda()
                .in(SysOganizationReport::getReportId, ids)
                .eq(SysOganizationReport::getOrgId,orgId));

        //保存模块资源
        for(String reportId : reportIds){
            if(StringUtils.isEmpty(reportId)){
                continue;
            }
            SysOganizationReport sysOganizationReport = new SysOganizationReport();
            sysOganizationReport.setOrgId(orgId);
            sysOganizationReport.setReportId(Long.valueOf(reportId));
            this.save(sysOganizationReport);
        }
    }

    @Override
    public SysOganizationReport findSysOganizationReportById(Long sysOganizationReportId){
        return this.baseMapper.selectOne(new QueryWrapper<SysOganizationReport>().lambda().eq(SysOganizationReport::getId, sysOganizationReportId));
    }
}
