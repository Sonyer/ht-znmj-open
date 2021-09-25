package com.ht.znmj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ht.znmj.common.customPage.CustomPage;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.entity.VisitorLog;
import org.apache.ibatis.annotations.Param;

/**
 * 人员日志信息 Mapper
 *
 * @author Eric
 * @date 2019-12-17 08:35:14
 */
public interface VisitorLogMapper extends BaseMapper<VisitorLog> {

    ICustomPage<VisitorLog> findForPage(CustomPage page, @Param("visitorLog") VisitorLog visitorLog);


}
