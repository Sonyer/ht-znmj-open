package com.handturn.bole.main.authArea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.authArea.entity.OcAuthAreaLog;
import com.handturn.bole.main.authArea.mapper.OcAuthAreaLogMapper;
import com.handturn.bole.main.authArea.service.IOcAuthAreaLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.utils.CopyUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 网点-访客区域权限日志 Service实现
 *
 * @author Eric
 * @date 2020-12-05 20:04:29
 */
@Service("OcAuthAreaLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OcAuthAreaLogServiceImpl extends ServiceImpl<OcAuthAreaLogMapper, OcAuthAreaLog> implements IOcAuthAreaLogService {

    @Override
    public ICustomPage<OcAuthAreaLog> findOcAuthAreaLogs(QueryRequest request, OcAuthAreaLog ocAuthAreaLog) {
        CustomPage<OcAuthAreaLog> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, ocAuthAreaLog);
    }

    @Override
    @Transactional
    public OcAuthAreaLog saveOcAuthAreaLog(OcAuthAreaLog ocAuthAreaLog) {
        if(ocAuthAreaLog.getId() == null){
            this.save(ocAuthAreaLog);
            return ocAuthAreaLog;
        }else{
            OcAuthAreaLog ocAuthAreaLogOld = this.baseMapper.selectById(ocAuthAreaLog.getId());
            CopyUtils.copyProperties(ocAuthAreaLog,ocAuthAreaLogOld);
            this.updateById(ocAuthAreaLogOld);
            return ocAuthAreaLogOld;
        }
    }

    @Override
    @Transactional
    public void enableOcAuthAreaLog(String[] ocAuthAreaLogIds) {
        Arrays.stream(ocAuthAreaLogIds).forEach(ocAuthAreaLogId -> {
            OcAuthAreaLog ocAuthAreaLog = this.baseMapper.selectById(ocAuthAreaLogId);
            //ocAuthAreaLog.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(ocAuthAreaLog);
        });
	}

    @Override
    @Transactional
    public void disableOcAuthAreaLog(String[] ocAuthAreaLogIds) {
        Arrays.stream(ocAuthAreaLogIds).forEach(ocAuthAreaLogId -> {
            OcAuthAreaLog ocAuthAreaLog = this.baseMapper.selectById(ocAuthAreaLogId);
            //ocAuthAreaLog.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(ocAuthAreaLog);
        });
    }

    @Override
    public OcAuthAreaLog findOcAuthAreaLogById(Long ocAuthAreaLogId){
        return this.baseMapper.selectOne(new QueryWrapper<OcAuthAreaLog>().lambda().eq(OcAuthAreaLog::getId, ocAuthAreaLogId));
    }
}
