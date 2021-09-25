package com.handturn.bole.sysconf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.sysconf.entity.SysconfDictCode;
import org.springframework.data.repository.query.Param;

/**
 * 系统-数据字典明细 Mapper
 *
 * @author MrBird
 * @date 2019-12-08 21:05:28
 */
public interface SysconfDictCodeMapper extends BaseMapper<SysconfDictCode> {

    ICustomPage<SysconfDictCode> findForPage(CustomPage page, @Param("sysconfDictCode") SysconfDictCode sysconfDictCode);

}
