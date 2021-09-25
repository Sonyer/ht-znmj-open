package com.handturn.bole.portal.controller;

import com.handturn.bole.common.authentication.ShiroHelper;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.system.entity.SysResource;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysOrganizationService;
import com.handturn.bole.system.service.ISysResourceService;
import com.handturn.bole.system.service.ISysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Eric
 */
@Controller("systemView")
public class ViewController extends BaseController {
    @Autowired
    private ISysOrganizationService sysOrganizationService;

    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;

    @Autowired
    private ISysResourceService sysResourceService;

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ShiroHelper shiroHelper;

    @GetMapping("login")
    @ResponseBody
    public Object login(HttpServletRequest request) {
        if (FebsUtil.isAjaxRequest(request)) {
            throw new ExpiredSessionException();
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName(FebsUtil.view("login"));
            return mav;
        }
    }

    @RequestMapping("/logout")
    public void logout(HttpServletResponse response) {
        Subject subject= SecurityUtils.getSubject();
        System.out.println("登出操作!"+subject);
        if(subject != null){
            subject.logout();
        }
    }

    @GetMapping("unauthorized")
    public String unauthorized() {
        return FebsUtil.view("error/403");
    }


    @GetMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @GetMapping("index")
    public String index(Model model) {
        AuthorizationInfo authorizationInfo = shiroHelper.getCurrentuserAuthorizationInfo();
        SysUser user = super.getCurrentUser();
        SysUser currentUserDetail = sysUserService.findByUserCode(user.getUserCode());

        List<OptionVo> orgOptions = sysOrganizationService.getOrgOptionVoByCurrentUser(user);
        List<OptionVo> orgOcOptions = sysOrganizationOcService.getOrgOcOptionVoByCurrentUser(user,user.getCurrentOrg().getId());
        List<SysResource> resources = sysResourceService.findUserRootSysResources(user.getId());

        model.addAttribute("orgOptions", orgOptions);
        model.addAttribute("orgOcOptions", orgOcOptions);
        model.addAttribute("rootResources", resources);

        model.addAttribute("currentRootResource", user.getCurrentRootResource());
        model.addAttribute("currentOrg", user.getCurrentOrg());
        model.addAttribute("currentOrgOc", user.getCurrentOc());

        currentUserDetail.setPassword("It's a secret");
        model.addAttribute("user", currentUserDetail);
        model.addAttribute("permissions", authorizationInfo.getStringPermissions());
        model.addAttribute("roles", authorizationInfo.getRoles());
        return "index";
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "layout")
    public String layout() {
        return FebsUtil.view("layout");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "password/update")
    public String passwordUpdate() {
        return FebsUtil.view("portal/passwordUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "portal/profile")
    public String userProfile() {
        return FebsUtil.view("portal/userProfile");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "portal/avatar")
    public String userAvatar() {
        return FebsUtil.view("portal/avatar");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "portal/profile/update")
    public String profileUpdate() {
        return FebsUtil.view("portal/profileUpdate");
    }

    @RequestMapping(FebsConstant.VIEW_PREFIX + "index")
    public String pageIndex() {
        return FebsUtil.view("index");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "404")
    public String error404() {
        return FebsUtil.view("error/404");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "403")
    public String error403() {
        return FebsUtil.view("error/403");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "500")
    public String error500() {
        return FebsUtil.view("error/500");
    }


}
