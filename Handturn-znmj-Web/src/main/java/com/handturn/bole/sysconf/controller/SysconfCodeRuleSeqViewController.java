package com.handturn.bole.sysconf.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.entity.SysconfCodeRuleSeq;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleSeqService;
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
* 系统配置-编码流水 View Controller
*
* @author MrBird
* @date 2019-12-08 21:44:27
*/
@Slf4j
@Validated
@Controller
public class SysconfCodeRuleSeqViewController extends BaseController {

    @Autowired
    private ISysconfCodeRuleSeqService sysconfCodeRuleSeqService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfCodeRuleSeq")
    @RequiresPermissions("sysconfCodeRuleSeq:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfCodeRuleSeq/sysconfCodeRuleSeqList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfCodeRuleSeq/add")
    @RequiresPermissions("sysconfCodeRuleSeq:add")
    public String add(Model model) {
        //组装新增对象
        SysconfCodeRuleSeq sysconfCodeRuleSeq = new SysconfCodeRuleSeq();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfCodeRuleSeq",sysconfCodeRuleSeq);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfCodeRuleSeq/sysconfCodeRuleSeqEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfCodeRuleSeq/update/{sysconfCodeRuleSeqId}")
    @RequiresPermissions("sysconfCodeRuleSeq:update")
    public String update(@PathVariable Long sysconfCodeRuleSeqId, Model model) {
        SysconfCodeRuleSeq sysconfCodeRuleSeq = sysconfCodeRuleSeqService.findSysconfCodeRuleSeqById(sysconfCodeRuleSeqId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfCodeRuleSeq",sysconfCodeRuleSeq);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfCodeRuleSeq/sysconfCodeRuleSeqEdit");
    }
}
