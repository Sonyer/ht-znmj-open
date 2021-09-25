package com.handturn.bole.system.controller;

import com.handturn.bole.common.annotation.Limit;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CaptchaUtil;
import com.handturn.bole.common.utils.MD5Util;
import com.handturn.bole.monitor.entity.SysLoginLog;
import com.handturn.bole.monitor.service.ISysLoginLogService;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.service.ISysUserService;
import com.wf.captcha.base.Captcha;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eric
 */
@Validated
@RestController
public class LoginController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysLoginLogService loginLogService;

    @PostMapping("login")
    @Limit(key = "login", period = 60, count = 20, name = "登录接口", prefix = "limit")
    public FebsResponse login(
            @NotBlank(message = "{required}") String userCode,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        if (!CaptchaUtil.verify(verifyCode, request)) {
            throw new FebsException("验证码错误！");
        }
        password = MD5Util.encrypt(userCode.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(userCode, password, rememberMe);
        super.login(token);
        // 保存登录日志
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setUserCode(userCode);
        loginLog.setUserName(userCode);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveSysLoginLog(loginLog);

        return new FebsResponse().success();
    }

    @PostMapping("regist")
    public FebsResponse regist(
            @NotBlank(message = "{required}") String userCode,
            @NotBlank(message = "{required}") String password) throws FebsException {
        SysUser sysUser = sysUserService.findByUserCode(userCode);
        if (sysUser != null) {
            throw new FebsException("该用户名已存在");
        }
        this.sysUserService.regist(userCode, password);
        return new FebsResponse().success();
    }

    @GetMapping("index/{userCode}")
    public FebsResponse index(@NotBlank(message = "{required}") @PathVariable String userCode) {
        // 更新登录时间
        this.sysUserService.updateLoginTime(userCode);
        Map<String, Object> data = new HashMap<>();
        // 获取系统访问记录
        Long totalVisitCount = this.loginLogService.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = this.loginLogService.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = this.loginLogService.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastSevenVisitCount = this.loginLogService.findLastSevenDaysVisitCount(null);
        data.put("lastSevenVisitCount", lastSevenVisitCount);
        SysUser param = new SysUser();
        param.setUserCode(userCode);
        List<Map<String, Object>> lastSevenUserVisitCount = this.loginLogService.findLastSevenDaysVisitCount(param);
        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return new FebsResponse().success().data(data);
    }

    @GetMapping("images/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtil.outPng(110, 34, 4, Captcha.TYPE_ONLY_NUMBER, request, response);
    }
}
