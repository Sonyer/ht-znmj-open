package com.handturn.bole.front.api.me.service.impl;

import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.common.utils.StringUtil;
import com.handturn.bole.front.api.me.service.IMeService;
import com.handturn.bole.front.api.me.vo.InitMeResponseVo;
import com.handturn.bole.master.member.entity.Member;
import com.handturn.bole.master.member.service.IMemberService;
import com.handturn.bole.master.member.service.IMemberWxMpService;
import com.handturn.bole.master.set.entity.NotifySet;
import com.handturn.bole.master.set.entity.SitSet;
import com.handturn.bole.master.set.service.IImgStoreSetService;
import com.handturn.bole.master.set.service.IMinichatSetService;
import com.handturn.bole.master.set.service.INotifySetService;
import com.handturn.bole.master.set.service.ISitSetService;
import com.handturn.bole.monitor.service.IRedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MeServiceImpl implements IMeService {
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

    /**
     * 获取我的信息
     * @param request
     * @return
     */
    @Override
    public InitMeResponseVo initMe(HttpServletRequest request){
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

        Set<String> statuses = new HashSet<>();
        statuses.add(BaseStatus.ENABLED);
        List<NotifySet> setNotifys = notifySetService.findNotifySetByIs(null,null, BaseBoolean.TRUE,null,statuses);
        List<String> notifyTemplateIds = new ArrayList<String>();
        setNotifys.forEach(setNotify ->{
            notifyTemplateIds.add(setNotify.getWxNotifyTemplateId());
        });

        InitMeResponseVo initMeVo = new InitMeResponseVo();
        initMeVo.setAccountCode(member.getAccountCode());
        initMeVo.setNickName(member.getNickName());
        initMeVo.setGender(member.getGender());
        initMeVo.setAvatarRequest(member.getAvatarRequest());
        initMeVo.setPhoneNumber(member.getPhoneNumber());
        initMeVo.setVipLevel(member.getVipLevel());
        initMeVo.setCertificationType(member.getCertificationType());
        initMeVo.setIsPersionUser(member.getIsPersionUser());
        initMeVo.setIsClientUser(member.getIsClientUser());

        initMeVo.setFollowCount(member.getFollowCount());

        SitSet sitSet = sitSetService.findlastSitSets();
        initMeVo.setCopyright(sitSet.getCopyright());
        initMeVo.setAboutUs(sitSet.getAboutUs());
        initMeVo.setInstDocRequest(sitSet.getInstDocRequest());

        initMeVo.setWxNotifyTemplatIds(notifyTemplateIds);
        return initMeVo;

    }

    /**
     * 修改昵称
     * @param request
     * @param nickName
     * @return
     */
    @Override
    @Transactional
    public Boolean updateNickName(HttpServletRequest request,String nickName){
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

        int strLength = StringUtil.strLength(nickName);
        if(strLength < 4 || strLength > 32){
            throw new FebsException("昵称长度必须在中文[2-16]英文[4-32]之间!");
        }

        member.setNickName(nickName);
        memberService.saveMember(member);

        return true;
    }

    /**
     * 上传个人图片
     * @param file
     * @param accountCode
     * @param token
     * @return
     */
    @Override
    @Transactional
    public String uploadMeAvatar(MultipartFile file, String accountCode, String token){
        String accountCodeTemp = null;
        try {
            accountCodeTemp = redisService.get(token);
        } catch (RedisConnectException e) {
            e.printStackTrace();
            throw new FebsException("您还未登陆!");
        }

        if (!accountCodeTemp.equals(accountCode)){
            throw new FebsException("您还未登陆!");
        }

        Member member = memberService.findMemberByAccountCode(accountCode);
        if(member == null){
            throw new FebsException("您还未登陆!");
        }


        ImportResultBean resultBean = null;
        try {
            resultBean = imgStoreSetService.importUpload(file, imgStoreSetService.IMGS_ME_AVATAR);
        }catch (Exception e){
            e.printStackTrace();
            throw new FebsException("图片存储失败!");
        }

        member.setAvatarRequest(resultBean.getFileRequestUrl());
        member.setAvatarAttchment(resultBean.getFilePath());
        memberService.saveMember(member);

        return member.getAvatarRequest();

    }

}
