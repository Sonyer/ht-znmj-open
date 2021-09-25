package com.handturn.bole.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.common.utils.MD5Util;import lombok.extern.slf4j.Slf4j;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.service.ISysUserService;
import ${basePackage}.${entityPackage}.${className};
import ${basePackage}.${servicePackage}.I${className}Service;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import com.handturn.bole.common.utils.ExportReflectTo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * ${tableComment} Controller
 *
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@Validated
@RestController
@RequestMapping("${className?uncap_first}")
public class ${className}Controller extends BaseController {

    @Autowired
    private I${className}Service ${className?uncap_first}Service;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("${className?uncap_first}:view")
    public FebsResponse ${className?uncap_first}List(QueryRequest request, ${className} ${className?uncap_first}) {
        Map<String, Object> dataTable = getDataTable(this.${className?uncap_first}Service.find${className}s(request, ${className?uncap_first}));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("${className?uncap_first}:export")
    public void export${className}(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        ${className} ${className?uncap_first} = JSONObject.parseObject(queryData,${className}.class);
        //-------控制导出权限-------begin
        ${className?uncap_first}.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ${className?uncap_first}.setClientCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, ${className}.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,${className?uncap_first},
        request,response,
        "${className}Service","find${className}s",paramClassName);
    }

    @ControllerEndpoint(operation = "编辑${className}/按钮", exceptionMessage = "编辑${className}失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"${className?uncap_first}:add","${className?uncap_first}:update"})
    public FebsResponse add${className}r(@Valid ${className} ${className?uncap_first}) {
        this.${className?uncap_first}Service.save${className}(${className?uncap_first});
        return new FebsResponse().success();
    }


    @PostMapping("enable/{${className?uncap_first}Ids}")
    @RequiresPermissions("${className?uncap_first}:enable")
    @ControllerEndpoint(exceptionMessage = "启用${className}失败")
    public FebsResponse enable${className}(@NotBlank(message = "{required}") @PathVariable String ${className?uncap_first}Ids) {
        String[] ${className?uncap_first}IdArr = ${className?uncap_first}Ids.split(StringPool.COMMA);
        this.${className?uncap_first}Service.enable${className}(${className?uncap_first}IdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{${className?uncap_first}Ids}")
    @RequiresPermissions("${className?uncap_first}:disable")
    @ControllerEndpoint(exceptionMessage = "禁用${className}失败")
    public FebsResponse disable${className}(@NotBlank(message = "{required}") @PathVariable String ${className?uncap_first}Ids) {
        String[] ${className?uncap_first}IdArr = ${className?uncap_first}Ids.split(StringPool.COMMA);
        this.${className?uncap_first}Service.disable${className}(${className?uncap_first}IdArr);
        return new FebsResponse().success();
    }
}
