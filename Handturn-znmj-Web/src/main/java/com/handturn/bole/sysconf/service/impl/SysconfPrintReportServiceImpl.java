package com.handturn.bole.sysconf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.entity.SysconfPrint;
import com.handturn.bole.sysconf.entity.SysconfPrintReport;
import com.handturn.bole.sysconf.mapper.SysconfPrintReportMapper;
import com.handturn.bole.sysconf.service.ISysconfPrintReportService;
import com.handturn.bole.sysconf.service.ISysconfPrintService;
import com.handturn.bole.system.entity.SysOrganization;
import com.handturn.bole.system.entity.SysOrganizationOc;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;

/**
 * 系统配置-打印模板 Service实现
 *
 * @author Eric
 * @date 2020-01-31 09:19:11
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysconfPrintReportServiceImpl extends ServiceImpl<SysconfPrintReportMapper, SysconfPrintReport> implements ISysconfPrintReportService {

    @Autowired
    private ISysconfPrintService sysconfPrintService;

    @Autowired
    private ISysOrganizationService sysOrganizationService;

    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;

    @Override
    public ICustomPage<SysconfPrintReport> findSysconfPrintReports(QueryRequest request, SysconfPrintReport sysconfPrintReport) {
        CustomPage<SysconfPrintReport> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysconfPrintReport);
    }

    @Override
    @Transactional
    public SysconfPrintReport saveSysconfPrintReport(SysconfPrintReport sysconfPrintReport) {
        if(!StringUtils.isEmpty(sysconfPrintReport.getOrgCode())){
            SysOrganization org = sysOrganizationService.findSysOrganizationByCode(sysconfPrintReport.getOrgCode());
            sysconfPrintReport.setOrgName(org.getOrgName());
        }
        if(!StringUtils.isEmpty(sysconfPrintReport.getWarehouseCode())){
            SysOrganizationOc warehouse = sysOrganizationOcService.findSysOrganizationOcByCode(sysconfPrintReport.getWarehouseCode());
            sysconfPrintReport.setWarehouseName(warehouse.getOcName());
        }
        if(!StringUtils.isEmpty(sysconfPrintReport.getClientCode())){
            SysOrganizationOc client = sysOrganizationOcService.findSysOrganizationOcByCode(sysconfPrintReport.getClientCode());
            sysconfPrintReport.setClientName(client.getOcName());
        }

        if(sysconfPrintReport.getId() == null){
            SysconfPrint sysconfPrint = sysconfPrintService.findSysconfPrintByPrintCode(sysconfPrintReport.getPrintCode());
            if(sysconfPrint == null){
                throw new FebsException("打印配置未找到!");
            }
            sysconfPrintReport.setPrintName(sysconfPrint.getPrintName());
            sysconfPrintReport.setStatus(BaseStatus.ENABLED);
            this.save(sysconfPrintReport);
            return sysconfPrintReport;
        }else{
            SysconfPrintReport sysconfPrintReportOld = this.baseMapper.selectById(sysconfPrintReport.getId());
            CopyUtils.copyProperties(sysconfPrintReport,sysconfPrintReportOld);
            this.updateById(sysconfPrintReportOld);
            return sysconfPrintReportOld;
        }
    }

    @Override
    @Transactional
    public void enableSysconfPrintReport(String[] sysconfPrintReportIds) {
        Arrays.stream(sysconfPrintReportIds).forEach(sysconfPrintReportId -> {
            SysconfPrintReport sysconfPrintReport = this.baseMapper.selectById(sysconfPrintReportId);
            sysconfPrintReport.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysconfPrintReport);
        });
	}

    @Override
    @Transactional
    public void disableSysconfPrintReport(String[] sysconfPrintReportIds) {
        Arrays.stream(sysconfPrintReportIds).forEach(sysconfPrintReportId -> {
            SysconfPrintReport sysconfPrintReport = this.baseMapper.selectById(sysconfPrintReportId);
            sysconfPrintReport.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysconfPrintReport);
        });
    }

    @Override
    public SysconfPrintReport findSysconfPrintReportById(Long sysconfPrintReportId){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfPrintReport>().lambda().eq(SysconfPrintReport::getId, sysconfPrintReportId));
    }

    /**
     * 通过相关信息获取报表模板
     * @param printCode
     * @return
     */
    @Override
    public SysconfPrintReport findSysconfPrintReportByPrintInfo(String printCode,String orgCode,String warehouseCode,String clientCode){
        QueryWrapper<SysconfPrintReport> queryWrapper = new QueryWrapper<SysconfPrintReport>();
        queryWrapper.lambda().eq(SysconfPrintReport::getPrintCode,printCode);
        if(!StringUtils.isEmpty(orgCode)){
            queryWrapper.lambda().eq(SysconfPrintReport::getOrgCode,orgCode);
        }
        if(!StringUtils.isEmpty(warehouseCode)){
            queryWrapper.lambda().eq(SysconfPrintReport::getWarehouseCode,warehouseCode);
        }
        if(!StringUtils.isEmpty(clientCode)){
            queryWrapper.lambda().eq(SysconfPrintReport::getClientCode,clientCode);
        }
        return this.baseMapper.selectOne(queryWrapper);
    }
}
