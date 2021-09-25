package com.handturn.bole.common.interceptcontent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截违规信息
 */
public class InterceptContentUntil {

    /**
     *  纯文本拦截敏感词
     * @param textConetnt
     * @return
     */
    public static Boolean checkText(String textConetnt,String accessToken) {

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            CloseableHttpResponse response = null;

            HttpPost request = new HttpPost("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken);
            request.addHeader("Content-Type", "application/json;charset=UTF-8");

            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("content", textConetnt);
            request.setEntity(new StringEntity(JSONObject.toJSONString(paramMap), ContentType.create("application/json", "utf-8")));


            response = httpclient.execute(request);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");// 转成string
            JSONObject jso = JSONObject.parseObject(result);


            Object errcode = jso.get("errcode");
            int errCode = (int) errcode;
            if (errCode == 0) {
                return true;
            } else if (errCode == 87014) {
                System.out.println("内容违规-----------" + textConetnt);
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("----------------调用腾讯内容过滤系统出错------------------");
            return false;
        }
    }

    public static Boolean checkPic(MultipartFile file,String accessToken) {
        String token = "";
        try {
            String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken;
            String result = uploadFile(url, file);
            Map<String,Object> resultMap = JSON.parseObject(result, Map.class);
            System.out.println("图片检测结果 = " + result);
            if(resultMap != null && resultMap.get("errcode").toString().trim().equals("0")){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("----------------调用腾讯内容过滤系统出错------------------" + e.getMessage());
            return false;
        }
    }

    /**
     * 上传二进制文件
     * @param graphurl 接口地址
     * @param file 图片文件
     * @return
     */
    public static String uploadFile(String graphurl,MultipartFile file) {
        String line = null;//接口返回的结果
        try {
            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9";
            // 服务器的域名
            URL url = new URL(graphurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
            OutputStream out = new DataOutputStream(conn.getOutputStream());

            // 上传文件
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"image\";filename=\""
                    + "https://api.weixin.qq.com" + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);

            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());

            // 读取文件数据
            out.write(file.getBytes());
            // 最后添加换行
            out.write(newLine.getBytes());

            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY
                    + boundaryPrefix + newLine).getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();
            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                return line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
        }
        return line;
    }
}
