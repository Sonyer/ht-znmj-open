package ${basePackage}.${servicePackage};

import ${basePackage}.${entityPackage}.${className};

import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ${tableComment} Service接口
 *
 * @author ${author}
 * @date ${date}
 */
public interface I${className}Service extends IService<${className}> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param ${className?uncap_first} ${className?uncap_first}
     * @return ICustomPage<${className}>
     */
    ICustomPage<${className}> find${className}s(QueryRequest request, ${className} ${className?uncap_first});

    /**
     * 修改
     *
     * @param ${className?uncap_first} ${className?uncap_first}
     */
    ${className} save${className}(${className} ${className?uncap_first});

    /**
     * 启用
     *
     * @param ${className?uncap_first}Ids ${className?uncap_first}Ids
     */
    void enable${className}(String[] ${className?uncap_first}Ids);

    /**
    * 禁用
    *
    * @param ${className?uncap_first}Ids ${className?uncap_first}Ids
    */
    void disable${className}(String[] ${className?uncap_first}Ids);


    /**
    * 通过Id获取信息
    * @param ${className?uncap_first}Id
    * @return
    */
    ${className} find${className}ById(Long ${className?uncap_first}Id);
}
