package com.handturn.bole.websocket.service;

/**
 * @Auther: Eric
 * @Date: 2019/1/11 11:48
 * @Description: websocket 服务类
 */

import com.alibaba.fastjson.JSONObject;
import com.handturn.bole.common.utils.DESToolUtil;
import com.handturn.bole.common.utils.SpringContextUtil;
import com.handturn.bole.main.equipment.entity.MiddlewareInfo;
import com.handturn.bole.system.entity.SysOrganizationOc;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.websocket.bean.EquipmentRemoteOpenRequestInterfaceBean;
import com.handturn.bole.websocket.bean.HeartbeatInterfaceBean;
import com.handturn.bole.websocket.bean.VisitorLogAsyRequestInterfaceBean;
import com.handturn.bole.websocket.bean.entity.WebSocketOpenBean;
import com.handturn.bole.websocket.constant.WebSocketConstant;
import com.handturn.bole.websocket.dto.HeartbeatReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @ServerEndpoint
 *
 * 这个注解用于标识作用在类上，它的主要功能是把当前类标识成一个WebSocket的服务端
 * 注解的值用户客户端连接访问的URL地址
 *
 */

@Slf4j
@Component
@ServerEndpoint("/websocket")
public class WebSocketServer {

    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 标识当前连接客户端的MAC地址
     */
    private String clientMac;

