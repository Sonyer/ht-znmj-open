package com.handturn.bole.master.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.member.entity.MemberVisitorInfo;

import java.util.List;
import java.util.Set;

/**
 * 会员-会员访客信息 Service接口
 *
 * @author Eric
 * @date 2020-09-22 08:57:18
 */
public interface IMemberVisitorInfoService extends IService<MemberVisitorInfo> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param memberVisitorInfo memberVisitorInfo
     * @return ICustomPage<MemberVisitorInfo>
     */
    ICustomPage<MemberVisitorInfo> findMemberVisitorInfos(QueryRequest request, MemberVisitorInfo memberVisitorInfo);

    /**
     * 修改
     *
     * @param memberVisitorInfo memberVisitorInfo
     */
    MemberVisitorInfo saveMemberVisitorInfo(MemberVisitorInfo memberVisitorInfo);

    /**
     * 启用
     *
     * @param memberVisitorInfoIds memberVisitorInfoIds
     */
    void enableMemberVisitorInfo(String[] memberVisitorInfoIds);

    /**
    * 禁用
    *
    * @param memberVisitorInfoIds memberVisitorInfoIds
    */
    void disableMemberVisitorInfo(String[] memberVisitorInfoIds);


    /**
    * 通过Id获取信息
    * @param memberVisitorInfoId
    * @return
    */
    MemberVisitorInfo findMemberVisitorInfoById(Long memberVisitorInfoId);

    /**
     * 通过Id获取信息
     * @param memeberAccountCode
     * @return
     */
    Set<Long> findByAccountCode(String memeberAccountCode);
}
