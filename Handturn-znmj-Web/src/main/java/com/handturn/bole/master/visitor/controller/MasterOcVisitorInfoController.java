package com.handturn.bole.master.visitor.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.main.authArea.entity.OcAuthArea;
import com.handturn.bole.main.authArea.service.IOcAuthAreaService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaVisitorService;
import com.handturn.bole.main.visitor.entity.OcVisitorCreateType;
import com.handturn.bole.main.visitor.entity.OcVisitorInfo;
import com.handturn.bole.main.visitor.entity.OcVisitorUploadImg;
import com.handturn.bole.main.visitor.entity.OcVisitorUploadImgStatus;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import com.handturn.bole.main.visitor.service.IOcVisitorUploadImgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 网点-会员访客信息 Controller
 *
 * @author Eric
 * @date 2020-09-22 08:57:38
 */
@Slf4j
@Validated
@RestController
@RequestMapping("master/visitor/ocVisitorInfo")
public class MasterOcVisitorInfoController extends BaseController {

    @Autowired
    private IOcVisitorInfoService ocVisitorInfoService;

    @Autowired
    private IOcVisitorUploadImgService ocVisitorUploadImgService;

    @Autowired
    private IOcAuthAreaVisitorService ocAuthAreaVisitorService;

    @Autowired
    private IOcAuthAreaService ocAuthAreaService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("master:visitor:ocVisitorInfo:view")
    public FebsResponse ocVisitorInfoList(QueryRequest request, OcVisitorInfo ocVisitorInfo) {
        Map<String, Object> dataTable = getDataTable(this.ocVisitorInfoService.findOcVisitorInfos(request, ocVisitorInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("master:visitor:ocVisitorInfo:view")
    public void exportOcVisitorInfo(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        OcVisitorInfo ocVisitorInfo = JSONObject.parseObject(queryData,OcVisitorInfo.class);
        //-------控制导出权限-------begin
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, OcVisitorInfo.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,ocVisitorInfo,
        request,response,
        "OcVisitorInfoService","findOcVisitorInfos",paramClassName);
    }

    @GetMapping("ocAuthAreaList")
    @ResponseBody
    @RequiresPermissions("master:visitor:ocVisitorInfo:view")
    public FebsResponse ocAuthAreaList(QueryRequest request, OcAuthArea ocAuthArea) {
        ocAuthArea.setAuthCheck(true);
        Map<String, Object> dataTable = getDataTable(this.ocAuthAreaService.findOcAuthAreas(request, ocAuthArea));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("ocAuthAreaExport")
    @RequiresPermissions("master:visitor:ocVisitorInfo:view")
    public void ocAuthAreaExport(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        OcAuthArea ocAuthArea = JSONObject.parseObject(queryData,OcAuthArea.class);
        //-------控制导出权限-------begin
        ocAuthArea.setAuthCheck(true);
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, OcAuthArea.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,ocAuthArea,
                request,response,
                "OcAuthAreaService","findOcAuthAreas",paramClassName);
    }

}
