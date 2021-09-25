package com.handturn.bole.main.equipment.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.main.area.service.IAreaInfoService;
import com.handturn.bole.master.equipment.service.IEquipmentModelService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.main.equipment.entity.EquipmentInfo;
import com.handturn.bole.main.equipment.service.IEquipmentInfoService;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysOrganizationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* 基础资料-设备管理 View Controller
*
* @author Eric
* @date 2020-09-11 08:20:12
*/
@Slf4j
@Validated
@Controller
public class EquipmentInfoViewController extends BaseController {

    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    @Autowired
    private IAreaInfoService areaInfoService;

    @Autowired
    private IEquipmentModelService equipmentModelService;
    @Autowired
    private ISysOrganizationService sysOrganizationService;
    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;
    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/equipment/equipmentInfo")
    @RequiresPermissions("main:equipment:equipmentInfo:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> equipmentInfoErectStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInfoErectStatus");
        List<OptionVo> equipmentInfoOnlineStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInfoOnlineStatus");
        List<OptionVo> equipmentInOutOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInOut");
        List<OptionVo> equipmentModelsOption = equipmentModelService.findOptionVo(null);
        List<OptionVo> areaInfosOption = areaInfoService.findOptionVo4CodeKeyByOcCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode(),UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());

        model.addAttribute("equipmentInfoErectStatusOption",equipmentInfoErectStatusOption);
        model.addAttribute("equipmentInfoOnlineStatusOption",equipmentInfoOnlineStatusOption);
        model.addAttribute("equipmentInOutOption",equipmentInOutOption);
        model.addAttribute("equipmentModelsOption",equipmentModelsOption);
        model.addAttribute("areaInfosOption",areaInfosOption);
        return FebsUtil.view("main/equipment/equipmentInfo/equipmentInfoList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/equipment/equipmentInfo/erectShow/{equipmentInfoId}")
    @RequiresPermissions("main:equipment:equipmentInfo:erect")
    public String erectShow(@PathVariable Long equipmentInfoId, Model model) {
        EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(equipmentInfoId);

        //获取数据字典
        List<OptionVo> equipmentInOutOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInOut");
        List<OptionVo> equipmentModelsOption = equipmentModelService.findOptionVo(null);
       /* List<OptionVo> sysOrganizationsOption =  sysOrganizationService.getOrgCodeKeyOptionVoForAll();*/
        List<OptionVo> sysOrganizationsOption = new ArrayList<>();
        OptionVo orgOptionVo = new OptionVo();
        orgOptionVo.setValue(equipmentInfo.getOrgCode());
        orgOptionVo.setText(equipmentInfo.getOrgName());
        sysOrganizationsOption.add(orgOptionVo);

        List<OptionVo> sysOrganizationOcsOption = new ArrayList<>();
        OptionVo optionVo = new OptionVo();
        optionVo.setValue(equipmentInfo.getOcCode());
        optionVo.setText(equipmentInfo.getOcName());
        sysOrganizationOcsOption.add(optionVo);

        List<OptionVo> areaInfosOption = areaInfoService.findOptionVo4CodeKeyByOcCode(equipmentInfo.getOrgCode(),equipmentInfo.getOcCode());

        //传参
        model.addAttribute("equipmentInfo",equipmentInfo);
        model.addAttribute("equipmentModelsOption",equipmentModelsOption);
        model.addAttribute("equipmentInOutOption",equipmentInOutOption);
        model.addAttribute("sysOrganizationsOption",sysOrganizationsOption);
        model.addAttribute("sysOrganizationOcsOption",sysOrganizationOcsOption);
        model.addAttribute("areaInfosOption",areaInfosOption);

        return FebsUtil.view("main/equipment/equipmentInfo/equipmentInfoErect.html");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/equipment/equipmentInfo/update/{equipmentInfoId}")
    @RequiresPermissions("main:equipment:equipmentInfo:view")
    public String update(@PathVariable Long equipmentInfoId, Model model) {
        EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(equipmentInfoId);

        //获取数据字典
        List<OptionVo> equipmentInOutOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInOut");
        List<OptionVo> equipmentModelsOption = equipmentModelService.findOptionVo(null);
        List<OptionVo> sysOrganizationsOption =  sysOrganizationService.getOrgCodeKeyOptionVoForAll();

        List<OptionVo> sysOrganizationOcsOption = new ArrayList<>();
        OptionVo optionVo = new OptionVo();
        optionVo.setValue(equipmentInfo.getOcCode());
        optionVo.setText(equipmentInfo.getOcName());
        sysOrganizationOcsOption.add(optionVo);

        List<OptionVo> areaInfosOption = areaInfoService.findOptionVo4CodeKeyByOcCode(equipmentInfo.getOrgCode(),equipmentInfo.getOcCode());

        //传参
        model.addAttribute("equipmentInfo",equipmentInfo);
        model.addAttribute("equipmentInOutOption",equipmentInOutOption);
        model.addAttribute("equipmentModelsOption",equipmentModelsOption);
        model.addAttribute("sysOrganizationsOption",sysOrganizationsOption);
        model.addAttribute("sysOrganizationOcsOption",sysOrganizationOcsOption);
        model.addAttribute("areaInfosOption",areaInfosOption);
        return FebsUtil.view("main/equipment/equipmentInfo/equipmentInfoErect");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "main/equipment/equipmentInfo/authCodeShow/{equipmentInfoIds}")
    @RequiresPermissions("main:equipment:equipmentInfo:authCode")
    public String authCodeShow(@PathVariable String equipmentInfoIds, Model model) {
        String[] equipmentInfoIdArr = equipmentInfoIds.split(StringPool.COMMA);
        String equipmentAuthCodes = equipmentInfoService.findEquipmentAuthCodeByEquipmentIds(equipmentInfoIdArr);

        model.addAttribute("equipmentInfoIds",equipmentInfoIds);
        model.addAttribute("equipmentAuthCodes",equipmentAuthCodes);
        return FebsUtil.view("main/equipment/equipmentInfo/equipmentAuthCode.html");
    }
}
