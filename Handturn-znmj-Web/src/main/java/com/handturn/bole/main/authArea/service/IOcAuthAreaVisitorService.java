package com.handturn.bole.main.authArea.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.main.authArea.entity.OcAuthAreaVisitor;

import java.util.List;
import java.util.Set;

/**
 * 网点-访客区域权限 Service接口
 *
 * @author Eric
 * @date 2020-09-22 08:58:06
 */
public interface IOcAuthAreaVisitorService extends IService<OcAuthAreaVisitor> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param ocAuthAreaVisitor ocAuthAreaVisitor
     * @return ICustomPage<OcAuthAreaVisitor>
     */
    ICustomPage<OcAuthAreaVisitor> findOcAuthAreaVisitors(QueryRequest request, OcAuthAreaVisitor ocAuthAreaVisitor);

    /**
     * 修改
     *
     * @param ocAuthAreaVisitor ocAuthAreaVisitor
     */
    OcAuthAreaVisitor saveOcAuthAreaVisitor(OcAuthAreaVisitor ocAuthAreaVisitor);

    /**
     * 启用
     *
     * @param ocAuthAreaVisitorIds ocAuthAreaVisitorIds
     */
    void enableOcAuthAreaVisitor(String[] ocAuthAreaVisitorIds);

    /**
    * 禁用
    *
    * @param ocAuthAreaVisitorIds ocAuthAreaVisitorIds
    */
    void disableOcAuthAreaVisitor(String[] ocAuthAreaVisitorIds);


    /**
    * 通过Id获取信息
    * @param ocAuthAreaVisitorId
    * @return
    */
    OcAuthAreaVisitor findOcAuthAreaVisitorById(Long ocAuthAreaVisitorId);
    /**
     * 授权日志
     * @param ocVisitorId
     */
    void updateAuthOcVisitors(String ocVisitorId,String orgCode,String ocCode,String orgName,String ocName);

    /**
     * 授权
     * @param oauthAreaId
     * @param ocVisitorIdsArr
     */
    void authOcVisitors(String oauthAreaId,String[] ocVisitorIdsArr,String orgCode,String ocCode,String orgName,String ocName);

    /**
     * 授权
     * @param ocAuthAreaIdsArr
     * @param visitorId
     */
    void authOcVisitors(String[] ocAuthAreaIdsArr,String visitorId,String orgCode,String ocCode,String orgName,String ocName);

    /**
     * 取消授权
     * @param oauthAreaId
     * @param ocVisitorIdsArr
     */
    void cancelAuthOcVisitors(String oauthAreaId,String[] ocVisitorIdsArr,String orgCode,String ocCode,String orgName,String ocName);

    /**
     * 取消授权
     * @param ocAuthAreaIdsArr
     * @param visitorId
     */
    void cancelAuthOcVisitors(String[] ocAuthAreaIdsArr,String visitorId,String orgCode,String ocCode,String orgName,String ocName);

    /**
     * 通过VisitorId获取所有访问授权
     * @param visitorIds
     */
    Set<Long> findAuthAreaIdByVisitorIds(List<Long> visitorIds);

    /**
     * 通过VisitorId获取所有访问授权
     * @param authAreaIds
     */
    List<OcAuthAreaVisitor> findAuthAreaIdByAuthAreaIds(Set<String> authAreaIds);
}
