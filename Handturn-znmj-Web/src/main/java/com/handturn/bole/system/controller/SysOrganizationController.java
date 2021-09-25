package com.handturn.bole.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.master.set.service.IImgStoreSetService;
import com.handturn.bole.sysconf.entity.SysconfQueryReport;
import com.handturn.bole.sysconf.service.ISysconfQueryReportService;
import com.handturn.bole.system.entity.OcType;
import com.handturn.bole.system.entity.OrgType;
import com.handturn.bole.system.entity.SysOrganization;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.service.ISysOganizationReportService;
import com.handturn.bole.system.service.ISysOganizationResourceService;
import com.handturn.bole.system.service.ISysOrganizationService;
import com.handturn.bole.system.service.ISysResourceService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统-组织 Controller
 *
 * @author MrBird
 * @date 2019-12-08 10:00:07
 */
@Slf4j
@Validated
@RestController
@RequestMapping("system/sysOrganization")
public class SysOrganizationController extends BaseController {

    @Autowired
    private ISysOrganizationService sysOrganizationService;
    @Autowired
    private ISysResourceService sysResourceService;
    @Autowired
    private ISysOganizationResourceService sysOganizationResourceService;
    @Autowired
    private ISysOganizationReportService sysOganizationReportService;

    @Autowired
    private ISysconfQueryReportService sysconfQueryReportService;

    @Autowired
    private IImgStoreSetService imgStoreSetService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysOrganization:view")
    public FebsResponse sysOrganizationList(QueryRequest request, SysOrganization sysOrganization) {
        Map<String, Object> dataTable = getDataTable(this.sysOrganizationService.findSysOrganizations(request, sysOrganization));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑组织/按钮", exceptionMessage = "编辑组织失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysOrganization:add","sysOrganization:update"})
    public FebsResponse addSysOrganization(@Valid SysOrganization sysOrganization) {
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }else{
            throw new FebsException("非系统组织不允许操作!");
        }
        this.sysOrganizationService.saveSysOrganization(sysOrganization);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{sysOrganizationIds}")
    @RequiresPermissions("sysOrganization:enable")
    @ControllerEndpoint(exceptionMessage = "启用组织失败")
    public FebsResponse enableSysOrganization(@NotBlank(message = "{required}") @PathVariable String sysOrganizationIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            throw new FebsException("非系统组织不允许操作!");
        }else{
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }
        String[] sysOrganizationIdArr = sysOrganizationIds.split(StringPool.COMMA);
        this.sysOrganizationService.enableSysOrganization(sysOrganizationIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{sysOrganizationIds}")
    @RequiresPermissions("sysOrganization:disable")
    @ControllerEndpoint(exceptionMessage = "禁用组织失败")
    public FebsResponse disableSysOrganization(@NotBlank(message = "{required}") @PathVariable String sysOrganizationIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            throw new FebsException("非系统组织不允许操作!");
        }else{
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }
        String[] sysOrganizationIdArr = sysOrganizationIds.split(StringPool.COMMA);
        this.sysOrganizationService.disableSysOrganization(sysOrganizationIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出组织", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("sysOrganization:export")
    public void exportSysOrganization(QueryRequest queryRequest, SysOrganization sysOrganization, HttpServletResponse response) {
        List<SysOrganization> sysOrganizations = this.sysOrganizationService.findSysOrganizations(queryRequest, sysOrganization).getRecords();
        ExcelKit.$Export(SysOrganization.class, response).downXlsx(sysOrganizations, false);
    }

    @PostMapping("resourceTree")
    @RequiresPermissions("sysOrganization:auth")
    @ControllerEndpoint(exceptionMessage = "获取菜单树失败")
    public FebsResponse getResourceTree(String orgId,String rootRourceId) {

        Set<Long> resourceIds = sysOganizationResourceService.findSysResourceIds4Org(Long.valueOf(orgId));
        CommonTree<SysResource> sysResources = this.sysResourceService.findSysResourcesByRootId(rootRourceId,resourceIds);

        return new FebsResponse().success().data(sysResources.getChilds());
    }

    @PostMapping("saveOrgResource")
    @RequiresPermissions("sysOrganization:auth")
    @ControllerEndpoint(exceptionMessage = "保存组织菜单")
    public FebsResponse saveOrgResource(String orgId,String rootModuleId,String resourceIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            throw new FebsException("非系统组织不允许操作!");
        }else{
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }
        String[] resourceIdArr = resourceIds.split(",");
        sysOganizationResourceService.saveOrgResource(Long.valueOf(orgId),Long.valueOf(rootModuleId),resourceIdArr);
        return new FebsResponse().success().data(null);
    }

    @PostMapping("reportTree")
    @RequiresPermissions("sysOrganization:report")
    @ControllerEndpoint(exceptionMessage = "获取报表树失败")
    public FebsResponse getReportTree(String orgId,String rootReportId) {

        Set<Long> reportIds = sysOganizationReportService.findSysReportIds4Org(Long.valueOf(orgId));
        CommonTree<SysconfQueryReport> sysReports = this.sysconfQueryReportService.findReportsByRootId(rootReportId,reportIds);

        return new FebsResponse().success().data(sysReports.getChilds());
    }

    @PostMapping("saveOrgReport")
    @RequiresPermissions("sysOrganization:report")
    @ControllerEndpoint(exceptionMessage = "保存组织报表")
    public FebsResponse saveOrgReport(String orgId,String rootReportId,String reportIds) {
        if(!UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){
            throw new FebsException("非系统组织不允许操作!");
        }else{
            if(!UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
                    && !UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)){
                throw new FebsException("非系统管理不允许操作!");
            }
        }
        String[] reportIdArr = reportIds.split(",");
        sysOganizationReportService.saveOrgReport(Long.valueOf(orgId),Long.valueOf(rootReportId),reportIdArr);
        return new FebsResponse().success().data(null);
    }

    /**
     * 导入
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping("/importUpload")
    @RequiresPermissions({"sysOrganization:add","sysOrganization:update"})
    public FebsResponse importUpload (@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            //ImportResultBean resultBean = sysOrganizationService.importUpload(file);
            ImportResultBean resultBean = imgStoreSetService.importUpload(file, IImgStoreSetService.IMGS_ORG_LOGO);
            return new FebsResponse().success().data(resultBean);
        }catch (Exception e){
            throw new FebsException(e.getMessage());
        }
    }

    @RequestMapping(value="/logoImgShow/{logoFileName}")
    @RequiresPermissions("sysOrganization:view")
    public void logoImgShow(@NotBlank(message = "{required}") @PathVariable String logoFileName,HttpServletRequest request, HttpServletResponse response){
        try {
            sysOrganizationService.logoImgShow(logoFileName,request, response);
        }catch (Exception e){
            throw new FebsException(e.getMessage());
        }
    }
}
