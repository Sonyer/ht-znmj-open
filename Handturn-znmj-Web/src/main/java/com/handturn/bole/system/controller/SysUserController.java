package com.handturn.bole.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.service.ISysUserService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统-用户 Controller
 *
 * @author Eric
 * @date 2019-12-01 10:46:37
 */
@Slf4j
@Validated
@RestController
@RequestMapping("system/sysUser")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "sysUser")
    public String sysUserIndex(){
        return FebsUtil.view("sysUser/sysUser");
    }

    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysUser:view")
    public FebsResponse sysUserList(QueryRequest request, SysUser sysUser) {
        if(!StringUtils.isEmpty(sysUser.getCommandCodeIsExpires())){
            sysUser.setCommandExpiresTime(new Date());
        }
        Map<String, Object> dataTable = getDataTable(this.sysUserService.findSysUsers(request, sysUser));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑用户/按钮", exceptionMessage = "编辑用户失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysUser:add","sysUser:update"})
    public FebsResponse addSysUser(@Valid SysUser sysUser) {
        sysUser.setOrgId(UserInfoHolder.getUserInfo().getCurrentOrg().getId());
        sysUser.setOcId(UserInfoHolder.getUserInfo().getCurrentOc().getId());
        this.sysUserService.saveSysUser(sysUser,false);
        return new FebsResponse().success();
    }


    @PostMapping("passwordReset/{userIds}")
    @RequiresPermissions("sysUser:passwordReset")
    @ControllerEndpoint(exceptionMessage = "重置用户密码失败")
    public FebsResponse passwordReset(@NotBlank(message = "{required}") @PathVariable String userIds) {
        String[] userIdArr = userIds.split(StringPool.COMMA);
        this.sysUserService.resetPassword(userIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("enable/{userIds}")
    @RequiresPermissions("sysUser:enable")
    @ControllerEndpoint(exceptionMessage = "启用用户失败")
    public FebsResponse enable(@NotBlank(message = "{required}") @PathVariable String userIds) {
        String[] userIdArr = userIds.split(StringPool.COMMA);
        this.sysUserService.enable(userIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{userIds}")
    @RequiresPermissions("sysUser:disable")
    @ControllerEndpoint(exceptionMessage = "禁用用户失败")
    public FebsResponse disable(@NotBlank(message = "{required}") @PathVariable String userIds) {
        String[] userIdArr = userIds.split(StringPool.COMMA);
        this.sysUserService.disable(userIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("removeMobile/{userIds}")
    @RequiresPermissions("sysUser:removeMobile")
    @ControllerEndpoint(exceptionMessage = "解除移动端用户失败")
    public FebsResponse removeMobile(@NotBlank(message = "{required}") @PathVariable String userIds) {
        String[] userIdArr = userIds.split(StringPool.COMMA);
        this.sysUserService.removeMobile(userIdArr,UserInfoHolder.getUserInfo().getOrgId(),UserInfoHolder.getUserInfo().getOcId());
        return new FebsResponse().success();
    }



    @ControllerEndpoint(operation = "导出用户", exceptionMessage = "导出Excel失败")
    @PostMapping("excel")
    @ResponseBody
    @RequiresPermissions("sysUser:export")
    public void export(QueryRequest queryRequest, SysUser sysUser, HttpServletResponse response) {
        List<SysUser> sysUsers = this.sysUserService.findSysUsers(queryRequest, sysUser).getRecords();
        ExcelKit.$Export(SysUser.class, response).downXlsx(sysUsers, false);
    }
}
