package com.handturn.bole.front.api.me.service.impl;

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.front.api.me.service.IMeNoticeService;
import com.handturn.bole.front.api.me.vo.InitMeNoticeResponseVo;
import com.handturn.bole.master.member.entity.MemberNotify;
import com.handturn.bole.master.member.entity.MemberNotifyReadStatus;
import com.handturn.bole.master.member.mapper.MemberNotifyMapper;
import com.handturn.bole.master.member.service.IMemberNotifyService;
import com.handturn.bole.master.member.service.IMemberService;
import com.handturn.bole.master.member.service.IMemberWxMpService;
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
public class MeNoticeServiceImpl implements IMeNoticeService {
    @Autowired
    private IRedisService redisService;
    @Autowired
    private ISysconfDictCodeService sysconfDictCodeService;
    @Autowired
    private IMemberWxMpService memberWxMpService;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IMemberNotifyService memberNotifyService;
    @Autowired
    private MemberNotifyMapper memberNotifyMapper;

    /**
     * 获取我的通知
     * @param request
     * @return
     */
    @Override
    public List<InitMeNoticeResponseVo> initMeNotice(HttpServletRequest request, Integer pageIndex){
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

        CustomPage<MemberNotify> page = new CustomPage<>(Integer.valueOf(pageIndex), 20);
        page.setDesc("id");
        Set<String> readStatuses = new HashSet<String>();
        readStatuses.add(MemberNotifyReadStatus.UNREAD);
        readStatuses.add(MemberNotifyReadStatus.READED);

        //获取字典
        Map<String, Object> memberNotifyTypeMap = sysconfDictCodeService.getDictMapByTypeCode("MemberNotifyType");

        List<MemberNotify> notifys = memberNotifyMapper.findForPageByMember(page,accountCode,readStatuses);

        List<InitMeNoticeResponseVo> noticeResponseVos = new ArrayList<InitMeNoticeResponseVo>();
        notifys.forEach(notify ->{
            InitMeNoticeResponseVo responseVo = new InitMeNoticeResponseVo();
            responseVo.setNotifyType(memberNotifyTypeMap.get(notify.getNotifyType()) == null?"消息通知":memberNotifyTypeMap.get(notify.getNotifyType()).toString());
            responseVo.setNotifyMessage(notify.getInnerNotifyMessage());
            responseVo.setPage(notify.getInnerNotifyToPage());
            responseVo.setReadStatus(notify.getReadStatus());
            responseVo.setCreateDate(notify.getCreateDate());
            noticeResponseVos.add(responseVo);
        });

        return noticeResponseVos;
    }

}
