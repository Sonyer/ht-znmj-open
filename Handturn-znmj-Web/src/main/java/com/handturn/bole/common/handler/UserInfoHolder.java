package com.handturn.bole.common.handler;

import com.handturn.bole.system.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 获取当前用户信息
 */
@Slf4j
public class UserInfoHolder {
    private static ThreadLocal<SysUser> userInfo = new InheritableThreadLocal<SysUser>();

    private Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    protected SysUser getCurrentUser() {
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        setUserInfo(sysUser);
        return userInfo.get();
    }

    public UserInfoHolder() {
    }

    public static SysUser getUserInfo(){
        try {
            SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            setUserInfo(sysUser);
        }catch(Exception e){
            log.error("Security异常:"+e.getLocalizedMessage());
        }

        return userInfo.get();
    }

    public static void setUserInfo(SysUser sysUser){
        userInfo.set(sysUser);
    }
}
