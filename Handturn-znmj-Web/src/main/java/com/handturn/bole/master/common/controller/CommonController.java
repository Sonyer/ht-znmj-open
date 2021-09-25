package com.handturn.bole.master.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.master.common.service.IWxAccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 智能门禁-微信登陆 Controller
 *
 * @author Eric
 * @date 2020-04-11 20:07:29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sitapi/common")
public class CommonController {

    @Autowired
    private IWxAccessTokenService wxAccessTokenService;

    @GetMapping("miniqrQrImg")
    public FebsResponse miniqrQr(HttpServletRequest request, HttpServletResponse response, String scene, String pagePath) {
        String accessToken = wxAccessTokenService.getAccessToken4Wx();

        try {
            pagePath = java.net.URLDecoder.decode(pagePath,"UTF-8");
            System.out.println(pagePath);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //组装url
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+ accessToken;
        //组装参数
        Map<String, Object> paraMap = new HashMap();
        //二维码携带参数 不超过32位
        paraMap.put("scene", scene);
        //二维码跳转页面
        paraMap.put("page", pagePath);
        paraMap.put("width", 250);
        paraMap.put("auto_color", false);
        Map<String,Object> lineColorMap = new HashMap<>();
        lineColorMap.put("r","0");
        lineColorMap.put("g","0");
        lineColorMap.put("b","0");
        paraMap.put("line_color", lineColorMap);
        //执行post 获取数据流
        byte[] result = this.doImgPost(url, paraMap);

        //输出图片到页面
        PrintWriter out = null;
        try {
            out = response.getWriter();
            InputStream is = new ByteArrayInputStream(result);
            int a = is.read();
            while (a != -1) {
                out.print((char) a);
                a = is.read();
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null){
                out.close();
            }
        }
        return null;
    }

    /**
     *获取 数据流
     *@Params: [url, map]
     *@Date: 2018/5/15 15:34
     *@Author: jinghan
     */
    public static byte[] doImgPost(String url, Map<String,Object> map) {
        byte[] result = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        try {
            // 设置请求的参数
            /*JSONObject postData = new JSONObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                postData.put(entry.getKey(), entry.getValue());
            }*/
            String jsonStr = JSONObject.toJSONString(map);
            System.out.println("生成微信二维码请求:"+jsonStr);
            httpPost.setEntity(new StringEntity(jsonStr, "UTF-8"));
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toByteArray(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return result;
    }

}
