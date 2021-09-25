package com.handturn.bole.sysconf.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.entity.SysconfGlobalGroup;
import com.handturn.bole.sysconf.entity.SysconfGlobalParam;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.sysconf.service.ISysconfGlobalGroupService;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
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
* 系统-系统配置 View Controller
*
* @author Eric
* @date 2019-12-18 20:25:27
*/
@Slf4j
@Validated
@Controller
public class SysconfGlobalViewController extends BaseController {
    @Autowired
    private ISysconfDictCodeService sysconfDictCodeService;

    @Autowired
    private ISysconfGlobalGroupService sysconfGlobalGroupService;

    @Autowired
    private ISysconfGlobalParamService sysconfGlobalParamService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfGlobal")
    @RequiresPermissions("sysconfGlobal:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);
        return FebsUtil.view("sysconf/sysconfGlobal/sysconfGlobalList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfGlobalGroup/add")
    @RequiresPermissions("sysconfGlobalGroup:add")
    public String add(Model model) {
        //组装新增对象
        SysconfGlobalGroup sysconfGlobalGroup = new SysconfGlobalGroup();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfGlobalGroup",sysconfGlobalGroup);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfGlobal/sysconfGlobalGroupEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfGlobalGroup/update/{sysconfGlobalGroupId}")
    @RequiresPermissions("sysconfGlobalGroup:update")
    public String update(@PathVariable Long sysconfGlobalGroupId, Model model) {
        SysconfGlobalGroup sysconfGlobalGroup = sysconfGlobalGroupService.findSysconfGlobalGroupById(sysconfGlobalGroupId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfGlobalGroup",sysconfGlobalGroup);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfGlobal/sysconfGlobalGroupEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfGlobalParam/add/{groupCode}")
    @RequiresPermissions("sysconfGlobalParam:add")
    public String addDictCode(@PathVariable String groupCode,Model model) {
        //组装新增对象
        SysconfGlobalParam sysconfGlobalParam = new SysconfGlobalParam();
        sysconfGlobalParam.setGroupCode(groupCode);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfGlobalParam",sysconfGlobalParam);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfGlobal/sysconfGlobalParamEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysconf/sysconfGlobalParam/update/{sysconfGlobalParamId}")
    @RequiresPermissions("sysconfGlobalParam:update")
    public String updateDictCode(@PathVariable Long sysconfGlobalParamId, Model model) {
        SysconfGlobalParam sysconfGlobalParam = sysconfGlobalParamService.findSysconfGlobalParamById(sysconfGlobalParamId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("sysconfGlobalParam",sysconfGlobalParam);
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("sysconf/sysconfGlobal/sysconfGlobalParamEdit");
    }
}
