package com.handturn.bole.system.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.system.entity.ResourceNodeType;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.service.ISysResourceService;
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
 * 系统-资源 Controller
 *
 * @author Eric
 * @date 2019-12-01 10:31:46
 */
@Slf4j
@Validated
@Controller
public class SysResourceViewController extends BaseController {

    @Autowired
    private ISysResourceService sysResourceService;
    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysResource")
    @RequiresPermissions("sysResource:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> menuTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCode("ResourceType");

        //传参
        model.addAttribute("menuTypeOption",menuTypeOption);

        return FebsUtil.view("system/sysResource/sysResourceList");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysResource/add/{parentSysResourceId}")
    @RequiresPermissions("sysResource:add")
    public String add(@PathVariable Long parentSysResourceId, Model model) {
        SysResource parentResource = sysResourceService.findById(parentSysResourceId);

        //组装新增对象
        SysResource sysResource = new SysResource();
        sysResource.setParentResourceId(parentSysResourceId);
        sysResource.setParentResourceName(parentResource == null?"":parentResource.getResourceName());

        //获取数据字典
        List<OptionVo> menuTypeOption = null;
        if(sysResource.getParentResourceId() == 0){
            sysResource.setResourceNodeType(ResourceNodeType.MENU_ROOT);
            sysResource.setRootResourceId(0L);
            sysResource.setRootResourceName("");

            Set<String> contens = new HashSet<String>();
            contens.add(ResourceNodeType.MENU_ROOT);
            menuTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeContains("ResourceType",contens);
        }else{
            SysResource rootResource = sysResourceService.findById(parentResource.getRootResourceId());
            sysResource.setRootResourceId(parentResource.getRootResourceId());
            sysResource.setRootResourceName(rootResource == null?"":rootResource.getResourceName());
            Set<String> contens = new HashSet<String>();
            contens.add(ResourceNodeType.MENU_ROOT);
            menuTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeExit("ResourceType",contens);
        }

        //传参
        model.addAttribute("sysResource",sysResource);
        model.addAttribute("sysResourceTypeOption",menuTypeOption);
        return FebsUtil.view("system/sysResource/sysResourceEdit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/sysResource/update/{sysResourceId}")
    @RequiresPermissions("sysResource:update")
    public String update(@PathVariable Long sysResourceId, Model model) {
        SysResource sysResource = sysResourceService.findById(sysResourceId);
        SysResource parentResource = sysResourceService.findById(sysResource.getParentResourceId());
        sysResource.setParentResourceName(parentResource == null?"":parentResource.getResourceName());

        //获取数据字典
        List<OptionVo> menuTypeOption = null;
        if(sysResource.getParentResourceId() == 0){
            sysResource.setResourceNodeType(ResourceNodeType.MENU_ROOT);
            sysResource.setRootResourceId(0L);
            sysResource.setRootResourceName("");

            Set<String> contens = new HashSet<String>();
            contens.add(ResourceNodeType.MENU_ROOT);
            menuTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeContains("ResourceType",contens);
        }else{
            SysResource rootResource = sysResourceService.findById(parentResource.getRootResourceId());
            sysResource.setRootResourceId(parentResource.getRootResourceId());
            sysResource.setRootResourceName(rootResource == null?"":rootResource.getResourceName());
            Set<String> contens = new HashSet<String>();
            contens.add(ResourceNodeType.MENU_ROOT);
            menuTypeOption = sysconfDictCodeService.getDictOptionVoByTypeCodeExit("ResourceType",contens);
        }

        //传参
        model.addAttribute("sysResource",sysResource);
        model.addAttribute("sysResourceTypeOption",menuTypeOption);
        return FebsUtil.view("system/sysResource/sysResourceEdit");
    }
}
