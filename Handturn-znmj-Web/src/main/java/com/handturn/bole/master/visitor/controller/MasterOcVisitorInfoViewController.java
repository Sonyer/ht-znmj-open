package com.handturn.bole.master.visitor.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.main.visitor.entity.OcVisitorInfo;
import com.handturn.bole.main.visitor.entity.OcVisitorUploadImg;
import com.handturn.bole.main.visitor.entity.OcVisitorUploadImgStatus;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import com.handturn.bole.main.visitor.service.IOcVisitorUploadImgService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* 网点-会员访客信息 View Controller
*
* @author Eric
* @date 2020-09-22 08:57:38
*/
@Slf4j
@Validated
@Controller
public class MasterOcVisitorInfoViewController extends BaseController {

    @Autowired
    private IOcVisitorInfoService ocVisitorInfoService;

    @Autowired
    private IOcVisitorUploadImgService ocVisitorUploadImgService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/visitor/ocVisitorInfo")
    @RequiresPermissions("master:visitor:ocVisitorInfo:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> ocVisitorTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorType");
        List<OptionVo> ocVisitorAuditStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorAuditStatus");
        List<OptionVo> ocVisitorFreezeStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorFreezeStatus");
        List<OptionVo> ocVisitorCreateTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorCreateType");


        model.addAttribute("ocVisitorTypeOption",ocVisitorTypeOption);
        model.addAttribute("ocVisitorAuditStatusOption",ocVisitorAuditStatusOption);
        model.addAttribute("ocVisitorFreezeStatusOption",ocVisitorFreezeStatusOption);
        model.addAttribute("ocVisitorCreateTypeOption",ocVisitorCreateTypeOption);
        return FebsUtil.view("master/visitor/ocVisitorInfo/ocVisitorInfoList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/visitor/ocVisitorInfo/update/{ocVisitorInfoId}")
    @RequiresPermissions("master:visitor:ocVisitorInfo:view")
    public String update(@PathVariable Long ocVisitorInfoId, Model model) {
        OcVisitorInfo ocVisitorInfo = ocVisitorInfoService.findOcVisitorInfoById(ocVisitorInfoId);

        //获取数据字典
        List<OptionVo> ocVisitorTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("OcVisitorType");
        Set<String> status = new HashSet<>();
        status.add(OcVisitorUploadImgStatus.INIT);
        List<OcVisitorUploadImg>  ocVisitorUploadImgs = ocVisitorUploadImgService.findOcVisitorUploadImgByOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode(),
                UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode(),status,null);

        //传参
        model.addAttribute("ocVisitorInfo",ocVisitorInfo);
        model.addAttribute("ocVisitorUploadImgs",ocVisitorUploadImgs);
        model.addAttribute("ocVisitorTypeOption",ocVisitorTypeOption);
        return FebsUtil.view("master/visitor/ocVisitorInfo/ocVisitorInfoEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/visitor/ocVisitorInfo/authShow/{ocVisitorInfoId}")
    @RequiresPermissions("master:visitor:ocVisitorInfo:view")
    public String authShow(@PathVariable Long ocVisitorInfoId, Model model) {
        OcVisitorInfo ocVisitorInfo = ocVisitorInfoService.findOcVisitorInfoById(ocVisitorInfoId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        //传参
        model.addAttribute("ocVisitorInfo",ocVisitorInfo);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        return FebsUtil.view("master/visitor/ocVisitorInfo/ocVisitorInfoAuth");
    }

}
