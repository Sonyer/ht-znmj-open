package com.handturn.bole.master.set.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.master.set.entity.NotifySet;
import com.handturn.bole.master.set.mapper.NotifySetMapper;
import com.handturn.bole.master.set.service.INotifySetService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 智能门禁-通知设置 Service实现
 *
 * @author Eric
 * @date 2020-06-09 18:43:12
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class NotifySetServiceImpl extends ServiceImpl<NotifySetMapper, NotifySet> implements INotifySetService {

    @Override
    public ICustomPage<NotifySet> findNotifySets(QueryRequest request, NotifySet notifySet) {
        CustomPage<NotifySet> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, notifySet);
    }

    @Override
    @Transactional
    public NotifySet saveNotifySet(NotifySet notifySet) {
        if(notifySet.getId() == null){
            checkByUk(notifySet);
            notifySet.setStatus(BaseStatus.DISABLED);
            this.save(notifySet);
            return notifySet;
        }else{
            NotifySet notifySetOld = this.baseMapper.selectById(notifySet.getId());
            CopyUtils.copyProperties(notifySet,notifySetOld);
            this.updateById(notifySetOld);
            return notifySetOld;
        }
    }

    @Override
    @Transactional
    public void enableNotifySet(String[] notifySetIds) {
        Arrays.stream(notifySetIds).forEach(notifySetId -> {
            NotifySet notifySet = this.baseMapper.selectById(notifySetId);
            notifySet.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(notifySet);
        });
	}

    @Override
    @Transactional
    public void disableNotifySet(String[] notifySetIds) {
        Arrays.stream(notifySetIds).forEach(notifySetId -> {
            NotifySet notifySet = this.baseMapper.selectById(notifySetId);
            notifySet.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(notifySet);
        });
    }

    @Override
    public NotifySet findNotifySetById(Long notifySetId){
        return this.baseMapper.selectOne(new QueryWrapper<NotifySet>().lambda().eq(NotifySet::getId, notifySetId));
    }

    public void checkByUk(NotifySet notifySet){
        QueryWrapper<NotifySet> queryWrapper = new QueryWrapper<NotifySet>();
        queryWrapper.lambda().eq(NotifySet::getNotifyType, notifySet.getNotifyType());
        if(notifySet.getId() != null && notifySet.getId() != 0){
            queryWrapper.lambda().ne(NotifySet::getId, notifySet.getId());
        }

        NotifySet temp = this.baseMapper.selectOne(queryWrapper);

        if(temp != null){
            throw new FebsException("记录已经存在!请确认!");
        }
    }

    /**
     * 通过Code获取信息
     * @param notifyType
     * @return
     */
    @Override
    public NotifySet findNotifySetByType(String notifyType, Set<String> statuses){
        QueryWrapper<NotifySet> queryWrapper = new QueryWrapper<NotifySet>();
        queryWrapper.lambda().eq(NotifySet::getNotifyType,notifyType);
        if(statuses != null && statuses.size() > 0){
            queryWrapper.lambda().in(NotifySet::getStatus,statuses);
        }
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 通过使用类型获取信息
     * @return
     */
    @Override
    public List<NotifySet> findNotifySetByIs(Set<String> notifyTypeSet,String isOnInner, String isOnWx, String isOnSms, Set<String> statuses){
        QueryWrapper<NotifySet> queryWrapper = new QueryWrapper<NotifySet>();
        if(notifyTypeSet != null && notifyTypeSet.size() > 0){
            queryWrapper.lambda().in(NotifySet::getNotifyType,notifyTypeSet);
        }
        if(!StringUtils.isEmpty(isOnInner)){
            queryWrapper.lambda().in(NotifySet::getIsOnInner,isOnInner);
        }
        if(!StringUtils.isEmpty(isOnWx)){
            queryWrapper.lambda().in(NotifySet::getIsOnWx,isOnWx);
        }
        if(!StringUtils.isEmpty(isOnSms)){
            queryWrapper.lambda().in(NotifySet::getIsOnSms,isOnSms);
        }
        if(statuses != null && statuses.size() > 0){
            queryWrapper.lambda().in(NotifySet::getStatus,statuses);
        }
        queryWrapper.lambda().orderByAsc(NotifySet::getOwnerType);
        return this.baseMapper.selectList(queryWrapper);
    }
}
