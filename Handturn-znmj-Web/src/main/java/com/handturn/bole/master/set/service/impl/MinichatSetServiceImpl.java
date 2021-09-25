package com.handturn.bole.master.set.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.master.set.entity.MinichatSet;
import com.handturn.bole.master.set.mapper.MinichatSetMapper;
import com.handturn.bole.master.set.service.IMinichatSetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 智能门禁-小程序参数配置 Service实现
 *
 * @author Eric
 * @date 2020-02-28 11:05:13
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MinichatSetServiceImpl extends ServiceImpl<MinichatSetMapper, MinichatSet> implements IMinichatSetService {

    @Override
    public ICustomPage<MinichatSet> findMinichatSets(QueryRequest request, MinichatSet linichatSet) {
        CustomPage<MinichatSet> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, linichatSet);
    }

    @Override
    public MinichatSet findlastMinichatSets(){
        QueryWrapper<MinichatSet> queryWrapper = new QueryWrapper<MinichatSet>();
        List<MinichatSet> linichatSets =  this.baseMapper.selectList(queryWrapper);
        return linichatSets == null || linichatSets.size() <=0 ? null:linichatSets.get(0);
    }

    @Override
    @Transactional
    public MinichatSet saveMinichatSet(MinichatSet linichatSet) {
        if(linichatSet.getId() == null){
            this.save(linichatSet);
            return linichatSet;
        }else{
            MinichatSet linichatSetOld = this.baseMapper.selectById(linichatSet.getId());
            CopyUtils.copyProperties(linichatSet,linichatSetOld);
            this.updateById(linichatSetOld);
            return linichatSetOld;
        }
    }

    @Override
    public MinichatSet findMinichatSetById(Long linichatSetId){
        return this.baseMapper.selectOne(new QueryWrapper<MinichatSet>().lambda().eq(MinichatSet::getId, linichatSetId));
    }
}