    /**
     *  用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, WebSocketOpenBean> webSocketBeanMap = new ConcurrentHashMap<>();


    private IWebSocketServerService getWebSocketServerService(){
       return SpringContextUtil.getBean(IWebSocketServerService.class);
    }

    @OnOpen
    public void OnOpen(Session session){
        Boolean openFlag = true;

        Map<String, List<String>> params = session.getRequestParameterMap();
        String ocToken = params.get(WebSocketConstant.CONNECT_PARAM_OCTOKEN) == null || params.get(WebSocketConstant.CONNECT_PARAM_OCTOKEN).size() <= 0?"":params.get(WebSocketConstant.CONNECT_PARAM_OCTOKEN).get(0);
        String mac = params.get(WebSocketConstant.CONNECT_PARAM_MAC) == null || params.get(WebSocketConstant.CONNECT_PARAM_MAC).size() <= 0?"":params.get(WebSocketConstant.CONNECT_PARAM_MAC).get(0);
        String lanIp = params.get(WebSocketConstant.CONNECT_PARAM_LANIP) == null || params.get(WebSocketConstant.CONNECT_PARAM_LANIP).size() <= 0?"":params.get(WebSocketConstant.CONNECT_PARAM_LANIP).get(0);
        String accountCode = params.get(WebSocketConstant.CONNECT_PARAM_ACCOUNTCODE) == null || params.get(WebSocketConstant.CONNECT_PARAM_ACCOUNTCODE).size() <= 0?"":params.get(WebSocketConstant.CONNECT_PARAM_ACCOUNTCODE).get(0);
        String password = params.get(WebSocketConstant.CONNECT_PARAM_PASSWORD) == null || params.get(WebSocketConstant.CONNECT_PARAM_PASSWORD).size() <= 0?"":params.get(WebSocketConstant.CONNECT_PARAM_PASSWORD).get(0);

        String appVersion = params.get(WebSocketConstant.CONNECT_PARAM_APPVERSION) == null || params.get(WebSocketConstant.CONNECT_PARAM_APPVERSION).size() <= 0?"":params.get(WebSocketConstant.CONNECT_PARAM_APPVERSION).get(0);
        String systemName = params.get(WebSocketConstant.CONNECT_PARAM_SYSTEMNAME) == null || params.get(WebSocketConstant.CONNECT_PARAM_SYSTEMNAME).size() <= 0?"":params.get(WebSocketConstant.CONNECT_PARAM_SYSTEMNAME).get(0);
        String systemVersion = params.get(WebSocketConstant.CONNECT_PARAM_SYSTEMVERSION) == null || params.get(WebSocketConstant.CONNECT_PARAM_SYSTEMVERSION).size() <= 0?"":params.get(WebSocketConstant.CONNECT_PARAM_SYSTEMVERSION).get(0);

        String ocCode = "";
        try {
            if(StringUtils.isEmpty(ocToken) || StringUtils.isEmpty(mac) || StringUtils.isEmpty(accountCode) || StringUtils.isEmpty(password)){
                this.OnClose();
                return;
            }

            ocCode = DESToolUtil.decrypt(ocToken, DESToolUtil.KEY_);
            String passwordM = DESToolUtil.decrypt(password, DESToolUtil.KEY_);

            SysOrganizationOc ocInfo = getWebSocketServerService().findSysOrganizationOcByCode(ocCode);
            SysUser user = getWebSocketServerService().findByUserCode(accountCode);
            if(ocInfo != null && user != null){
                if (user.getOcId() != ocInfo.getId()){
                    openFlag = false;
                }
                if(!user.getUserCode().equals(passwordM)){
                    openFlag = false;
                }
            }else{
                openFlag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            openFlag = false;
        }

        if(openFlag){
            this.session = session;
            this.clientMac = mac;

            WebSocketOpenBean webSocketOpenBean = new WebSocketOpenBean();
            webSocketOpenBean.setMacId(mac);
            webSocketOpenBean.setLanIp(lanIp);
            webSocketOpenBean.setOcCode(ocCode);
            webSocketOpenBean.setAppVersion(appVersion);
            webSocketOpenBean.setSystemName(systemName);
            webSocketOpenBean.setSystemVersion(systemVersion);
            webSocketBeanMap.put(mac,webSocketOpenBean);

            // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
            webSocketSet.put(mac,this);
            log.info("[WebSocket] 连接成功，当前连接人数为：={}",webSocketSet.size());
        }else{
            this.OnClose();
            log.info("[WebSocket] 连接验证失败");
        }
    }


    @OnClose
    public void OnClose(){
        if(webSocketSet != null){
            webSocketSet.remove(this.clientMac);
            log.info("[WebSocket] 退出成功，当前连接人数为：={}");
        }
        if(webSocketBeanMap != null){
            getWebSocketServerService().closeConnect(webSocketBeanMap.get(this.clientMac));
            webSocketBeanMap.remove(this.clientMac);
        }


    }

    @OnMessage
    public void OnMessage(String message){
        log.info("[WebSocket] 收到消息：{}",message);
        //心跳
        if(message.contains(WebSocketConstant.INTERFACE_TYPE_HEARTBEAT)){
            HeartbeatInterfaceBean heartbeatInterfaceBean =JSONObject.parseObject(message, HeartbeatInterfaceBean.class);
            HeartbeatReturnDto heartbeatReturnDto = getWebSocketServerService().heartbeat(heartbeatInterfaceBean,webSocketBeanMap.get(this.clientMac));

            //发送设备信息
            getWebSocketServerService().equipmentAsyReturn(heartbeatReturnDto,webSocketBeanMap.get(this.clientMac),this);
            //发送离线访客信息
            getWebSocketServerService().visitorAsyReturn(heartbeatReturnDto,webSocketBeanMap.get(this.clientMac),this);
        }

        //手工同步
        if(message.contains(WebSocketConstant.INTERFACE_TYPE_ASY_APPLY)){
            getWebSocketServerService().asyApply(webSocketBeanMap.get(this.clientMac),this);
        }
        //日志同步
        if(message.contains(WebSocketConstant.INTERFACE_TYPE_VISITORLOG_REQUEST)){
            VisitorLogAsyRequestInterfaceBean visitorLogAsyRequestInterfaceBean =JSONObject.parseObject(message, VisitorLogAsyRequestInterfaceBean.class);
            getWebSocketServerService().visitorLogAsyRequest(visitorLogAsyRequestInterfaceBean,webSocketBeanMap.get(this.clientMac));
        }


        //判断是否需要指定发送，具体规则自定义
        /*if(message.indexOf("TOUSER") == 0){
            String name = message.substring(message.indexOf("TOUSER")+6,message.indexOf(";"));
            AppointSending(name,message.substring(message.indexOf(";")+1,message.length()));
        }else{
            GroupSending(message);
        }*/
    }

    @OnMessage
    public void OnByte(byte[] messageByte){
        log.info("[WebSocket] 收到二进制：{}",messageByte);
    }

    /**
     * 远程开门
     * @param equipmentSn
     */
    public Boolean remoteOpen(String equipmentSn,String ocCode){
        MiddlewareInfo middlewareInfo = getWebSocketServerService().findMiddlewareInfoByEquipmentSn(equipmentSn,ocCode);
        if(middlewareInfo != null){
            try {
                EquipmentRemoteOpenRequestInterfaceBean remoteOpenBean = new EquipmentRemoteOpenRequestInterfaceBean();
                Set<String> equipmentSns = new HashSet<>();
                equipmentSns.add(equipmentSn);
                remoteOpenBean.setEquipmentSn(equipmentSns);
                String jsonStr = JSONObject.toJSONString(remoteOpenBean);
                AppointSending(middlewareInfo.getMacId(), jsonStr);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * 群发
     * @param message
     */
    public void GroupSending(String message){
        for (String name : webSocketSet.keySet()){
            try {
                webSocketSet.get(name).session.getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定发送
     * @param clientSn
     * @param message
     */
    public void AppointSending(String clientSn,String message){
        try {
            webSocketSet.get(clientSn).session.getBasicRemote().sendText(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
