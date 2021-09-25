package com.handturn.bole.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.main.equipment.entity.MiddlewareInfo;
import com.handturn.bole.system.entity.SysOrganizationOc;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.websocket.bean.HeartbeatInterfaceBean;
import com.handturn.bole.websocket.bean.VisitorLogAsyRequestInterfaceBean;
import com.handturn.bole.websocket.bean.entity.WebSocketOpenBean;
import com.handturn.bole.websocket.dto.HeartbeatReturnDto;

/**
 * WEB SOCKET 服务 Service接口
 *
 * @author Eric
 * @date 2020-09-11 08:20:03
 */
public interface IWebSocketServerService {

    /**
     * 通过Id获取网点信息
     * @param ocCode
     * @return
     */
    SysOrganizationOc findSysOrganizationOcByCode(String ocCode);

    /**
     * 通过用户名查找用户
     *
     * @param userCode 用户编号
     * @return 用户
     */
    SysUser findByUserCode(String userCode);

    /**
     * 通过设备SN获取 中间件信息
     *
     * @return 用户
     */
    MiddlewareInfo findMiddlewareInfoByEquipmentSn(String sn,String ocCode);

    /**
     * 心跳
     */
    HeartbeatReturnDto heartbeat(HeartbeatInterfaceBean heartbeatInterfaceBean, WebSocketOpenBean webSocketOpenBean);

    /**
     * 心跳-设备更新
     */
    void equipmentAsyReturn(HeartbeatReturnDto heartbeatReturnDto, WebSocketOpenBean webSocketOpenBean, WebSocketServer webSocketServer);

    /**
     * 心跳-人员授权更新
     */
    void visitorAsyReturn(HeartbeatReturnDto heartbeatReturnDto, WebSocketOpenBean webSocketOpenBean, WebSocketServer webSocketServer);

    /**
     * 正常关闭连接
     */
    void closeConnect(WebSocketOpenBean webSocketOpenBean);

    /**
     * 手工同步
     */
    void asyApply(WebSocketOpenBean webSocketOpenBean,WebSocketServer webSocketServer);

    void visitorLogAsyRequest(VisitorLogAsyRequestInterfaceBean visitorLogAsyRequestInterfaceBean,WebSocketOpenBean webSocketOpenBean);

}
