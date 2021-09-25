package com.handturn.bole.master.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.member.entity.MemberWxMp;

/**
 * 智能门禁-微信小程序会员 Service接口
 *
 * @author Eric
 * @date 2020-03-16 13:19:19
 */
public interface IMemberWxMpService extends IService<MemberWxMp> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param memberWxMp memberWxMp
     * @return ICustomPage<MemberWxMp>
     */
    ICustomPage<MemberWxMp> findMemberWxMps(QueryRequest request, MemberWxMp memberWxMp);

    /**
     * 修改
     *
     * @param memberWxMp memberWxMp
     */
    MemberWxMp saveMemberWxMp(MemberWxMp memberWxMp);

    /**
     * 启用
     *
     * @param memberWxMpIds memberWxMpIds
     */
    void enableMemberWxMp(String[] memberWxMpIds);

    /**
    * 禁用
    *
    * @param memberWxMpIds memberWxMpIds
    */
    void disableMemberWxMp(String[] memberWxMpIds);


    /**
    * 通过Id获取信息
    * @param memberWxMpId
    * @return
    */
    MemberWxMp findMemberWxMpById(Long memberWxMpId);

    /**
     * 通过Id获取信息
     * @param memberId
     * @return
     */
    MemberWxMp findMemberWxMpByMemberId(Long memberId);

    /**
     * 通过微信OPENID获取微信用户信息
     * @param openId
     * @return
     */
    MemberWxMp findMemberWxMpById(String openId);
}
