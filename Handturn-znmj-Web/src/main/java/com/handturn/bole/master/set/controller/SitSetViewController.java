package com.handturn.bole.master.set.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.master.set.entity.SitSet;
import com.handturn.bole.master.set.service.ISitSetService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
* 智能门禁-站点配置 View Controller
*
* @author Eric
* @date 2020-02-28 09:41:18
*/
@Slf4j
@Validated
@Controller
public class SitSetViewController extends BaseController {

    @Autowired
    private ISitSetService sitSetService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/set/sitSet")
    @RequiresPermissions("master:set:sitSet:view")
    public String view(Model model) {
        SitSet sitSet = sitSetService.findlastSitSets();

        if(sitSet == null){
            sitSet = new SitSet();
        }
        model.addAttribute("sitSet",sitSet);
        return FebsUtil.view("master/set/sitSet/sitSetEdit");
    }
}
