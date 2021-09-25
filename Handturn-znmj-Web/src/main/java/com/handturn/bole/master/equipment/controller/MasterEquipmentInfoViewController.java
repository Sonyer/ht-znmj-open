package com.handturn.bole.master.equipment.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.main.equipment.entity.EquipmentInfo;
import com.handturn.bole.main.equipment.service.IEquipmentInfoService;
import com.handturn.bole.master.equipment.service.IEquipmentModelService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.system.entity.OcType;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
public class MasterEquipmentInfoViewController extends BaseController {

    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    @Autowired
    private IEquipmentModelService equipmentModelService;
    @Autowired
    private ISysOrganizationService sysOrganizationService;
    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/equipment/equipmentInfo")
    @RequiresPermissions("master:equipment:equipmentInfo:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> equipmentInfoErectStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInfoErectStatus");
        List<OptionVo> equipmentInfoOnlineStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInfoOnlineStatus");
        List<OptionVo> equipmentInOutOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInOut");
        List<OptionVo> equipmentModelsOption = equipmentModelService.findOptionVo(null);

        model.addAttribute("equipmentInfoErectStatusOption",equipmentInfoErectStatusOption);
        model.addAttribute("equipmentInfoOnlineStatusOption",equipmentInfoOnlineStatusOption);
        model.addAttribute("equipmentInOutOption",equipmentInOutOption);
        model.addAttribute("equipmentModelsOption",equipmentModelsOption);
        return FebsUtil.view("master/equipment/equipmentInfo/equipmentInfoList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/equipment/equipmentInfo/add")
    @RequiresPermissions("master:equipment:equipmentInfo:add")
    public String add(Model model) {
        //组装新增对象
        EquipmentInfo equipmentInfo = new EquipmentInfo();

        //获取数据字典
        //List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> equipmentInOutOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInOut");
        Set<String> statuses = new HashSet<>();
        statuses.add(BaseStatus.ENABLED);
        List<OptionVo> equipmentModelsOption = equipmentModelService.findOptionVo(statuses);
        List<OptionVo> sysOrganizationsOption =  sysOrganizationService.getOrgCodeKeyOptionVoForAll();

        //传参
        model.addAttribute("equipmentInfo",equipmentInfo);
        model.addAttribute("equipmentInOutOption",equipmentInOutOption);
        model.addAttribute("equipmentModelsOption",equipmentModelsOption);
        model.addAttribute("sysOrganizationsOption",sysOrganizationsOption);
        return FebsUtil.view("master/equipment/equipmentInfo/equipmentInfoEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/equipment/equipmentInfo/update/{equipmentInfoId}")
    @RequiresPermissions("master:equipment:equipmentInfo:update")
    public String update(@PathVariable Long equipmentInfoId, Model model) {
        EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(equipmentInfoId);

        //获取数据字典
        //List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> equipmentInOutOption = sysconfDictCodeService.getDictOptionVoByTypeCode("EquipmentInOut");
        List<OptionVo> equipmentModelsOption = equipmentModelService.findOptionVo(null);
        List<OptionVo> sysOrganizationsOption =  sysOrganizationService.getOrgCodeKeyOptionVoForAll();
        /*Set<String> ocTypes = new HashSet<>();
        ocTypes.add(OcType.ORG_CN);
        ocTypes.add(OcType.ORG_OC);
        ocTypes.add(OcType.ORG_3TH);
        List<OptionVo> sysOrganizationOcsOption = sysOrganizationOcService.findOptionVoByOcType(ocTypes);*/

        List<OptionVo> sysOrganizationOcsOption = new ArrayList<>();
        OptionVo optionVo = new OptionVo();
        optionVo.setValue(equipmentInfo.getOcCode());
        optionVo.setText(equipmentInfo.getOcName());
        sysOrganizationOcsOption.add(optionVo);

        //传参
        model.addAttribute("equipmentInfo",equipmentInfo);
        model.addAttribute("equipmentInOutOption",equipmentInOutOption);
        model.addAttribute("equipmentModelsOption",equipmentModelsOption);
        model.addAttribute("sysOrganizationsOption",sysOrganizationsOption);
        model.addAttribute("sysOrganizationOcsOption",sysOrganizationOcsOption);
        return FebsUtil.view("master/equipment/equipmentInfo/equipmentInfoEdit");
    }



    @GetMapping(FebsConstant.VIEW_PREFIX + "master/equipment/equipmentInfo/authCodeShow/{equipmentInfoIds}")
    @RequiresPermissions("master:equipment:equipmentInfo:authCode")
    public String authCodeShow(@PathVariable String equipmentInfoIds, Model model) {
        String[] equipmentInfoIdArr = equipmentInfoIds.split(StringPool.COMMA);
        String equipmentAuthCodes = equipmentInfoService.findEquipmentAuthCodeByEquipmentIds(equipmentInfoIdArr);

        model.addAttribute("equipmentInfoIds",equipmentInfoIds);
        model.addAttribute("equipmentAuthCodes",equipmentAuthCodes);
        return FebsUtil.view("master/equipment/equipmentInfo/equipmentAuthCode.html");
    }
}
