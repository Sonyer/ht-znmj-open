package com.handturn.bole.sysconf.service.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.constant.SysconfGlobalConstant;
import com.handturn.bole.sysconf.entity.SysconfPrint;
import com.handturn.bole.sysconf.entity.SysconfPrintReport;
import com.handturn.bole.sysconf.mapper.SysconfPrintMapper;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
import com.handturn.bole.sysconf.service.ISysconfPrintReportService;
import com.handturn.bole.sysconf.service.ISysconfPrintService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

/**
 * 系统配置-打印配置 Service实现
 *
 * @author Eric
 * @date 2020-01-31 09:15:26
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysconfPrintServiceImpl extends ServiceImpl<SysconfPrintMapper, SysconfPrint> implements ISysconfPrintService {

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Autowired
    private ISysconfGlobalParamService sysconfGlobalParamService;

    @Autowired
    private ISysconfPrintReportService sysconfPrintReportService;

    @Override
    public ICustomPage<SysconfPrint> findSysconfPrints(QueryRequest request, SysconfPrint sysconfPrint) {
        CustomPage<SysconfPrint> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysconfPrint);
    }

    @Override
    @Transactional
    public SysconfPrint saveSysconfPrint(SysconfPrint sysconfPrint) {
        if(sysconfPrint.getId() == null){
            sysconfPrint.setStatus(BaseStatus.ENABLED);
            this.save(sysconfPrint);
            return sysconfPrint;
        }else{
            SysconfPrint sysconfPrintOld = this.baseMapper.selectById(sysconfPrint.getId());
            CopyUtils.copyProperties(sysconfPrint,sysconfPrintOld);
            this.updateById(sysconfPrintOld);
            return sysconfPrintOld;
        }
    }

    @Override
    @Transactional
    public void enableSysconfPrint(String[] sysconfPrintIds) {
        Arrays.stream(sysconfPrintIds).forEach(sysconfPrintId -> {
            SysconfPrint sysconfPrint = this.baseMapper.selectById(sysconfPrintId);
            sysconfPrint.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysconfPrint);
        });
	}

    @Override
    @Transactional
    public void disableSysconfPrint(String[] sysconfPrintIds) {
        Arrays.stream(sysconfPrintIds).forEach(sysconfPrintId -> {
            SysconfPrint sysconfPrint = this.baseMapper.selectById(sysconfPrintId);
            sysconfPrint.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysconfPrint);
        });
    }

    @Override
    public SysconfPrint findSysconfPrintById(Long sysconfPrintId){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfPrint>().lambda().eq(SysconfPrint::getId, sysconfPrintId));
    }

    /**
     * 导入
     * @param uploadFile
     * @return
     */
    public ImportResultBean importUpload(MultipartFile uploadFile){
        ImportResultBean resultBean = new ImportResultBean();
        // 获取临时上传文件目录
        String tempPath = sysconfGlobalParamService.getParamValueWithDiffSystem(SysconfGlobalConstant.SysconfGlobalGroupCode.TEMPLET_PRINTE_FILE_PATH,
                UserInfoHolder.getUserInfo().getCurrentOc().getOcCode(),UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());

        // 新建一个文件
        String originalFileName = uploadFile.getOriginalFilename();
        originalFileName.replaceAll("/", "\\");

        String fileName = originalFileName.substring(originalFileName.lastIndexOf("\\")+1);
        String fileName_begin = fileName.replace(".jasper","");
        String filePath = tempPath + fileName_begin+"-"+System.currentTimeMillis()+".jasper";
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            //将上传的文件写入新建的文件中
            uploadFile.transferTo(dest);

            resultBean.setSuccess(true);
            resultBean.setFileName(fileName);
            resultBean.setFilePath(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setSuccess(false);
            resultBean.setReturnInfo("Import Progress exception! "+e.getMessage());
        } finally {

        }
        return resultBean;
    }

    @Override
    public SysconfPrint findSysconfPrintByPrintCode(String printCode){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfPrint>().lambda().eq(SysconfPrint::getPrintCode, printCode));
    }

    /**
     * 报表生成返回页面
     * @param printCode
     * @param orgCode
     * @param warehouseCode
     * @param clientCode
     * @return
     */
    @Override
    public JasperPrint generatePrintReport(String printCode, String orgCode, String warehouseCode, String clientCode,Map<String,Object> params){
        SysconfPrintReport sysconfPrintReport = null;
        if(!StringUtils.isEmpty(clientCode)){
            sysconfPrintReport = sysconfPrintReportService.findSysconfPrintReportByPrintInfo(printCode,null,null,clientCode);
        }
        if(sysconfPrintReport == null && !StringUtils.isEmpty(warehouseCode)){
            sysconfPrintReport = sysconfPrintReportService.findSysconfPrintReportByPrintInfo(printCode,null,warehouseCode,null);
        }
        if(sysconfPrintReport == null && !StringUtils.isEmpty(orgCode)){
            sysconfPrintReport = sysconfPrintReportService.findSysconfPrintReportByPrintInfo(printCode,orgCode,null,null);
        }
        if(sysconfPrintReport == null){
            sysconfPrintReport = sysconfPrintReportService.findSysconfPrintReportByPrintInfo(printCode,null,null,null);
        }

        if(sysconfPrintReport == null){
            throw new FebsException("打印模板未维护!");
        }

        Connection conn = null;
        try {
            DataSource quartz = dynamicRoutingDataSource.getDataSource("quartz");
            conn = quartz.getConnection();
            File file = new File(sysconfPrintReport.getFilePath().replace("file:", ""));
            InputStream in = new FileInputStream(file);
            JasperPrint jasperPrint = JasperFillManager.fillReport(in, params, conn);
           /* OutputStream output = new FileOutputStream(new File("d:/JasperReport.pdf"));
            JasperExportManager.exportReportToPdfStream(jasperPrint, output);*/
            return jasperPrint;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FebsException("打印模板调用失败!");
        } catch (JRException e) {
            e.printStackTrace();
            throw new FebsException("打印模板调用失败!");
        } catch (SQLException e){
            e.printStackTrace();
            throw new FebsException("打印模板调用失败!");
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
