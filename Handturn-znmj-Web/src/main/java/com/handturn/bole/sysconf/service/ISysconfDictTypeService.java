package com.handturn.bole.sysconf.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfDictType;

/**
 * 系统-数据字典类型表 Service接口
 *
 * @author MrBird
 * @date 2019-12-08 20:56:08
 */
public interface ISysconfDictTypeService extends IService<SysconfDictType> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysconfDictType sysconfDictType
     * @return ICustomPage<SysconfDictType>
     */
    ICustomPage<SysconfDictType> findSysconfDictTypes(QueryRequest request, SysconfDictType sysconfDictType);

    /**
     * 修改
     *
     * @param sysconfDictType sysconfDictType
     */
    void saveSysconfDictType(SysconfDictType sysconfDictType);

    /**
     * 启用
     *
     * @param sysconfDictTypeIds sysconfDictTypeIds
     */
    void enableSysconfDictType(String[] sysconfDictTypeIds);

    /**
    * 禁用
    *
    * @param sysconfDictTypeIds sysconfDictTypeIds
    */
    void disableSysconfDictType(String[] sysconfDictTypeIds);


    /**
    * 通过Id获取信息
    * @param sysconfDictTypeId
    * @return
    */
    SysconfDictType findSysconfDictTypeById(Long sysconfDictTypeId);

    /**
     * 刷新缓存
     *
     */
    void refreshCacheSysconfDictType();

}
