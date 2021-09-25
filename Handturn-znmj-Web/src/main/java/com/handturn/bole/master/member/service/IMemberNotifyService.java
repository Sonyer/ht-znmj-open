package com.handturn.bole.master.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.member.entity.MemberNotify;
import org.springframework.scheduling.annotation.Async;

/**
 * 智能门禁-会员通知 Service接口
 *
 * @author Eric
 * @date 2020-05-19 09:22:40
 */
public interface IMemberNotifyService extends IService<MemberNotify> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param memberNotify memberNotify
     * @return ICustomPage<MemberNotify>
     */
    ICustomPage<MemberNotify> findMemberNotifys(QueryRequest request, MemberNotify memberNotify);

    /**
     * 修改
     *
     * @param memberNotify memberNotify
     */
    MemberNotify saveMemberNotify(MemberNotify memberNotify);

    /**
     * 启用
     *
     * @param memberNotifyIds memberNotifyIds
     */
    void enableMemberNotify(String[] memberNotifyIds);

    /**
    * 禁用
    *
    * @param memberNotifyIds memberNotifyIds
    */
    void disableMemberNotify(String[] memberNotifyIds);


    /**
    * 通过Id获取信息
    * @param memberNotifyId
    * @return
    */
    MemberNotify findMemberNotifyById(Long memberNotifyId);


    /**
     * 异步 用户通知
     * @param memberId
     * @param notifyType
     */
    @Async(FebsConstant.ASYNC_POOL)
    void notifyMessage(Long memberId, String accountCode, String notifyType, Object messageDataDto);

    /**
     * 异步 用户触发缓存通知
     * @param accountCode
     */
    @Async(FebsConstant.ASYNC_POOL)
    void cacheNotifyMessage(String accountCode);
}
