package com.handturn.bole.main.area.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.main.area.entity.AreaInfo;
import com.handturn.bole.main.area.service.IAreaInfoService;
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
 * 基础资料-区域管理 Controller
 *
 * @author Eric
 * @date 2020-09-11 08:20:03
 */
@Slf4j
@Validated
@RestController
@RequestMapping("main/area/areaInfo")
public class MainAreaInfoController extends BaseController {

    @Autowired
    private IAreaInfoService areaInfoService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("main:area:areaInfo:view")
    public FebsResponse areaInfoList(QueryRequest request, AreaInfo areaInfo) {
        areaInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        areaInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        Map<String, Object> dataTable = getDataTable(this.areaInfoService.findAreaInfos(request, areaInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("main:area:areaInfo:view")
    public void exportAreaInfo(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        AreaInfo areaInfo = JSONObject.parseObject(queryData,AreaInfo.class);
        //-------控制导出权限-------begin
        areaInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        areaInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, AreaInfo.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,areaInfo,
        request,response,
        "AreaInfoService","findAreaInfos",paramClassName);
    }

    @ControllerEndpoint(operation = "编辑/按钮", exceptionMessage = "编辑失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"main:area:areaInfo:add","main:area:areaInfo:update"})
    public FebsResponse addAreaInfor(@Valid AreaInfo areaInfo) {
        areaInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        areaInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        areaInfo.setOrgName(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName());
        areaInfo.setOcName(UserInfoHolder.getUserInfo().getCurrentOc().getOcName());
        this.areaInfoService.saveAreaInfo(areaInfo);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{areaInfoIds}")
    @RequiresPermissions("main:area:areaInfo:enable")
    @ControllerEndpoint(exceptionMessage = "启用失败")
    public FebsResponse enableAreaInfo(@NotBlank(message = "{required}") @PathVariable String areaInfoIds) {
        String[] areaInfoIdArr = areaInfoIds.split(StringPool.COMMA);
        this.areaInfoService.enableAreaInfo(areaInfoIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{areaInfoIds}")
    @RequiresPermissions("main:area:areaInfo:disable")
    @ControllerEndpoint(exceptionMessage = "禁用失败")
    public FebsResponse disableAreaInfo(@NotBlank(message = "{required}") @PathVariable String areaInfoIds) {
        String[] areaInfoIdArr = areaInfoIds.split(StringPool.COMMA);
        this.areaInfoService.disableAreaInfo(areaInfoIdArr);
        return new FebsResponse().success();
    }
}
