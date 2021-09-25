package com.handturn.bole.sysconf.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.entity.SysconfDictType;
import com.handturn.bole.sysconf.service.ISysconfDictTypeService;
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
 * 系统-数据字典类型表 Controller
 *
 * @author MrBird
 * @date 2019-12-08 20:56:08
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sysconf/sysconfDictType")
public class SysconfDictTypeController extends BaseController {

    @Autowired
    private ISysconfDictTypeService sysconfDictTypeService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysconfDict:view")
    public FebsResponse sysconfDictTypeList(QueryRequest request, SysconfDictType sysconfDictType) {
        Map<String, Object> dataTable = getDataTable(this.sysconfDictTypeService.findSysconfDictTypes(request, sysconfDictType));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑字典类型/按钮", exceptionMessage = "编辑字典类型失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysconfDictType:add","sysconfDictType:update"})
    public FebsResponse addSysconfDictType(@Valid SysconfDictType sysconfDictType) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
            sysconfDictType.setIsSysCreated(BaseBoolean.TRUE);
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        this.sysconfDictTypeService.saveSysconfDictType(sysconfDictType);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{sysconfDictTypeIds}")
    @RequiresPermissions("sysconfDictType:enable")
    @ControllerEndpoint(exceptionMessage = "启用字典类型失败")
    public FebsResponse enableSysconfDictType(@NotBlank(message = "{required}") @PathVariable String sysconfDictTypeIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfDictTypeIdArr = sysconfDictTypeIds.split(StringPool.COMMA);
        this.sysconfDictTypeService.enableSysconfDictType(sysconfDictTypeIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysconfDictTypeIds}")
    @RequiresPermissions("sysconfDictType:disable")
    @ControllerEndpoint(exceptionMessage = "禁用字典类型失败")
    public FebsResponse disableSysconfDictType(@NotBlank(message = "{required}") @PathVariable String sysconfDictTypeIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfDictTypeIdArr = sysconfDictTypeIds.split(StringPool.COMMA);
        this.sysconfDictTypeService.disableSysconfDictType(sysconfDictTypeIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("refreshCache")
    @RequiresPermissions("sysconfDictType:refreshCache")
    @ControllerEndpoint(exceptionMessage = "刷新缓存失败")
    public FebsResponse refreshCacheSysconfDictType() {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        this.sysconfDictTypeService.refreshCacheSysconfDictType();
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出字典类型", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("sysconfDictType:export")
    public void exportSysconfDictType(QueryRequest queryRequest, SysconfDictType sysconfDictType, HttpServletResponse response) {
        List<SysconfDictType> sysconfDictTypes = this.sysconfDictTypeService.findSysconfDictTypes(queryRequest, sysconfDictType).getRecords();
        ExcelKit.$Export(SysconfDictType.class, response).downXlsx(sysconfDictTypes, false);
    }
}
