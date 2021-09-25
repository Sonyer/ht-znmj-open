package com.handturn.bole.main.equipment.controller;

import com.alibaba.fastjson.JSONObject;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.main.equipment.entity.MiddlewareInfo;
import com.handturn.bole.main.equipment.service.IMiddlewareInfoService;
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
 * 基础资料-中间件管理 Controller
 *
 * @author Eric
 * @date 2020-12-02 11:13:52
 */
@Slf4j
@Validated
@RestController
@RequestMapping("main/equipment/middlewareInfo")
public class MiddlewareInfoController extends BaseController {

    @Autowired
    private IMiddlewareInfoService middlewareInfoService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("main:equipment:middlewareInfo:view")
    public FebsResponse middlewareInfoList(QueryRequest request, MiddlewareInfo middlewareInfo) {
        middlewareInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        middlewareInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        Map<String, Object> dataTable = getDataTable(this.middlewareInfoService.findMiddlewareInfos(request, middlewareInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("main:equipment:middlewareInfo:view")
    public void exportMiddlewareInfo(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        MiddlewareInfo middlewareInfo = JSONObject.parseObject(queryData,MiddlewareInfo.class);
        //-------控制导出权限-------begin
        middlewareInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        middlewareInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, MiddlewareInfo.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,middlewareInfo,
        request,response,
        "MiddlewareInfoService","findMiddlewareInfos",paramClassName);
    }
}
