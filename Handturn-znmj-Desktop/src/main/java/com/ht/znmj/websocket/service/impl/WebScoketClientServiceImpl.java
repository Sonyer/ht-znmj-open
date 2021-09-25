package com.ht.znmj.websocket.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ht.znmj.common.customPage.CustomPage;
import com.ht.znmj.entity.*;
import com.ht.znmj.sdk.MyFunction;
import com.ht.znmj.sdk.ZBX_Global;
import com.ht.znmj.sdk.libFaceRecognition;
import com.ht.znmj.service.*;
import com.ht.znmj.utils.*;
import com.ht.znmj.view.service.ApplicationUtil;
import com.ht.znmj.view.service.ManagerVisitorLogUtil;
import com.ht.znmj.websocket.bean.*;
import com.ht.znmj.websocket.bean.entity.HeartbeatInterfaceBean;
import com.ht.znmj.websocket.bean.entity.VisitorInfoAsyApplyReturnVo;
import com.ht.znmj.websocket.constant.WebSocketConstant;
import com.ht.znmj.websocket.dto.HeartbeatRequestDto;
import com.ht.znmj.websocket.service.IWebSocketClientService;
import com.sun.jna.ptr.IntByReference;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

/**
 * @Auther: eric
 * @Date: 2019/1/12 10:56
 * @Description: websocket接口实现类
 */
@Slf4j
@Component
@EnableScheduling
public class WebScoketClientServiceImpl implements IWebSocketClientService {

    private static WebSocketClient webSocketClient;

    @Autowired
    private ICloudInterfaceInfoService cloudInterfaceInfoService;
    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    @Autowired
    private IVisitorInfoService visitorInfoService;
    @Autowired
    private IVisitorEquipmentService visitorEquipmentService;

    @Autowired
    private IVisitorLogService visitorLogService;

    @Override
    public void groupSending(String message) {
        // 这里我加了6666-- 是因为我在index.html页面中，要拆分用户编号和消息的标识，只是一个例子而已
        // 在index.html会随机生成用户编号，这里相当于模拟页面发送消息
        // 实际这样写就行了 webSocketClient.send(message)
        webSocketClient.send(message+"---6666");
    }

    @Override
    public void appointSending(String name, String message) {
        // 这里指定发送的规则由服务端决定参数格式
        webSocketClient.send("TOUSER"+name+";"+message);
    }

    @Scheduled(cron = "*/30 * * * * ?")
    public void heartbeat(){
        if(webSocketClient != null && webSocketClient.isOpen()){
            try {
                CloudInterfaceInfo cloudInterfaceInfo = cloudInterfaceInfoService.findCloudInterfaceInfoById("1");
                Date currentHearBeatTime = new Date();

                List<EquipmentInfo> equipmentInfos = equipmentInfoService.findEquipmentInfosByConnStatus(EquipmentInfoConnStatus.CONNECTED.getCode());

                HeartbeatInterfaceBean heartbeatInterfaceBean = new HeartbeatInterfaceBean();
                heartbeatInterfaceBean.setEquipmentInfoList(equipmentInfos);
               // String equipmentJsonStr = JSONArray.toJSONString(equipmentInfos);
                String jsonStr = JSONObject.toJSONString(heartbeatInterfaceBean);

                webSocketClient.send(jsonStr);
                cloudInterfaceInfo.setHeartbeatTime(currentHearBeatTime);
                cloudInterfaceInfoService.saveCloudInterfaceInfo(cloudInterfaceInfo);

                List<HeartbeatRequestDto> requestVos = new ArrayList<>();
                equipmentInfos.forEach(equipmentInfo ->{
                    HeartbeatRequestDto heartbeatRequestDto = new HeartbeatRequestDto();
                    heartbeatRequestDto.setStartDateTime(equipmentInfo.getHeartbeatTime());
                    heartbeatRequestDto.setEndDaateTime(currentHearBeatTime);
                    heartbeatRequestDto.setMaxLogSeq(equipmentInfo.getMaxLogSeq());
                    Set<String> equipmentMac = new HashSet<>();
                    equipmentMac.add(equipmentInfo.getSn());
                    heartbeatRequestDto.setEquipmentMacIds(equipmentMac);
                    requestVos.add(heartbeatRequestDto);

                    equipmentInfo.setHeartbeatTime(currentHearBeatTime);
                    equipmentInfoService.saveEquipmentInfo(equipmentInfo);
                });

                //日志请求
                visitorLogRequest(requestVos);

                equipmentVisitorLogLineOff(requestVos);
            }catch(Exception e){
                e.printStackTrace();
                log.error("心跳执行失败");
            }
        }else{
            //重新初始化
            initWebSocketClient();
        }
    }

