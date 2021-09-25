package com.handturn.bole.main.equipment.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.main.equipment.service.IMiddlewareInfoService;
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
* 基础资料-中间件管理 View Controller
*
* @author Eric
* @date 2020-12-02 11:13:52
*/
@Slf4j
@Validated
@Controller
public class MiddlewareInfoViewController extends BaseController {

    @Autowired
    private IMiddlewareInfoService middlewareInfoService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/equipment/middlewareInfo")
    @RequiresPermissions("main:equipment:middlewareInfo:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> equipmentInOutOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInOut");
        List<OptionVo> equipmentInfoOnlineStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInfoOnlineStatus");

        model.addAttribute("equipmentInOutOption",equipmentInOutOption);
        model.addAttribute("equipmentInfoOnlineStatusOption",equipmentInfoOnlineStatusOption);
        return FebsUtil.view("main/equipment/middlewareInfo/middlewareInfoList");
    }

}
