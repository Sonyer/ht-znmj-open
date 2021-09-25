package com.handturn.bole.main.authArea.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.main.authArea.entity.OcAuthArea;
import com.handturn.bole.main.authArea.entity.OcAuthAreaResponsible;
import com.handturn.bole.main.authArea.service.IOcAuthAreaEquipmentService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaResponsibleService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaVisitorService;
import com.handturn.bole.main.equipment.entity.EquipmentInfo;
import com.handturn.bole.main.equipment.service.IEquipmentInfoService;
import com.handturn.bole.main.visitor.entity.OcVisitorInfo;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 网点-权限区域 Controller
 *
 * @author Eric
 * @date 2020-09-22 08:57:47
 */
@Slf4j
@Validated
@RestController
@RequestMapping("main/authArea/ocAuthArea")
public class OcAuthAreaController extends BaseController {

    @Autowired
    private IOcAuthAreaService ocAuthAreaService;
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IOcVisitorInfoService ocVisitorInfoService;
    @Autowired
    private IOcAuthAreaVisitorService ocAuthAreaVisitorService;
    @Autowired
    private IOcAuthAreaEquipmentService ocAuthAreaEquipmentService;

    @Autowired
    private IOcAuthAreaResponsibleService ocAuthAreaResponsibleService;
    @Autowired
    private ISysUserService sysUserService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("main:authArea:ocAuthArea:view")
    public FebsResponse ocAuthAreaList(QueryRequest request, OcAuthArea ocAuthArea) {
        ocAuthArea.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocAuthArea.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        Map<String, Object> dataTable = getDataTable(this.ocAuthAreaService.findOcAuthAreas(request, ocAuthArea));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("main:authArea:ocAuthArea:view")
    public void exportOcAuthArea(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        OcAuthArea ocAuthArea = JSONObject.parseObject(queryData,OcAuthArea.class);
        //-------控制导出权限-------begin
        ocAuthArea.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocAuthArea.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, OcAuthArea.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,ocAuthArea,
        request,response,
        "OcAuthAreaService","findOcAuthAreas",paramClassName);
    }

