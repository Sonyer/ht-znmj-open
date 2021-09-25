package com.handturn.bole.master.member.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.master.member.entity.MemberVisitorInfo;
import com.handturn.bole.master.member.service.IMemberVisitorInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 会员-会员访客信息 View Controller
*
* @author Eric
* @date 2020-09-22 08:57:18
*/
@Slf4j
@Validated
@Controller
public class MemberVisitorInfoViewController extends BaseController {

    @Autowired
    private IMemberVisitorInfoService memberVisitorInfoService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "memberVisitorInfo/memberVisitorInfo")
    @RequiresPermissions("memberVisitorInfo:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("memberVisitorInfo/memberVisitorInfo/memberVisitorInfoList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "memberVisitorInfo/memberVisitorInfo/add")
    @RequiresPermissions("memberVisitorInfo:add")
    public String add(Model model) {
        //组装新增对象
        MemberVisitorInfo memberVisitorInfo = new MemberVisitorInfo();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("memberVisitorInfo",memberVisitorInfo);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("memberVisitorInfo/memberVisitorInfo/memberVisitorInfoEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "memberVisitorInfo/memberVisitorInfo/update/{memberVisitorInfoId}")
    @RequiresPermissions("memberVisitorInfo:update")
    public String update(@PathVariable Long memberVisitorInfoId, Model model) {
        MemberVisitorInfo memberVisitorInfo = memberVisitorInfoService.findMemberVisitorInfoById(memberVisitorInfoId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("memberVisitorInfo",memberVisitorInfo);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("memberVisitorInfo/memberVisitorInfo/memberVisitorInfoEdit");
    }
}
