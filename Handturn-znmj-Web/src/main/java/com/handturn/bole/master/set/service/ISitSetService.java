package com.handturn.bole.master.set.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.set.entity.SitSet;
import org.springframework.web.multipart.MultipartFile;

/**
 * 智能门禁-站点配置 Service接口
 *
 * @author Eric
 * @date 2020-02-28 09:41:18
 */
public interface ISitSetService extends IService<SitSet> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sitSet sitSet
     * @return ICustomPage<SitSet>
     */
    ICustomPage<SitSet> findSitSets(QueryRequest request, SitSet sitSet);


    /**
     * 获取最后一条
     * @return
     */
    SitSet findlastSitSets();

    /**
     * 修改
     *
     * @param sitSet sitSet
     */
    SitSet saveSitSet(SitSet sitSet);

    /**
    * 通过Id获取信息
    * @param sitSetId
    * @return
    */
    SitSet findSitSetById(Long sitSetId);

    /**
    * 导入
     * @param uploadFile
     * @return
     */
    ImportResultBean importUpload(MultipartFile uploadFile);
}
