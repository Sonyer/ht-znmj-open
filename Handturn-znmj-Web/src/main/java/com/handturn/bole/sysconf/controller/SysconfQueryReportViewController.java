package com.handturn.bole.sysconf.controller;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.entity.QueryReportType;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.entity.SysconfQueryReportColumn;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.sysconf.service.ISysconfQueryReportColumnService;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.sql.DataSource;
import java.util.*;

/**
* 系统-报表 View Controller
*
* @author Eric
* @date 2020-02-12 09:06:44
*/
@Slf4j
@Validated
@Controller
public class SysconfQueryReportViewController extends BaseController {
    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;

    @Autowired
    private ISysconfDictCodeService sysconfDictCodeService;
    @Autowired
    private ISysconfQueryReportColumnService sysconfQueryReportColumnService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfQueryReport")
    @RequiresPermissions("sysconfQueryReport:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> queryReportTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("QueryReportType");
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        model.addAttribute("queryReportTypeOption",queryReportTypeOption);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfQueryReport/sysconfQueryReportList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfQueryReport/add/{parentSysconfQueryReportId}")
    @RequiresPermissions("sysconfQueryReport:add")
    public String add(@PathVariable Long parentSysconfQueryReportId,Model model) {
        SysconfQueryReport parentReport = sysconfQueryReportService.findSysconfQueryReportById(parentSysconfQueryReportId);

        //组装新增对象
        SysconfQueryReport sysconfQueryReport = new SysconfQueryReport();
        sysconfQueryReport.setParentReportId(parentSysconfQueryReportId);
        sysconfQueryReport.setParentReportName(parentReport == null?"":parentReport.getReportName());

        //获取数据字典
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");
        List<OptionVo> queryReportColumnTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("QueryReportColumnType");

        Map<String, DataSource> dataSourceMap = dynamicRoutingDataSource.getCurrentDataSources();
        List<OptionVo> dataSourceOption = new ArrayList<OptionVo>();
        dataSourceMap.keySet().forEach(dataSourceKey ->{
            OptionVo optionVo = new OptionVo();
            optionVo.setText(dataSourceKey);
            optionVo.setValue(dataSourceKey);
            dataSourceOption.add(optionVo);
        });

        List<OptionVo> queryReportTypeOption = null;
        if(sysconfQueryReport.getParentReportId() == 0){
            sysconfQueryReport.setReportNodeType(QueryReportType.REPORT_ROOT);
            sysconfQueryReport.setRootReportId(0L);
            sysconfQueryReport.setRootReportName("");

            Set<String> contens = new HashSet<String>();
            contens.add(QueryReportType.REPORT_ROOT);
            queryReportTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeContains("QueryReportType",contens);
        }else{
            SysconfQueryReport rootSysconfQueryReport = sysconfQueryReportService.findSysconfQueryReportById(parentReport.getRootReportId());
            sysconfQueryReport.setRootReportId(parentReport.getRootReportId());
            sysconfQueryReport.setRootReportName(rootSysconfQueryReport == null?"":rootSysconfQueryReport.getReportName());
            Set<String> contens = new HashSet<String>();
            contens.add(QueryReportType.REPORT_ROOT);
            queryReportTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeExit("QueryReportType",contens);
        }

        //传参
        model.addAttribute("sysconfQueryReport",sysconfQueryReport);
        model.addAttribute("queryReportTypeOption",queryReportTypeOption);
        model.addAttribute("dataSourceOption",dataSourceOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        model.addAttribute("queryReportColumnTypeOption",queryReportColumnTypeOption);
        return FebsUtil.view("sysconf/sysconfQueryReport/sysconfQueryReportEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfQueryReport/update/{sysconfQueryReportId}")
    @RequiresPermissions("sysconfQueryReport:update")
    public String update(@PathVariable Long sysconfQueryReportId, Model model) {
        SysconfQueryReport sysconfQueryReport = sysconfQueryReportService.findSysconfQueryReportById(sysconfQueryReportId);
        SysconfQueryReport parentReport = sysconfQueryReportService.findSysconfQueryReportById(sysconfQueryReport.getParentReportId());
        sysconfQueryReport.setParentReportName(parentReport == null?"":parentReport.getParentReportName());

        //获取数据字典
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");
        List<OptionVo> queryReportColumnTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("QueryReportColumnType");
        List<OptionVo> queryReportColumnQueryMethodOption = sysconfDictCodeService.getDictOptionVoByTypeCode("QueryReportColumnQueryMethod");

        Map<String, DataSource> dataSourceMap = dynamicRoutingDataSource.getCurrentDataSources();
        List<OptionVo> dataSourceOption = new ArrayList<OptionVo>();
        dataSourceMap.keySet().forEach(dataSourceKey ->{
            OptionVo optionVo = new OptionVo();
            optionVo.setText(dataSourceKey);
            optionVo.setValue(dataSourceKey);
            dataSourceOption.add(optionVo);
        });

        List<OptionVo> queryReportTypeOption = null;
        if(sysconfQueryReport.getParentReportId() == 0){
            sysconfQueryReport.setReportNodeType(QueryReportType.REPORT_ROOT);
            sysconfQueryReport.setRootReportId(0L);
            sysconfQueryReport.setRootReportName("");

            Set<String> contens = new HashSet<String>();
            contens.add(QueryReportType.REPORT_ROOT);
            queryReportTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeContains("QueryReportType",contens);
        }else{
            SysconfQueryReport rootReport = sysconfQueryReportService.findSysconfQueryReportById(parentReport.getRootReportId());
            sysconfQueryReport.setRootReportId(parentReport.getRootReportId());
            sysconfQueryReport.setRootReportName(rootReport == null?"":rootReport.getReportName());
            Set<String> contens = new HashSet<String>();
            contens.add(QueryReportType.REPORT_ROOT);
            queryReportTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeExit("QueryReportType",contens);
        }

        //传参
        model.addAttribute("sysconfQueryReport",sysconfQueryReport);
        model.addAttribute("queryReportTypeOption",queryReportTypeOption);
        model.addAttribute("dataSourceOption",dataSourceOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        model.addAttribute("queryReportColumnTypeOption",queryReportColumnTypeOption);
        model.addAttribute("queryReportColumnQueryMethodOption",queryReportColumnQueryMethodOption);
        return FebsUtil.view("sysconf/sysconfQueryReport/sysconfQueryReportEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfQueryReportColumn/update/{sysconfQueryReportColumnId}")
    @RequiresPermissions("sysconfQueryReport:update")
    public String updateQueryReportColumn(@PathVariable Long sysconfQueryReportColumnId, Model model) {
        SysconfQueryReportColumn sysconfQueryReportColumn = sysconfQueryReportColumnService.findSysconfQueryReportColumnById(sysconfQueryReportColumnId);

        //获取数据字典
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");
        List<OptionVo> queryReportColumnTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("QueryReportColumnType");
        List<OptionVo> queryReportColumnQueryMethodOption = sysconfDictCodeService.getDictOptionVoByTypeCode("QueryReportColumnQueryMethod");

        //传参
        model.addAttribute("sysconfQueryReportColumn",sysconfQueryReportColumn);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        model.addAttribute("queryReportColumnTypeOption",queryReportColumnTypeOption);
        model.addAttribute("queryReportColumnQueryMethodOption",queryReportColumnQueryMethodOption);
        return FebsUtil.view("sysconf/sysconfQueryReport/sysconfQueryReportColumnEdit");
    }
}
