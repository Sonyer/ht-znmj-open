package com.handturn.bole.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.system.entity.SysOrganizationOc;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 系统-公司操作运营中心 Mapper
 *
 * @author MrBird
 * @date 2019-12-08 19:36:28
 */
public interface SysOrganizationOcMapper extends BaseMapper<SysOrganizationOc> {

    ICustomPage<SysOrganizationOc> findForPage(CustomPage page, @Param("sysOrganizationOc") SysOrganizationOc sysOrganizationOc);

    List<SysOrganizationOc> getOrgOcOptionVoByCurrentUser(@Param("userId") String userId, @Param("currentOrgId") String currentOrgId);

    List<SysOrganizationOc> getWarehouseOptionVoByClient(@Param("clientCode") String clientCode);

    List<SysOrganizationOc> getClientOptionVoByWarehouse(@Param("warehouseCode") String warehouseCode);

}
