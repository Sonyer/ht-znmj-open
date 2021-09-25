package com.handturn.bole.system.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * 系统-用户 Service接口
 *
 * @author Eric
 * @date 2019-12-01 10:46:37
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 通过用户名查找用户
     *
     * @param userCode 用户编号
     * @return 用户
     */
    SysUser findByUserCode(String userCode);

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysUser sysUser
     * @return ICustomPage<SysUser>
     */
    ICustomPage<SysUser> findSysUsers(QueryRequest request, SysUser sysUser);

    /**
     * 修改
     *
     * @param sysUser sysUser
     */
    SysUser saveSysUser(SysUser sysUser,boolean isSystem);

    /**
     * 修改
     *
     * @param sysUser sysUser
     */
    SysUser saveSysUser(SysUser sysUser,boolean isSystem,String passWord);

    /**
     * 删除
     *
     * @param sysUser sysUser
     */
    void deleteSysUser(SysUser sysUser);


    /**
     * 更新用户登录时间
     *
     * @param userCode 用户编号
     */
    void updateLoginTime(String userCode);

    /**
     * 重置密码
     *
     * @param userIds 用户编号数组
     */
    void resetPassword(String[] userIds);

    /**
     * 启用
     *
     * @param userIds 用户编号数组
     */
    void enable(String[] userIds);


    /**
     * 禁用
     *
     * @param userIds 用户编号数组
     */
    void disable(String[] userIds);

    /**
     * 解除移动端管理
     *
     * @param userIds 用户编号数组
     */
    void removeMobile(String[] userIds,Long orgId,Long ocId);

    /**
     * 注册用户
     *
     * @param userCode 用户名
     * @param password 密码
     */
    void regist(String userCode, String password);

    /**
     * 修改密码
     *
     * @param userCode 用户编号
     * @param password 新密码
     */
    void updatePassword(String userCode, String password);

    /**
     * 更新用户头像
     *
     * @param userCode 用户编号
     * @param avatar   用户头像
     */
    void updateAvatar(String userCode, String avatar);

    /**
     * 修改用户系统配置（个性化配置）
     *
     * @param userCode 用户编号
     * @param theme    主题风格
     * @param isTab    是否开启 TAB
     */
    void updateTheme(String userCode, String theme, String isTab);

    /**
     * 更新个人信息
     *
     * @param sysUser 个人信息
     */
    void updateProfile(SysUser sysUser);

    /**
     * 通过Id获取信息
     * @param userId
     * @return
     */
    SysUser findById(Long userId);


    CommonTree<SysUser> findSysUserByOcId(Long ocId, Set<Long> userIds);

    /**
     * 通过Id获取信息
     * @param orgId
     * @return
     */
    List<OptionVo> findOptionVoByOrg(Long orgId, Long ocId);


    /**
     * 通过Id获取 口令
     * @param ids
     * @return
     */
    String findCommandByIds(String[] ids,Long orgId, Long ocId);

    /**
     * 通过 口令 账户名获取
     * @param commandCode
     * @return
     */
    SysUser findByCommandCode(String commandCode,String userName);

    /**
     * 通过账号获取用户ID
     * @param accountCode
     * @return
     */
    Set<Long> findIdsByAccountCode(String accountCode);

    /**
     * 通过账号
     * @param userIds
     * @return
     */
    List<SysUser> findByIds(Set<Long> userIds);

    /**
     * 通过Id获取 口令
     * @param ids
     * @return
     */
    String findInterfaceSetByIds(String[] ids,Long orgId, Long ocId);

}
