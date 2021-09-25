package com.handturn.bole.master.equipment.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.master.equipment.service.IEquipmentModelService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.master.equipment.entity.EquipmentModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 基础资料-设备型号管理 View Controller
*
* @author Eric
* @date 2020-09-11 08:20:08
*/
@Slf4j
@Validated
@Controller
public class EquipmentModelViewController extends BaseController {

    @Autowired
    private IEquipmentModelService equipmentModelService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/equipment/equipmentModel")
    @RequiresPermissions("master:equipment:equipmentModel:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> equipmentModelTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentModelType");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("equipmentModelTypeOption",equipmentModelTypeOption);
        return FebsUtil.view("master/equipment/equipmentModel/equipmentModelList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/equipment/equipmentModel/add")
    @RequiresPermissions("master:equipment:equipmentModel:add")
    public String add(Model model) {
        //组装新增对象
        EquipmentModel equipmentModel = new EquipmentModel();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> equipmentModelTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentModelType");

        //传参
        model.addAttribute("equipmentModel",equipmentModel);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("equipmentModelTypeOption",equipmentModelTypeOption);
        return FebsUtil.view("master/equipment/equipmentModel/equipmentModelEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/equipment/equipmentModel/update/{equipmentModelId}")
    @RequiresPermissions("master:equipment:equipmentModel:update")
    public String update(@PathVariable Long equipmentModelId, Model model) {
        EquipmentModel equipmentModel = equipmentModelService.findEquipmentModelById(equipmentModelId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> equipmentModelTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentModelType");

        //传参
        model.addAttribute("equipmentModel",equipmentModel);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("equipmentModelTypeOption",equipmentModelTypeOption);
        return FebsUtil.view("master/equipment/equipmentModel/equipmentModelEdit");
    }
}
