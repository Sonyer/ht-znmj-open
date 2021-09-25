package com.handturn.bole.front.api.me.service.impl;

import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.front.api.me.service.IMeCommandService;
import com.handturn.bole.front.api.me.vo.MeSubmitCommandResponseVo;
import com.handturn.bole.master.member.entity.Member;
import com.handturn.bole.master.member.service.IMemberService;
import com.handturn.bole.master.member.service.IMemberWxMpService;
import com.handturn.bole.master.set.service.IImgStoreSetService;
import com.handturn.bole.master.set.service.IMinichatSetService;
import com.handturn.bole.master.set.service.INotifySetService;
import com.handturn.bole.master.set.service.ISitSetService;
import com.handturn.bole.monitor.service.IRedisService;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MeCommandServiceImpl implements IMeCommandService {
    @Autowired
    private IRedisService redisService;
    @Autowired
    private IImgStoreSetService imgStoreSetService;
    @Autowired
    private IMinichatSetService minichatSetService;
    @Autowired
    private IMemberWxMpService memberWxMpService;
    @Autowired
    private IMemberService memberService;

    @Autowired
    private ISitSetService sitSetService;

    @Autowired
    private INotifySetService notifySetService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 提交口令
     * @param request
     * @return
     */
    @Override
    @Transactional
    public void subimitCommand(HttpServletRequest request, MeSubmitCommandResponseVo requestVo){
        //验证码的KEY
        String key = requestVo.getPhoneNumber()+"validateCode";
        try {
            String validateCode = redisService.get(key);
            if(StringUtils.isEmpty(validateCode)){
                throw new FebsException("验证码已失效,请重新获取后再提交!");
            }
            if(!validateCode.equals(requestVo.getValidateCode())){
                throw new FebsException("验证码已失效,请重新获取后再提交!");
            }
        } catch (RedisConnectException e) {
            e.printStackTrace();
            throw new FebsException("验证码已失效,请重新获取后再提交!");
        }

        if(StringUtils.isEmpty(requestVo.getCommandCode())
                || StringUtils.isEmpty(requestVo.getUserName())
                || StringUtils.isEmpty(requestVo.getPhoneNumber())
                || StringUtils.isEmpty(requestVo.getValidateCode())){
            throw new FebsException("请填写管理口令、管理账号、手机号码、验证码相关信息!");
        }

        //获取token
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            throw new FebsException("您还未登陆!");
        }

        String accountCode = null;
        try {
            accountCode = redisService.get(token);
        } catch (RedisConnectException e) {
            e.printStackTrace();
            throw new FebsException("您还未登陆!");
        }

        Member member = memberService.findMemberByAccountCode(accountCode);
        if(member == null){
            throw new FebsException("您还未登陆!");
        }

        SysUser sysUser = sysUserService.findByCommandCode(requestVo.getCommandCode(),requestVo.getUserName());
        if(sysUser == null){
            throw new FebsException("管理口令无效!");
        }else{
            if(sysUser.getCommandExpiresTime() != null && sysUser.getCommandExpiresTime().getTime() < new Date().getTime()){
                throw new FebsException("管理口令已过期!");
            }
        }

        sysUser.setMemberAccountCode(member.getAccountCode());
        sysUser.setIsBindMember(BaseBoolean.TRUE);
        sysUserService.getBaseMapper().updateById(sysUser);
    }

}
