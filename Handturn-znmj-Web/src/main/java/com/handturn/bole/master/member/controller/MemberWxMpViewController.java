package com.handturn.bole.master.member.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.master.member.service.IMemberWxMpService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
* 智能门禁-微信小程序会员 View Controller
*
* @author Eric
* @date 2020-03-16 13:19:19
*/
@Slf4j
@Validated
@Controller
public class MemberWxMpViewController extends BaseController {

    @Autowired
    private IMemberWxMpService memberWxMpService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/member/memberWxMp")
    @RequiresPermissions("master:member:memberWxMp:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> memberStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("MemberStatus");

        model.addAttribute("memberStatusOption",memberStatusOption);
        return FebsUtil.view("master/member/memberWxMp/memberWxMpList");
    }
}
