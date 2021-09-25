package com.handturn.bole.sysconf.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.entity.SysconfCodeRuleSeq;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleSeqService;
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
 * 系统配置-编码流水 Controller
 *
 * @author MrBird
 * @date 2019-12-08 21:44:27
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sysconf/sysconfCodeRuleSeq")
public class SysconfCodeRuleSeqController extends BaseController {

    @Autowired
    private ISysconfCodeRuleSeqService sysconfCodeRuleSeqService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysconfCodeRuleSeq:view")
    public FebsResponse sysconfCodeRuleSeqList(QueryRequest request, SysconfCodeRuleSeq sysconfCodeRuleSeq) {
        Map<String, Object> dataTable = getDataTable(this.sysconfCodeRuleSeqService.findSysconfCodeRuleSeqs(request, sysconfCodeRuleSeq));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑编码规则生成/按钮", exceptionMessage = "编辑编码规则生成失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysconfCodeRuleSeq:add","sysconfCodeRuleSeq:update"})
    public FebsResponse addSysconfCodeRuleSeq(@Valid SysconfCodeRuleSeq sysconfCodeRuleSeq) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
            && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
            sysconfCodeRuleSeq.setIsSysCreated(BaseBoolean.TRUE);
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        this.sysconfCodeRuleSeqService.saveSysconfCodeRuleSeq(sysconfCodeRuleSeq);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{sysconfCodeRuleSeqIds}")
    @RequiresPermissions("sysconfCodeRuleSeq:enable")
    @ControllerEndpoint(exceptionMessage = "启用编码规则生成失败")
    public FebsResponse enableSysconfCodeRuleSeq(@NotBlank(message = "{required}") @PathVariable String sysconfCodeRuleSeqIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfCodeRuleSeqIdArr = sysconfCodeRuleSeqIds.split(StringPool.COMMA);
        this.sysconfCodeRuleSeqService.enableSysconfCodeRuleSeq(sysconfCodeRuleSeqIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysconfCodeRuleSeqIds}")
    @RequiresPermissions("sysconfCodeRuleSeq:disable")
    @ControllerEndpoint(exceptionMessage = "禁用编码规则生成失败")
    public FebsResponse disableSysconfCodeRuleSeq(@NotBlank(message = "{required}") @PathVariable String sysconfCodeRuleSeqIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfCodeRuleSeqIdArr = sysconfCodeRuleSeqIds.split(StringPool.COMMA);
        this.sysconfCodeRuleSeqService.disableSysconfCodeRuleSeq(sysconfCodeRuleSeqIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出编码规则生成", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("sysconfCodeRuleSeq:export")
    public void exportSysconfCodeRuleSeq(QueryRequest queryRequest, SysconfCodeRuleSeq sysconfCodeRuleSeq, HttpServletResponse response) {
        List<SysconfCodeRuleSeq> sysconfCodeRuleSeqs = this.sysconfCodeRuleSeqService.findSysconfCodeRuleSeqs(queryRequest, sysconfCodeRuleSeq).getRecords();
        ExcelKit.$Export(SysconfCodeRuleSeq.class, response).downXlsx(sysconfCodeRuleSeqs, false);
    }
}
