package com.handturn.bole.main.equipment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.main.equipment.entity.MiddlewareInfo;

import java.util.List;
import java.util.Set;

/**
 * 基础资料-中间件管理 Service接口
 *
 * @author Eric
 * @date 2020-12-02 11:13:52
 */
public interface IMiddlewareInfoService extends IService<MiddlewareInfo> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param middlewareInfo middlewareInfo
     * @return ICustomPage<MiddlewareInfo>
     */
    ICustomPage<MiddlewareInfo> findMiddlewareInfos(QueryRequest request, MiddlewareInfo middlewareInfo);

    /**
     * 修改
     *
     * @param middlewareInfo middlewareInfo
     */
    MiddlewareInfo saveMiddlewareInfo(MiddlewareInfo middlewareInfo);

    /**
    * 通过Id获取信息
    * @param middlewareInfoId
    * @return
    */
    MiddlewareInfo findMiddlewareInfoById(Long middlewareInfoId);

    List<MiddlewareInfo> findMiddlewareInfoOnLineByEquipmentMac(String equipmac,String ocCode);

    List<MiddlewareInfo> findMiddlewareInfoByMac(String sysmac, String equipmac);

    List<MiddlewareInfo> findMiddlewareInfoNotInByMac(String sysmac, Set<String> notInEquipmac);

    List<MiddlewareInfo> findMiddlewareInfoByMacOc(String sysmac,String ocCode);
}
