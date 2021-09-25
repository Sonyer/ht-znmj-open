package com.handturn.bole.master.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.handturn.bole.master.common.service.IWxAccessTokenService;
import com.handturn.bole.master.set.entity.MinichatSet;
import com.handturn.bole.master.set.service.IMinichatSetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WxAccessTokenServiceImpl implements IWxAccessTokenService {
    @Autowired
    private IMinichatSetService minichatSetService;

    /**
     * 获取微信小程序AccessTocken
     * @return
     */
    public String getAccessToken4Wx() {
        MinichatSet minichatSet = minichatSetService.findlastMinichatSets();

        return getAccessToken4Wx(minichatSet);
    }

    /**
     * 获取微信小程序AccessTocken
     * @return
     */
    public String getAccessToken4Wx(MinichatSet minichatSet) {

        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?" +
                "grant_type=client_credential" +
                "&appid="+minichatSet.getMinichatAppId()+
                "&secret="+minichatSet.getMinichatAppSecret();
        try {
            HttpClient client = HttpClientBuilder.create().build();//构建一个Client
            HttpGet get = new HttpGet(access_token_url.toString());    //构建一个GET请求
            get.addHeader("Content-Type", "application/json");
            HttpResponse response = client.execute(get);//提交GET请求
            HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
            String content = EntityUtils.toString(result);

            System.out.println("获取微信小程序AccessTocken请求:"+access_token_url);
            System.out.println("获取微信小程序AccessTocken返回:"+content);
            Map<String,String> map = JSONObject.parseObject(content,Map.class);
            return map.get("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
