package com.handturn.bole.sysconf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.sysconf.entity.SysconfDictType;
import org.springframework.data.repository.query.Param;

/**
 * 系统-数据字典类型表 Mapper
 *
 * @author MrBird
 * @date 2019-12-08 20:56:08
 */
public interface SysconfDictTypeMapper extends BaseMapper<SysconfDictType> {

    ICustomPage<SysconfDictType> findForPage(CustomPage page, @Param("sysconfDictType") SysconfDictType sysconfDictType);

}
