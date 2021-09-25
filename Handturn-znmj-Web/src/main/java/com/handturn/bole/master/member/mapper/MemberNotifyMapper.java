package com.handturn.bole.master.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.master.member.entity.MemberNotify;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * 智能门禁-会员通知 Mapper
 *
 * @author Eric
 * @date 2020-05-19 09:22:40
 */
public interface MemberNotifyMapper extends BaseMapper<MemberNotify> {

    ICustomPage<MemberNotify> findForPage(CustomPage page, @Param("memberNotify") MemberNotify memberNotify);

    List<MemberNotify> findForPageByMember(CustomPage page, @Param("accountCode") String accountCode, @Param("readStatuses") Set<String> readStatuses);

}
