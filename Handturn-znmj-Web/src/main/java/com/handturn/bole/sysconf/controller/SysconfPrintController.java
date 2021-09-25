package com.handturn.bole.sysconf.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.sysconf.entity.SysconfPrint;
import com.handturn.bole.sysconf.service.ISysconfPrintService;
import com.handturn.bole.system.entity.OcType;
import com.handturn.bole.system.entity.OrgType;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 系统配置-打印配置 Controller
 *
 * @author Eric
 * @date 2020-01-31 09:15:26
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sysconf/sysconfPrint")
public class SysconfPrintController extends BaseController {

    @Autowired
    private ISysconfPrintService sysconfPrintService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysconfPrint:view")
    public FebsResponse sysconfPrintList(QueryRequest request, SysconfPrint sysconfPrint) {
        Map<String, Object> dataTable = getDataTable(this.sysconfPrintService.findSysconfPrints(request, sysconfPrint));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑打印配置/按钮", exceptionMessage = "编辑打印配置失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysconfPrint:add","sysconfPrint:update"})
    public FebsResponse addSysconfPrintr(@Valid SysconfPrint sysconfPrint) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
            sysconfPrint.setIsSysCreated(BaseBoolean.TRUE);
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        this.sysconfPrintService.saveSysconfPrint(sysconfPrint);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{sysconfPrintIds}")
    @RequiresPermissions("sysconfPrint:enable")
    @ControllerEndpoint(exceptionMessage = "启用打印配置失败")
    public FebsResponse enableSysconfPrint(@NotBlank(message = "{required}") @PathVariable String sysconfPrintIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfPrintIdArr = sysconfPrintIds.split(StringPool.COMMA);
        this.sysconfPrintService.enableSysconfPrint(sysconfPrintIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysconfPrintIds}")
    @RequiresPermissions("sysconfPrint:disable")
    @ControllerEndpoint(exceptionMessage = "禁用打印配置失败")
    public FebsResponse disableSysconfPrint(@NotBlank(message = "{required}") @PathVariable String sysconfPrintIds) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        String[] sysconfPrintIdArr = sysconfPrintIds.split(StringPool.COMMA);
        this.sysconfPrintService.disableSysconfPrint(sysconfPrintIdArr);
        return new FebsResponse().success();
    }

    /**
     * 导入
     * @param fileField
     * @return
     */
    @ResponseBody
    @PostMapping("/importUpload")
    @RequiresPermissions("sysconfPrint:view")
    public FebsResponse importUpload (@RequestParam(value = "fileField", required = false) MultipartFile fileField) {
        try {
            ImportResultBean resultBean = sysconfPrintService.importUpload(fileField);
            return new FebsResponse().success().data(resultBean);
        }catch (Exception e){
            throw new FebsException(e.getMessage());
        }
    }
}
