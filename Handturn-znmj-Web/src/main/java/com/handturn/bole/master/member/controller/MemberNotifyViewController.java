package com.handturn.bole.master.member.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.master.member.entity.MemberNotify;
import com.handturn.bole.master.member.service.IMemberNotifyService;
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
* 智能门禁-会员通知 View Controller
*
* @author Eric
* @date 2020-05-19 09:22:40
*/
@Slf4j
@Validated
@Controller
public class MemberNotifyViewController extends BaseController {

    @Autowired
    private IMemberNotifyService memberNotifyService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/member/memberNotify")
    @RequiresPermissions("master:member:memberNotify:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("master/member/memberNotify/memberNotifyList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/member/memberNotify/add")
    @RequiresPermissions("master:member:memberNotify:add")
    public String add(Model model) {
        //组装新增对象
        MemberNotify memberNotify = new MemberNotify();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("memberNotify",memberNotify);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("master/member/memberNotify/memberNotifyEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/member/memberNotify/update/{memberNotifyId}")
    @RequiresPermissions("master:member:memberNotify:update")
    public String update(@PathVariable Long memberNotifyId, Model model) {
        MemberNotify memberNotify = memberNotifyService.findMemberNotifyById(memberNotifyId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("memberNotify",memberNotify);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("master/member/memberNotify/memberNotifyEdit");
    }
}
