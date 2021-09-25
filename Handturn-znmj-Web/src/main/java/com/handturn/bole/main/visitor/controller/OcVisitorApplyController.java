package com.handturn.bole.main.visitor.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.main.visitor.entity.OcVisitorApply;
import com.handturn.bole.main.visitor.service.IOcVisitorApplyService;
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
 * 网点-会员访客审核 Controller
 *
 * @author Eric
 * @date 2020-09-22 08:57:57
 */
@Slf4j
@Validated
@RestController
@RequestMapping("main/visitor/ocVisitorApply")
public class OcVisitorApplyController extends BaseController {

    @Autowired
    private IOcVisitorApplyService ocVisitorApplyService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("main:visitor:ocVisitorApply:view")
    public FebsResponse ocVisitorApplyList(QueryRequest request, OcVisitorApply ocVisitorApply) {
        ocVisitorApply.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisitorApply.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        Map<String, Object> dataTable = getDataTable(this.ocVisitorApplyService.findOcVisitorApplys(request, ocVisitorApply));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("main:visitor:ocVisitorApply:view")
    public void exportOcVisitorApply(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        OcVisitorApply ocVisitorApply = JSONObject.parseObject(queryData,OcVisitorApply.class);
        //-------控制导出权限-------begin
        ocVisitorApply.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisitorApply.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, OcVisitorApply.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,ocVisitorApply,
        request,response,
        "OcVisitorApplyService","findOcVisitorApplys",paramClassName);
    }

    @ControllerEndpoint(operation = "编辑/按钮", exceptionMessage = "编辑失败")
    @PostMapping("auditSave")
    @ResponseBody
    @RequiresPermissions("main:visitor:ocVisitorApply:audit")
    public FebsResponse auditSave(@Valid OcVisitorApply ocVisitorApply) {
        ocVisitorApply.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisitorApply.setOrgName(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName());
        ocVisitorApply.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        ocVisitorApply.setOcName(UserInfoHolder.getUserInfo().getCurrentOc().getOcName());
        this.ocVisitorApplyService.auditSave(ocVisitorApply);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "编辑/按钮", exceptionMessage = "编辑失败")
    @PostMapping("auditsSave")
    @ResponseBody
    @RequiresPermissions("main:visitor:ocVisitorApply:audit")
    public FebsResponse auditsSave(@Valid OcVisitorApply ocVisitorApply) {
        ocVisitorApply.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisitorApply.setOrgName(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName());
        ocVisitorApply.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        ocVisitorApply.setOcName(UserInfoHolder.getUserInfo().getCurrentOc().getOcName());
        this.ocVisitorApplyService.auditsSave(ocVisitorApply);
        return new FebsResponse().success();
    }


}