    @GetMapping("equipmentList")
    @ResponseBody
    @RequiresPermissions("main:authArea:ocAuthArea:auth")
    public FebsResponse equipmentInfoList(QueryRequest request, EquipmentInfo equipmentInfo) {
        equipmentInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        equipmentInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        equipmentInfo.setAuthCheck(true);
        Map<String, Object> dataTable = getDataTable(this.equipmentInfoService.findEquipmentInfos(request, equipmentInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("equipmentExport")
    @RequiresPermissions("main:authArea:ocAuthArea:auth")
    public void exportEquipmentInfo(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        EquipmentInfo equipmentInfo = JSONObject.parseObject(queryData,EquipmentInfo.class);
        //-------控制导出权限-------begin
        equipmentInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        equipmentInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, EquipmentInfo.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,equipmentInfo,
                request,response,
                "EquipmentInfoService","findEquipmentInfos",paramClassName);
    }

    @GetMapping("ocVisitorInfoList")
    @ResponseBody
    @RequiresPermissions("main:authArea:ocAuthArea:auth")
    public FebsResponse ocVisitorInfoList(QueryRequest request, OcVisitorInfo ocVisitorInfo) {
        ocVisitorInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisitorInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        ocVisitorInfo.setAuthCheck(true);
        Map<String, Object> dataTable = getDataTable(this.ocVisitorInfoService.findOcVisitorInfos(request, ocVisitorInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("ocVisitorInfoExport")
    @RequiresPermissions("main:authArea:ocAuthArea:auth")
    public void exportOcVisitorInfo(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        OcVisitorInfo ocVisitorInfo = JSONObject.parseObject(queryData,OcVisitorInfo.class);
        //-------控制导出权限-------begin
        ocVisitorInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisitorInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, OcVisitorInfo.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,ocVisitorInfo,
                request,response,
                "OcVisitorInfoService","findOcVisitorInfos",paramClassName);
    }

    @ControllerEndpoint(operation = "编辑/按钮", exceptionMessage = "编辑失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"main:authArea:ocAuthArea:add","main:authArea:ocAuthArea:update"})
    public FebsResponse addOcAuthArear(@Valid OcAuthArea ocAuthArea) {
        ocAuthArea.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocAuthArea.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        ocAuthArea.setOrgName(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName());
        ocAuthArea.setOcName(UserInfoHolder.getUserInfo().getCurrentOc().getOcName());
        if(ocAuthArea.getId() == null || ocAuthArea.getId() == 0L){
            ocAuthArea.setStatus(BaseStatus.ENABLED);
        }

        this.ocAuthAreaService.saveOcAuthArea(ocAuthArea);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{ocAuthAreaIds}")
    @RequiresPermissions("main:authArea:ocAuthArea:enable")
    @ControllerEndpoint(exceptionMessage = "启用失败")
    public FebsResponse enableOcAuthArea(@NotBlank(message = "{required}") @PathVariable String ocAuthAreaIds) {
        String[] ocAuthAreaIdArr = ocAuthAreaIds.split(StringPool.COMMA);
        this.ocAuthAreaService.enableOcAuthArea(ocAuthAreaIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{ocAuthAreaIds}")
    @RequiresPermissions("main:authArea:ocAuthArea:disable")
    @ControllerEndpoint(exceptionMessage = "禁用失败")
    public FebsResponse disableOcAuthArea(@NotBlank(message = "{required}") @PathVariable String ocAuthAreaIds) {
        String[] ocAuthAreaIdArr = ocAuthAreaIds.split(StringPool.COMMA);
        this.ocAuthAreaService.disableOcAuthArea(ocAuthAreaIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("authOcVisitors")
    @RequiresPermissions("main:authArea:ocAuthArea:auth")
    @ControllerEndpoint(exceptionMessage = "授权失败")
    public FebsResponse authOcVisitors(String authAreaId,String ocVisitorIds) {
        String[] ocVisitorIdsArr = ocVisitorIds.split(StringPool.COMMA);
        String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
        String orgName = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName();
        String ocName = UserInfoHolder.getUserInfo().getCurrentOc().getOcName();
        this.ocAuthAreaVisitorService.authOcVisitors(authAreaId,ocVisitorIdsArr,orgCode,ocCode,orgName,ocName);
        return new FebsResponse().success();
    }

    @PostMapping("cancelAuthOcVisitors")
    @RequiresPermissions("main:authArea:ocAuthArea:auth")
    @ControllerEndpoint(exceptionMessage = "取消授权失败")
    public FebsResponse cancelAuthOcVisitors(String authAreaId,String ocVisitorIds) {
        String[] ocVisitorIdsArr = ocVisitorIds.split(StringPool.COMMA);
        String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
        String orgName = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName();
        String ocName = UserInfoHolder.getUserInfo().getCurrentOc().getOcName();
        this.ocAuthAreaVisitorService.cancelAuthOcVisitors(authAreaId,ocVisitorIdsArr,orgCode,ocCode,orgName,ocName);
        return new FebsResponse().success();
    }

    @PostMapping("authOcEquipments")
    @RequiresPermissions("main:authArea:ocAuthArea:auth")
    @ControllerEndpoint(exceptionMessage = "授权失败")
    public FebsResponse authOcEquipments(String authAreaId,String equipmentIds) {
        String[] equipmentIdsArr = equipmentIds.split(StringPool.COMMA);
        String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
        String orgName = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName();
        String ocName = UserInfoHolder.getUserInfo().getCurrentOc().getOcName();
        this.ocAuthAreaEquipmentService.authOcEquipments(authAreaId,equipmentIdsArr,orgCode,ocCode,orgName,ocName);
        return new FebsResponse().success();
    }

    @PostMapping("cancelAuthOcEquipments")
    @RequiresPermissions("main:authArea:ocAuthArea:auth")
    @ControllerEndpoint(exceptionMessage = "取消授权失败")
    public FebsResponse cancelAuthOcEquipments(String authAreaId,String equipmentIds) {
        String[] equipmentIdsArr = equipmentIds.split(StringPool.COMMA);
        String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
        String orgName = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName();
        String ocName = UserInfoHolder.getUserInfo().getCurrentOc().getOcName();
        this.ocAuthAreaEquipmentService.cancelAuthOcEquipments(authAreaId,equipmentIdsArr,orgCode,ocCode,orgName,ocName);
        return new FebsResponse().success();
    }

    @GetMapping("responsibleList")
    @ResponseBody
    @RequiresPermissions("main:authArea:ocAuthArea:view")
    public FebsResponse responsibleList(QueryRequest request, OcAuthAreaResponsible ocAuthAreaResponsible) {
        ocAuthAreaResponsible.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocAuthAreaResponsible.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        Map<String, Object> dataTable = getDataTable(this.ocAuthAreaResponsibleService.findOcAuthAreaResponsibles(request, ocAuthAreaResponsible));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("responsibleExport")
    @RequiresPermissions("main:authArea:ocAuthArea:view")
    public void responsibleExport(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        OcAuthAreaResponsible ocAuthAreaResponsible = JSONObject.parseObject(queryData,OcAuthAreaResponsible.class);
        //-------控制导出权限-------begin
        ocAuthAreaResponsible.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocAuthAreaResponsible.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, OcAuthAreaResponsible.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,ocAuthAreaResponsible,
                request,response,
                "OcAuthAreaResponsibleService","findOcAuthAreaResponsibles",paramClassName);
    }

    @ControllerEndpoint(operation = "添加负责人/按钮", exceptionMessage = "添加负责人失败")
    @PostMapping("responsibleSave")
    @ResponseBody
    @RequiresPermissions({"main:authArea:ocAuthArea:add","main:authArea:ocAuthArea:update"})
    public FebsResponse responsibleSave(@Valid OcAuthAreaResponsible ocAuthAreaResponsible) {
        ocAuthAreaResponsible.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocAuthAreaResponsible.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        SysUser user = sysUserService.findByUserCode(ocAuthAreaResponsible.getUserCode());
        if(user == null){
            throw new FebsException("用户未找到!");
        }
        ocAuthAreaResponsible.setUserId(user.getId());
        this.ocAuthAreaResponsibleService.saveOcAuthAreaResponsible(ocAuthAreaResponsible);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "移除负责人/按钮", exceptionMessage = "移除负责人失败")
    @PostMapping("deleteResponsible/{responsibleIds}")
    @ResponseBody
    @RequiresPermissions({"main:authArea:ocAuthArea:add","main:authArea:ocAuthArea:update"})
    public FebsResponse deleteResponsible(@NotBlank(message = "{required}") @PathVariable String responsibleIds) {
        String[] responsibleIdsArr = responsibleIds.split(StringPool.COMMA);
        String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
        this.ocAuthAreaResponsibleService.deleteResponsible(responsibleIdsArr,orgCode,ocCode);
        return new FebsResponse().success();
    }



}
