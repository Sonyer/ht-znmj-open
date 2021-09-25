package com.handturn.bole.sysconf.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.entity.SysconfGlobalGroup;
import com.handturn.bole.sysconf.service.ISysconfGlobalGroupService;
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
 * 系统-系统配置 Controller
 *
 * @author Eric
 * @date 2019-12-18 20:25:27
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sysconf/sysconfGlobalGroup")
public class SysconfGlobalGroupController extends BaseController {

    @Autowired
    private ISysconfGlobalGroupService sysconfGlobalGroupService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysconfGlobal:view")
    public FebsResponse sysconfGlobalGroupList(QueryRequest request, SysconfGlobalGroup sysconfGlobalGroup) {
        sysconfGlobalGroup.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        sysconfGlobalGroup.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        Map<String, Object> dataTable = getDataTable(this.sysconfGlobalGroupService.findSysconfGlobalGroups(request, sysconfGlobalGroup));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑系统配置/按钮", exceptionMessage = "编辑系统配置失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysconfGlobalGroup:add","sysconfGlobalGroup:update"})
    public FebsResponse addSysconfGlobalGroup(@Valid SysconfGlobalGroup sysconfGlobalGroup) {
        if(sysconfGlobalGroup.getId() == null || sysconfGlobalGroup.getId() == 0){
            if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
                if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                        && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                    throw new FebsException("非系统管理不允许操作!");
                }
                sysconfGlobalGroup.setIsSysCreated(BaseBoolean.TRUE);
            }else{
                throw new FebsException("非系统组织不允许操作!");
            }
        }
        sysconfGlobalGroup.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        sysconfGlobalGroup.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        this.sysconfGlobalGroupService.saveSysconfGlobalGroup(sysconfGlobalGroup);
        return new FebsResponse().success();
    }

    @PostMapping("synchro/{sysconfGlobalGroupIds}")
    @RequiresPermissions("sysconfGlobal:view")
    @ControllerEndpoint(exceptionMessage = "同步系统配置失败")
    public FebsResponse synchroSysconfGlobalGroup(@NotBlank(message = "{required}") @PathVariable String sysconfGlobalGroupIds) {
        String[] sysconfGlobalGroupIdArr = sysconfGlobalGroupIds.split(StringPool.COMMA);
        this.sysconfGlobalGroupService.synchroSysconfGlobalGroup(sysconfGlobalGroupIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("enable/{sysconfGlobalGroupIds}")
    @RequiresPermissions("sysconfGlobalGroup:enable")
    @ControllerEndpoint(exceptionMessage = "启用系统配置失败")
    public FebsResponse enableSysconfGlobalGroup(@NotBlank(message = "{required}") @PathVariable String sysconfGlobalGroupIds) {
        String[] sysconfGlobalGroupIdArr = sysconfGlobalGroupIds.split(StringPool.COMMA);
        this.sysconfGlobalGroupService.enableSysconfGlobalGroup(sysconfGlobalGroupIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysconfGlobalGroupIds}")
    @RequiresPermissions("sysconfGlobalGroup:disable")
    @ControllerEndpoint(exceptionMessage = "禁用系统配置失败")
    public FebsResponse disableSysconfGlobalGroup(@NotBlank(message = "{required}") @PathVariable String sysconfGlobalGroupIds) {
        String[] sysconfGlobalGroupIdArr = sysconfGlobalGroupIds.split(StringPool.COMMA);
        this.sysconfGlobalGroupService.disableSysconfGlobalGroup(sysconfGlobalGroupIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出系统配置", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("sysconfGlobal:view")
    public void exportSysconfGlobalGroup(QueryRequest queryRequest, SysconfGlobalGroup sysconfGlobalGroup, HttpServletResponse response) {
        sysconfGlobalGroup.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        sysconfGlobalGroup.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        List<SysconfGlobalGroup> sysconfGlobalGroups = this.sysconfGlobalGroupService.findSysconfGlobalGroups(queryRequest, sysconfGlobalGroup).getRecords();
        ExcelKit.$Export(SysconfGlobalGroup.class, response).downXlsx(sysconfGlobalGroups, false);
    }
}
