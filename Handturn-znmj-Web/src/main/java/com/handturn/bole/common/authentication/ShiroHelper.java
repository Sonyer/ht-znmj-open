package com.handturn.bole.common.authentication;

import com.handturn.bole.common.annotation.Helper;
import org.apache.shiro.authz.AuthorizationInfo;

/**
 * @author Eric
 */
@Helper
public class ShiroHelper extends ShiroRealm {

    /**
     * 获取当前用户的角色和权限集合
     *
     * @return AuthorizationInfo
     */
    public AuthorizationInfo getCurrentuserAuthorizationInfo() {
        return super.doGetAuthorizationInfo(null);
    }
}
