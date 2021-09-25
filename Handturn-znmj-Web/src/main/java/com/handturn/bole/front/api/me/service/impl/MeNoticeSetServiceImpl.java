package com.handturn.bole.front.api.me.service.impl;

import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.front.api.me.service.IMeNoticeSetService;
import com.handturn.bole.front.api.me.vo.InitMeNoticeSetResponseVo;
import com.handturn.bole.master.set.entity.NotifySet;
import com.handturn.bole.master.set.service.INotifySetService;
import com.handturn.bole.monitor.service.IRedisService;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MeNoticeSetServiceImpl implements IMeNoticeSetService {
    @Autowired
    private IRedisService redisService;
    @Autowired
    private ISysconfDictCodeService sysconfDictCodeService;
    @Autowired
    private INotifySetService notifySetService;

    /**
     * 获取我的通知
     * @param request
     * @return
     */
    @Override
    public List<InitMeNoticeSetResponseVo> initMeNoticeSet(HttpServletRequest request){
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

        Map<String,Object> typeMap = sysconfDictCodeService.getDictMapByTypeCode("MemberNotifyType");

        Set<String> statuese = new HashSet<String>();
        statuese.add(BaseStatus.ENABLED);
        List<NotifySet> setNotifys = notifySetService.findNotifySetByIs(null,null, BaseBoolean.TRUE,null,statuese);
        List<InitMeNoticeSetResponseVo> result = new ArrayList<>();
        setNotifys.forEach(setNotify ->{
            InitMeNoticeSetResponseVo responseVo = new InitMeNoticeSetResponseVo();
            responseVo.setNotifyType(typeMap.get(setNotify.getNotifyType()) == null?setNotify.getNotifyType():typeMap.get(setNotify.getNotifyType()).toString());
            responseVo.setWxNotifyTemplateId(setNotify.getWxNotifyTemplateId());
            responseVo.setOwnerType(setNotify.getOwnerType());
            responseVo.setNotifyDec(setNotify.getNotifyDec());
            result.add(responseVo);
        });

        return result;
    }

}
