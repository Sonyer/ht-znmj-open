package com.handturn.bole.master.equipment.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import lombok.extern.slf4j.Slf4j;
import com.handturn.bole.master.equipment.entity.EquipmentModel;
import com.handturn.bole.master.equipment.service.IEquipmentModelService;
import com.handturn.bole.common.utils.ExportReflectTo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 基础资料-设备型号管理 Controller
 *
 * @author Eric
 * @date 2020-09-11 08:20:08
 */
@Slf4j
@Validated
@RestController
@RequestMapping("master/equipment/equipmentModel")
public class EquipmentModelController extends BaseController {

    @Autowired
    private IEquipmentModelService equipmentModelService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("master:equipment:equipmentModel:view")
    public FebsResponse equipmentModelList(QueryRequest request, EquipmentModel equipmentModel) {
        Map<String, Object> dataTable = getDataTable(this.equipmentModelService.findEquipmentModels(request, equipmentModel));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("master:equipment:equipmentModel:view")
    public void exportEquipmentModel(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        EquipmentModel equipmentModel = JSONObject.parseObject(queryData,EquipmentModel.class);
        //-------控制导出权限-------begin
        //equipmentModel.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        //equipmentModel.setClientCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, EquipmentModel.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,equipmentModel,
        request,response,
        "EquipmentModelService","findEquipmentModels",paramClassName);
    }

    @ControllerEndpoint(operation = "编辑/按钮", exceptionMessage = "编辑失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"master:equipment:equipmentModel:add","master:equipment:equipmentModel:update"})
    public FebsResponse addEquipmentModelr(@Valid EquipmentModel equipmentModel) {
        this.equipmentModelService.saveEquipmentModel(equipmentModel);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{equipmentModelIds}")
    @RequiresPermissions("master:equipment:equipmentModel:enable")
    @ControllerEndpoint(exceptionMessage = "启用失败")
    public FebsResponse enableEquipmentModel(@NotBlank(message = "{required}") @PathVariable String equipmentModelIds) {
        String[] equipmentModelIdArr = equipmentModelIds.split(StringPool.COMMA);
        this.equipmentModelService.enableEquipmentModel(equipmentModelIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{equipmentModelIds}")
    @RequiresPermissions("master:equipment:equipmentModel:disable")
    @ControllerEndpoint(exceptionMessage = "禁用失败")
    public FebsResponse disableEquipmentModel(@NotBlank(message = "{required}") @PathVariable String equipmentModelIds) {
        String[] equipmentModelIdArr = equipmentModelIds.split(StringPool.COMMA);
        this.equipmentModelService.disableEquipmentModel(equipmentModelIdArr);
        return new FebsResponse().success();
    }
}
