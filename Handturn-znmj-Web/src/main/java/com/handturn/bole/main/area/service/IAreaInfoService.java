package com.handturn.bole.main.area.service;

import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.main.area.entity.AreaInfo;

import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 基础资料-区域管理 Service接口
 *
 * @author Eric
 * @date 2020-09-11 08:20:03
 */
public interface IAreaInfoService extends IService<AreaInfo> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param areaInfo areaInfo
     * @return ICustomPage<AreaInfo>
     */
    ICustomPage<AreaInfo> findAreaInfos(QueryRequest request, AreaInfo areaInfo);

    /**
     * 修改
     *
     * @param areaInfo areaInfo
     */
    AreaInfo saveAreaInfo(AreaInfo areaInfo);

    /**
     * 启用
     *
     * @param areaInfoIds areaInfoIds
     */
    void enableAreaInfo(String[] areaInfoIds);

    /**
    * 禁用
    *
    * @param areaInfoIds areaInfoIds
    */
    void disableAreaInfo(String[] areaInfoIds);


    /**
    * 通过Id获取信息
    * @param areaInfoId
    * @return
    */
    AreaInfo findAreaInfoById(Long areaInfoId);

    /**
     * 获取Id KEY
     * @param orgCode
     * @return
     */
    List<OptionVo> findOptionVo4IdKeyByOcCode(String orgCode,String ocCode);

    /**
     * 获取CODE KEY
     * @param orgCode
     * @return
     */
    List<OptionVo> findOptionVo4CodeKeyByOcCode(String orgCode,String ocCode);

    Map<String,String> findMap4CodeKeyByOcCode(String ocCode);
}
