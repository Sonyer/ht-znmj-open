package ${basePackage}.${mapperPackage};

import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import ${basePackage}.${entityPackage}.${className};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

/**
 * ${tableComment} Mapper
 *
 * @author ${author}
 * @date ${date}
 */
public interface ${className}Mapper extends BaseMapper<${className}> {

    ICustomPage<${className}> findForPage(CustomPage page, @Param("${className?uncap_first}") ${className} ${className?uncap_first});

}
