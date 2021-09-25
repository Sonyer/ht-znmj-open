package com.handturn.bole.portal.controller;

import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.MD5Util;
import com.handturn.bole.system.entity.SysOrganization;
import com.handturn.bole.system.entity.SysOrganizationOc;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysOrganizationService;
import com.handturn.bole.system.service.ISysResourceService;
import com.handturn.bole.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 系统-用户 Controller
 *
 * @author Eric
 * @date 2019-12-01 10:46:37
 */
@Slf4j
@Validated
@RestController
@RequestMapping("portal")
public class PortalController extends BaseController {
    @Autowired
    private ISysOrganizationService sysOrganizationService;
    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysResourceService sysResourceService;

    @GetMapping("userResource/{userCode}")
    public FebsResponse getUserResource(@NotBlank(message = "{required}") @PathVariable String userCode) throws FebsException {
        SysUser currentUser = getCurrentUser();
        if (!StringUtils.equalsIgnoreCase(userCode, currentUser.getUserCode()))
            throw new FebsException("您无权获取别人的菜单");
        CommonTree<SysResource> userMenus = this.sysResourceService.findUserSysResources(userCode,currentUser.getCurrentRootResource().getId());
        return new FebsResponse().data(userMenus);
    }

    @PostMapping("password/update")
    @ControllerEndpoint(exceptionMessage = "修改密码失败")
    public FebsResponse updatePassword(
            @NotBlank(message = "{required}") String oldPassword,
            @NotBlank(message = "{required}") String newPassword) {
        SysUser sysUser = getCurrentUser();
        if (!StringUtils.equals(sysUser.getPassword(), MD5Util.encrypt(sysUser.getUserCode(), oldPassword))) {
            throw new FebsException("原密码不正确");
        }
        sysUserService.updatePassword(sysUser.getUserCode(), newPassword);
        return new FebsResponse().success();
    }

    @GetMapping("avatar/{image}")
    @ControllerEndpoint(exceptionMessage = "修改头像失败")
    public FebsResponse updateAvatar(@NotBlank(message = "{required}") @PathVariable String image) {
        SysUser sysUser = getCurrentUser();
        this.sysUserService.updateAvatar(sysUser.getUserCode(), image);
        return new FebsResponse().success();
    }

    @PostMapping("theme/update")
    @ControllerEndpoint(exceptionMessage = "修改系统配置失败")
    public FebsResponse updateTheme(String theme, String isTab) {
        SysUser sysUser = getCurrentUser();
        this.sysUserService.updateTheme(sysUser.getUserCode(), theme, isTab);
        return new FebsResponse().success();
    }

    @PostMapping("profile/update")
    @ControllerEndpoint(exceptionMessage = "修改个人信息失败")
    public FebsResponse updateProfile(SysUser sysUser) throws FebsException {
        SysUser currentUser = getCurrentUser();
        sysUser.setId(currentUser.getId());
        this.sysUserService.updateProfile(sysUser);
        return new FebsResponse().success();
    }

    @RequestMapping(value="/logoShow/{logoFileName}")
    @ControllerEndpoint(exceptionMessage = "Logo加载失败")
    public FebsResponse logoShow(@NotBlank(message = "{required}") @PathVariable String logoFileName, HttpServletRequest request, HttpServletResponse response) {
        try{
            this.sysOrganizationService.logoImgShow(logoFileName,request,response);
        }catch (Exception e){
            throw new FebsException(e.getMessage());
        }
        return new FebsResponse().success();
    }

    @PostMapping("switchModule")
    @ControllerEndpoint(exceptionMessage = "切换模块失败")
    public FebsResponse switchModule(
            @NotBlank(message = "{required}") String rootResourceId) {
        SysUser sysUser = getCurrentUser();
        SysResource sysRootResource = sysResourceService.findById(Long.valueOf(rootResourceId));
        sysUser.setCurrentRootResource(sysRootResource);
        //起换用户
        setSubject(sysUser);
        return new FebsResponse().success();
    }

    @PostMapping("switchOrg")
    @ControllerEndpoint(exceptionMessage = "切换组织失败")
    public FebsResponse switchOrg(
            @NotBlank(message = "{required}") String orgId) {
        SysUser sysUser = getCurrentUser();

        SysOrganization sysOrganization = this.sysOrganizationService.findSysOrganizationById(Long.valueOf(orgId));

        List<SysOrganizationOc> orgOcs = sysOrganizationOcService.getOrgOcByCurrentUser(sysUser,Long.valueOf(orgId));

        sysUser.setCurrentOrg(sysOrganization);
        sysUser.setCurrentOc(orgOcs.get(0));  //取第一个
        //起换用户
        setSubject(sysUser);
        return new FebsResponse().success();
    }

    @PostMapping("switchOc")
    @ControllerEndpoint(exceptionMessage = "切换网点失败")
    public FebsResponse switchOc(
            @NotBlank(message = "{required}") String ocId) {
        SysUser sysUser = getCurrentUser();

        SysOrganizationOc sysOrganizationOc = this.sysOrganizationOcService.findSysOrganizationOcById(Long.valueOf(ocId));

        sysUser.setCurrentOc(sysOrganizationOc);
        //起换用户
        setSubject(sysUser);
        return new FebsResponse().success();
    }
}
