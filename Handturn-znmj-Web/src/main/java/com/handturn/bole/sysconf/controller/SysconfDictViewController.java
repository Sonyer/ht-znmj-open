package com.handturn.bole.sysconf.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.entity.SysconfDictCode;
import com.handturn.bole.sysconf.entity.SysconfDictType;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.sysconf.service.ISysconfDictTypeService;
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
* 系统-数据字典类型表 View Controller
*
* @author MrBird
* @date 2019-12-08 20:56:08
*/
@Slf4j
@Validated
@Controller
public class SysconfDictViewController extends BaseController {

    @Autowired
    private ISysconfDictTypeService sysconfDictTypeService;

    @Autowired
    private ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfDict")
    @RequiresPermissions("sysconfDict:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        return FebsUtil.view("sysconf/sysconfDict/sysconfDictList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfDictType/add")
    @RequiresPermissions("sysconfDictType:add")
    public String add(Model model) {
        //组装新增对象
        SysconfDictType sysconfDictType = new SysconfDictType();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfDictType",sysconfDictType);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfDict/sysconfDictTypeEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfDictType/update/{sysconfDictTypeId}")
    @RequiresPermissions("sysconfDictType:update")
    public String update(@PathVariable Long sysconfDictTypeId, Model model) {
        SysconfDictType sysconfDictType = sysconfDictTypeService.findSysconfDictTypeById(sysconfDictTypeId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfDictType",sysconfDictType);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfDict/sysconfDictTypeEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfDictCode/add/{typeCode}")
    @RequiresPermissions("sysconfDictCode:add")
    public String addDictCode(@PathVariable String typeCode,Model model) {
        //组装新增对象
        SysconfDictCode sysconfDictCode = new SysconfDictCode();
        sysconfDictCode.setTypeCode(typeCode);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfDictCode",sysconfDictCode);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfDict/sysconfDictCodeEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfDictCode/update/{sysconfDictCodeId}")
    @RequiresPermissions("sysconfDictCode:update")
    public String updateDictCode(@PathVariable Long sysconfDictCodeId, Model model) {
        SysconfDictCode sysconfDictCode = sysconfDictCodeService.findSysconfDictCodeById(sysconfDictCodeId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfDictCode",sysconfDictCode);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfDict/sysconfDictCodeEdit");
    }
}
