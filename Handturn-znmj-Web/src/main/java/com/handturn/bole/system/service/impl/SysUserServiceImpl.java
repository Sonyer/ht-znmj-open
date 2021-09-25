package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.*;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.*;
import com.handturn.bole.system.entity.SysOrganization;
import com.handturn.bole.system.entity.SysOrganizationOc;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.mapper.SysUserMapper;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysOrganizationService;
import com.handturn.bole.system.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统-用户 Service实现
 *
 * @author Eric
 * @date 2019-12-01 10:46:37
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Value("${webSocket.domain}")
    private String webSocketDomain;

    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;

    /**
     * 通过用户名查找用户
     *
     * @param userCode 用户编号
     * @return 用户
     */
    @Override
    public SysUser findByUserCode(String userCode){
        return this.baseMapper.findByUserCode(userCode);
    }


    @Override
    public ICustomPage<SysUser> findSysUsers(QueryRequest request, SysUser sysUser) {
        sysUser.setOcId(UserInfoHolder.getUserInfo().getCurrentOc().getId());

        CustomPage<SysUser> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysUser);
    }

    @Override
    @Transactional
    public SysUser saveSysUser(SysUser sysUser,boolean isSystem) {
        if(sysUser.getId() == null){
            checkByUk(sysUser);

            sysUser.setPassword(MD5Util.encrypt(sysUser.getUserCode(), SysUser.DEFAULT_PASSWORD));
            sysUser.setAvatar(SysUser.DEFAULT_AVATAR);
            this.save(sysUser);
            return sysUser;
        }else{
            SysUser userOld = this.baseMapper.selectById(sysUser.getId());
            if(!isSystem && userOld.getIsSysCreate().equals(BaseBoolean.TRUE)){
                throw new FebsException("不允许对系统数据进行调整!");
            }
            CopyUtils.copyProperties(sysUser,userOld);

            checkByUk(userOld);

            this.updateById(userOld);
            return userOld;
        }
    }

    /**
     * 修改
     *
     * @param sysUser sysUser
     */
    @Override
    @Transactional
    public SysUser saveSysUser(SysUser sysUser,boolean isSystem,String passWord){
        if(sysUser.getId() == null){
            checkByUk(sysUser);

            sysUser.setPassword(MD5Util.encrypt(sysUser.getUserCode(), passWord));
            sysUser.setAvatar(SysUser.DEFAULT_AVATAR);
            this.save(sysUser);
            return sysUser;
        }else{
            SysUser userOld = this.baseMapper.selectById(sysUser.getId());

            sysUser.setPassword(MD5Util.encrypt(sysUser.getUserCode(), passWord));

            CopyUtils.copyProperties(sysUser,userOld);

            checkByUk(userOld);

            this.updateById(userOld);
            return userOld;
        }
    }

    @Override
    @Transactional
    public void deleteSysUser(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}

    @Override
    @Transactional
    public void updateLoginTime(String userCode) {
        SysUser sysUser = new SysUser();
        CopyUtils.setObjectFieldsEmpty(sysUser);
        sysUser.setLoginTime(new Date());
        this.baseMapper.update(sysUser, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserCode, userCode));
    }

    @Override
    @Transactional
    public void resetPassword(String[] userIds) {
        Arrays.stream(userIds).forEach(userId -> {
            SysUser sysUser = this.baseMapper.selectById(userId);
            sysUser.setFirstLogin(true);
            sysUser.setPassword(MD5Util.encrypt(sysUser.getUserCode(), SysUser.DEFAULT_PASSWORD));
            this.baseMapper.updateById(sysUser);
        });
    }


    /**
     * 启用
     *
     * @param userIds 用户编号数组
     */
    @Override
    @Transactional
    public void enable(String[] userIds){
        Arrays.stream(userIds).forEach(userId -> {
            SysUser sysUser = this.baseMapper.selectById(userId);
            sysUser.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysUser);
        });
    }


    /**
     * 禁用
     *
     * @param userIds 用户编号数组
     */
    @Override
    @Transactional
    public void disable(String[] userIds){
        Arrays.stream(userIds).forEach(userId -> {
            SysUser sysUser = this.baseMapper.selectById(userId);
            sysUser.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysUser);
        });
    }

    /**
     * 解除移动端管理
     *
     * @param userIds 用户编号数组
     */
    @Override
    @Transactional
    public void removeMobile(String[] userIds,Long orgId,Long ocId){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysUser::getId,userIds);
        queryWrapper.lambda().eq(SysUser::getOrgId,orgId);
        queryWrapper.lambda().eq(SysUser::getOcId,ocId);
        List<SysUser> users = this.baseMapper.selectList(queryWrapper);
        users.forEach(user ->{
            user.setIsBindMember(BaseBoolean.FALSE);
            user.setMemberAccountCode("");
            this.baseMapper.updateById(user);
        });
    }

    @Override
    @Transactional
    public void regist(String userCode, String password) {
        SysUser sysUser = new SysUser();
        sysUser.setPassword(MD5Util.encrypt(userCode, password));
        sysUser.setUserCode(userCode);
        sysUser.setCreateDate(new Date());
        sysUser.setStatus(SysUser.STATUS_VALID);
        sysUser.setSex(SysUser.SEX_UNKNOW);
        sysUser.setAvatar(SysUser.DEFAULT_AVATAR);
        sysUser.setTheme(SysUser.THEME_BLACK);
        sysUser.setIsTab(SysUser.TAB_OPEN);
        sysUser.setDescription("注册用户");
        this.save(sysUser);

        /*UserRole ur = new UserRole();
        ur.setUserId(sysUser.getId());
        ur.setRoleId(2L); // 注册用户角色 ID
        this.userRoleService.save(ur);*/
    }

    @Override
    @Transactional
    public void updatePassword(String userCode, String password) {
        SysUser sysUser = new SysUser();
        CopyUtils.setObjectFieldsEmpty(sysUser);
        sysUser.setPassword(MD5Util.encrypt(userCode, password));
        this.baseMapper.update(sysUser, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserCode, userCode));
    }

    @Override
    @Transactional
    public void updateAvatar(String userCode, String avatar) {
        SysUser sysUser = new SysUser();
        CopyUtils.setObjectFieldsEmpty(sysUser);
        sysUser.setAvatar(avatar);
        this.baseMapper.update(sysUser, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserCode, userCode));
    }

    @Override
    @Transactional
    public void updateTheme(String userCode, String theme, String isTab) {
        SysUser sysUser = new SysUser();
        CopyUtils.setObjectFieldsEmpty(sysUser);
        sysUser.setTheme(theme);
        sysUser.setIsTab(isTab);
        this.baseMapper.update(sysUser, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserCode, userCode));
    }

    @Override
    @Transactional
    public void updateProfile(SysUser sysUser) {
        sysUser.setUserCode(null);
        sysUser.setRoleId(null);
        sysUser.setPassword(null);
        updateById(sysUser);
    }

    /**
     * 通过Id获取信息
     * @param userId
     * @return
     */
    public SysUser findById(Long userId){
        return this.baseMapper.selectOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getId, userId));
    }

    @Override
    public CommonTree<SysUser> findSysUserByOcId(Long ocId, Set<Long> userIds){
        List<SysUser> userList = this.baseMapper.selectList(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getOcId, ocId));
        List<CommonTree<SysUser>> trees = this.convertCommonTree(userList,userIds);
        return CommonTree.buildTree(trees,false,null);
    }

    private List<CommonTree<SysUser>> convertCommonTree(List<SysUser> sysUsers, Set<Long> permUserIds) {
        List<CommonTree<SysUser>> trees = new ArrayList<>();

        sysUsers.forEach(sysUser -> {
            CommonTree<SysUser> tree = new CommonTree<>();
            tree.setId(String.valueOf(sysUser.getId()));
            tree.setParentId(String.valueOf(0));
            tree.setTitle(sysUser.getUserName());
            sysUser.setPassword("");
            tree.setData(sysUser);
            if(permUserIds.contains(sysUser.getId())){
                tree.setChecked(true);
            }
            trees.add(tree);
        });
        return trees;
    }

    public void checkByUk(SysUser user){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
        queryWrapper.lambda().eq(SysUser::getUserCode, user.getUserCode());
        if(user.getId() != null && user.getId() != 0){
            queryWrapper.lambda().ne(SysUser::getId, user.getId());
        }

        SysUser temp = this.baseMapper.selectOne(queryWrapper);

        if(temp != null){
            throw new FebsException("记录已经存在!请确认!");
        }
    }

    /**
     * 通过Id获取信息
     * @param orgId
     * @return
     */
    public List<OptionVo> findOptionVoByOrg(Long orgId, Long ocId){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
        queryWrapper.lambda().eq(SysUser::getOrgId, orgId);
        queryWrapper.lambda().eq(SysUser::getOcId, ocId);
        queryWrapper.lambda().eq(SysUser::getStatus,BaseStatus.ENABLED);

        List<SysUser> users = this.baseMapper.selectList(queryWrapper);

        List<OptionVo> result = new ArrayList<>();
        users.forEach(user -> {
            OptionVo optionVo = new OptionVo();
            optionVo.setText(user.getUserCode()+"-"+user.getUserName()+"-["+user.getDepName()+"]");
            optionVo.setValue(user.getUserCode());
            result.add(optionVo);
        });

        return result;
    }

    /**
     * 通过Id获取 口令
     * @param ids
     * @return
     */
    @Override
    public String findCommandByIds(String[] ids,Long orgId, Long ocId){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
        queryWrapper.lambda().in(SysUser::getId, ids);
        queryWrapper.lambda().eq(SysUser::getOrgId, orgId);
        queryWrapper.lambda().eq(SysUser::getOcId, ocId);
        List<SysUser> users = this.baseMapper.selectList(queryWrapper);

        StringBuffer result = new StringBuffer();
        users.forEach(user ->{
            if (!StringUtils.isEmpty(user.getCommandCode()) && user.getCommandExpiresTime().getTime() > new Date().getTime()){

            }else{
                user.setCommandCode(StringRandom.getStringRandom(4));
                user.setCommandExpiresTime(DateUtil.days(new Date(),365));
                this.baseMapper.updateById(user);
            }
            result.append("#---管理口令邀请码,请按照步骤操作---#\n");
            result.append("#---进入【大智门禁】微信小程序->我的->管理口令---#\n");
            result.append("#---管理口令邀请码:【"+user.getCommandCode()+"】---#\n");
            result.append("#---账号名称:【"+user.getUserName()+"】---#\n");
            result.append("#---口令有效期:【"+ DateUtil.getDateFormat(user.getCommandExpiresTime(),DateUtil.CN_TIMS_SPLIT_PATTERN)+"】---#\n");

            result.append("\n");
            result.append("\n");
            result.append("\n");
        });

        return result.toString();
    }

    /**
     * 通过 口令 账户名获取
     * @param commandCode
     * @return
     */
    @Override
    public SysUser findByCommandCode(String commandCode,String userName){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
        queryWrapper.lambda().eq(SysUser::getCommandCode, commandCode);
        queryWrapper.lambda().eq(SysUser::getUserName, userName);
        List<SysUser> users = this.baseMapper.selectList(queryWrapper);
        return users == null || users.size() <= 0? null: users.get(0);
    }

    /**
     * 通过账号获取用户ID
     * @param accountCode
     * @return
     */
    @Override
    public Set<Long> findIdsByAccountCode(String accountCode){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
        queryWrapper.lambda().eq(SysUser::getMemberAccountCode, accountCode);
        queryWrapper.lambda().select(SysUser::getId);
        List<SysUser> users = this.baseMapper.selectList(queryWrapper);
        Set<Long> result = new HashSet<>();
        users.forEach(user ->{
            result.add(user.getId());
        });
        return result;
    }

    /**
     * 通过账号
     * @param userIds
     * @return
     */
    @Override
    public List<SysUser> findByIds(Set<Long> userIds){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
        queryWrapper.lambda().in(SysUser::getId, userIds);
        List<SysUser> users = this.baseMapper.selectList(queryWrapper);
        return users;
    }

    /**
     * 通过Id获取 口令
     * @param ids
     * @return
     */
    @Override
    public String findInterfaceSetByIds(String[] ids,Long orgId, Long ocId){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
        queryWrapper.lambda().in(SysUser::getId, ids);
        queryWrapper.lambda().eq(SysUser::getOrgId, orgId);
        queryWrapper.lambda().eq(SysUser::getOcId, ocId);
        List<SysUser> users = this.baseMapper.selectList(queryWrapper);

        SysOrganizationOc organizationOc = sysOrganizationOcService.findSysOrganizationOcById(ocId);
        if(organizationOc == null){
            return "";
        }

        String ocTocken = null;
        try {
            ocTocken = DESToolUtil.encrypt(organizationOc.getOcCode(),DESToolUtil.KEY_);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        StringBuffer result = new StringBuffer();
        String finalOcTocken = ocTocken;
        users.forEach(user ->{
            String passwordTocken = null;
            try {
                passwordTocken = DESToolUtil.encrypt(user.getUserCode(),DESToolUtil.KEY_);
            } catch (Exception e) {
                e.printStackTrace();
                return ;
            }

            result.append("#---生成接口配置,请按照步骤操作---#\n");
            result.append("#---进入【大智门禁】桌面程序->系统管理->接口配置---#\n");
            result.append("#---接口地址:【"+webSocketDomain+"】---#\n");
            result.append("#---接入密钥:【"+ finalOcTocken +"】---#\n");
            result.append("#---账号名称:【"+user.getUserCode()+"】---#\n");
            result.append("#---账号密钥:【"+ passwordTocken+"】---#\n");
            result.append("#---注终端接口密码不要随意变动，避免终端连接异常!(如需变动，请随时调节终端接口配置)\n");

            result.append("\n");
            result.append("\n");
            result.append("\n");
        });

        return result.toString();
    }
}

