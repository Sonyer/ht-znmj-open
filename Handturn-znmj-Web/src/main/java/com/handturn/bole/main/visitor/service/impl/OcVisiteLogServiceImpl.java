package com.handturn.bole.main.visitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.visitor.entity.OcVisiteLog;
import com.handturn.bole.main.visitor.mapper.OcVisiteLogMapper;
import com.handturn.bole.main.visitor.service.IOcVisiteLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 网点-会员访客日志 Service实现
 *
 * @author Eric
 * @date 2020-12-06 17:24:27
 */
@Service("OcVisiteLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OcVisiteLogServiceImpl extends ServiceImpl<OcVisiteLogMapper, OcVisiteLog> implements IOcVisiteLogService {

    @Override
    public ICustomPage<OcVisiteLog> findOcVisiteLogs(QueryRequest request, OcVisiteLog ocVisiteLog) {
        CustomPage<OcVisiteLog> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, ocVisiteLog);
    }

    @Override
    @Transactional
    public OcVisiteLog saveOcVisiteLog(OcVisiteLog ocVisiteLog) {
        if(ocVisiteLog.getId() == null){
            this.save(ocVisiteLog);
            return ocVisiteLog;
        }else{
            OcVisiteLog ocVisiteLogOld = this.baseMapper.selectById(ocVisiteLog.getId());
            CopyUtils.copyProperties(ocVisiteLog,ocVisiteLogOld);
            this.updateById(ocVisiteLogOld);
            return ocVisiteLogOld;
        }
    }

    @Override
    @Transactional
    public void enableOcVisiteLog(String[] ocVisiteLogIds) {
        Arrays.stream(ocVisiteLogIds).forEach(ocVisiteLogId -> {
            OcVisiteLog ocVisiteLog = this.baseMapper.selectById(ocVisiteLogId);
            //ocVisiteLog.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(ocVisiteLog);
        });
	}

    @Override
    @Transactional
    public void disableOcVisiteLog(String[] ocVisiteLogIds) {
        Arrays.stream(ocVisiteLogIds).forEach(ocVisiteLogId -> {
            OcVisiteLog ocVisiteLog = this.baseMapper.selectById(ocVisiteLogId);
            //ocVisiteLog.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(ocVisiteLog);
        });
    }

    @Override
    public OcVisiteLog findOcVisiteLogById(Long ocVisiteLogId){
        return this.baseMapper.selectOne(new QueryWrapper<OcVisiteLog>().lambda().eq(OcVisiteLog::getId, ocVisiteLogId));
    }
}
