package com.handturn.bole.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.generator.entity.GeneratorConfig;

/**
 * @author Eric
 */
public interface IGeneratorConfigService extends IService<GeneratorConfig> {

    /**
     * 查询
     *
     * @return GeneratorConfig
     */
    GeneratorConfig findGeneratorConfig();

    /**
     * 修改
     *
     * @param generatorConfig generatorConfig
     */
    void updateGeneratorConfig(GeneratorConfig generatorConfig);

}
