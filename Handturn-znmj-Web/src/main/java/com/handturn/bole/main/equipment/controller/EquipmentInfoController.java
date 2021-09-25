package com.handturn.bole.main.equipment.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.main.equipment.entity.EquipmentInfo;
import com.handturn.bole.main.equipment.service.IEquipmentInfoService;
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
 * 基础资料-设备管理 Controller
 *
 * @author Eric
 * @date 2020-09-11 08:20:12
 */
@Slf4j
@Validated
@RestController
@RequestMapping("main/equipment/equipmentInfo")
public class EquipmentInfoController extends BaseController {

    @Autowired
    private IEquipmentInfoService equipmentInfoService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("main:equipment:equipmentInfo:view")
    public FebsResponse equipmentInfoList(QueryRequest request, EquipmentInfo equipmentInfo) {
        equipmentInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        equipmentInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        equipmentInfo.setAuthCheck(false);
        Map<String, Object> dataTable = getDataTable(this.equipmentInfoService.findEquipmentInfos(request, equipmentInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("main:equipment:equipmentInfo:view")
    public void exportEquipmentInfo(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        EquipmentInfo equipmentInfo = JSONObject.parseObject(queryData,EquipmentInfo.class);
        //-------控制导出权限-------begin
        equipmentInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        equipmentInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //equipmentInfo.setClientCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, EquipmentInfo.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,equipmentInfo,
        request,response,
        "EquipmentInfoService","findEquipmentInfos",paramClassName);
    }

    @ControllerEndpoint(operation = "安装/按钮", exceptionMessage = "安装失败")
    @PostMapping("erectSave")
    @ResponseBody
    @RequiresPermissions("main:equipment:equipmentInfo:erect")
    public FebsResponse erectSave(@Valid EquipmentInfo equipmentInfo) {
        this.equipmentInfoService.erect(equipmentInfo);
        return new FebsResponse().success();
    }


    @PostMapping("down/{equipmentInfoIds}")
    @RequiresPermissions("main:equipment:equipmentInfo:down")
    @ControllerEndpoint(exceptionMessage = "拆卸失败")
    public FebsResponse down(@NotBlank(message = "{required}") @PathVariable String equipmentInfoIds) {
        String[] equipmentInfoIdArr = equipmentInfoIds.split(StringPool.COMMA);
        this.equipmentInfoService.down(equipmentInfoIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("remoteOpen/{equipmentInfoIds}")
    @RequiresPermissions("main:equipment:equipmentInfo:remoteOpen")
    @ControllerEndpoint(exceptionMessage = "远程开门")
    public FebsResponse remoteOpen(@NotBlank(message = "{required}") @PathVariable String equipmentInfoIds) {
        String[] equipmentInfoIdArr = equipmentInfoIds.split(StringPool.COMMA);
        this.equipmentInfoService.remoteOpen(equipmentInfoIdArr);
        return new FebsResponse().success();
    }


}
