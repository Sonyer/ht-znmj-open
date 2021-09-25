package com.handturn.bole.master.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.member.entity.Member;

import java.util.Set;

/**
 * 智能门禁-平台会员 Service接口
 *
 * @author Eric
 * @date 2020-03-13 20:42:35
 */
public interface IMemberService extends IService<Member> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param member member
     * @return ICustomPage<Member>
     */
    ICustomPage<Member> findMembers(QueryRequest request, Member member);

    /**
     * 修改
     *
     * @param member member
     */
    Member saveMember(Member member);

    /**
     * 特约会员
     *
     * @param memberIds memberIds
     */
    void specialUserMember(String[] memberIds);

    /**
     * 取消特约会员
     *
     * @param memberIds memberIds
     */
    void cancelSpecialUserMember(String[] memberIds);


    /**
     * 启用
     *
     * @param memberIds memberIds
     */
    void enableMember(String[] memberIds);

    /**
    * 禁用
    *
    * @param memberIds memberIds
    */
    void disableMember(String[] memberIds);


    /**
    * 通过Id获取信息
    * @param memberId
    * @return
    */
    Member findMemberById(Long memberId);

    /**
     * 账号编码获取
     * @param accountCode
     * @return
     */
    Member findMemberByAccountCode(String accountCode);

}
