package com.handturn.bole.sysconf.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfGlobalGroup;

/**
 * 系统-系统配置 Service接口
 *
 * @author Eric
 * @date 2019-12-18 20:25:27
 */
public interface ISysconfGlobalGroupService extends IService<SysconfGlobalGroup> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysconfGlobalGroup sysconfGlobalGroup
     * @return ICustomPage<SysconfGlobalGroup>
     */
    ICustomPage<SysconfGlobalGroup> findSysconfGlobalGroups(QueryRequest request, SysconfGlobalGroup sysconfGlobalGroup);

    /**
     * 修改
     *
     * @param sysconfGlobalGroup sysconfGlobalGroup
     */
    SysconfGlobalGroup saveSysconfGlobalGroup(SysconfGlobalGroup sysconfGlobalGroup);

    /**
     * 同步
     *
     * @param sysconfGlobalGroupIds sysconfGlobalGroupIds
     */
    void synchroSysconfGlobalGroup(String[] sysconfGlobalGroupIds);

    /**
     * 启用
     *
     * @param sysconfGlobalGroupIds sysconfGlobalGroupIds
     */
    void enableSysconfGlobalGroup(String[] sysconfGlobalGroupIds);

    /**
    * 禁用
    *
    * @param sysconfGlobalGroupIds sysconfGlobalGroupIds
    */
    void disableSysconfGlobalGroup(String[] sysconfGlobalGroupIds);


    /**
    * 通过Id获取信息
    * @param sysconfGlobalGroupId
    * @return
    */
    SysconfGlobalGroup findSysconfGlobalGroupById(Long sysconfGlobalGroupId);

    SysconfGlobalGroup findSysconfGlobalGroupByGroupCode(String groupCode);
}
