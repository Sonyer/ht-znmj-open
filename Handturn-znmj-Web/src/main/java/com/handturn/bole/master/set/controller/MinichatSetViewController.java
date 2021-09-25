package com.handturn.bole.master.set.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.master.set.entity.MinichatSet;
import com.handturn.bole.master.set.service.IMinichatSetService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
* 智能门禁-小程序参数配置 View Controller
*
* @author Eric
* @date 2020-02-28 11:05:13
*/
@Slf4j
@Validated
@Controller
public class MinichatSetViewController extends BaseController {

    @Autowired
    private IMinichatSetService minichatSetService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/set/minichatSet")
    @RequiresPermissions("master:set:minichatSet:view")
    public String view(Model model) {
        MinichatSet minichatSet = minichatSetService.findlastMinichatSets();

        if(minichatSet == null){
            minichatSet = new MinichatSet();
        }
        model.addAttribute("minichatSet",minichatSet);
        return FebsUtil.view("master/set/minichatSet/minichatSetEdit");

    }
}
