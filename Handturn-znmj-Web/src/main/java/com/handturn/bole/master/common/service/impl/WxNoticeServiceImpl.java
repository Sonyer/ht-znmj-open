package com.handturn.bole.master.common.service.impl;

import com.handturn.bole.master.common.service.IWxAccessTokenService;
import com.handturn.bole.master.common.service.IWxNoticeService;
import com.handturn.bole.master.common.vo.WxNoticeRequestVo;
import com.handturn.bole.master.member.entity.MemberWxMp;
import com.handturn.bole.master.member.service.IMemberWxMpService;
import com.handturn.bole.monitor.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WxNoticeServiceImpl implements IWxNoticeService {
    @Autowired
    private IRedisService redisService;

    @Autowired
    private IWxAccessTokenService wxAccessTokenService;
    @Autowired
    private IMemberWxMpService memberWxMpService;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Autowired
    private RestTemplate restTemplate;

    @Value("${backEnd.miniprogramState}")
    private String backEnMiniprogramState;

    /**
     * 微信通知服务
     * @param memeberId
     * @param templateId
     * @param mapData
     * @return
     */
    @Override
    public String pushNoticeToUser(Long memeberId,String templateId,String toPage,Map<String, Map<String,String>> mapData){
        MemberWxMp wxmember = memberWxMpService.findMemberWxMpByMemberId(memeberId);
        if(wxmember == null){
            log.debug("消息通知:会员ID"+memeberId+"找不到微信唯一码!");
        }
        return this.pushNoticeToUser(wxmember.getOpenid(),templateId,toPage,mapData);
    }

    /**
     * @param openid 用户openid
     * @param templateId 模板ID
     * @param mapData
     * @return
     */
    @Override
    public String pushNoticeToUser(String openid,String templateId,String toPage,Map<String, Map<String,String>> mapData) {

        String access_token = wxAccessTokenService.getAccessToken4Wx();

        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send" +
                "?access_token=" + access_token;

        //拼接推送的模版
        WxNoticeRequestVo wxNoticeVo = new WxNoticeRequestVo();
        wxNoticeVo.setTouser(openid);//用户openid
        wxNoticeVo.setPage(toPage);
        wxNoticeVo.setTemplate_id(templateId);//模版id
        wxNoticeVo.setMiniprogram_state(backEnMiniprogramState);
        wxNoticeVo.setData(mapData);

        if(restTemplate==null){
            restTemplate = new RestTemplate();
        }

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, wxNoticeVo, String.class);
        log.error("小程序推送结果={}", responseEntity.getBody());
        return responseEntity.getBody();
    }

    public static void main(String[] args) {
        /*IWxNoticeService service = new WxNoticeServiceImpl();
        String values[] ={"李小姐-18607316753","2020-5-8 10:10:10","新鲜蔬菜500g\n地址:湖南省长沙市岳麓区阳光100后海74栋","生意来了，立即备货>>"};
        service.pushNoticeToUser("oXPQs5O4gJZsYBfUeZZxTYJHevLU","ec76b8b81cd04cf6b464bb0adf309d3b","XVmwXTNaZ9rAFFB_wuzyDO2N6nfYR6LmzyQMgonBYlE"
                ,values);*/
    }
}
