package com.handturn.bole.master.set.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.set.entity.MinichatSet;

/**
 * 智能门禁-小程序参数配置 Service接口
 *
 * @author Eric
 * @date 2020-02-28 11:05:13
 */
public interface IMinichatSetService extends IService<MinichatSet> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param minichatSet minichatSet
     * @return ICustomPage<MinichatSet>
     */
    ICustomPage<MinichatSet> findMinichatSets(QueryRequest request, MinichatSet minichatSet);

    MinichatSet findlastMinichatSets();

    /**
     * 修改
     *
     * @param minichatSet minichatSet
     */
    MinichatSet saveMinichatSet(MinichatSet minichatSet);

    /**
    * 通过Id获取信息
    * @param minichatSetId
    * @return
    */
    MinichatSet findMinichatSetById(Long minichatSetId);
}
