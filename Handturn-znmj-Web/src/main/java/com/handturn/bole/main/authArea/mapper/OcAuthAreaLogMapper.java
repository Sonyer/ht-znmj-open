package com.handturn.bole.main.authArea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.authArea.entity.OcAuthAreaLog;
import org.springframework.data.repository.query.Param;

/**
 * 网点-访客区域权限日志 Mapper
 *
 * @author Eric
 * @date 2020-12-05 20:04:29
 */
public interface OcAuthAreaLogMapper extends BaseMapper<OcAuthAreaLog> {

    ICustomPage<OcAuthAreaLog> findForPage(CustomPage page, @Param("ocAuthAreaLog") OcAuthAreaLog ocAuthAreaLog);

}
