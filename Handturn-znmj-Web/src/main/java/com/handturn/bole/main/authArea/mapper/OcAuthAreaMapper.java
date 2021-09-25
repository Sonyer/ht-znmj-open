package com.handturn.bole.main.authArea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.authArea.entity.OcAuthArea;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * 网点-权限区域 Mapper
 *
 * @author Eric
 * @date 2020-09-22 08:57:47
 */
public interface OcAuthAreaMapper extends BaseMapper<OcAuthArea> {

    ICustomPage<OcAuthArea> findForPage(CustomPage page, @Param("ocAuthArea") OcAuthArea ocAuthArea);

    List<OcAuthArea> authAreaPageQuery(CustomPage page,@Param("searchValue") String searchValue,
                                               @Param("authAreaIds") Set<Long> authAreaIds);

}
