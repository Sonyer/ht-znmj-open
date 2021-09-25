package com.handturn.bole.main.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.equipment.entity.EquipmentInfoOnlineStatus;
import com.handturn.bole.main.equipment.entity.MiddlewareInfo;
import com.handturn.bole.main.equipment.mapper.MiddlewareInfoMapper;
import com.handturn.bole.main.equipment.service.IMiddlewareInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 基础资料-中间件管理 Service实现
 *
 * @author Eric
 * @date 2020-12-02 11:13:52
 */
@Service("MiddlewareInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MiddlewareInfoServiceImpl extends ServiceImpl<MiddlewareInfoMapper, MiddlewareInfo> implements IMiddlewareInfoService {

    @Override
    public ICustomPage<MiddlewareInfo> findMiddlewareInfos(QueryRequest request, MiddlewareInfo middlewareInfo) {
        CustomPage<MiddlewareInfo> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, middlewareInfo);
    }

    @Override
    @Transactional
    public MiddlewareInfo saveMiddlewareInfo(MiddlewareInfo middlewareInfo) {
        if(middlewareInfo.getId() == null){
            this.save(middlewareInfo);
            return middlewareInfo;
        }else{
            MiddlewareInfo middlewareInfoOld = this.baseMapper.selectById(middlewareInfo.getId());
            CopyUtils.copyProperties(middlewareInfo,middlewareInfoOld);
            this.updateById(middlewareInfoOld);
            return middlewareInfoOld;
        }
    }

    @Override
    public MiddlewareInfo findMiddlewareInfoById(Long middlewareInfoId){
        return this.baseMapper.selectOne(new QueryWrapper<MiddlewareInfo>().lambda().eq(MiddlewareInfo::getId, middlewareInfoId));
    }

    @Override
    public List<MiddlewareInfo> findMiddlewareInfoOnLineByEquipmentMac(String equipmac,String ocCode){
        QueryWrapper<MiddlewareInfo> queryWrapper = new QueryWrapper<MiddlewareInfo>();
        queryWrapper.lambda().eq(MiddlewareInfo::getEquipmentMacId,equipmac);
        queryWrapper.lambda().eq(MiddlewareInfo::getOcCode,ocCode);
        queryWrapper.lambda().eq(MiddlewareInfo::getOnlineStatus,EquipmentInfoOnlineStatus.ONLINE);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<MiddlewareInfo> findMiddlewareInfoByMac(String sysmac, String equipmac){
        QueryWrapper<MiddlewareInfo> queryWrapper = new QueryWrapper<MiddlewareInfo>();
        queryWrapper.lambda().eq(MiddlewareInfo::getMacId,sysmac);
        queryWrapper.lambda().eq(MiddlewareInfo::getEquipmentMacId,equipmac);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<MiddlewareInfo> findMiddlewareInfoNotInByMac(String sysmac, Set<String> notInEquipmac){
        QueryWrapper<MiddlewareInfo> queryWrapper = new QueryWrapper<MiddlewareInfo>();
        queryWrapper.lambda().eq(MiddlewareInfo::getMacId,sysmac);
        queryWrapper.lambda().notIn(MiddlewareInfo::getEquipmentMacId,notInEquipmac);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<MiddlewareInfo> findMiddlewareInfoByMacOc(String sysmac,String ocCode){
        QueryWrapper<MiddlewareInfo> queryWrapper = new QueryWrapper<MiddlewareInfo>();
        queryWrapper.lambda().eq(MiddlewareInfo::getMacId,sysmac);
        queryWrapper.lambda().eq(MiddlewareInfo::getOcCode,ocCode);
        queryWrapper.lambda().eq(MiddlewareInfo::getOnlineStatus, EquipmentInfoOnlineStatus.ONLINE);
        return this.baseMapper.selectList(queryWrapper);
    }
}
