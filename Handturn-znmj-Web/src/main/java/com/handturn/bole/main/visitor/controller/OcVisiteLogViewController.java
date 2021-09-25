package com.handturn.bole.main.visitor.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.main.visitor.entity.OcVisiteLog;
import com.handturn.bole.main.visitor.service.IOcVisiteLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 网点-会员访客日志 View Controller
*
* @author Eric
* @date 2020-12-06 17:24:27
*/
@Slf4j
@Validated
@Controller
public class OcVisiteLogViewController extends BaseController {

    @Autowired
    private IOcVisiteLogService ocVisiteLogService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/visitor/ocVisiteLog")
    @RequiresPermissions("main:visitor:ocVisiteLog:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> equipmentInOutOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInOut");
        List<OptionVo> ocVisiteLogFlagOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisiteLogFlag");
        List<OptionVo> ocVisiteLogOpenStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisiteLogOpenStatus");

        model.addAttribute("equipmentInOutOption",equipmentInOutOption);
        model.addAttribute("ocVisiteLogFlagOption",ocVisiteLogFlagOption);
        model.addAttribute("ocVisiteLogOpenStatusOption",ocVisiteLogOpenStatusOption);
        return FebsUtil.view("main/visitor/ocVisiteLog/ocVisiteLogList");
    }
}
