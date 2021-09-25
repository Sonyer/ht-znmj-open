package com.handturn.bole.sysconf.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.entity.SysconfCodeRule;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleService;
import com.handturn.bole.system.entity.OcType;
import com.handturn.bole.system.entity.OrgType;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 系统配置-编码规则 Controller
 *
 * @author MrBird
 * @date 2019-12-08 21:25:21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sysconf/sysconfCodeRule")
public class SysconfCodeRuleController extends BaseController {

    @Autowired
    private ISysconfCodeRuleService sysconfCodeRuleService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysconfCodeRule:view")
    public FebsResponse sysconfCodeRuleList(QueryRequest request, SysconfCodeRule sysconfCodeRule) {
        Map<String, Object> dataTable = getDataTable(this.sysconfCodeRuleService.findSysconfCodeRules(request, sysconfCodeRule));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑编码规则/按钮", exceptionMessage = "编辑编码规则失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysconfCodeRule:add","sysconfCodeRule:update"})
    public FebsResponse addSysconfCodeRuler(@Valid SysconfCodeRule sysconfCodeRule) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
            sysconfCodeRule.setIsSysCreated(BaseBoolean.TRUE);
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        this.sysconfCodeRuleService.saveSysconfCodeRule(sysconfCodeRule);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{sysconfCodeRuleIds}")
    @RequiresPermissions("sysconfCodeRule:enable")
    @ControllerEndpoint(exceptionMessage = "启用编码规则失败")
    public FebsResponse enableSysconfCodeRule(@NotBlank(message = "{required}") @PathVariable String sysconfCodeRuleIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfCodeRuleIdArr = sysconfCodeRuleIds.split(StringPool.COMMA);
        this.sysconfCodeRuleService.enableSysconfCodeRule(sysconfCodeRuleIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysconfCodeRuleIds}")
    @RequiresPermissions("sysconfCodeRule:disable")
    @ControllerEndpoint(exceptionMessage = "禁用编码规则失败")
    public FebsResponse disableSysconfCodeRule(@NotBlank(message = "{required}") @PathVariable String sysconfCodeRuleIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfCodeRuleIdArr = sysconfCodeRuleIds.split(StringPool.COMMA);
        this.sysconfCodeRuleService.disableSysconfCodeRule(sysconfCodeRuleIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出编码规则", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("sysconfCodeRule:export")
    public void exportSysconfCodeRule(QueryRequest queryRequest, SysconfCodeRule sysconfCodeRule, HttpServletResponse response) {
        List<SysconfCodeRule> sysconfCodeRules = this.sysconfCodeRuleService.findSysconfCodeRules(queryRequest, sysconfCodeRule).getRecords();
        ExcelKit.$Export(SysconfCodeRule.class, response).downXlsx(sysconfCodeRules, false);
    }
}
