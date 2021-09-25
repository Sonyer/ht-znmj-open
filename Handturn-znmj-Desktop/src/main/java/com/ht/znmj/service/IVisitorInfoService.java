package com.ht.znmj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.VisitorInfo;

import java.util.List;
import java.util.Set;

/**
 * 人员信息 Service接口
 *
 * @author Eric
 * @date 2020-01-29 14:34:06
 */
public interface IVisitorInfoService extends IService<VisitorInfo> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param visitorInfo visitorInfo
     * @return ICustomPage<BasRoute>
     */
    ICustomPage<VisitorInfo> findVisitorInfos(QueryRequest request, VisitorInfo visitorInfo);

    /**
     * 通过ID查询
     */
    VisitorInfo findVisitorInfoById(String id);

    /**
     * 通过云端ID查询
     */
    List<VisitorInfo> findVisitorInfoByCloudId(String cloudId);

    /**
     * 通过姓名、手机号查询
     */
    VisitorInfo findVisitorInfoByUK(String name,String phoneNumber,String id);

    /**
     * 保存设备信息
     * @param visitorInfo
     */
    VisitorInfo saveVisitorInfo(VisitorInfo visitorInfo);

    /**
     * 删除访客
     * @param visitorIds
     */
    void deleteVisitors(Set<String> visitorIds);

    /**
     * 删除所有访客
     */
    void deleteAllVisitors();
}
