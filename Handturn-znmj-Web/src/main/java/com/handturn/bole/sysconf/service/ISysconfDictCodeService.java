package com.handturn.bole.sysconf.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.sysconf.entity.SysconfDictCode;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统-数据字典明细 Service接口
 *
 * @author Eric
 * @date 2019-12-01 10:59:43
 */
public interface ISysconfDictCodeService extends IService<SysconfDictCode> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysconfDictCode sysconfDictCode
     * @return ICustomPage<SysconfDictCode>
     */
    ICustomPage<SysconfDictCode> findSysconfDictCodes(QueryRequest request, SysconfDictCode sysconfDictCode);

    /**
     * 修改
     *
     * @param sysconfDictCode sysconfDictCode
     */
    void saveSysconfDictCode(SysconfDictCode sysconfDictCode);

    /**
     * 启用
     *
     * @param sysconfDictCodeIds sysconfDictCodeIds
     */
    void enableSysconfDictCode(String[] sysconfDictCodeIds);

    /**
     * 禁用
     *
     * @param sysconfDictCodeIds sysconfDictCodeIds
     */
    void disableSysconfDictCode(String[] sysconfDictCodeIds);


    /**
     * 通过Id获取信息
     * @param sysconfDictCodeId
     * @return
     */
    SysconfDictCode findSysconfDictCodeById(Long sysconfDictCodeId);


    /**
     * 查询
     * @param typeCode
     * @return
     */
    List<OptionVo> getDictOptionVoByTypeCode(String typeCode);

    /**
     * 查询显示名
     * @param typeCode
     * @return
     */
    Map<String,String> getDictNameByTypeCode(String typeCode);

    /**
     * 查询显示名
     * @param typeCode
     * @return
     */
    Map<String,Object> getDictMapByTypeCode(String typeCode);

    /**
     * 指定查询
     * @param typeCode
     * @return
     */
    List<OptionVo> getDictOptionVoByTypeCodeContains(String typeCode, Set<String> dictCode);

    /**
     * 指定查询
     * @param typeCode
     * @return
     */
    List<OptionVo> getDictOptionVoByTypeCodeExit(String typeCode,Set<String> dictCode);
}
