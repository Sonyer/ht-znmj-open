package com.handturn.bole.main.visitor.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.main.visitor.service.IOcVisitorApplyService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.main.visitor.entity.OcVisitorApply;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 网点-会员访客审核 View Controller
*
* @author Eric
* @date 2020-09-22 08:57:57
*/
@Slf4j
@Validated
@Controller
public class OcVisitorApplyViewController extends BaseController {

    @Autowired
    private IOcVisitorApplyService ocVisitorApplyService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/visitor/ocVisitorApply")
    @RequiresPermissions("main:visitor:ocVisitorApply:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> ocVisitorTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorType");
        List<OptionVo> ocVisitorAuditStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorAuditStatus");


        model.addAttribute("ocVisitorTypeOption",ocVisitorTypeOption);
        model.addAttribute("ocVisitorAuditStatusOption",ocVisitorAuditStatusOption);
        return FebsUtil.view("main/visitor/ocVisitorApply/ocVisitorApplyList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/visitor/ocVisitorApply/audit/{ocVisitorApplyId}")
    @RequiresPermissions("main:visitor:ocVisitorApply:audit")
    public String audit(@PathVariable Long ocVisitorApplyId, Model model) {
        OcVisitorApply ocVisitorApply = ocVisitorApplyService.findOcVisitorApplyById(ocVisitorApplyId);

        //获取数据字典
        List<OptionVo> ocVisitorTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorType");
        List<OptionVo> ocVisitorAuditStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorAuditStatus");

        //传参
        model.addAttribute("ocVisitorApply",ocVisitorApply);
        model.addAttribute("ocVisitorTypeOption",ocVisitorTypeOption);
        model.addAttribute("ocVisitorAuditStatusOption",ocVisitorAuditStatusOption);
        return FebsUtil.view("main/visitor/ocVisitorApply/ocVisitorApplyEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/visitor/ocVisitorApply/audits/{ocVisitorApplyIds}")
    @RequiresPermissions("main:visitor:ocVisitorApply:audit")
    public String audits(@PathVariable String ocVisitorApplyIds, Model model) {
        OcVisitorApply ocVisitorApply = new OcVisitorApply();
        ocVisitorApply.setIds(ocVisitorApplyIds);

        //获取数据字典
        List<OptionVo> ocVisitorTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorType");
        List<OptionVo> ocVisitorAuditStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorAuditStatus");

        //传参
        model.addAttribute("ocVisitorApply",ocVisitorApply);
        model.addAttribute("ocVisitorTypeOption",ocVisitorTypeOption);
        model.addAttribute("ocVisitorAuditStatusOption",ocVisitorAuditStatusOption);
        return FebsUtil.view("main/visitor/ocVisitorApply/ocVisitorApplyAudits");
    }
}
