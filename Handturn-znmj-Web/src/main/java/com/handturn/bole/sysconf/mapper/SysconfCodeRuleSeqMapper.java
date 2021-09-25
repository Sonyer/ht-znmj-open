package com.handturn.bole.sysconf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.sysconf.entity.SysconfCodeRuleSeq;
import org.springframework.data.repository.query.Param;

/**
 * 系统配置-编码流水 Mapper
 *
 * @author MrBird
 * @date 2019-12-08 21:44:27
 */
public interface SysconfCodeRuleSeqMapper extends BaseMapper<SysconfCodeRuleSeq> {

    ICustomPage<SysconfCodeRuleSeq> findForPage(CustomPage page, @Param("sysconfCodeRuleSeq") SysconfCodeRuleSeq sysconfCodeRuleSeq);

}
