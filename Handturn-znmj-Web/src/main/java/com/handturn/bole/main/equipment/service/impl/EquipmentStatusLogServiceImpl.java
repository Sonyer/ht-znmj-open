package com.handturn.bole.main.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.equipment.entity.EquipmentStatusLog;
import com.handturn.bole.main.equipment.mapper.EquipmentStatusLogMapper;
import com.handturn.bole.main.equipment.service.IEquipmentStatusLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 基础资料-设备状态日志 Service实现
 *
 * @author Eric
 * @date 2020-09-11 08:20:34
 */
@Service("EquipmentStatusLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EquipmentStatusLogServiceImpl extends ServiceImpl<EquipmentStatusLogMapper, EquipmentStatusLog> implements IEquipmentStatusLogService {

    @Override
    public ICustomPage<EquipmentStatusLog> findEquipmentStatusLogs(QueryRequest request, EquipmentStatusLog equipmentStatusLog) {
        CustomPage<EquipmentStatusLog> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, equipmentStatusLog);
    }

    @Override
    @Transactional
    public EquipmentStatusLog saveEquipmentStatusLog(EquipmentStatusLog equipmentStatusLog) {
        if(equipmentStatusLog.getId() == null){
            this.save(equipmentStatusLog);
            return equipmentStatusLog;
        }else{
            EquipmentStatusLog equipmentStatusLogOld = this.baseMapper.selectById(equipmentStatusLog.getId());
            CopyUtils.copyProperties(equipmentStatusLog,equipmentStatusLogOld);
            this.updateById(equipmentStatusLogOld);
            return equipmentStatusLogOld;
        }
    }

    @Override
    @Transactional
    public void enableEquipmentStatusLog(String[] equipmentStatusLogIds) {
        Arrays.stream(equipmentStatusLogIds).forEach(equipmentStatusLogId -> {
            EquipmentStatusLog equipmentStatusLog = this.baseMapper.selectById(equipmentStatusLogId);
            //equipmentStatusLog.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(equipmentStatusLog);
        });
	}

    @Override
    @Transactional
    public void disableEquipmentStatusLog(String[] equipmentStatusLogIds) {
        Arrays.stream(equipmentStatusLogIds).forEach(equipmentStatusLogId -> {
            EquipmentStatusLog equipmentStatusLog = this.baseMapper.selectById(equipmentStatusLogId);
            //equipmentStatusLog.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(equipmentStatusLog);
        });
    }

    @Override
    public EquipmentStatusLog findEquipmentStatusLogById(Long equipmentStatusLogId){
        return this.baseMapper.selectOne(new QueryWrapper<EquipmentStatusLog>().lambda().eq(EquipmentStatusLog::getId, equipmentStatusLogId));
    }
}
