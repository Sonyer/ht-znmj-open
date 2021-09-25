package com.handturn.bole.main.equipment.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.main.equipment.entity.EquipmentStatusLog;
import com.handturn.bole.main.equipment.service.IEquipmentStatusLogService;
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
* 基础资料-设备状态日志 View Controller
*
* @author Eric
* @date 2020-09-11 08:20:34
*/
@Slf4j
@Validated
@Controller
public class EquipmentStatusLogViewController extends BaseController {

    @Autowired
    private IEquipmentStatusLogService equipmentStatusLogService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "equipmentStatusLog/equipmentStatusLog")
    @RequiresPermissions("equipmentStatusLog:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("equipmentStatusLog/equipmentStatusLog/equipmentStatusLogList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "equipmentStatusLog/equipmentStatusLog/add")
    @RequiresPermissions("equipmentStatusLog:add")
    public String add(Model model) {
        //组装新增对象
        EquipmentStatusLog equipmentStatusLog = new EquipmentStatusLog();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("equipmentStatusLog",equipmentStatusLog);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("equipmentStatusLog/equipmentStatusLog/equipmentStatusLogEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "equipmentStatusLog/equipmentStatusLog/update/{equipmentStatusLogId}")
    @RequiresPermissions("equipmentStatusLog:update")
    public String update(@PathVariable Long equipmentStatusLogId, Model model) {
        EquipmentStatusLog equipmentStatusLog = equipmentStatusLogService.findEquipmentStatusLogById(equipmentStatusLogId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("equipmentStatusLog",equipmentStatusLog);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("equipmentStatusLog/equipmentStatusLog/equipmentStatusLogEdit");
    }
}
