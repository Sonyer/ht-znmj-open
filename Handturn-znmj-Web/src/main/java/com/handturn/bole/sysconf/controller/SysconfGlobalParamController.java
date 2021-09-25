package com.handturn.bole.sysconf.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.entity.SysconfGlobalParam;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
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
 * 系统-系统配置参数 Controller
 *
 * @author Eric
 * @date 2019-12-18 20:31:06
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sysconf/sysconfGlobalParam")
public class SysconfGlobalParamController extends BaseController {

    @Autowired
    private ISysconfGlobalParamService sysconfGlobalParamService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysconfGlobal:view")
    public FebsResponse sysconfGlobalParamList(QueryRequest request, SysconfGlobalParam sysconfGlobalParam) {
        sysconfGlobalParam.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        sysconfGlobalParam.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        Map<String, Object> dataTable = getDataTable(this.sysconfGlobalParamService.findSysconfGlobalParams(request, sysconfGlobalParam));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑系统参数/按钮", exceptionMessage = "编辑系统参数失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysconfGlobalParam:add","sysconfGlobalParam:update"})
    public FebsResponse addSysconfGlobalParamr(@Valid SysconfGlobalParam sysconfGlobalParam) {
        if(sysconfGlobalParam.getId() == null || sysconfGlobalParam.getId() == 0){
            if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
                if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                        && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                    throw new FebsException("非系统管理不允许操作!");
                }
                sysconfGlobalParam.setIsSysCreated(BaseBoolean.TRUE);
            }else{
                throw new FebsException("非系统组织不允许操作!");
            }
        }
        sysconfGlobalParam.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        sysconfGlobalParam.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        this.sysconfGlobalParamService.saveSysconfGlobalParam(sysconfGlobalParam);
        return new FebsResponse().success();
    }

    @PostMapping("synchro/{sysconfGlobalParamIds}")
    @RequiresPermissions("sysconfGlobal:view")
    @ControllerEndpoint(exceptionMessage = "同步系统参数失败")
    public FebsResponse synchroSysconfGlobalParam(@NotBlank(message = "{required}") @PathVariable String sysconfGlobalParamIds) {
        String[] sysconfGlobalParamIdArr = sysconfGlobalParamIds.split(StringPool.COMMA);
        this.sysconfGlobalParamService.synchroSysconfGlobalParam(sysconfGlobalParamIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("enable/{sysconfGlobalParamIds}")
    @RequiresPermissions("sysconfGlobalParam:enable")
    @ControllerEndpoint(exceptionMessage = "系统参数")
    public FebsResponse enableSysconfGlobalParam(@NotBlank(message = "{required}") @PathVariable String sysconfGlobalParamIds) {
        String[] sysconfGlobalParamIdArr = sysconfGlobalParamIds.split(StringPool.COMMA);
        this.sysconfGlobalParamService.enableSysconfGlobalParam(sysconfGlobalParamIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysconfGlobalParamIds}")
    @RequiresPermissions("sysconfGlobalParam:disable")
    @ControllerEndpoint(exceptionMessage = "禁用系统参数失败")
    public FebsResponse disableSysconfGlobalParam(@NotBlank(message = "{required}") @PathVariable String sysconfGlobalParamIds) {
        String[] sysconfGlobalParamIdArr = sysconfGlobalParamIds.split(StringPool.COMMA);
        this.sysconfGlobalParamService.disableSysconfGlobalParam(sysconfGlobalParamIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出系统参数", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("sysconfGlobal:view")
    public void exportSysconfGlobalParam(QueryRequest queryRequest, SysconfGlobalParam sysconfGlobalParam, HttpServletResponse response) {
        sysconfGlobalParam.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        sysconfGlobalParam.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        List<SysconfGlobalParam> sysconfGlobalParams = this.sysconfGlobalParamService.findSysconfGlobalParams(queryRequest, sysconfGlobalParam).getRecords();
        ExcelKit.$Export(SysconfGlobalParam.class, response).downXlsx(sysconfGlobalParams, false);
    }
}
