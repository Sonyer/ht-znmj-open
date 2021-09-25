package com.handturn.bole.master.set.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.master.set.entity.NotifySet;
import com.handturn.bole.master.set.service.INotifySetService;
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
* 智能门禁-通知设置 View Controller
*
* @author Eric
* @date 2020-06-09 18:43:12
*/
@Slf4j
@Validated
@Controller
public class SetNotifyViewController extends BaseController {

    @Autowired
    private INotifySetService notifySetService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/set/notifySet")
    @RequiresPermissions("master:set:notifySet:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");
        List<OptionVo> memberNotifyTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("MemberNotifyType");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        model.addAttribute("memberNotifyTypeOption",memberNotifyTypeOption);
        return FebsUtil.view("master/set/notifySet/notifySetList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/set/notifySet/add")
    @RequiresPermissions("master:set:notifySet:add")
    public String add(Model model) {
        //组装新增对象
        NotifySet notifySet = new NotifySet();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");
        List<OptionVo> memberNotifyTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("MemberNotifyType");

        //传参
        model.addAttribute("notifySet",notifySet);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        model.addAttribute("memberNotifyTypeOption",memberNotifyTypeOption);
        return FebsUtil.view("master/set/notifySet/notifySetEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/set/notifySet/update/{notifySetId}")
    @RequiresPermissions("master:set:notifySet:update")
    public String update(@PathVariable Long notifySetId, Model model) {
        NotifySet notifySet = notifySetService.findNotifySetById(notifySetId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");
        List<OptionVo> memberNotifyTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("MemberNotifyType");

        //传参
        model.addAttribute("notifySet",notifySet);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        model.addAttribute("memberNotifyTypeOption",memberNotifyTypeOption);
        return FebsUtil.view("master/set/notifySet/notifySetEdit");
    }
}
