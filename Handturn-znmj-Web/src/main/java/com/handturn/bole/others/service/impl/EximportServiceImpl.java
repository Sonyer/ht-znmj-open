package com.handturn.bole.others.service.impl;

import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.others.entity.Eximport;
import com.handturn.bole.others.mapper.EximportMapper;
import com.handturn.bole.others.service.IEximportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Eric
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EximportServiceImpl extends ServiceImpl<EximportMapper, Eximport> implements IEximportService {

    @Value("${febs.max.batch.insert.num:1000}")
    private int batchInsertMaxNum;

    @Override
    public ICustomPage<Eximport> findEximports(QueryRequest request, Eximport eximport) {
        CustomPage<Eximport> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        return (ICustomPage<Eximport>) this.page(page, null);
    }

    @Override
    @Transactional
    public void batchInsert(List<Eximport> list) {
        saveBatch(list, batchInsertMaxNum);
    }

}
