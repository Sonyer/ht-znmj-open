package com.handturn.bole.front.api.bas.service.impl;

import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.common.utils.StringRandom;
import com.handturn.bole.front.api.bas.service.IFrontBasService;
import com.handturn.bole.master.member.entity.Member;
import com.handturn.bole.master.member.entity.MemberNotifyType;
import com.handturn.bole.master.member.service.IMemberNotifyService;
import com.handturn.bole.master.member.service.IMemberService;
import com.handturn.bole.monitor.service.IRedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FrontBasServiceImpl implements IFrontBasService {
    @Autowired
    private IRedisService redisService;
    @Autowired
    private IMemberService memberService;

    @Autowired
    private IMemberNotifyService memberNotifyService;

    /**
     * 验证码
     * @param request
     * @param phoneNumber
     * @return
     */
    @Override
    @Transactional
    public Boolean validateCode(HttpServletRequest request, String phoneNumber){
        if(StringUtils.isEmpty(phoneNumber)){
            throw new FebsException("必须填写联系手机号码!");
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

        try {
            String key = phoneNumber+"validateCode"; //保留验证码KEY
            String validateCode = "";
            if(redisService.get(key) != null){
                throw new FebsException("您的操作太频繁了,1分钟后再操作!");
            }else{
                validateCode = StringRandom.getStringNumRandom(6);

                Map<String,String> param = new HashMap<String,String>();
                param.put("validateCode",validateCode);
                param.put("phoneNumber",phoneNumber);

                //消息通知发送
                memberNotifyService.notifyMessage(member.getId(),member.getAccountCode(), MemberNotifyType.VALIDATE_CODE_NOTIFY,param);

                redisService.set(phoneNumber+"validateCode",validateCode,1000*60*1L);  //保留1分钟
            }

        } catch (RedisConnectException e) {
            e.printStackTrace();
            throw new FebsException("验证码生成错误!");
        }
        return true;
    }
}
