package com.handturn.bole.websocket.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.utils.DateUtil;
import com.handturn.bole.common.utils.ImgHelper;
import com.handturn.bole.common.utils.PicConverUtil;
import com.handturn.bole.main.area.service.IAreaInfoService;
import com.handturn.bole.main.authArea.entity.OcAuthAreaEquipment;
import com.handturn.bole.main.authArea.entity.OcAuthAreaLog;
import com.handturn.bole.main.authArea.entity.OcAuthAreaVisitor;
import com.handturn.bole.main.authArea.service.IOcAuthAreaEquipmentService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaLogService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaVisitorService;
import com.handturn.bole.main.equipment.entity.EquipmentInfo;
import com.handturn.bole.main.equipment.entity.EquipmentInfoOnlineStatus;
import com.handturn.bole.main.equipment.entity.MiddlewareInfo;
import com.handturn.bole.main.equipment.service.IEquipmentInfoService;
import com.handturn.bole.main.equipment.service.IMiddlewareInfoService;
import com.handturn.bole.main.visitor.entity.OcVisiteLog;
import com.handturn.bole.main.visitor.entity.OcVisitorInfo;
import com.handturn.bole.main.visitor.service.IOcVisiteLogService;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import com.handturn.bole.master.set.service.IImgStoreSetService;
import com.handturn.bole.system.entity.SysOrganization;
import com.handturn.bole.system.entity.SysOrganizationOc;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysOrganizationService;
import com.handturn.bole.system.service.ISysUserService;
import com.handturn.bole.websocket.bean.*;
import com.handturn.bole.websocket.bean.entity.EquipmentInfoVo;
import com.handturn.bole.websocket.bean.entity.VisitorInfoAsyApplyReturnVo;
import com.handturn.bole.websocket.bean.entity.VisitorLogVo;
import com.handturn.bole.websocket.bean.entity.WebSocketOpenBean;
import com.handturn.bole.websocket.dto.HeartbeatReturnDto;
import com.handturn.bole.websocket.service.IWebSocketServerService;
import com.handturn.bole.websocket.service.WebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


/**
 * WEB SOCKET 服务 Service接口
 *
 * @author Eric
 * @date 2020-09-11 08:20:03
 */
