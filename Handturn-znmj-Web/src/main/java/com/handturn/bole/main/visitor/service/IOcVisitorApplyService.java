package com.handturn.bole.main.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.main.visitor.entity.OcVisitorApply;

/**
 * 网点-会员访客审核 Service接口
 *
 * @author Eric
 * @date 2020-09-22 08:57:57
 */
public interface IOcVisitorApplyService extends IService<OcVisitorApply> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param ocVisitorApply ocVisitorApply
     * @return ICustomPage<OcVisitorApply>
     */
    ICustomPage<OcVisitorApply> findOcVisitorApplys(QueryRequest request, OcVisitorApply ocVisitorApply);

    /**
     * 修改
     *
     * @param ocVisitorApply ocVisitorApply
     */
    OcVisitorApply saveOcVisitorApply(OcVisitorApply ocVisitorApply);

    /**
     * 启用
     *
     * @param ocVisitorApplyIds ocVisitorApplyIds
     */
    void enableOcVisitorApply(String[] ocVisitorApplyIds);

    /**
    * 禁用
    *
    * @param ocVisitorApplyIds ocVisitorApplyIds
    */
    void disableOcVisitorApply(String[] ocVisitorApplyIds);


    /**
    * 通过Id获取信息
    * @param ocVisitorApplyId
    * @return
    */
    OcVisitorApply findOcVisitorApplyById(Long ocVisitorApplyId);

    /**
     * 审核保存
     * @param ocVisitorApply
     * @return
     */
    OcVisitorApply auditSave(OcVisitorApply ocVisitorApply);

    /**
     * 审核保存
     * @param ocVisitorApply
     * @return
     */
    void auditsSave(OcVisitorApply ocVisitorApply);

}
