package com.handturn.bole.main.authArea.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.main.authArea.entity.OcAuthAreaResponsible;

/**
 * 网点-访客区域责任人 Service接口
 *
 * @author Eric
 * @date 2020-09-30 08:32:35
 */
public interface IOcAuthAreaResponsibleService extends IService<OcAuthAreaResponsible> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param ocAuthAreaResponsible ocAuthAreaResponsible
     * @return ICustomPage<OcAuthAreaResponsible>
     */
    ICustomPage<OcAuthAreaResponsible> findOcAuthAreaResponsibles(QueryRequest request, OcAuthAreaResponsible ocAuthAreaResponsible);

    /**
     * 修改
     *
     * @param ocAuthAreaResponsible ocAuthAreaResponsible
     */
    OcAuthAreaResponsible saveOcAuthAreaResponsible(OcAuthAreaResponsible ocAuthAreaResponsible);

    /**
     * 启用
     *
     * @param ocAuthAreaResponsibleIds ocAuthAreaResponsibleIds
     */
    void enableOcAuthAreaResponsible(String[] ocAuthAreaResponsibleIds);

    /**
    * 禁用
    *
    * @param ocAuthAreaResponsibleIds ocAuthAreaResponsibleIds
    */
    void disableOcAuthAreaResponsible(String[] ocAuthAreaResponsibleIds);


    /**
    * 通过Id获取信息
    * @param ocAuthAreaResponsibleId
    * @return
    */
    OcAuthAreaResponsible findOcAuthAreaResponsibleById(Long ocAuthAreaResponsibleId);

    /**
     * 移除责任人
     */
    void deleteResponsible(String[] responsibleIdsArr,String orgCode,String ocCode);

}
