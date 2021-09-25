package com.handturn.bole.sysconf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.monitor.contant.RedisContant;
import com.handturn.bole.monitor.service.IRedisService;
import com.handturn.bole.sysconf.entity.SysconfDictType;
import com.handturn.bole.sysconf.mapper.SysconfDictTypeMapper;
import com.handturn.bole.sysconf.service.ISysconfDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;

/**
 * 系统-数据字典类型表 Service实现
 *
 * @author MrBird
 * @date 2019-12-08 20:56:08
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysconfDictTypeServiceImpl extends ServiceImpl<SysconfDictTypeMapper, SysconfDictType> implements ISysconfDictTypeService {

    @Autowired
    private IRedisService redisService;

    @Override
    public ICustomPage<SysconfDictType> findSysconfDictTypes(QueryRequest request, SysconfDictType sysconfDictType) {
        CustomPage<SysconfDictType> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysconfDictType);
    }

    @Override
    @Transactional
    public void saveSysconfDictType(SysconfDictType sysconfDictType) {
        if(sysconfDictType.getId() == null){
            sysconfDictType.setStatus(BaseStatus.ENABLED);
            this.save(sysconfDictType);
        }else{
            SysconfDictType sysconfDictTypeOld = this.baseMapper.selectById(sysconfDictType.getId());
            CopyUtils.copyProperties(sysconfDictType,sysconfDictTypeOld);
            this.updateById(sysconfDictTypeOld);
        }
    }

    @Override
    @Transactional
    public void enableSysconfDictType(String[] sysconfDictTypeIds) {
        Arrays.stream(sysconfDictTypeIds).forEach(sysconfDictTypeId -> {
            SysconfDictType sysconfDictType = this.baseMapper.selectById(sysconfDictTypeId);
            sysconfDictType.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysconfDictType);
        });
	}

    @Override
    @Transactional
    public void disableSysconfDictType(String[] sysconfDictTypeIds) {
        Arrays.stream(sysconfDictTypeIds).forEach(sysconfDictTypeId -> {
            SysconfDictType sysconfDictType = this.baseMapper.selectById(sysconfDictTypeId);
            sysconfDictType.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysconfDictType);
        });
    }

    /**
     * 刷新缓存
     */
    @Override
    @Transactional
    public void refreshCacheSysconfDictType(){
        try {
            Set<String> dicOptionKeys = redisService.getKeys(RedisContant.PREFIX_DIC_TYPE_OPTION+"*");
            Set<String> dicMapKeys = redisService.getKeys(RedisContant.PREFIX_DIC_TYPE_MAP+"*");
            if(dicOptionKeys != null && dicOptionKeys.size() > 0){
                redisService.del(dicOptionKeys.toArray(new String[dicOptionKeys.size()]));
            }
            if(dicMapKeys!= null && dicMapKeys.size() > 0){
                redisService.del(dicMapKeys.toArray(new String[dicMapKeys.size()]));
            }
        } catch (RedisConnectException e) {
            log.error("数据字典刷新Redis异常:"+e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public SysconfDictType findSysconfDictTypeById(Long sysconfDictTypeId){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfDictType>().lambda().eq(SysconfDictType::getId, sysconfDictTypeId));
    }
}
