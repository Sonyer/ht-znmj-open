package com.handturn.bole.master.equipment.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.main.equipment.entity.EquipmentInfo;
import com.handturn.bole.main.equipment.service.IEquipmentInfoService;
import com.handturn.bole.system.entity.OcType;
import com.handturn.bole.system.entity.SysOrganization;
import com.handturn.bole.system.entity.SysOrganizationOc;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基础资料-设备管理 Controller
 *
 * @author Eric
 * @date 2020-09-11 08:20:12
 */
@Slf4j
@Validated
@RestController
@RequestMapping("master/equipment/equipmentInfo")
public class MasterEquipmentInfoController extends BaseController {

    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private ISysOrganizationService sysOrganizationService;
    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("master:equipment:equipmentInfo:view")
    public FebsResponse equipmentInfoList(QueryRequest request, EquipmentInfo equipmentInfo) {
        equipmentInfo.setAuthCheck(false);
        Map<String, Object> dataTable = getDataTable(this.equipmentInfoService.findEquipmentInfos(request, equipmentInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("master:equipment:equipmentInfo:view")
    public void exportEquipmentInfo(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        EquipmentInfo equipmentInfo = JSONObject.parseObject(queryData,EquipmentInfo.class);
        //-------控制导出权限-------begin
        //equipmentInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        //equipmentInfo.setClientCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, EquipmentInfo.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,equipmentInfo,
        request,response,
        "EquipmentInfoService","findEquipmentInfos",paramClassName);
    }

    @ControllerEndpoint(operation = "编辑/按钮", exceptionMessage = "编辑失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"master:equipment:equipmentInfo:add","master:equipment:equipmentInfo:update"})
    public FebsResponse addEquipmentInfor(@Valid EquipmentInfo equipmentInfo) {

        if(!StringUtils.isEmpty(equipmentInfo.getOrgCode())){
            SysOrganization org = sysOrganizationService.findSysOrganizationByCode(equipmentInfo.getOrgCode());
            if(org == null){
                throw new FebsException("组织未找到!");
            }else{
                equipmentInfo.setOrgName(org.getOrgName());
            }

            if(StringUtils.isEmpty(equipmentInfo.getOcCode())){
                throw new FebsException("请选择网点!");
            }
        }

        if(!StringUtils.isEmpty(equipmentInfo.getOcCode())){
            SysOrganizationOc oc = sysOrganizationOcService.findSysOrganizationOcByCode(equipmentInfo.getOcCode());
            if(oc == null){
                throw new FebsException("网点未找到!");
            }else{
                equipmentInfo.setOcName(oc.getOcName());
            }
        }


        this.equipmentInfoService.saveEquipmentInfo(equipmentInfo);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "通过组织获取网点", exceptionMessage = "通过组织获取网点失败")
    @PostMapping("/findOcByOrgCode")
    @ResponseBody
    @RequiresPermissions({"master:equipment:equipmentInfo:view"})
    public FebsResponse findOcByOrgCode(String orgCode) {
        SysOrganization organization = sysOrganizationService.findSysOrganizationByCode(orgCode);
        Set<String> ocTypes = new HashSet<String>();
        ocTypes.add(OcType.ORG_CN);
        ocTypes.add(OcType.ORG_OC);
        ocTypes.add(OcType.ORG_3TH);
        List<OptionVo> sysOrganizationOcsOption = sysOrganizationOcService.findOptionVoByOcType(ocTypes,organization.getId());
        return new FebsResponse().success().data(sysOrganizationOcsOption);
    }
}
