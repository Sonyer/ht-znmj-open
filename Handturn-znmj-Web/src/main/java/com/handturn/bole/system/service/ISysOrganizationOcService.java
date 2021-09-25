package com.handturn.bole.system.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.CommonTree;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysOrganizationOc;
import com.handturn.bole.system.entity.SysUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统-公司操作运营中心 Service接口
 *
 * @author MrBird
 * @date 2019-12-08 19:36:28
 */
public interface ISysOrganizationOcService extends IService<SysOrganizationOc> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysOrganizationOc sysOrganizationOc
     * @return ICustomPage<SysOrganizationOc>
     */
    ICustomPage<SysOrganizationOc> findSysOrganizationOcs(QueryRequest request, SysOrganizationOc sysOrganizationOc);

    /**
     * 修改
     *
     * @param sysOrganizationOc sysOrganizationOc
     */
    SysOrganizationOc saveSysOrganizationOc(SysOrganizationOc sysOrganizationOc,boolean isSystem);

    /**
     * 修改
     *
     * @param sysOrganizationOc sysOrganizationOc
     */
    SysOrganizationOc saveSysOrganizationOcNoneExtend(SysOrganizationOc sysOrganizationOc,boolean isSystem);


    /**
     * 启用
     *
     * @param sysOrganizationOcIds sysOrganizationOcIds
     */
    void enableSysOrganizationOc(String[] sysOrganizationOcIds);

    /**
    * 禁用
    *
    * @param sysOrganizationOcIds sysOrganizationOcIds
    */
    void disableSysOrganizationOc(String[] sysOrganizationOcIds);


    /**
    * 通过Id获取信息
    * @param sysOrganizationOcId
    * @return
    */
    SysOrganizationOc findSysOrganizationOcById(Long sysOrganizationOcId);

    /**
     * 通过Id获取信息
     * @param ocCode
     * @return
     */
    SysOrganizationOc findSysOrganizationOcByCode(String ocCode);

    /**
     * 获取当前用户的组织选项
     * @param user
     * @param currentOrgId
     * @return
     */
    List<SysOrganizationOc> getOrgOcByCurrentUser(SysUser user, Long currentOrgId);



    /**
     * 获取当前用户的组织选项
     * @param user
     * @param currentOrgId
     * @return
     */
    List<OptionVo> getOrgOcOptionVoByCurrentUser(SysUser user, Long currentOrgId);

    /**
     * 获取
     * @return
     */
    List<OptionVo>  getOrgOcKeyOptionVoByOcType(List<String> ocTypes);

    CommonTree<SysOrganizationOc> findSysOcByOrgId(Long orgId, Set<Long> ocIds);

    CommonTree<SysOrganizationOc> findSysOcByOcId(Long ocId, Set<Long> ocIds);

    /**
     * 通过类型查询当前组织的网点
     * @param ocType
     * @param orgId
     * @return
     */
    List<SysOrganizationOc> findClientByOcType(String ocType,Long orgId);

    /**
     * 通过类型查询当前组织的网点
     * @param oc
     * @return
     */
    List<OptionVo> findOptionVoByClient(SysOrganizationOc oc);

    /**
     * 通过类型查询网点
     * @param ocTypes
     * @return
     */
    List<OptionVo> findOptionVoByOcType(Set<String> ocTypes);

    /**
     * 通过类型查询网点
     * @param ocTypes
     * @return
     */
    List<OptionVo> findOptionVoByOcType(Set<String> ocTypes,Long orgId);

    /**
     * 通过类型查询当前组织的网点
     * @param clientCode
     * @return
     */
    List<OptionVo> findWarehouseOptionVoByClient(String clientCode);

    /**
     * 通过类型查询当前组织的网点
     * @param warehouseCode
     * @return
     */
    List<OptionVo> findClientOptionVoByWarehouse(String warehouseCode);

    /**
     * 通过类型查询当前组织的网点
     * @param clientCode
     * @return
     */
    Map<String,SysOrganizationOc> findWarehouseByClient(String clientCode);

    /**
     * 通过类型查询当前组织的网点
     * @param warehouseCode
     * @return
     */
    Map<String,SysOrganizationOc> findClientByWarehouse(String warehouseCode);

}
