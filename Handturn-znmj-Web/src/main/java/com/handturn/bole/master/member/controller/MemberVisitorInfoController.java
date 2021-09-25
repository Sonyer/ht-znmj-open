package com.handturn.bole.master.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.master.member.entity.MemberVisitorInfo;
import com.handturn.bole.master.member.service.IMemberVisitorInfoService;
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
 * 会员-会员访客信息 Controller
 *
 * @author Eric
 * @date 2020-09-22 08:57:18
 */
@Slf4j
@Validated
@RestController
@RequestMapping("memberVisitorInfo")
public class MemberVisitorInfoController extends BaseController {

    @Autowired
    private IMemberVisitorInfoService memberVisitorInfoService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("memberVisitorInfo:view")
    public FebsResponse memberVisitorInfoList(QueryRequest request, MemberVisitorInfo memberVisitorInfo) {
        Map<String, Object> dataTable = getDataTable(this.memberVisitorInfoService.findMemberVisitorInfos(request, memberVisitorInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("memberVisitorInfo:export")
    public void exportMemberVisitorInfo(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        MemberVisitorInfo memberVisitorInfo = JSONObject.parseObject(queryData,MemberVisitorInfo.class);
        //-------控制导出权限-------begin
        //memberVisitorInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        //memberVisitorInfo.setClientCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, MemberVisitorInfo.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,memberVisitorInfo,
        request,response,
        "MemberVisitorInfoService","findMemberVisitorInfos",paramClassName);
    }

    @ControllerEndpoint(operation = "编辑MemberVisitorInfo/按钮", exceptionMessage = "编辑MemberVisitorInfo失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"memberVisitorInfo:add","memberVisitorInfo:update"})
    public FebsResponse addMemberVisitorInfor(@Valid MemberVisitorInfo memberVisitorInfo) {
        this.memberVisitorInfoService.saveMemberVisitorInfo(memberVisitorInfo);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{memberVisitorInfoIds}")
    @RequiresPermissions("memberVisitorInfo:enable")
    @ControllerEndpoint(exceptionMessage = "启用MemberVisitorInfo失败")
    public FebsResponse enableMemberVisitorInfo(@NotBlank(message = "{required}") @PathVariable String memberVisitorInfoIds) {
        String[] memberVisitorInfoIdArr = memberVisitorInfoIds.split(StringPool.COMMA);
        this.memberVisitorInfoService.enableMemberVisitorInfo(memberVisitorInfoIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{memberVisitorInfoIds}")
    @RequiresPermissions("memberVisitorInfo:disable")
    @ControllerEndpoint(exceptionMessage = "禁用MemberVisitorInfo失败")
    public FebsResponse disableMemberVisitorInfo(@NotBlank(message = "{required}") @PathVariable String memberVisitorInfoIds) {
        String[] memberVisitorInfoIdArr = memberVisitorInfoIds.split(StringPool.COMMA);
        this.memberVisitorInfoService.disableMemberVisitorInfo(memberVisitorInfoIdArr);
        return new FebsResponse().success();
    }
}