@Service("WebSocketServerService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WebSocketServerServiceImpl implements IWebSocketServerService {

    @Autowired
    private IImgStoreSetService imgStoreSetService;

    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;
    @Autowired
    private ISysOrganizationService sysOrganizationService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IMiddlewareInfoService middlewareInfoService;

    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    @Autowired
    private IAreaInfoService areaInfoService;

    @Autowired
    private IOcAuthAreaEquipmentService ocAuthAreaEquipmentService;

    @Autowired
    private IOcAuthAreaVisitorService ocAuthAreaVisitorService;

    @Autowired
    private IOcVisitorInfoService ocVisitorInfoService;

    @Autowired
    private IOcAuthAreaLogService OcAuthAreaLogService;

    @Autowired
    private IOcVisiteLogService ocVisiteLogService;


    /**
     * 通过Id获取网点信息
     * @param ocCode
     * @return
     */
    public SysOrganizationOc findSysOrganizationOcByCode(String ocCode){
        return sysOrganizationOcService.findSysOrganizationOcByCode(ocCode);
    }

    /**
     * 通过用户名查找用户
     *
     * @param userCode 用户编号
     * @return 用户
     */
    public SysUser findByUserCode(String userCode){
        return sysUserService.findByUserCode(userCode);
    }

    /**
     * 通过设备SN获取 中间件信息
     *
     * @return 用户
     */
    public MiddlewareInfo findMiddlewareInfoByEquipmentSn(String sn,String ocCode){
        List<MiddlewareInfo> middlewareInfos = middlewareInfoService.findMiddlewareInfoOnLineByEquipmentMac(sn,ocCode);
        return middlewareInfos == null || middlewareInfos.size() <=0 ? null:middlewareInfos.get(0);
    }

    /**
     * 心跳
     */
    @Override
    @Transactional
    public HeartbeatReturnDto heartbeat(HeartbeatInterfaceBean heartbeatInterfaceBean, WebSocketOpenBean webSocketOpenBean){
        Date currentDate = new Date();
        List<EquipmentInfoVo> equipmentVos = heartbeatInterfaceBean.getEquipmentInfoList();

        SysOrganizationOc ocInfo = null;
        SysOrganization orgInfo = null;
        if(!StringUtils.isEmpty(webSocketOpenBean.getOcCode())){
            ocInfo = sysOrganizationOcService.findSysOrganizationOcByCode(webSocketOpenBean.getOcCode());
            orgInfo = sysOrganizationService.findSysOrganizationById(ocInfo.getOrgId());
        }

        SysOrganizationOc finalOcInfo = ocInfo;
        SysOrganization finalOrgInfo = orgInfo;

        Map<String,Date> minOnlineTimeMap = new HashMap<>();
        Set<String> equipmentMacIds = new HashSet<>();

        equipmentVos.forEach(equipmentVo -> {
            MiddlewareInfo middlewareInfo = new MiddlewareInfo();
            middlewareInfo.setMacId(webSocketOpenBean.getMacId());
            middlewareInfo.setAppVersion(webSocketOpenBean.getAppVersion());
            middlewareInfo.setSystemName(webSocketOpenBean.getSystemName());
            middlewareInfo.setSystemVersion(webSocketOpenBean.getSystemVersion());
            middlewareInfo.setLanIp(webSocketOpenBean.getLanIp());
            middlewareInfo.setOrgCode(finalOrgInfo == null?"": finalOrgInfo.getOrgCode());
            middlewareInfo.setOcCode(webSocketOpenBean.getOcCode());
            middlewareInfo.setOrgName(finalOrgInfo == null?"": finalOrgInfo.getOrgName());
            middlewareInfo.setOcName(finalOcInfo == null?"": finalOcInfo.getOcName());
            middlewareInfo.setEquipmentMacId(equipmentVo.getSn());
            middlewareInfo.setSeqNum(equipmentVo.getSeqNum());
            middlewareInfo.setAreaName(equipmentVo.getAreaName());
            middlewareInfo.setInOutFlag(equipmentVo.getInOut());
            middlewareInfo.setOnlineTime(currentDate);
            middlewareInfo.setOnlineStatus(EquipmentInfoOnlineStatus.ONLINE);

            List<MiddlewareInfo> middlewareInfos = middlewareInfoService.findMiddlewareInfoByMac(middlewareInfo.getMacId(),middlewareInfo.getEquipmentMacId());
            //拿到最小日期
            if(middlewareInfos != null && middlewareInfos.size() > 0){
                middlewareInfo.setId(middlewareInfos.get(0).getId());
                if(minOnlineTimeMap.get("minOnlineTime") == null){
                    minOnlineTimeMap.put("minOnlineTime",middlewareInfos.get(0).getOnlineTime());
                }else{
                    if(middlewareInfos.get(0).getOnlineTime().getTime() < minOnlineTimeMap.get("minOnlineTime").getTime()){
                        minOnlineTimeMap.put("minOnlineTime",middlewareInfos.get(0).getOnlineTime());
                    }
                }
            }else{
                try {
                    Date minDate = DateUtil.formatStr2Date("1970-01-01 00:00:00",DateUtil.FULL_TIME_SPLIT_PATTERN);
                    minOnlineTimeMap.put("minOnlineTime", minDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            middlewareInfoService.saveMiddlewareInfo(middlewareInfo);

            EquipmentInfo equipment = equipmentInfoService.findEquipmentInfoByMac(middlewareInfo.getEquipmentMacId(),middlewareInfo.getOcCode());
            if(equipment != null){
                equipment.setOnlineTime(currentDate);
                equipment.setOnlineStatus(middlewareInfo.getOnlineStatus());
                equipmentInfoService.saveEquipmentInfo(equipment);
            }

            equipmentMacIds.add(equipmentVo.getSn());
        });

        //没有的设备的,设置成离线
        if(equipmentMacIds != null && equipmentMacIds.size() > 0){
            List<MiddlewareInfo> middlewareInfoNotIns = middlewareInfoService.findMiddlewareInfoNotInByMac(webSocketOpenBean.getMacId(),equipmentMacIds);
            middlewareInfoNotIns.forEach(middlewareInfoNotIn ->{
                if(!middlewareInfoNotIn.getOnlineStatus().equals(EquipmentInfoOnlineStatus.OFFLINE)){
                    middlewareInfoNotIn.setOnlineStatus(EquipmentInfoOnlineStatus.OFFLINE);
                    middlewareInfoService.saveMiddlewareInfo(middlewareInfoNotIn);

                    EquipmentInfo equipment = equipmentInfoService.findEquipmentInfoByMac(middlewareInfoNotIn.getEquipmentMacId(),middlewareInfoNotIn.getOcCode());
                    if(equipment != null){
                        equipment.setOnlineStatus(middlewareInfoNotIn.getOnlineStatus());
                        equipmentInfoService.saveEquipmentInfo(equipment);
                    }
                }
            });
        }

        //返回数据
        HeartbeatReturnDto heartbeatReturnDto = new HeartbeatReturnDto();
        heartbeatReturnDto.setStartDateTime(minOnlineTimeMap.get("minOnlineTime"));
        heartbeatReturnDto.setEndDaateTime(currentDate);
        heartbeatReturnDto.setEquipmentMacIds(equipmentMacIds);
        heartbeatReturnDto.setOcCode(webSocketOpenBean.getOcCode());
        return heartbeatReturnDto;
    }

    /**
     * 心跳-设备更新
     */
    @Override
    public void equipmentAsyReturn(HeartbeatReturnDto heartbeatReturnDto, WebSocketOpenBean webSocketOpenBean, WebSocketServer webSocketServer){
        if(heartbeatReturnDto.getEquipmentMacIds() != null && heartbeatReturnDto.getEquipmentMacIds().size() > 0){
            QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EquipmentInfo::getEquipmentMdCode,heartbeatReturnDto.getEquipmentMacIds());
            queryWrapper.lambda().le(EquipmentInfo::getLastUpdDate,heartbeatReturnDto.getEndDaateTime());
            queryWrapper.lambda().ge(EquipmentInfo::getLastUpdDate,heartbeatReturnDto.getStartDateTime());
            List<EquipmentInfo> equipmentInfos = equipmentInfoService.getBaseMapper().selectList(queryWrapper);

            Map<String,String> equipmentMacMap = new HashMap<String,String>();
            List<EquipmentInfoVo> returnEquipmentInfos = new ArrayList<>();
            Map<String,String> areaInfoMap = areaInfoService.findMap4CodeKeyByOcCode(webSocketOpenBean.getOcCode());
            equipmentInfos.forEach(equipmentInfo ->{
                EquipmentInfoVo infoVo = new EquipmentInfoVo();
                infoVo.setSn(equipmentInfo.getEquipmentMdCode());
                infoVo.setAreaName(areaInfoMap.get(equipmentInfo.getAreaCode()));
                infoVo.setSeqNum(equipmentInfo.getSeqNum());
                infoVo.setInOut(equipmentInfo.getInOutFlag());
                infoVo.setCloudId(equipmentInfo.getId().toString());

                returnEquipmentInfos.add(infoVo);
                equipmentMacMap.put(equipmentInfo.getId().toString(),equipmentInfo.getEquipmentMdCode());
            });
            /**
             * 设备信息返回
             */
            if(returnEquipmentInfos.size() > 0){
                EquipmentAsyApplyReturnInterfaceBean asyApplyReturnInterfaceBean = new EquipmentAsyApplyReturnInterfaceBean();
                asyApplyReturnInterfaceBean.setEquipmentInfoList(returnEquipmentInfos);
                String jsonStr = JSONObject.toJSONString(asyApplyReturnInterfaceBean);
                webSocketServer.AppointSending(webSocketOpenBean.getMacId(),jsonStr); //定点发送
            }
        }
    }

    /**
     * 心跳-人员授权更新
     */
    @Override
    public void visitorAsyReturn(HeartbeatReturnDto heartbeatReturnDto, WebSocketOpenBean webSocketOpenBean, WebSocketServer webSocketServer){
        if(heartbeatReturnDto.getEquipmentMacIds() != null && heartbeatReturnDto.getEquipmentMacIds().size() > 0){
            QueryWrapper<OcAuthAreaLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(OcAuthAreaLog::getEquipmentMac,heartbeatReturnDto.getEquipmentMacIds());
            queryWrapper.lambda().le(OcAuthAreaLog::getLastUpdDate,heartbeatReturnDto.getEndDaateTime());
            queryWrapper.lambda().ge(OcAuthAreaLog::getLastUpdDate,heartbeatReturnDto.getStartDateTime());
            List<OcAuthAreaLog> ocAuthAreaLogs = OcAuthAreaLogService.getBaseMapper().selectList(queryWrapper);

            List<EquipmentInfo> equipmentInfos = equipmentInfoService.findEquipmentInfoByMacs(heartbeatReturnDto.getEquipmentMacIds(),webSocketOpenBean.getOcCode());

            Map<String,String> equipmentMacMap = new HashMap<String,String>();
            equipmentInfos.forEach(equipmentInfo ->{
                equipmentMacMap.put(equipmentInfo.getId().toString(),equipmentInfo.getEquipmentMdCode());
            });

            ocAuthAreaLogs.forEach(ocAuthAreaLog ->{
                OcVisitorInfo ocVisitorInfo = ocVisitorInfoService.findOcVisitorInfoById(ocAuthAreaLog.getOcVisitorId());

                VisitorAsyApplyReturnInterfaceBean visitorAsyApplyReturnInterfaceBean = new VisitorAsyApplyReturnInterfaceBean();
                visitorAsyApplyReturnInterfaceBean.setAuthType(ocAuthAreaLog.getAuthType());
                VisitorInfoAsyApplyReturnVo asyApplyReturnVo = new VisitorInfoAsyApplyReturnVo();
                asyApplyReturnVo.setName(ocVisitorInfo.getIdCardName());
                asyApplyReturnVo.setPhoneNumber(ocVisitorInfo.getPhoneNumber());
                asyApplyReturnVo.setIdCard(ocVisitorInfo.getIdCard());
                asyApplyReturnVo.setWigan(ocVisitorInfo.getWegan());
                asyApplyReturnVo.setDepartment(ocVisitorInfo.getDepName());
                asyApplyReturnVo.setPositor(ocVisitorInfo.getPositionName());
                asyApplyReturnVo.setCloudId(ocVisitorInfo.getId().toString());

                visitorAsyApplyReturnInterfaceBean.setVisitorInfoAsyApplyReturnVo(asyApplyReturnVo);
                String imgStream = null;
                try {
                    imgStream = ImgHelper.encodeImage(ocVisitorInfo.getFaceImgAttchment());
                    visitorAsyApplyReturnInterfaceBean.setFaceFileStream(imgStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Set<String> equipmentMacs = new HashSet<>();
                equipmentMacs.add(ocAuthAreaLog.getEquipmentMac());
                visitorAsyApplyReturnInterfaceBean.setEquipmentMacs(equipmentMacs);
                String jsonStr = JSONObject.toJSONString(visitorAsyApplyReturnInterfaceBean);
                webSocketServer.AppointSending(webSocketOpenBean.getMacId(),jsonStr); //定点发送
            });
        }
    }

    /**
     * 正常关闭连接
     */
    @Override
    @Transactional
    public void closeConnect(WebSocketOpenBean webSocketOpenBean){
        QueryWrapper<MiddlewareInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MiddlewareInfo::getMacId,webSocketOpenBean.getMacId());
        queryWrapper.lambda().eq(MiddlewareInfo::getOnlineStatus,EquipmentInfoOnlineStatus.ONLINE);

        List<MiddlewareInfo> middlewareInfos = middlewareInfoService.getBaseMapper().selectList(queryWrapper);
        middlewareInfos.forEach( middlewareInfo ->{
            middlewareInfo.setOnlineStatus(EquipmentInfoOnlineStatus.OFFLINE);
            middlewareInfoService.saveMiddlewareInfo(middlewareInfo);

            EquipmentInfo equipment = equipmentInfoService.findEquipmentInfoByMac(middlewareInfo.getEquipmentMacId(),middlewareInfo.getOcCode());
            if(equipment != null){
                equipment.setOnlineTime(middlewareInfo.getOnlineTime());
                equipment.setOnlineStatus(middlewareInfo.getOnlineStatus());
                equipmentInfoService.saveEquipmentInfo(equipment);
            }
        });
    }

    /**
     * 手工同步
     */
    @Override
    @Transactional
    public void asyApply(WebSocketOpenBean webSocketOpenBean, WebSocketServer webSocketServer){
        //获取设备信息
        List<MiddlewareInfo> middlewareInfos = middlewareInfoService.findMiddlewareInfoByMacOc(webSocketOpenBean.getMacId(),webSocketOpenBean.getOcCode());
        Set<String> equipmentMacs = new HashSet<>();
        middlewareInfos.forEach(middlewareInfo ->{
            equipmentMacs.add(middlewareInfo.getEquipmentMacId());
        });

        if(equipmentMacs != null && equipmentMacs.size() > 0){
            List<EquipmentInfoVo> returnEquipmentInfos = new ArrayList<>();
            List<EquipmentInfo> equipmentInfos = equipmentInfoService.findEquipmentInfoByMacs(equipmentMacs,webSocketOpenBean.getOcCode());
            Map<String,String> areaInfoMap = areaInfoService.findMap4CodeKeyByOcCode(webSocketOpenBean.getOcCode());

            Map<String,String> equipmentMacMap = new HashMap<String,String>();
            equipmentInfos.forEach(equipmentInfo ->{
                EquipmentInfoVo infoVo = new EquipmentInfoVo();
                infoVo.setSn(equipmentInfo.getEquipmentMdCode());
                infoVo.setAreaName(areaInfoMap.get(equipmentInfo.getAreaCode()));
                infoVo.setSeqNum(equipmentInfo.getSeqNum());
                infoVo.setInOut(equipmentInfo.getInOutFlag());
                infoVo.setCloudId(equipmentInfo.getId().toString());

                returnEquipmentInfos.add(infoVo);
                equipmentMacMap.put(equipmentInfo.getId().toString(),equipmentInfo.getEquipmentMdCode());
            });

            /**
             * 设备信息返回
             */
            if(returnEquipmentInfos.size() > 0){
                EquipmentAsyApplyReturnInterfaceBean asyApplyReturnInterfaceBean = new EquipmentAsyApplyReturnInterfaceBean();
                asyApplyReturnInterfaceBean.setEquipmentInfoList(returnEquipmentInfos);
                String jsonStr = JSONObject.toJSONString(asyApplyReturnInterfaceBean);
                webSocketServer.AppointSending(webSocketOpenBean.getMacId(),jsonStr); //定点发送
            }

            /**
             * 人员信息返回
             */
            Map<String,Set<String>> equipmentAuthEquipmentMap = new HashMap<>();
            List<OcAuthAreaEquipment> ocAuthAreaEquipments = ocAuthAreaEquipmentService.findOcAuthAreaEquipmentByEquipmentIds(equipmentMacMap.keySet());
            ocAuthAreaEquipments.forEach(ocAuthAreaEquipment ->{
                Set<String> macSet = null;
                if(equipmentAuthEquipmentMap.get(ocAuthAreaEquipment.getAuthAreaId()) == null){
                    macSet = new HashSet<>();
                }else{
                    macSet = equipmentAuthEquipmentMap.get(ocAuthAreaEquipment.getAuthAreaId());
                }
                macSet.add(equipmentMacMap.get(ocAuthAreaEquipment.getEquipmentId()));
                equipmentAuthEquipmentMap.put(ocAuthAreaEquipment.getAuthAreaId(),macSet);
            });

            Map<String,Set<String>> visitorEquipmentMap = new HashMap<>();
            List<OcAuthAreaVisitor> ocAuthAreaVisitors =ocAuthAreaVisitorService.findAuthAreaIdByAuthAreaIds(equipmentAuthEquipmentMap.keySet());
            ocAuthAreaVisitors.forEach(ocAuthAreaVisitor ->{
                Set<String> equipmentMacSet = null;
                if(visitorEquipmentMap.get(ocAuthAreaVisitor.getOcVisitorId()) == null){
                    equipmentMacSet = new HashSet<>();
                }else{
                    equipmentMacSet = visitorEquipmentMap.get(ocAuthAreaVisitor.getOcVisitorId());
                }

                if(equipmentAuthEquipmentMap.get(ocAuthAreaVisitor.getAuthAreaId().toString()) != null){
                    equipmentMacSet.addAll(equipmentAuthEquipmentMap.get(ocAuthAreaVisitor.getAuthAreaId().toString()));
                    visitorEquipmentMap.put(ocAuthAreaVisitor.getOcVisitorId().toString(),equipmentMacSet);
                }
            });

            List<OcVisitorInfo> ocVisitorInfos = ocVisitorInfoService.findOcVisitorInfoByIds(visitorEquipmentMap.keySet());
            ocVisitorInfos.forEach(ocVisitorInfo ->{
                VisitorAsyApplyReturnInterfaceBean visitorAsyApplyReturnInterfaceBean = new VisitorAsyApplyReturnInterfaceBean();
                visitorAsyApplyReturnInterfaceBean.setAuthType(InterfaceAreaAuthType.AUTHTYPE_UPDATE);
                VisitorInfoAsyApplyReturnVo asyApplyReturnVo = new VisitorInfoAsyApplyReturnVo();
                asyApplyReturnVo.setName(ocVisitorInfo.getIdCardName());
                asyApplyReturnVo.setPhoneNumber(ocVisitorInfo.getPhoneNumber());
                asyApplyReturnVo.setIdCard(ocVisitorInfo.getIdCard());
                asyApplyReturnVo.setWigan(ocVisitorInfo.getWegan());
                asyApplyReturnVo.setDepartment(ocVisitorInfo.getDepName());
                asyApplyReturnVo.setPositor(ocVisitorInfo.getPositionName());
                asyApplyReturnVo.setCloudId(ocVisitorInfo.getId().toString());

                visitorAsyApplyReturnInterfaceBean.setVisitorInfoAsyApplyReturnVo(asyApplyReturnVo);
                String imgStream = null;
                try {
                    imgStream = ImgHelper.encodeImage(ocVisitorInfo.getFaceImgAttchment());
                    visitorAsyApplyReturnInterfaceBean.setFaceFileStream(imgStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                visitorAsyApplyReturnInterfaceBean.setEquipmentMacs(visitorEquipmentMap.get(ocVisitorInfo.getId().toString()));
                String jsonStr = JSONObject.toJSONString(visitorAsyApplyReturnInterfaceBean);
                webSocketServer.AppointSending(webSocketOpenBean.getMacId(),jsonStr); //定点发送
            });
        }

    }

    @Override
    @Transactional
    public void visitorLogAsyRequest(VisitorLogAsyRequestInterfaceBean visitorLogAsyRequestInterfaceBean,WebSocketOpenBean webSocketOpenBean){
        SysOrganizationOc sysOrganizationOc = sysOrganizationOcService.findSysOrganizationOcByCode(webSocketOpenBean.getOcCode());
        SysOrganization sysOrganization = null;

        if(sysOrganizationOc != null){
            sysOrganization = sysOrganizationService.findSysOrganizationById(sysOrganizationOc.getOrgId());
        }

        VisitorLogVo visitorLogVo = visitorLogAsyRequestInterfaceBean.getVisitorLog();

        EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoByMac(visitorLogVo.getEquipmentSn(),webSocketOpenBean.getOcCode());

        OcVisiteLog ocVisiteLog = new OcVisiteLog();
        try {
            if(!StringUtils.isEmpty(visitorLogAsyRequestInterfaceBean.getFaceFileStream())){
                byte[] imgByte = ImgHelper.decode(visitorLogAsyRequestInterfaceBean.getFaceFileStream());
                ImportResultBean importResultBean = imgStoreSetService.importUpload(imgByte,imgStoreSetService.IMGS_VISITOR_LOG_FACE);
                ocVisiteLog.setFaceImgAttchment(importResultBean.getFilePath());
                ocVisiteLog.setFaceImgRequest(importResultBean.getFileRequestUrl());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ocVisiteLog.setAreaName(visitorLogVo.getAreaName());
        ocVisiteLog.setDepName(visitorLogVo.getDepartment());
        ocVisiteLog.setEquipmentCode(equipmentInfo == null?"":equipmentInfo.getEquipmentCode());
        ocVisiteLog.setEquipmentId(Long.valueOf(visitorLogVo.getEquipmentCloudId()==null?"0":visitorLogVo.getEquipmentCloudId()));
        ocVisiteLog.setEquipmentMdCode(visitorLogVo.getEquipmentSn());
        ocVisiteLog.setIdCard(visitorLogVo.getIdCard());
        ocVisiteLog.setIdCardName(visitorLogVo.getVisitorName());
        ocVisiteLog.setInOutFlag(visitorLogVo.getInOut());
        ocVisiteLog.setOcCode(webSocketOpenBean.getOcCode());
        ocVisiteLog.setOcName(sysOrganizationOc== null?"":sysOrganizationOc.getOcName());
        ocVisiteLog.setOcVisitorId(Long.valueOf(visitorLogVo.getVisitorCloudId()==null?"0":visitorLogVo.getVisitorCloudId()));
        ocVisiteLog.setOrgCode(sysOrganization == null?"":sysOrganization.getOrgCode());
        ocVisiteLog.setOrgName(sysOrganization == null?"":sysOrganization.getOrgName());
        ocVisiteLog.setPhoneNumber(visitorLogVo.getPhoneNumber());
        ocVisiteLog.setPositionName(visitorLogVo.getPositor());
        ocVisiteLog.setSeqNum(visitorLogVo.getSeqNum());
        ocVisiteLog.setWegin(visitorLogVo.getWigan());
        ocVisiteLog.setLogFlag(visitorLogVo.getLogFlag());
        ocVisiteLog.setOpenTime(visitorLogVo.getOpenTime());
        ocVisiteLog.setOpenStatus(visitorLogVo.getOpenStatus());
        ocVisiteLogService.getBaseMapper().insert(ocVisiteLog);
    }
}
