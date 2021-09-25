package com.handturn.bole.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.system.entity.SysUser;
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
 * 系统-用户 Controller
 *
 * @author Eric
 * @date 2019-12-01 10:46:37
 */
@Slf4j
@Validated
@Controller
public class SysUserViewController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysUser")
    @RequiresPermissions("sysUser:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseSexOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseSex");
        List<OptionVo> baseBooleanOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseBoolean");

        //传参
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseSexOption",baseSexOption);
        model.addAttribute("baseBooleanOption",baseBooleanOption);

        return FebsUtil.view("system/sysUser/sysUserList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysUser/add")
    @RequiresPermissions("sysUser:add")
    public String add(Model model) {
        //组装新增对象
        SysUser sysUser = new SysUser();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseSexOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseSex");

        //传参
        model.addAttribute("sysUser",sysUser);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseSexOption",baseSexOption);
        return FebsUtil.view("system/sysUser/sysUserEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysUser/update/{sysUserId}")
    @RequiresPermissions("sysUser:update")
    public String update(@PathVariable Long sysUserId, Model model) {
        SysUser sysUser = sysUserService.findById(sysUserId);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");
        List<OptionVo> baseSexOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseSex");

        //传参
        model.addAttribute("sysUser",sysUser);
        model.addAttribute("baseStatusOption",baseStatusOption);
        model.addAttribute("baseSexOption",baseSexOption);
        return FebsUtil.view("system/sysUser/sysUserEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysUser/command/{sysUserIds}")
    @RequiresPermissions("sysUser:command")
    public String command(@PathVariable String sysUserIds, Model model) {
        String[] userIdArr = sysUserIds.split(StringPool.COMMA);
        String commands = sysUserService.findCommandByIds(userIdArr, UserInfoHolder.getUserInfo().getCurrentOrg().getId(), UserInfoHolder.getUserInfo().getCurrentOc().getId());

        model.addAttribute("sysUserIds",sysUserIds);
        model.addAttribute("commands",commands);
        return FebsUtil.view("system/sysUser/sysUserCommand");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysUser/interfaceSet/{sysUserIds}")
    @RequiresPermissions("sysUser:interfaceSet")
    public String interfaceSet(@PathVariable String sysUserIds, Model model) {
        String[] userIdArr = sysUserIds.split(StringPool.COMMA);
        String commands = sysUserService.findInterfaceSetByIds(userIdArr, UserInfoHolder.getUserInfo().getCurrentOrg().getId(), UserInfoHolder.getUserInfo().getCurrentOc().getId());

        model.addAttribute("sysUserIds",sysUserIds);
        model.addAttribute("interfaceSets",commands);
        return FebsUtil.view("system/sysUser/sysUserInterfaceSet");
    }
}
