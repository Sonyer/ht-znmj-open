package com.handturn.bole.sysconf.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfPrint;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 系统配置-打印配置 Service接口
 *
 * @author Eric
 * @date 2020-01-31 09:15:26
 */
public interface ISysconfPrintService extends IService<SysconfPrint> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysconfPrint sysconfPrint
     * @return ICustomPage<SysconfPrint>
     */
    ICustomPage<SysconfPrint> findSysconfPrints(QueryRequest request, SysconfPrint sysconfPrint);

    /**
     * 修改
     *
     * @param sysconfPrint sysconfPrint
     */
    SysconfPrint saveSysconfPrint(SysconfPrint sysconfPrint);

    /**
     * 启用
     *
     * @param sysconfPrintIds sysconfPrintIds
     */
    void enableSysconfPrint(String[] sysconfPrintIds);

    /**
    * 禁用
    *
    * @param sysconfPrintIds sysconfPrintIds
    */
    void disableSysconfPrint(String[] sysconfPrintIds);


    /**
    * 通过Id获取信息
    * @param sysconfPrintId
    * @return
    */
    SysconfPrint findSysconfPrintById(Long sysconfPrintId);


    /**
     * 导入
     * @param uploadFile
     * @return
     */
    ImportResultBean importUpload(MultipartFile uploadFile);

    SysconfPrint findSysconfPrintByPrintCode(String printCode);


    /**
     * 报表生成返回页面
     * @param printCode
     * @param orgCode
     * @param warehouseCode
     * @param clientCode
     * @return
     */
    JasperPrint generatePrintReport(String printCode, String orgCode, String warehouseCode, String clientCode, Map<String,Object> params);
}
