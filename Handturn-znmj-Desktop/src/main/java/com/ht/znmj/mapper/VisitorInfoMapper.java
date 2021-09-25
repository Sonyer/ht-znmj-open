package com.ht.znmj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ht.znmj.common.customPage.CustomPage;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.entity.VisitorInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 人员信息 Mapper
 *
 * @author Eric
 * @date 2019-12-17 08:35:14
 */
public interface VisitorInfoMapper extends BaseMapper<VisitorInfo> {

    ICustomPage<VisitorInfo> findForPage(CustomPage page, @Param("visitorInfo") VisitorInfo visitorInfo);


}
