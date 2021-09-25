package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * 系统-用户 Mapper
 *
 * @author Eric
 * @date 2019-12-01 10:46:37
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 通过用户编号查找用户
     *
     * @param userCode 用户编号
     * @return 用户
     */
    SysUser findByUserCode(String userCode);


    ICustomPage<SysUser> findForPage(CustomPage page, @Param("sysUser") SysUser sysUser);

}
