package com.handturn.bole.front.api.login.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.common.utils.AddressUtil;
import com.handturn.bole.common.utils.IPUtil;
import com.handturn.bole.common.utils.StringRandom;
import com.handturn.bole.common.utils.WeChatUtils;
import com.handturn.bole.front.api.login.constant.WechatConstants;
import com.handturn.bole.front.api.login.service.IWXLoginService;
import com.handturn.bole.front.api.login.vo.WXLoginResponseVo;
import com.handturn.bole.front.api.login.vo.WXUserInfoRequest;
import com.handturn.bole.master.member.entity.*;
import com.handturn.bole.master.member.service.IMemberNotifyService;
import com.handturn.bole.master.member.service.IMemberService;
import com.handturn.bole.master.member.service.IMemberWxMpService;
import com.handturn.bole.master.set.entity.MinichatSet;
import com.handturn.bole.master.set.service.IMinichatSetService;
import com.handturn.bole.monitor.service.IRedisService;
import com.handturn.bole.sitCommon.common.utils.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WXLoginServiceImpl implements IWXLoginService {
    @Autowired
    private IRedisService redisService;
    @Autowired
    private IMinichatSetService minichatSetService;
    @Autowired
    private IMemberWxMpService memberWxMpService;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IMemberNotifyService memberNotifyService;

    /**
     * ????????????????????????????????????
     * @param request
     * @param wxCode
     * @param encryptedData
     * @param iv
     * @return
     */
    @Transactional
    public WXLoginResponseVo login(HttpServletRequest request, String userInfo, String wxCode, String encryptedData, String iv, String longitude, String latitude){
        //1?????????JSONObject
        WXUserInfoRequest userInfoTemp=JSONObject.parseObject(userInfo,WXUserInfoRequest.class);

        //????????????api???????????????openid???sessionKey
        JSONObject jsonObject = getUserWXLoginInfo(wxCode);
        if(jsonObject==null){
            throw new FebsException("????????????!");
        }
        if(jsonObject!=null&&!jsonObject.containsKey(WechatConstants.OPENID)) {
            throw new FebsException("????????????!");
        }
        String openid = (String)jsonObject.get(WechatConstants.OPENID);
       /* String sessionKey = (String)jsonObject.get(WechatConstants.SESSION_KEY);*/

       /* String userInfoStr = WeChatUtils.getUserInfo(encryptedData,sessionKey,iv);*/
        /*JSONObject userInfoJsonObject = JSON.parseObject(userInfoStr);*/

        /*String unionid = (String)userInfoJsonObject.get(WechatConstants.UNIONID);
        String nikeName = (String)userInfoJsonObject.get(WechatConstants.NICK_NAME);
        String language = (String)userInfoJsonObject.get(WechatConstants.LANGUAGE);
        String gender = userInfoJsonObject.get(WechatConstants.GENDER).toString();
        String city = (String)userInfoJsonObject.get(WechatConstants.CITY);
        String province = (String)userInfoJsonObject.get(WechatConstants.PROVINCE);
        String country = (String)userInfoJsonObject.get(WechatConstants.COUNTRY);
        String avatarUrl = (String)userInfoJsonObject.get(WechatConstants.AVATAR_URL);*/

        // ?????? IP??????
        String ip = IPUtil.getIpAddr(request);
        String ipRegion = AddressUtil.getCityInfo(ip);

        //???????????????????????????
        Member member = null;
        //??????openid?????????????????????????????????
        MemberWxMp memberWxMp = memberWxMpService.findMemberWxMpById(openid);
        if(memberWxMp != null) {
            if(memberWxMp.getStatus().equals(MemberStatus.FREEZE)){
                throw new FebsException("????????????????????????!");
            }

            member = memberService.findMemberById(memberWxMp.getBindMemberId());
            if(member != null){
                //member.setCertificationType(CertificationType.NORMAL_USER);
                member.setLoginCount(member.getLoginCount()+1);
                member.setLastLoginTime(new Date());
                member.setStatus(MemberStatus.NORMAL);
                member.setLastLoginIp(ip);
                member.setLastIpRegion(ipRegion);
                member.setGender(userInfoTemp.getGender());
                member.setLastLongitude(StringUtils.isEmpty(longitude) ?null:new BigDecimal(longitude));
                member.setLastLatitude(StringUtils.isEmpty(latitude)?null:new BigDecimal(latitude));
                memberService.saveMember(member);
            }else{
                member = new Member();
                member.setAvatarRequest(userInfoTemp.getAvatarUrl());
                member.setCertificationType(CertificationType.NORMAL_USER);
                member.setLoginCount(memberWxMp.getLoginCount());
                member.setFirstLoginTime(member.getFirstLoginTime());
                member.setLastLoginTime(new Date());
                member.setStatus(MemberStatus.NORMAL);
                member.setLastLoginIp(ip);
                member.setLastIpRegion(ipRegion);
                member.setGender(userInfoTemp.getGender());
                member.setLastLongitude(StringUtils.isEmpty(longitude)?null:new BigDecimal(longitude));
                member.setLastLatitude(StringUtils.isEmpty(latitude)?null:new BigDecimal(latitude));
                member = memberService.saveMember(member);

                //????????????
                //memberNotifyService.notifyMessage(member.getId(),member.getAccountCode(), MemberNotifyType.REGISTER_FIRST_SUCCESS_NOTIFY,null);
            }

            //?????????????????????????????????
           /* memberWxMp.setUnionid(unionid);*/
            memberWxMp.setNickName(userInfoTemp.getNickName());
            memberWxMp.setCity(userInfoTemp.getCity());
            memberWxMp.setProvince(userInfoTemp.getProvince());
            memberWxMp.setCountry(userInfoTemp.getCountry());
            memberWxMp.setLanguage(userInfoTemp.getLanguage());
            memberWxMp.setAvatarRequest(userInfoTemp.getAvatarUrl());
            memberWxMp.setLastLoginTime(new Date());
            memberWxMp.setLoginCount(memberWxMp.getLoginCount()+1);
            memberWxMp.setLastLoginIp(ip);
            memberWxMp.setLastIpRegion(ipRegion);
            memberWxMp.setIsAuth(BaseBoolean.TRUE);
            memberWxMp.setGender(userInfoTemp.getGender());
            memberWxMp.setBindMemberId(member.getId());
            memberWxMp.setLastLongitude(StringUtils.isEmpty(longitude)?null:new BigDecimal(longitude));
            memberWxMp.setLastLatitude(StringUtils.isEmpty(latitude)?null:new BigDecimal(latitude));
            memberWxMpService.saveMemberWxMp(memberWxMp);

        }else{
            member = new Member();
            member.setAvatarRequest(userInfoTemp.getAvatarUrl());
            member.setCertificationType(CertificationType.NORMAL_USER);
            member.setLoginCount(1);
            member.setFirstLoginTime(new Date());
            member.setLastLoginTime(new Date());
            member.setStatus(MemberStatus.NORMAL);
            member.setLastLoginIp(ip);
            member.setLastIpRegion(ipRegion);
            member.setLastLongitude(StringUtils.isEmpty(longitude)?null:new BigDecimal(longitude));
            member.setLastLatitude(StringUtils.isEmpty(latitude)?null:new BigDecimal(latitude));
            member = memberService.saveMember(member);

            memberWxMp = new MemberWxMp();
            memberWxMp.setBindMemberId(member.getId());
            memberWxMp.setOpenid(openid);
            /*memberWxMp.setUnionid(unionid);*/
            memberWxMp.setNickName(userInfoTemp.getNickName());
            memberWxMp.setCity(userInfoTemp.getCity());
            memberWxMp.setProvince(userInfoTemp.getProvince());
            memberWxMp.setCountry(userInfoTemp.getCountry());
            memberWxMp.setLanguage(userInfoTemp.getLanguage());
            memberWxMp.setAvatarRequest(userInfoTemp.getAvatarUrl());
            memberWxMp.setStatus(MemberStatus.NORMAL);
            memberWxMp.setFirstLoginTime(new Date());
            memberWxMp.setLastLoginTime(new Date());
            memberWxMp.setLoginCount(1);
            memberWxMp.setLastLoginIp(ip);
            memberWxMp.setLastIpRegion(ipRegion);
            memberWxMp.setIsAuth(BaseBoolean.FALSE);
            memberWxMp.setLastLongitude(StringUtils.isEmpty(longitude)?null:new BigDecimal(longitude));
            memberWxMp.setLastLatitude(StringUtils.isEmpty(latitude)?null:new BigDecimal(latitude));
            memberWxMpService.saveMemberWxMp(memberWxMp);
        }

        String token = StringRandom.getStringRandom(50);
        try {
            //??????token?????????????????? ?????????????????????
            redisService.set(token,member.getAccountCode(),1000*60*60*24*3L); //token??????3???
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }


        //????????????
        WXLoginResponseVo wxLoginResponseVo = new WXLoginResponseVo();
        wxLoginResponseVo.setAccountCode(member.getAccountCode());
        wxLoginResponseVo.setAvatarRequest(member.getAvatarRequest());
        wxLoginResponseVo.setCertificationType(member.getCertificationType());
        wxLoginResponseVo.setNickName(member.getNickName());
        wxLoginResponseVo.setIsAuth(BaseBoolean.TRUE);
        if(StringUtils.isEmpty(member.getPhoneNumber())){
            wxLoginResponseVo.setIsAuthPhoneNumber(BaseBoolean.FALSE);
        }else{
            wxLoginResponseVo.setIsAuthPhoneNumber(BaseBoolean.TRUE);
        }
        wxLoginResponseVo.setLastLongitude(StringUtils.isEmpty(longitude)?null:new BigDecimal(longitude));
        wxLoginResponseVo.setLastLatitude(StringUtils.isEmpty(latitude)?null:new BigDecimal(latitude));
        wxLoginResponseVo.setToken(token);
        return wxLoginResponseVo;
    }

    private JSONObject getUserWXLoginInfo(String wxCode) {
        MinichatSet minichatSet = minichatSetService.findlastMinichatSets();
        String requestUrl = WechatConstants.REQUEST_URL;
        Map<String,String> requestUrlParam = new HashMap<String,String>();
        requestUrlParam.put(WechatConstants.APPID, minichatSet.getMinichatAppId());	//?????????????????????appId
        requestUrlParam.put(WechatConstants.SECRET, minichatSet.getMinichatAppSecret());	//?????????????????????appSecret
        requestUrlParam.put(WechatConstants.JS_CODE, wxCode);	//???????????????wx.login?????????code
        requestUrlParam.put(WechatConstants.GRANT_TYPE, WechatConstants.GRANT_TYPE_authorization_code);	//????????????
        JSONObject jsonObject = JSON.parseObject(UrlUtil.sendPost(requestUrl, requestUrlParam));
        return jsonObject;
    }


    /**
     * ????????????????????????????????????
     * @param request
     * @param wxCode
     * @param encryptedData
     * @param iv
     * @return
     */
    @Transactional
    public WXLoginResponseVo getPhoneNumber(HttpServletRequest request, String wxCode, String encryptedData, String iv){
        //????????????api???????????????openid???sessionKey
        JSONObject jsonObject = getUserWXLoginInfo(wxCode);
        if(jsonObject!=null&&!jsonObject.containsKey(WechatConstants.OPENID)) {
            throw new FebsException("????????????!");
        }
        String openId = (String)jsonObject.get(WechatConstants.OPENID);
        String sessionKey = (String)jsonObject.get(WechatConstants.SESSION_KEY);

        String phoneNumberInfoStr = WeChatUtils.getUserInfo(encryptedData,sessionKey,iv);
        JSONObject userInfoJsonObject = JSON.parseObject(phoneNumberInfoStr);
        if(userInfoJsonObject == null ) {
            throw new FebsException("????????????!");
        }

        String phoneNumber = userInfoJsonObject.getString(WechatConstants.PHONE_NUMBER);
        String purPhoneNumber = userInfoJsonObject.getString(WechatConstants.PUR_PHONE_NUMBER);
        String countryCode = userInfoJsonObject.getString(WechatConstants.COUNTRY_CODE);

        if(StringUtils.isEmpty(phoneNumber)){
            throw new FebsException("????????????!");
        }

        //??????sessionKey??????????????????
        Member member = null;

        MemberWxMp memberWxMp = memberWxMpService.findMemberWxMpById(openId);
        if(memberWxMp == null){
            throw new FebsException("?????????????????????!");
        }else{
            member = memberService.findMemberById(memberWxMp.getBindMemberId());
            if(member == null){
                throw new FebsException("?????????????????????!");
            }else{
                memberWxMp.setPhoneNumber(phoneNumber);
                memberWxMp.setPurPhoneNumber(purPhoneNumber);
                memberWxMp.setCountryCode(countryCode);
                memberWxMpService.saveMemberWxMp(memberWxMp);

                member.setPhoneNumber(phoneNumber);
                memberService.saveMember(member);
            }
        }

        //??????token
        String token = request.getHeader("token");
        if(!StringUtils.isEmpty(token)) {
            try {
                redisService.set(token, member.getAccountCode(), 1000 * 60 * 60 * 24 * 3L); //token??????3???
            } catch (RedisConnectException e) {
                e.printStackTrace();
            }
        }

        //????????????
        WXLoginResponseVo wxLoginResponseVo = new WXLoginResponseVo();
        wxLoginResponseVo.setAccountCode(member.getAccountCode());
        wxLoginResponseVo.setAvatarRequest(member.getAvatarRequest());
        wxLoginResponseVo.setCertificationType(member.getCertificationType());
        wxLoginResponseVo.setNickName(member.getNickName());
        wxLoginResponseVo.setIsAuth(BaseBoolean.TRUE);
        if(StringUtils.isEmpty(member.getPhoneNumber())){
            wxLoginResponseVo.setIsAuthPhoneNumber(BaseBoolean.FALSE);
        }else{
            wxLoginResponseVo.setIsAuthPhoneNumber(BaseBoolean.TRUE);
        }
        wxLoginResponseVo.setLastLongitude(member.getLastLongitude());
        wxLoginResponseVo.setLastLatitude(member.getLastLatitude());
        wxLoginResponseVo.setToken(token);
        return wxLoginResponseVo;
    }

    /**
     * ??????????????????
     * @return
     */
    public boolean tokenVerify(String accountCode,String token){
        try {
            String accountCodeTemp = redisService.get(token);
            if(StringUtils.isEmpty(accountCodeTemp)){
               return false;
            }else{
                if(!accountCodeTemp.equals(accountCode)){
                   return false;
                }
            }
            return true;
        } catch (RedisConnectException e) {
            e.printStackTrace();
            throw new FebsException("????????????!");
        }

    }
}
