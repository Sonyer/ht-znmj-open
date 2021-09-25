package com.handturn.bole.sysconf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.entity.SysconfQueryReportColumn;
import com.handturn.bole.sysconf.mapper.SysconfQueryReportColumnMapper;
import com.handturn.bole.sysconf.service.ISysconfQueryReportColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统-报表查询列 Service实现
 *
 * @author Eric
 * @date 2020-02-12 09:14:32
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysconfQueryReportColumnServiceImpl extends ServiceImpl<SysconfQueryReportColumnMapper, SysconfQueryReportColumn> implements ISysconfQueryReportColumnService {

    @Override
    public ICustomPage<SysconfQueryReportColumn> findSysconfQueryReportColumns(QueryRequest request, SysconfQueryReportColumn sysconfQueryReportColumn) {
        CustomPage<SysconfQueryReportColumn> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "columnSeqNum", FebsConstant.ORDER_ASC, false);
        return this.baseMapper.findForPage(page, sysconfQueryReportColumn);
    }

    @Override
    @Transactional
    public SysconfQueryReportColumn saveSysconfQueryReportColumn(SysconfQueryReportColumn sysconfQueryReportColumn) {
        if(sysconfQueryReportColumn.getId() == null){
            this.save(sysconfQueryReportColumn);
            return sysconfQueryReportColumn;
        }else{
            SysconfQueryReportColumn sysconfQueryReportColumnOld = this.baseMapper.selectById(sysconfQueryReportColumn.getId());
            CopyUtils.copyProperties(sysconfQueryReportColumn,sysconfQueryReportColumnOld);
            this.updateById(sysconfQueryReportColumnOld);
            return sysconfQueryReportColumnOld;
        }
    }

    @Override
    @Transactional
    public void enableSysconfQueryReportColumn(String[] sysconfQueryReportColumnIds) {
        Arrays.stream(sysconfQueryReportColumnIds).forEach(sysconfQueryReportColumnId -> {
            SysconfQueryReportColumn sysconfQueryReportColumn = this.baseMapper.selectById(sysconfQueryReportColumnId);
            //sysconfQueryReportColumn.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysconfQueryReportColumn);
        });
	}

    @Override
    @Transactional
    public void disableSysconfQueryReportColumn(String[] sysconfQueryReportColumnIds) {
        Arrays.stream(sysconfQueryReportColumnIds).forEach(sysconfQueryReportColumnId -> {
            SysconfQueryReportColumn sysconfQueryReportColumn = this.baseMapper.selectById(sysconfQueryReportColumnId);
            //sysconfQueryReportColumn.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysconfQueryReportColumn);
        });
    }

    @Override
    public SysconfQueryReportColumn findSysconfQueryReportColumnById(Long sysconfQueryReportColumnId){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfQueryReportColumn>().lambda().eq(SysconfQueryReportColumn::getId, sysconfQueryReportColumnId));
    }

    /**
     * 通过报表ID获取字段
     * @param reportId
     * @return
     */
    @Override
    public List<SysconfQueryReportColumn> findColumnsByReportId(Long reportId){
        QueryWrapper<SysconfQueryReportColumn> queryWrapper = new QueryWrapper<SysconfQueryReportColumn>();
        queryWrapper.lambda().eq(SysconfQueryReportColumn::getReportId,reportId);
        queryWrapper.lambda().orderByAsc(SysconfQueryReportColumn::getColumnSeqNum);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 通过报表ID获取字段
     * @param reportId
     * @return
     */
    @Override
    public Map<String,SysconfQueryReportColumn> findColumnsMapByReportId(Long reportId){
        List<SysconfQueryReportColumn> lists = findColumnsByReportId(reportId);
        Map<String,SysconfQueryReportColumn> result = new HashMap<String,SysconfQueryReportColumn>();
        lists.forEach(obj ->{
            result.put(obj.getColumnCode(),obj);
        });

        return result;
    }
}
