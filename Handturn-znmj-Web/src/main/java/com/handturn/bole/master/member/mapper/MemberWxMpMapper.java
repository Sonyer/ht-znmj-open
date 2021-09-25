package com.handturn.bole.master.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.master.member.entity.MemberWxMp;
import org.springframework.data.repository.query.Param;

/**
 * 智能门禁-微信小程序会员 Mapper
 *
 * @author Eric
 * @date 2020-03-16 13:19:19
 */
public interface MemberWxMpMapper extends BaseMapper<MemberWxMp> {

    ICustomPage<MemberWxMp> findForPage(CustomPage page, @Param("memberWxMp") MemberWxMp memberWxMp);

}
