package com.handturn.bole.sysconf.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.entity.SysconfCodeRule;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
* 系统配置-编码规则 View Controller
*
* @author MrBird
* @date 2019-12-08 21:25:21
*/
@Slf4j
@Validated
@Controller
public class SysconfCodeRuleViewController extends BaseController {

    @Autowired
    private ISysconfCodeRuleService sysconfCodeRuleService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfCodeRule")
    @RequiresPermissions("sysconfCodeRule:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        return FebsUtil.view("sysconf/sysconfCodeRule/sysconfCodeRuleList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfCodeRule/add")
    @RequiresPermissions("sysconfCodeRule:add")
    public String add(Model model) {
        //组装新增对象
        SysconfCodeRule sysconfCodeRule = new SysconfCodeRule();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfCodeRule",sysconfCodeRule);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfCodeRule/sysconfCodeRuleEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfCodeRule/update/{sysconfCodeRuleId}")
    @RequiresPermissions("sysconfCodeRule:update")
    public String update(@PathVariable Long sysconfCodeRuleId, Model model) {
        SysconfCodeRule sysconfCodeRule = sysconfCodeRuleService.findSysconfCodeRuleById(sysconfCodeRuleId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfCodeRule",sysconfCodeRule);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfCodeRule/sysconfCodeRuleEdit");
    }
}
