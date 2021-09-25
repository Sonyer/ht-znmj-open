package com.handturn.bole.main.visitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.visitor.entity.OcVisitorInfo;
import org.springframework.data.repository.query.Param;

/**
 * 网点-会员访客信息 Mapper
 *
 * @author Eric
 * @date 2020-09-22 08:57:38
 */
public interface OcVisitorInfoMapper extends BaseMapper<OcVisitorInfo> {

    ICustomPage<OcVisitorInfo> findForPage(CustomPage page, @Param("ocVisitorInfo") OcVisitorInfo ocVisitorInfo);

}
