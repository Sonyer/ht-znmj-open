package com.ht.znmj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ht.znmj.common.customPage.CustomPage;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.common.entity.FebsConstant;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.entity.VisitorInfo;
import com.ht.znmj.entity.VisitorLog;
import com.ht.znmj.mapper.VisitorLogMapper;
import com.ht.znmj.service.IEquipmentInfoService;
import com.ht.znmj.service.IVisitorInfoService;
import com.ht.znmj.service.IVisitorLogService;
import com.ht.znmj.utils.CopyUtils;
import com.ht.znmj.utils.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 人员信息 Service实现
 *
 * @author Eric
 * @date 2020-01-29 14:34:06
 */
@Service("VisitoryLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VisitorLogServiceImpl extends ServiceImpl<VisitorLogMapper, VisitorLog> implements IVisitorLogService {

    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    @Autowired
    private IVisitorInfoService visitorService;

    @Override
    public ICustomPage<VisitorLog> findVisitorLogs(QueryRequest request, VisitorLog visitorLog) {
        CustomPage<VisitorLog> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, visitorLog);
    }

    /**
     * 通过ID查询
     */
    @Override
    public VisitorLog findVisitorLogById(String id){
        QueryWrapper<VisitorLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VisitorLog::getId,id);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 保存设备信息
     * @param visitorLog
     */
    @Override
    @Transactional
    public VisitorLog saveVisitorLog(VisitorLog visitorLog){
        if(visitorLog.getId() == null){
            visitorLog = patchInfoLog(visitorLog);

            visitorLog.setCreateTime(new Date());
            visitorLog.setUpdateTime(new Date());
            this.getBaseMapper().insert(visitorLog);
            return visitorLog;
        }else{
            VisitorLog visitorLogOld = this.baseMapper.selectById(visitorLog.getId());
            CopyUtils.copyProperties(visitorLog,visitorLogOld);
            visitorLogOld.setUpdateTime(new Date());
            this.updateById(visitorLogOld);
            return visitorLogOld;
        }
    }

    /**
     * 补充其他信息
     * @param visitorLog
     * @return
     */
    private VisitorLog patchInfoLog(VisitorLog visitorLog){
        if(!StringUtils.isEmpty(visitorLog.getEquipmentSn())){
            EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoBySn(visitorLog.getEquipmentSn());
            if(equipmentInfo != null){
                visitorLog.setEquipmentId(equipmentInfo.getId());
                visitorLog.setAreaName(equipmentInfo.getAreaName());
                visitorLog.setInOut(equipmentInfo.getInOut());
                visitorLog.setSeqNum(equipmentInfo.getSeqNum());
                visitorLog.setEquipmentCloudId(equipmentInfo.getCloudId());
            }
        }

        if(!StringUtils.isEmpty(visitorLog.getVisitorId())){
            VisitorInfo visitorInfo = visitorService.findVisitorInfoById(visitorLog.getVisitorId());
            if(visitorInfo != null){
                visitorLog.setVisitorName(visitorInfo.getName());
                visitorLog.setPhoneNumber(visitorInfo.getPhoneNumber());
                visitorLog.setIdCard(visitorInfo.getIdCard());
                visitorLog.setDepartment(visitorInfo.getDepartment());
                visitorLog.setPositor(visitorInfo.getPositor());
                visitorLog.setWigan(visitorInfo.getWigan());
                visitorLog.setVisitorCloudId(visitorInfo.getCloudId());
            }
        }
        return visitorLog;
    }
}
