package com.handturn.bole.main.equipment.mapper;

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.equipment.entity.MiddlewareInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

/**
 * 基础资料-中间件管理 Mapper
 *
 * @author Eric
 * @date 2020-12-02 11:13:52
 */
public interface MiddlewareInfoMapper extends BaseMapper<MiddlewareInfo> {

    ICustomPage<MiddlewareInfo> findForPage(CustomPage page, @Param("middlewareInfo") MiddlewareInfo middlewareInfo);

}
