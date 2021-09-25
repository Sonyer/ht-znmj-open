package com.handturn.bole.master.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.master.member.entity.Member;
import org.springframework.data.repository.query.Param;

/**
 * 智能门禁-平台会员 Mapper
 *
 * @author Eric
 * @date 2020-03-13 20:42:35
 */
public interface MemberMapper extends BaseMapper<Member> {

    ICustomPage<Member> findForPage(CustomPage page, @Param("member") Member member);

}
