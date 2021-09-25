package com.handturn.bole.sysconf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.sysconf.entity.SysconfCodeRule;
import org.springframework.data.repository.query.Param;

/**
 * 系统配置-编码规则 Mapper
 *
 * @author MrBird
 * @date 2019-12-08 21:25:21
 */
public interface SysconfCodeRuleMapper extends BaseMapper<SysconfCodeRule> {

    ICustomPage<SysconfCodeRule> findForPage(CustomPage page, @Param("sysconfCodeRule") SysconfCodeRule sysconfCodeRule);

}
