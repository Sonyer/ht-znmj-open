package com.handturn.bole.main.visitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.main.visitor.entity.OcVisitorInfo;
import com.handturn.bole.master.member.entity.MemberVisitorInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 网点-会员访客信息 Service接口
 *
 * @author Eric
 * @date 2020-09-22 08:57:38
 */
public interface IOcVisitorInfoService extends IService<OcVisitorInfo> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param ocVisitorInfo ocVisitorInfo
     * @return ICustomPage<OcVisitorInfo>
     */
    ICustomPage<OcVisitorInfo> findOcVisitorInfos(QueryRequest request, OcVisitorInfo ocVisitorInfo);

    /**
     * 修改
     *
     * @param ocVisitorInfo ocVisitorInfo
     */
    OcVisitorInfo saveOcVisitorInfo(OcVisitorInfo ocVisitorInfo);

    /**
     * 启用
     *
     * @param ocVisitorUploadImgIds ocVisitorUploadImgIds
     */
    void freeze(String[] ocVisitorUploadImgIds);

    /**
     * 禁用
     *
     * @param ocVisitorUploadImgIds ocVisitorUploadImgIds
     */
    void normal(String[] ocVisitorUploadImgIds);


    /**
    * 通过Id获取信息
    * @param ocVisitorInfoId
    * @return
    */
    OcVisitorInfo findOcVisitorInfoById(Long ocVisitorInfoId);

    /**
     * 通过Id获取信息
     * @param ocVisitorInfoIds
     * @return
     */
    List<OcVisitorInfo> findOcVisitorInfoByIds(Set<String> ocVisitorInfoIds);

    /**
     * 通过Id获取信息
     * @param idCardName
     * @return
     */
    OcVisitorInfo findOcVisitorInfoByIdCard(String idCardName,String phoneNumber,String ocCode,String orgCode);

    /**
     * 通过Id获取信息
     * @param memeberAccountCode
     * @return
     */
    Set<Long> findByAccountCode(String memeberAccountCode);
}
