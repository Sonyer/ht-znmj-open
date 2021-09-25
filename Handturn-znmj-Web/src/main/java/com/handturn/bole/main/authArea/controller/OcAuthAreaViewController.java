package com.handturn.bole.main.authArea.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.main.area.service.IAreaInfoService;
import com.handturn.bole.main.authArea.entity.OcAuthArea;
import com.handturn.bole.main.authArea.entity.OcAuthAreaResponsible;
import com.handturn.bole.main.authArea.service.IOcAuthAreaResponsibleService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaService;
import com.handturn.bole.master.equipment.service.IEquipmentModelService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
* 网点-权限区域 View Controller
*
* @author Eric
* @date 2020-09-22 08:57:47
*/
@Slf4j
@Validated
@Controller
public class OcAuthAreaViewController extends BaseController {

    @Autowired
    private IOcAuthAreaService ocAuthAreaService;

    @Autowired
    private IEquipmentModelService equipmentModelService;

    @Autowired
    private IAreaInfoService areaInfoService;
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/authArea/ocAuthArea")
    @RequiresPermissions("main:authArea:ocAuthArea:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        return FebsUtil.view("main/authArea/ocAuthArea/ocAuthAreaList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/authArea/ocAuthArea/add")
    @RequiresPermissions("main:authArea:ocAuthArea:add")
    public String add(Model model) {
        //组装新增对象
        OcAuthArea ocAuthArea = new OcAuthArea();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        //传参
        model.addAttribute("ocAuthArea",ocAuthArea);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        return FebsUtil.view("main/authArea/ocAuthArea/ocAuthAreaEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/authArea/ocAuthArea/update/{ocAuthAreaId}")
    @RequiresPermissions("main:authArea:ocAuthArea:update")
    public String update(@PathVariable Long ocAuthAreaId, Model model) {
        OcAuthArea ocAuthArea = ocAuthAreaService.findOcAuthAreaById(ocAuthAreaId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        //传参
        model.addAttribute("ocAuthArea",ocAuthArea);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        return FebsUtil.view("main/authArea/ocAuthArea/ocAuthAreaEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/authArea/ocAuthArea/authShow/{ocAuthAreaId}")
    @RequiresPermissions("main:authArea:ocAuthArea:auth")
    public String authShow(@PathVariable Long ocAuthAreaId, Model model) {
        OcAuthArea ocAuthArea = ocAuthAreaService.findOcAuthAreaById(ocAuthAreaId);

        //获取数据字典
        List<OptionVo> ocVisitorTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorType");
        List<OptionVo> ocVisitorAuditStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorAuditStatus");
        List<OptionVo> ocVisitorFreezeStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorFreezeStatus");
        List<OptionVo> ocVisitorCreateTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorCreateType");

        List<OptionVo> equipmentInfoErectStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInfoErectStatus");
        List<OptionVo> equipmentInfoOnlineStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInfoOnlineStatus");
        List<OptionVo> equipmentInOutOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInOut");
        List<OptionVo> equipmentModelsOption = equipmentModelService.findOptionVo(null);
        List<OptionVo> areaInfosOption = areaInfoService.findOptionVo4CodeKeyByOcCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode(),UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());

        //传参
        model.addAttribute("ocAuthArea",ocAuthArea);
        model.addAttribute("ocVisitorTypeOption",ocVisitorTypeOption);
        model.addAttribute("ocVisitorAuditStatusOption",ocVisitorAuditStatusOption);
        model.addAttribute("ocVisitorFreezeStatusOption",ocVisitorFreezeStatusOption);
        model.addAttribute("ocVisitorCreateTypeOption",ocVisitorCreateTypeOption);

        model.addAttribute("equipmentInfoErectStatusOption",equipmentInfoErectStatusOption);
        model.addAttribute("equipmentInfoOnlineStatusOption",equipmentInfoOnlineStatusOption);
        model.addAttribute("equipmentInOutOption",equipmentInOutOption);
        model.addAttribute("equipmentModelsOption",equipmentModelsOption);
        model.addAttribute("areaInfosOption",areaInfosOption);
        return FebsUtil.view("main/authArea/ocAuthArea/ocAuthAreaAuth");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/authArea/ocAuthArea/addResponsible/{authAreaId}")
    @RequiresPermissions({"main:authArea:ocAuthArea:add","main:authArea:ocAuthArea:update"})
    public String addResponsible(@PathVariable Long authAreaId,Model model) {
        //组装新增对象
        OcAuthAreaResponsible ocAuthAreaResponsible = new OcAuthAreaResponsible();
        ocAuthAreaResponsible.setAuthAreaId(authAreaId);

        List<OptionVo> usersOption = sysUserService.findOptionVoByOrg(UserInfoHolder.getUserInfo().getCurrentOrg().getId(), UserInfoHolder.getUserInfo().getCurrentOc().getId());

        //传参
        model.addAttribute("ocAuthAreaResponsible",ocAuthAreaResponsible);
        model.addAttribute("usersOption",usersOption);
        return FebsUtil.view("main/authArea/ocAuthArea/ocAuthAreaResponsibleEdit");
    }
}
