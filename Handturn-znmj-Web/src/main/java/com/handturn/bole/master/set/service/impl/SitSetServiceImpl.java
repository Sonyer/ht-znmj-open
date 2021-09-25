package com.handturn.bole.master.set.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.master.set.entity.SitSet;
import com.handturn.bole.master.set.mapper.SitSetMapper;
import com.handturn.bole.master.set.service.IImgStoreSetService;
import com.handturn.bole.master.set.service.ISitSetService;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 智能门禁-站点配置 Service实现
 *
 * @author Eric
 * @date 2020-02-28 09:41:18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SitSetServiceImpl extends ServiceImpl<SitSetMapper, SitSet> implements ISitSetService {

    @Autowired
    private ISysconfGlobalParamService sysconfGlobalParamService;

    @Autowired
    private IImgStoreSetService imgStoreSetService;

    @Override
    public ICustomPage<SitSet> findSitSets(QueryRequest request, SitSet sitSet) {
        CustomPage<SitSet> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sitSet);
    }

    /**
     * 获取最后一条
     * @return
     */
    @Override
    public SitSet findlastSitSets(){
        QueryWrapper<SitSet> queryWrapper = new QueryWrapper<SitSet>();
        List<SitSet> sitSets =  this.baseMapper.selectList(queryWrapper);
        return sitSets == null || sitSets.size() <=0 ? null:sitSets.get(0);
    }

    @Override
    @Transactional
    public SitSet saveSitSet(SitSet sitSet) {
        if(sitSet.getId() == null){
            this.save(sitSet);
            return sitSet;
        }else{
            SitSet sitSetOld = this.baseMapper.selectById(sitSet.getId());
            CopyUtils.copyProperties(sitSet,sitSetOld);
            this.updateById(sitSetOld);
            return sitSetOld;
        }
    }

    @Override
    public SitSet findSitSetById(Long sitSetId){
        return this.baseMapper.selectOne(new QueryWrapper<SitSet>().lambda().eq(SitSet::getId, sitSetId));
    }

    /**
     * 导入
     * @param uploadFile
     * @return
     */
    @Override
    public ImportResultBean importUpload(MultipartFile uploadFile){
        try {
            ImportResultBean resultBean = imgStoreSetService.importFileUpload(uploadFile, imgStoreSetService.FILE_PLATFORM);
            return resultBean;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
