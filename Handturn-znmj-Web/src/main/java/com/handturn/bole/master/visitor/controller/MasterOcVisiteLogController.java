package com.handturn.bole.master.visitor.controller;

import com.alibaba.fastjson.JSONObject;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.main.visitor.entity.OcVisiteLog;
import com.handturn.bole.main.visitor.service.IOcVisiteLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 网点-会员访客日志 Controller
 *
 * @author Eric
 * @date 2020-12-06 17:24:27
 */
@Slf4j
@Validated
@RestController
@RequestMapping("master/visitor/ocVisiteLog")
public class MasterOcVisiteLogController extends BaseController {

    @Autowired
    private IOcVisiteLogService ocVisiteLogService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("master:visitor:ocVisiteLog:view")
    public FebsResponse ocVisiteLogList(QueryRequest request, OcVisiteLog ocVisiteLog) {
        /*ocVisiteLog.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisiteLog.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());*/
        Map<String, Object> dataTable = getDataTable(this.ocVisiteLogService.findOcVisiteLogs(request, ocVisiteLog));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("master:visitor:ocVisiteLog:view")
    public void exportOcVisiteLog(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        OcVisiteLog ocVisiteLog = JSONObject.parseObject(queryData,OcVisiteLog.class);
        //-------控制导出权限-------begin
        /*ocVisiteLog.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisiteLog.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());*/
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, OcVisiteLog.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,ocVisiteLog,
        request,response,
        "OcVisiteLogService","findOcVisiteLogs",paramClassName);
    }
}