    /**
     * 触发设备访客设置
     * @param requestVos
     */
    public void equipmentVisitorLogLineOff(List<HeartbeatRequestDto> requestVos){
        requestVos.forEach(requestVo ->{
            int maxLogSeq = requestVo.getMaxLogSeq() == null?0:requestVo.getMaxLogSeq();
            String sn = requestVo.getEquipmentMacIds().iterator().next();
            IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(sn);

            while (true) {
                maxLogSeq++;

                libFaceRecognition.FaceRecoInfo cb = new libFaceRecognition.FaceRecoInfo();
                MyFunction.m_FaceRecognition.ZBX_QueryFaceOffLineReco(cameraPoint, maxLogSeq, cb);

                if(cb.sequence == 0){
                    break;
                }

                String imgOpenDirPath = LocalStoreUtils.imgOpenPath;
                File myPath = new File(imgOpenDirPath);
                if (!myPath.exists()) {
                    myPath.mkdirs();
                }

                long t = 1000;
                Date openTime = new Date();
                openTime.setTime(cb.tvSec * t);

                VisitorLog visitorLog = new VisitorLog();
                visitorLog.setEquipmentSn(sn);
                visitorLog.setOpenTime(openTime);
                visitorLog.setCreateTime(new Date());
                visitorLog.setUpdateTime(new Date());
                visitorLog.setCloudFlag(EquipmentInfoCloudFlag.NO_ASY.getCode());

                String filepath = "";
                if (cb.matched == -1) {
                    visitorLog.setOpenStatus(VisitorLogOpenStatus.NONE.getCode());
                    filepath = imgOpenDirPath + "\\"+ DateUtil.getDateFormat(new Date(),DateUtil.DATA_PATTERN_8)+"\\none\\";
                    File myfilepath = new File(filepath);
                    if (!myfilepath.exists()) {
                        myfilepath.mkdirs();
                    }
                } else {
                    visitorLog.setOpenStatus(VisitorLogOpenStatus.OPEN.getCode());
                    try {
                        String personname = new String(getfullbyte(cb.matchPersonName), StandardCharsets.UTF_8);
                        visitorLog.setVisitorName(personname.trim());
                        String perid = new String(getfullbyte(cb.matchPersonId),  StandardCharsets.UTF_8);

                        visitorLog.setVisitorId(perid.trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("获取设备离线抓拍数据-数据存在问题!");
                    }

                    filepath = imgOpenDirPath + "\\" + DateUtil.getDateFormat(new Date(), DateUtil.DATA_PATTERN_8) + "\\open\\";
                    File myfilepath = new File(filepath);
                    if (!myfilepath.exists()) {
                        myfilepath.mkdirs();
                    }
                }

                String desfileName = StringRandom.getStringNumRandom(20);
                String filename = filepath + desfileName  + ".jpg";
                if(cb.faceImg != null && cb.faceImgLen != 0){
                    byte[] data = cb.faceImg.getPointer().getByteArray(0, cb.faceImgLen);
                    try {
                        // 转换成图片
                        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(data));
                        //保存图片
                        PicConverUtil.writeImageFile(bi, filename);

                        visitorLog.setFaceFilePath(filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //保存日志
                visitorLog.setLogFlag(VisitorLogLogFlag.LINE_OFF.getCode());
                ManagerVisitorLogUtil.saveVisitorLog(visitorLog);
            }

            maxLogSeq --;
            equipmentInfoService.updateMaxLogSeq(sn,maxLogSeq);
        });
    }

    public static byte[] getfullbyte (byte[] orgbyte) {
        int length = 0;
        for (int i = 0; i < orgbyte.length; ++i) {
            if (orgbyte[i] != '\0') ++length;
            else break;
        }
        byte[] temp = new byte[length + 1];
        for (int i = 0; i < length; ++i) {
            temp[i] = orgbyte[i];
        }
        temp[length] = '\0';
        return temp;
    }

    public void visitorLogRequest(List<HeartbeatRequestDto> requestVos){

        //日志上传
        requestVos.forEach(requestVo ->{
            if(requestVo.getEquipmentMacIds() != null && requestVo.getEquipmentMacIds().size() > 0){
                Integer pageNum = 0;
                Integer pageSize = 200;
                while (true) {
                    pageNum ++;

                    CustomPage<VisitorLog> page = new CustomPage<>(pageNum, pageSize);
                    //查询日志
                    QueryWrapper<VisitorLog> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().in(VisitorLog::getEquipmentSn, requestVo.getEquipmentMacIds());
                    queryWrapper.lambda().le(VisitorLog::getUpdateTime, requestVo.getEndDaateTime());
                    queryWrapper.lambda().ge(VisitorLog::getUpdateTime, requestVo.getStartDateTime());
                    IPage<VisitorLog> page1 = visitorLogService.getBaseMapper().selectPage(page,queryWrapper);
                    if(page1.getRecords() == null || page1.getRecords().size() <=0){
                        break;
                    }else{
                        page1.getRecords().forEach(visitorLog ->{
                            VisitorLogAsyRequestInterfaceBean visitorLogAsyRequestInterfaceBean = new VisitorLogAsyRequestInterfaceBean();
                            String imgStream = "";
                            try {
                                if(!org.apache.commons.lang3.StringUtils.isEmpty(visitorLog.getFaceFilePath())){
                                    imgStream = ImgHelper.encodeImage(visitorLog.getFaceFilePath());
                                }
                                visitorLogAsyRequestInterfaceBean.setFaceFileStream(imgStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            visitorLogAsyRequestInterfaceBean.setVisitorLog(visitorLog);
                            String jsonStr = JSONObject.toJSONString(visitorLogAsyRequestInterfaceBean);
                            webSocketClient.send(jsonStr);
                        });
                    }
                }
            }
        });
    }

    /**
     * 同步请求 设备信息返回
     */
    @Transactional
    public void asyApplyEquipmentReturn(String message){
        log.info("收到远端设备更新信息-"+message);
        EquipmentAsyApplyReturnInterfaceBean equipmentAsyApplyReturnInterfaceBean =JSONObject.parseObject(message, EquipmentAsyApplyReturnInterfaceBean.class);
        List<EquipmentInfo> fromEquipments = equipmentAsyApplyReturnInterfaceBean.getEquipmentInfoList();
        fromEquipments.forEach(fromEquipment ->{
            EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoBySn(fromEquipment.getSn());
            if(equipmentInfo != null){
                equipmentInfo.setAreaName(fromEquipment.getAreaName());
                equipmentInfo.setInOut(fromEquipment.getInOut());
                equipmentInfo.setSeqNum(fromEquipment.getSeqNum());
                equipmentInfo.setUpdateTime(new Date());
                equipmentInfo.setCloudId(fromEquipment.getCloudId());
                equipmentInfoService.getBaseMapper().updateById(equipmentInfo);
            }
        });
    }

    /**
     * 同步请求 人员信息返回
     */
    @Transactional
    public void asyApplyVisitorReturn(String message){
        log.info("收到云端用户更新信息-"+message);
        VisitorAsyApplyReturnInterfaceBean visitorAsyApplyReturnInterfaceBean =JSONObject.parseObject(message, VisitorAsyApplyReturnInterfaceBean.class);
        VisitorInfoAsyApplyReturnVo visitorInfoAsyApplyReturnVo = visitorAsyApplyReturnInterfaceBean.getVisitorInfoAsyApplyReturnVo();

        if(visitorAsyApplyReturnInterfaceBean.getAuthType() != null && visitorAsyApplyReturnInterfaceBean.getAuthType().equals(InterfaceAreaAuthType.AUTHTYPE_DELETE)){
            if(visitorAsyApplyReturnInterfaceBean.getEquipmentMacs() != null && visitorAsyApplyReturnInterfaceBean.getEquipmentMacs().size() > 0) {
                visitorAsyApplyReturnInterfaceBean.getEquipmentMacs().forEach(mac->{
                    EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoBySn(mac);
                    if(equipmentInfo != null) {
                        if (!StringUtils.isEmpty(visitorInfoAsyApplyReturnVo.getCloudId())) {
                            List<VisitorInfo> visitorInfos = visitorInfoService.findVisitorInfoByCloudId(visitorInfoAsyApplyReturnVo.getCloudId());
                            Set<String> visitorIds = new HashSet<>();
                            visitorInfos.forEach(visitorInfo -> {
                                visitorIds.add(visitorInfo.getId());
                            });
                            if(visitorIds.size() > 0){
                                visitorEquipmentService.deleteVisitorEquipments(visitorIds,equipmentInfo.getId());
                            }
                        }
                    }
                });
            }
        }else {
            String faceFileStream = visitorAsyApplyReturnInterfaceBean.getFaceFileStream();
            Set<String> equipmentMacs = visitorAsyApplyReturnInterfaceBean.getEquipmentMacs();

            List<VisitorInfo> visitorInfos = visitorInfoService.findVisitorInfoByCloudId(visitorInfoAsyApplyReturnVo.getCloudId());
            VisitorInfo visitorInfo = null;
            if(visitorInfos != null && visitorInfos.size() > 0){
                visitorInfo = visitorInfos.get(0);
            }else{
                visitorInfo = new VisitorInfo();
            }

            visitorInfo.setName(visitorInfoAsyApplyReturnVo.getName());
            visitorInfo.setPhoneNumber(visitorInfoAsyApplyReturnVo.getPhoneNumber());
            visitorInfo.setIdCard(visitorInfoAsyApplyReturnVo.getIdCard());
            visitorInfo.setWigan(visitorInfoAsyApplyReturnVo.getWigan());
            visitorInfo.setDepartment(visitorInfoAsyApplyReturnVo.getDepartment());
            visitorInfo.setPositor(visitorInfoAsyApplyReturnVo.getPositor());
            visitorInfo.setStartTime(visitorInfoAsyApplyReturnVo.getStartTime() == null ? new Date() : visitorInfoAsyApplyReturnVo.getStartTime());
            visitorInfo.setCloudId(visitorInfoAsyApplyReturnVo.getCloudId());
            try {
                visitorInfo.setEndTime(visitorInfoAsyApplyReturnVo.getEndTime() == null ? DateUtil.formatStr2Date("2099-12-31 23:59:59", DateUtil.FULL_TIME_SPLIT_PATTERN) : visitorInfoAsyApplyReturnVo.getEndTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                byte[] imgByte = ImgHelper.decode(faceFileStream);
                String imgPath = PicConverUtil.toJPG(imgByte, LocalStoreUtils.imgFacePath);
                visitorInfo.setFaceFilePath(imgPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            visitorInfo = visitorInfoService.saveVisitorInfo(visitorInfo);

            VisitorInfo finalVisitorInfo = visitorInfo;
            equipmentMacs.forEach(equipmentMac -> {
                EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoBySn(equipmentMac);
                if (equipmentInfo != null) {
                    visitorEquipmentService.saveVisitorEquipment(finalVisitorInfo.getId(), equipmentInfo.getId());
                }
            });
        }
    }

    /**
     * 远程开门
     * @param message
     */
    public void remoteOpen(String message){
        log.info("收到云端远程开门请求-"+message);
        EquipmentRemoteOpenRequestInterfaceBean equipmentRemoteOpenRequestInterfaceBean =JSONObject.parseObject(message, EquipmentRemoteOpenRequestInterfaceBean.class);
        Set<String> equipmentSns = equipmentRemoteOpenRequestInterfaceBean.getEquipmentSn();
        if(equipmentSns != null && equipmentSns.size() > 0){
            equipmentSns.forEach(equipmentSn ->{
                boolean isConnect = false;
                IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(equipmentSn);
                if(cameraPoint == null){
                    isConnect = false;
                }else{
                    if (MyFunction.m_FaceRecognition.ZBX_Connected(cameraPoint) == 1) {
                        isConnect = true;
                    }else{
                        isConnect = false;
                    }
                }

                if(isConnect){
                    MyFunction.m_FaceRecognition.ZBX_WhiteListAlarm(cameraPoint,1,1);
                }else{
                    log.error("云端远程开门失败-设备为连接!");
                }
            });
        }
    }

    /**
     * 初始化连接sokect
     * @param
     * @return
     */
    @Override
    public Boolean initWebSocketClient(){
        if(ApplicationUtil.getApplicationProperties().getCloudFlag().equals("1")) {
            CloudInterfaceInfo cloudInterfaceInfo = cloudInterfaceInfoService.findCloudInterfaceInfoById("1");
            if (cloudInterfaceInfo == null) {
                return false;
            } else {
                return connectWebSocketServer(cloudInterfaceInfo);
            }
        }else{
            return false;
        }
    }


    /**
     * 发送同步请求
     * @return
     */
    @Override
    public Boolean sendAsyApply(){
        if(ApplicationUtil.getApplicationProperties().getCloudFlag().equals("1")) {
            AsyApplyInterfaceBean asyApplyInterfaceBean = new AsyApplyInterfaceBean();
            String jsonStr = JSONObject.toJSONString(asyApplyInterfaceBean);
            webSocketClient.send(jsonStr);
        }
        return true;
    }

    /**
     * 关闭连接sokect
     * @param cloudInterfaceInfo
     * @return
     */
    @Override
    @Transactional
    public Boolean closeWebSocketServer(CloudInterfaceInfo cloudInterfaceInfo){
        if(ApplicationUtil.getApplicationProperties().getCloudFlag().equals("1")) {
            //已连接的,无需连接
            if (webSocketClient != null && webSocketClient.isOpen()) {
                webSocketClient.close();
            }
            cloudInterfaceInfo.setConnStatus(CloudInterfaceConnStatus.NO_CONNECT.getCode());
            cloudInterfaceInfoService.saveCloudInterfaceInfo(cloudInterfaceInfo);
            return true;
        }else{
            return true;
        }

    }

    @Override
    @Transactional
    public Boolean connectWebSocketServer( CloudInterfaceInfo cloudInterfaceInfo) {
        if(ApplicationUtil.getApplicationProperties().getCloudFlag().equals("1")) {
            //先保存
            cloudInterfaceInfoService.saveCloudInterfaceInfo(cloudInterfaceInfo);

            //已连接的,无需连接
            if (webSocketClient != null && webSocketClient.isConnecting()) {
                return true;
            }

            SystemUtils.SystemInfo systemInfo = SystemUtils.getSystemInfo();

            try {
                String toUrl = cloudInterfaceInfo.getUrl() + "?" +
                        WebSocketConstant.CONNECT_PARAM_MAC + "=" + URLEncoder.encode(systemInfo.getMacId(), "UTF-8") + "&" + WebSocketConstant.CONNECT_PARAM_LANIP + "=" + URLEncoder.encode(systemInfo.getIp(), "UTF-8") +
                        "&" + WebSocketConstant.CONNECT_PARAM_ACCOUNTCODE + "=" + URLEncoder.encode(cloudInterfaceInfo.getAccountCode(), "UTF-8") +
                        "&" + WebSocketConstant.CONNECT_PARAM_PASSWORD + "=" + URLEncoder.encode(cloudInterfaceInfo.getPassword(), "UTF-8") +
                        "&" + WebSocketConstant.CONNECT_PARAM_OCTOKEN + "=" + URLEncoder.encode(cloudInterfaceInfo.getOcCode(), "UTF-8") +
                        "&" + WebSocketConstant.CONNECT_PARAM_APPVERSION + "=" + URLEncoder.encode(ApplicationUtil.getApplicationProperties().getVersion(), "UTF-8") +
                        "&" + WebSocketConstant.CONNECT_PARAM_SYSTEMNAME + "=" + URLEncoder.encode(systemInfo.getSystemName(), "UTF-8") +
                        "&" + WebSocketConstant.CONNECT_PARAM_SYSTEMVERSION + "=" + URLEncoder.encode(systemInfo.getSystemVersion(), "UTF-8");
                WebSocketClient webSocketClient = new WebSocketClient(new URI(toUrl), new Draft_6455()) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        cloudInterfaceInfo.setConnStatus(CloudInterfaceConnStatus.CONNECTED.getCode());
                        cloudInterfaceInfoService.saveCloudInterfaceInfo(cloudInterfaceInfo);
                        log.info("[websocket] 连接成功");
                    }

                    @Override
                    public void onMessage(String message) {
                        if (message.contains(WebSocketConstant.INTERFACE_TYPE_ASY_APPLY_EQUIPMENT_RETURN)) {   //同步申请设备返回 接收
                            asyApplyEquipmentReturn(message);
                        }

                        if (message.contains(WebSocketConstant.INTERFACE_TYPE_ASY_APPLY_VISITOR_RETURN)) {  //同步申请人员返回
                            asyApplyVisitorReturn(message);
                        }

                        if (message.contains(WebSocketConstant.INTERFACE_TYPE_EQUIPMENT_REMOTEOPEN)) {  //同步申请人员返回
                            remoteOpen(message);
                        }
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        cloudInterfaceInfo.setConnStatus(CloudInterfaceConnStatus.NO_CONNECT.getCode());
                        cloudInterfaceInfoService.saveCloudInterfaceInfo(cloudInterfaceInfo);
                        log.info("[websocket] 退出连接" + reason);
                    }

                    @Override
                    public void onError(Exception ex) {
                        log.info("[websocket] 连接错误={}", ex.getMessage());
                    }
                };
                webSocketClient.connect();
                this.webSocketClient = webSocketClient;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                this.webSocketClient = null;
                return false;
            }
        }else{
            return true;
        }
    }

}
