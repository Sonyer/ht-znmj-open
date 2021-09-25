package com.handturn.bole.master.area.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.main.area.entity.AreaInfo;
import com.handturn.bole.main.area.service.IAreaInfoService;
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
* 基础资料-区域管理 View Controller
*
* @author Eric
* @date 2020-09-11 08:20:03
*/
@Slf4j
@Validated
@Controller
public class MasterAreaInfoViewController extends BaseController {

    @Autowired
    private IAreaInfoService areaInfoService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/area/areaInfo")
    @RequiresPermissions("master:area:areaInfo:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("master/area/areaInfo/areaInfoList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/area/areaInfo/add")
    @RequiresPermissions("master:area:areaInfo:add")
    public String add(Model model) {
        //组装新增对象
        AreaInfo areaInfo = new AreaInfo();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("areaInfo",areaInfo);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("master/area/areaInfo/areaInfoEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/area/areaInfo/update/{areaInfoId}")
    @RequiresPermissions("master:area:areaInfo:view")
    public String update(@PathVariable Long areaInfoId, Model model) {
        AreaInfo areaInfo = areaInfoService.findAreaInfoById(areaInfoId);
        if(areaInfo.getParentAreaId() != null && areaInfo.getParentAreaId() != 0L){
            AreaInfo parentAreaInfo = areaInfoService.findAreaInfoById(areaInfo.getParentAreaId());
            areaInfo.setParentAreaName(parentAreaInfo.getAreaName());
            areaInfo.setParentAreaCode(parentAreaInfo.getParentAreaCode());
        }

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("areaInfo",areaInfo);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("master/area/areaInfo/areaInfoEdit");
    }
}
