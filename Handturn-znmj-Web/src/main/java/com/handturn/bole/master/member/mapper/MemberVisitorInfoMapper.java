package com.handturn.bole.master.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.master.member.entity.MemberVisitorInfo;
import org.springframework.data.repository.query.Param;

/**
 * 会员-会员访客信息 Mapper
 *
 * @author Eric
 * @date 2020-09-22 08:57:18
 */
public interface MemberVisitorInfoMapper extends BaseMapper<MemberVisitorInfo> {

    ICustomPage<MemberVisitorInfo> findForPage(CustomPage page, @Param("memberVisitorInfo") MemberVisitorInfo memberVisitorInfo);

}
