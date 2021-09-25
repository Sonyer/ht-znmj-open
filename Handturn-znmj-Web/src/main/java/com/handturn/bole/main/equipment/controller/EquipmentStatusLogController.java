package com.handturn.bole.main.equipment.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.main.equipment.entity.EquipmentStatusLog;
import com.handturn.bole.main.equipment.service.IEquipmentStatusLogService;
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
 * 基础资料-设备状态日志 Controller
 *
 * @author Eric
 * @date 2020-09-11 08:20:34
 */
@Slf4j
@Validated
@RestController
@RequestMapping("equipmentStatusLog")
public class EquipmentStatusLogController extends BaseController {

    @Autowired
    private IEquipmentStatusLogService equipmentStatusLogService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("equipmentStatusLog:view")
    public FebsResponse equipmentStatusLogList(QueryRequest request, EquipmentStatusLog equipmentStatusLog) {
        Map<String, Object> dataTable = getDataTable(this.equipmentStatusLogService.findEquipmentStatusLogs(request, equipmentStatusLog));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("equipmentStatusLog:export")
    public void exportEquipmentStatusLog(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        EquipmentStatusLog equipmentStatusLog = JSONObject.parseObject(queryData,EquipmentStatusLog.class);
        //-------控制导出权限-------begin
        equipmentStatusLog.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        //equipmentStatusLog.setClientCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, EquipmentStatusLog.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,equipmentStatusLog,
        request,response,
        "EquipmentStatusLogService","findEquipmentStatusLogs",paramClassName);
    }

    @ControllerEndpoint(operation = "编辑EquipmentStatusLog/按钮", exceptionMessage = "编辑EquipmentStatusLog失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"equipmentStatusLog:add","equipmentStatusLog:update"})
    public FebsResponse addEquipmentStatusLogr(@Valid EquipmentStatusLog equipmentStatusLog) {
        this.equipmentStatusLogService.saveEquipmentStatusLog(equipmentStatusLog);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{equipmentStatusLogIds}")
    @RequiresPermissions("equipmentStatusLog:enable")
    @ControllerEndpoint(exceptionMessage = "启用EquipmentStatusLog失败")
    public FebsResponse enableEquipmentStatusLog(@NotBlank(message = "{required}") @PathVariable String equipmentStatusLogIds) {
        String[] equipmentStatusLogIdArr = equipmentStatusLogIds.split(StringPool.COMMA);
        this.equipmentStatusLogService.enableEquipmentStatusLog(equipmentStatusLogIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{equipmentStatusLogIds}")
    @RequiresPermissions("equipmentStatusLog:disable")
    @ControllerEndpoint(exceptionMessage = "禁用EquipmentStatusLog失败")
    public FebsResponse disableEquipmentStatusLog(@NotBlank(message = "{required}") @PathVariable String equipmentStatusLogIds) {
        String[] equipmentStatusLogIdArr = equipmentStatusLogIds.split(StringPool.COMMA);
        this.equipmentStatusLogService.disableEquipmentStatusLog(equipmentStatusLogIdArr);
        return new FebsResponse().success();
    }
}
