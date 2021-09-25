package com.handturn.bole.sysconf.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.entity.SysconfDictCode;
import com.handturn.bole.sysconf.entity.SysconfPrint;
import com.handturn.bole.sysconf.entity.SysconfPrintReport;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.sysconf.service.ISysconfPrintReportService;
import com.handturn.bole.sysconf.service.ISysconfPrintService;
import com.handturn.bole.system.entity.OcType;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysOrganizationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
* 系统配置-打印配置 View Controller
*
* @author Eric
* @date 2020-01-31 09:15:26
*/
@Slf4j
@Validated
@Controller
public class SysconfPrintViewController extends BaseController {

    @Autowired
    private ISysconfPrintService sysconfPrintService;

    @Autowired
    private ISysconfPrintReportService sysconfPrintReportService;

    @Autowired
    private ISysconfDictCodeService sysconfDictCodeService;

    @Autowired
    private ISysOrganizationService sysOrganizationService;

    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfPrint")
    @RequiresPermissions("sysconfPrint:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        return FebsUtil.view("sysconf/sysconfPrint/sysconfPrintList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfPrint/add")
    @RequiresPermissions("sysconfPrint:add")
    public String add(Model model) {
        //组装新增对象
        SysconfPrint sysconfPrint = new SysconfPrint();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> orgsOption = sysOrganizationService.getOrgOptionVoByCurrentUser(UserInfoHolder.getUserInfo());


        //传参
        model.addAttribute("sysconfPrint",sysconfPrint);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("orgsOption",orgsOption);
        return FebsUtil.view("sysconf/sysconfPrint/sysconfPrintEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfPrint/update/{sysconfPrintId}")
    @RequiresPermissions("sysconfPrint:update")
    public String update(@PathVariable Long sysconfPrintId, Model model) {
        SysconfPrint sysconfPrint = sysconfPrintService.findSysconfPrintById(sysconfPrintId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfPrint",sysconfPrint);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfPrint/sysconfPrintEdit");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfPrint/addPrintReport/{printCode}")
    @RequiresPermissions("sysconfPrint:add")
    public String addPrintReport(@PathVariable String printCode,Model model) {
        //组装新增对象
        SysconfPrintReport sysconfPrintReport = new SysconfPrintReport();
        sysconfPrintReport.setPrintCode(printCode);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> orgsOption = sysOrganizationService.getOrgCodeKeyOptionVoForAll();
        List<String> ocTypes = new ArrayList<String>();
        ocTypes.add(OcType.SYS);
        ocTypes.add(OcType.ORG_OC);
        ocTypes.add(OcType.ORG_CN);
        List<OptionVo> warehousesOption = sysOrganizationOcService.getOrgOcKeyOptionVoByOcType(ocTypes);
        List<String> thOcTypes = new ArrayList<String>();
        thOcTypes.add(OcType.ORG_3TH);
        List<OptionVo> clientsOption = sysOrganizationOcService.getOrgOcKeyOptionVoByOcType(thOcTypes);


        //传参
        model.addAttribute("sysconfPrintReport",sysconfPrintReport);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("orgsOption",orgsOption);
        model.addAttribute("warehousesOption",warehousesOption);
        model.addAttribute("clientsOption",clientsOption);
        return FebsUtil.view("sysconf/sysconfPrint/sysconfPrintReportEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfPrint/updatePrintReport/{sysconfPrintReportId}")
    @RequiresPermissions("sysconfPrint:update")
    public String updatePrintReport(@PathVariable Long sysconfPrintReportId, Model model) {
        SysconfPrintReport sysconfPrintReport = sysconfPrintReportService.findSysconfPrintReportById(sysconfPrintReportId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> orgsOption = sysOrganizationService.getOrgCodeKeyOptionVoForAll();
        List<String> ocTypes = new ArrayList<String>();
        ocTypes.add(OcType.SYS);
        ocTypes.add(OcType.ORG_OC);
        ocTypes.add(OcType.ORG_CN);
        List<OptionVo> warehousesOption = sysOrganizationOcService.getOrgOcKeyOptionVoByOcType(ocTypes);
        List<String> thOcTypes = new ArrayList<String>();
        thOcTypes.add(OcType.ORG_3TH);
        List<OptionVo> clientsOption = sysOrganizationOcService.getOrgOcKeyOptionVoByOcType(thOcTypes);

        //传参
        model.addAttribute("sysconfPrintReport",sysconfPrintReport);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("orgsOption",orgsOption);
        model.addAttribute("warehousesOption",warehousesOption);
        model.addAttribute("clientsOption",clientsOption);
        return FebsUtil.view("sysconf/sysconfPrint/sysconfPrintReportEdit");
    }
}
