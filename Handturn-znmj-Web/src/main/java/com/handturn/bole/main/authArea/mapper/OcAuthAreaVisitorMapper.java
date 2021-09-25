package com.handturn.bole.main.authArea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.authArea.entity.OcAuthAreaVisitor;
import org.springframework.data.repository.query.Param;

/**
 * 网点-访客区域权限 Mapper
 *
 * @author Eric
 * @date 2020-09-22 08:58:06
 */
public interface OcAuthAreaVisitorMapper extends BaseMapper<OcAuthAreaVisitor> {

    ICustomPage<OcAuthAreaVisitor> findForPage(CustomPage page, @Param("ocAuthAreaVisitor") OcAuthAreaVisitor ocAuthAreaVisitor);

}
