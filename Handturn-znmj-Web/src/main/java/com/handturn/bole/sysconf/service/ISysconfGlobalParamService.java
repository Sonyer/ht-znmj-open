package com.handturn.bole.sysconf.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfGlobalParam;

/**
 * 系统-系统配置参数 Service接口
 *
 * @author Eric
 * @date 2019-12-18 20:31:06
 */
public interface ISysconfGlobalParamService extends IService<SysconfGlobalParam> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysconfGlobalParam sysconfGlobalParam
     * @return ICustomPage<SysconfGlobalParam>
     */
    ICustomPage<SysconfGlobalParam> findSysconfGlobalParams(QueryRequest request, SysconfGlobalParam sysconfGlobalParam);

    /**
     * 修改
     *
     * @param sysconfGlobalParam sysconfGlobalParam
     */
    SysconfGlobalParam saveSysconfGlobalParam(SysconfGlobalParam sysconfGlobalParam);

    /**
     * 同步
     *
     * @param sysconfGlobalParamIds sysconfGlobalParamIds
     */
    void synchroSysconfGlobalParam(String[] sysconfGlobalParamIds);

    /**
     * 启用
     *
     * @param sysconfGlobalParamIds sysconfGlobalParamIds
     */
    void enableSysconfGlobalParam(String[] sysconfGlobalParamIds);

    /**
    * 禁用
    *
    * @param sysconfGlobalParamIds sysconfGlobalParamIds
    */
    void disableSysconfGlobalParam(String[] sysconfGlobalParamIds);


    /**
    * 通过Id获取信息
    * @param sysconfGlobalParamId
    * @return
    */
    SysconfGlobalParam findSysconfGlobalParamById(Long sysconfGlobalParamId);


    /**
     * 根据不同系统获取配置值
     * @param groupCode
     * @return
     */
    String getParamValueWithDiffSystem(String groupCode,String ocCode,String orgCode);

    /**
     * 获取配置文件
     * @param groupCode
     * @param paramKey
     * @return
     */
    String getParamValueByGroupCode(String groupCode,String paramKey,String ocCode,String orgCode);

    /**
     * 获取配置文件
     * @param groupCode
     * @param paramKey
     * @return
     */
    String getParamValueByGroupCodeAndOrg(String groupCode,String paramKey,String ocCode,String orgCode);

    /**
     * 获取配置文件
     * @param groupCode
     * @param paramKey
     * @return
     */
    SysconfGlobalParam getParamByGroupCodeByOrg(String groupCode,String paramKey,String ocCode,String orgCode);
}
