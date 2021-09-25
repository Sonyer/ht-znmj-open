package com.handturn.bole.system.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import ${basePackage}.${entityPackage}.${className};
import ${basePackage}.${servicePackage}.I${className}Service;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* ${tableComment} View Controller
*
* @author ${author}
* @date ${date}
*/
@Slf4j
@Validated
@Controller
public class ${className}ViewController extends BaseController {

    @Autowired
    private I${className}Service ${className?uncap_first}Service;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "${className?uncap_first}/${className?uncap_first}")
    @RequiresPermissions("${className?uncap_first}:view")
    public String view(Model model) {
        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("${className?uncap_first}/${className?uncap_first}/${className?uncap_first}List");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "${className?uncap_first}/${className?uncap_first}/add")
    @RequiresPermissions("${className?uncap_first}:add")
    public String add(Model model) {
        //组装新增对象
        ${className} ${className?uncap_first} = new ${className}();

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("${className?uncap_first}",${className?uncap_first});
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("${className?uncap_first}/${className?uncap_first}/${className?uncap_first}Edit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "${className?uncap_first}/${className?uncap_first}/update/{${className?uncap_first}Id}")
    @RequiresPermissions("${className?uncap_first}:update")
    public String update(@PathVariable Long ${className?uncap_first}Id, Model model) {
        ${className} ${className?uncap_first} = ${className?uncap_first}Service.find${className}ById(${className?uncap_first}Id);

        //获取数据字典
        List<OptionVo> baseStatusOption = sysconfDictCodeService.getDictOptionVoByTypeCode("BaseStatus");

        //传参
        model.addAttribute("${className?uncap_first}",${className?uncap_first});
        model.addAttribute("baseStatusOption",baseStatusOption);
        return FebsUtil.view("${className?uncap_first}/${className?uncap_first}/${className?uncap_first}Edit");
    }
}
