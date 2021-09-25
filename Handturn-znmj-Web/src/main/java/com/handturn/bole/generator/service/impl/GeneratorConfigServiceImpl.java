package com.handturn.bole.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.generator.entity.GeneratorConfig;
import com.handturn.bole.generator.mapper.GeneratorConfigMapper;
import com.handturn.bole.generator.service.IGeneratorConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Eric
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GeneratorConfigServiceImpl extends ServiceImpl<GeneratorConfigMapper, GeneratorConfig> implements IGeneratorConfigService {

    @Override
    public GeneratorConfig findGeneratorConfig() {
        List<GeneratorConfig> generatorConfigs = this.baseMapper.selectList(null);
        return CollectionUtils.isNotEmpty(generatorConfigs) ? generatorConfigs.get(0) : null;
    }

    @Override
    @Transactional
    public void updateGeneratorConfig(GeneratorConfig generatorConfig) {
        this.saveOrUpdate(generatorConfig);
    }
}
