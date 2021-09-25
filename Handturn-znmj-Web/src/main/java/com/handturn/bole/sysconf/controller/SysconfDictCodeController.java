package com.handturn.bole.sysconf.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.entity.SysconfDictCode;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
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
 * 系统-数据字典明细 Controller
 *
 * @author MrBird
 * @date 2019-12-08 21:05:28
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sysconf/sysconfDictCode")
public class SysconfDictCodeController extends BaseController {

    @Autowired
    private ISysconfDictCodeService sysconfDictCodeService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysconfDict:view")
    public FebsResponse sysconfDictCodeList(QueryRequest request, SysconfDictCode sysconfDictCode) {
        Map<String, Object> dataTable = getDataTable(this.sysconfDictCodeService.findSysconfDictCodes(request, sysconfDictCode));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑数据字典/按钮", exceptionMessage = "编辑数据字典失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysconfDictCode:add","sysconfDictCode:update"})
    public FebsResponse addSysconfDictCode(@Valid SysconfDictCode sysconfDictCode) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
            sysconfDictCode.setIsSysCreated(BaseBoolean.TRUE);
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        this.sysconfDictCodeService.saveSysconfDictCode(sysconfDictCode);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{sysconfDictCodeIds}")
    @RequiresPermissions("sysconfDictCode:enable")
    @ControllerEndpoint(exceptionMessage = "启用数据字典失败")
    public FebsResponse enableSysconfDictCode(@NotBlank(message = "{required}") @PathVariable String sysconfDictCodeIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfDictCodeIdArr = sysconfDictCodeIds.split(StringPool.COMMA);
        this.sysconfDictCodeService.enableSysconfDictCode(sysconfDictCodeIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysconfDictCodeIds}")
    @RequiresPermissions("sysconfDictCode:disable")
    @ControllerEndpoint(exceptionMessage = "禁用数据字典失败")
    public FebsResponse disableSysconfDictCode(@NotBlank(message = "{required}") @PathVariable String sysconfDictCodeIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfDictCodeIdArr = sysconfDictCodeIds.split(StringPool.COMMA);
        this.sysconfDictCodeService.disableSysconfDictCode(sysconfDictCodeIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出数据字典", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("sysconfDictCode:export")
    public void exportSysconfDictCode(QueryRequest queryRequest, SysconfDictCode sysconfDictCode, HttpServletResponse response) {
        List<SysconfDictCode> sysconfDictCodes = this.sysconfDictCodeService.findSysconfDictCodes(queryRequest, sysconfDictCode).getRecords();
        ExcelKit.$Export(SysconfDictCode.class, response).downXlsx(sysconfDictCodes, false);
    }
}
