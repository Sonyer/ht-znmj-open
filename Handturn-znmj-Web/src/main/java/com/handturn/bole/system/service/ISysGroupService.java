package com.handturn.bole.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysGroup;

/**
 * 系统-资源组 Service接口
 *
 * @author Eric
 * @date 2020-05-23 14:55:18
 */
public interface ISysGroupService extends IService<SysGroup> {

    static final String BUSSINESS_GROUP = "BUSSINESS_GROUP"; //商户认证组


    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysGroup sysGroup
     * @return ICustomPage<SysGroup>
     */
    ICustomPage<SysGroup> findSysGroups(QueryRequest request, SysGroup sysGroup);

    /**
     * 修改
     *
     * @param sysGroup sysGroup
     */
    SysGroup saveSysGroup(SysGroup sysGroup);

    /**
     * 启用
     *
     * @param sysGroupIds sysGroupIds
     */
    void enableSysGroup(String[] sysGroupIds);

    /**
    * 禁用
    *
    * @param sysGroupIds sysGroupIds
    */
    void disableSysGroup(String[] sysGroupIds);

    /**
     * 一键刷新
     *
     * @param sysGroupIds sysGroupIds
     */
    void oneTouchFlush(String[] sysGroupIds);



    /**
    * 通过Id获取信息
    * @param sysGroupId
    * @return
    */
    SysGroup findSysGroupById(Long sysGroupId);

    /**
     * 通过Id获取信息
     * @param groupCode
     * @return
     */
    SysGroup findSysGroupByCode(String groupCode);
}
