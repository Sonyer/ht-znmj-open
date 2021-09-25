package com.handturn.bole.common.authentication;

import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.system.entity.*;
import com.handturn.bole.system.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 *
 * @author Eric
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private ISysOrganizationService sysOrganizationService;
    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysResourceService sysResourceService;

    /**
     * 授权模块，获取用户角色和权限
     *
     * @param principal principal
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        String userCode = sysUser.getUserCode();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色集
        List<SysRole> roleList = this.sysRoleService.findUserRole(userCode);
        Set<String> roleSet = roleList.stream().map(SysRole::getRoleName).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限集
        List<SysResource> permissionList = this.sysResourceService.findUserPermissions(userCode);
        Set<String> permissionSet = permissionList.stream().map(SysResource::getPerms).collect(Collectors.toSet());
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param token AuthenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String userCode = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        // 通过用户名到数据库查询用户信息
        SysUser sysUser = this.sysUserService.findByUserCode(userCode);

        if (sysUser == null) {
            throw new UnknownAccountException("账号未注册！");
        } else if (!StringUtils.equals(password, sysUser.getPassword())) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        } else if (BaseStatus.DISABLED.equals(sysUser.getStatus())) {
            throw new LockedAccountException("账号已被禁用,请联系管理员！");
        } else if (SysUser.STATUS_LOCK.equals(sysUser.getStatus())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        } else {
            SysOrganization sysOrganizationDef = this.sysOrganizationService.findSysOrganizationById(sysUser.getOrgId());

            //SysOrganizationOc sysOrganizationOcDef = this.sysOrganizationOcService.findSysOrganizationOcById(sysUser.getOcId());
            List<SysOrganizationOc> sysOrganizationOcDefs = sysOrganizationOcService.getOrgOcByCurrentUser(sysUser,sysUser.getOrgId());
            SysOrganizationOc sysOrganizationOcDef = null;
            if(sysOrganizationOcDefs.size() > 0){
                sysOrganizationOcDef = sysOrganizationOcDefs.get(0);
            }else{
                throw new LockedAccountException("账号没有系统授权！");
            }

            List<SysResource> rootResources = sysResourceService.findUserRootSysResources(sysUser.getId());
            // 所属组织
            sysUser.setOrg(sysOrganizationDef);
            sysUser.setOc(sysOrganizationOcDef);

            // 当前线程组织
            sysUser.setCurrentOrg(sysOrganizationDef);
            sysUser.setCurrentOc(sysOrganizationOcDef);
            sysUser.setCurrentRootResource(rootResources.get(0));
        }
        return new SimpleAuthenticationInfo(sysUser, password, getName());
    }

    /**
     * 清除当前用户权限缓存
     * 使用方法:在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
