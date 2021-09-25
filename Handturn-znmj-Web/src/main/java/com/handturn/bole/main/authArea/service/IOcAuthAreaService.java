package com.handturn.bole.main.authArea.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.main.authArea.entity.OcAuthArea;

import java.util.List;
import java.util.Set;

/**
 * 网点-权限区域 Service接口
 *
 * @author Eric
 * @date 2020-09-22 08:57:47
 */
public interface IOcAuthAreaService extends IService<OcAuthArea> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param ocAuthArea ocAuthArea
     * @return ICustomPage<OcAuthArea>
     */
    ICustomPage<OcAuthArea> findOcAuthAreas(QueryRequest request, OcAuthArea ocAuthArea);

    /**
     * 修改
     *
     * @param ocAuthArea ocAuthArea
     */
    OcAuthArea saveOcAuthArea(OcAuthArea ocAuthArea);

    /**
     * 启用
     *
     * @param ocAuthAreaIds ocAuthAreaIds
     */
    void enableOcAuthArea(String[] ocAuthAreaIds);

    /**
    * 禁用
    *
    * @param ocAuthAreaIds ocAuthAreaIds
    */
    void disableOcAuthArea(String[] ocAuthAreaIds);


    /**
    * 通过Id获取信息
    * @param ocAuthAreaId
    * @return
    */
    OcAuthArea findOcAuthAreaById(Long ocAuthAreaId);

    /**
     * 通过Id获取信息
     * @param ocAuthAreaCode
     * @return
     */
    OcAuthArea findOcAuthAreaByCode(String ocAuthAreaCode,String orgCode,String ocCode);

    /**
     * 前端分页查询
     * @param searchValue
     * @param authAreaIds
     * @return
     */
    List<OcAuthArea> authAreaPageQuery(Integer pageIndex,String searchValue, Set<Long> authAreaIds);

    /**
     * 前端分页查询
     * @param authAreaCode
     * @param orgCode
     * @param ocCode
     * @return
     */
    OcAuthArea findByAuthAreaCode(String authAreaCode,String orgCode,String ocCode);
}
