package ${basePackage}.${serviceImplPackage};

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.QueryRequest;
import ${basePackage}.${entityPackage}.${className};
import ${basePackage}.${mapperPackage}.${className}Mapper;
import ${basePackage}.${servicePackage}.I${className}Service;
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
 * ${tableComment} Service实现
 *
 * @author ${author}
 * @date ${date}
 */
@Service("${className}Service")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ${className}ServiceImpl extends ServiceImpl<${className}Mapper, ${className}> implements I${className}Service {

    @Override
    public ICustomPage<${className}> find${className}s(QueryRequest request, ${className} ${className?uncap_first}) {
        CustomPage<${className}> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, ${className?uncap_first});
    }

    @Override
    @Transactional
    public ${className} save${className}(${className} ${className?uncap_first}) {
        if(${className?uncap_first}.getId() == null){
            this.save(${className?uncap_first});
            return ${className?uncap_first};
        }else{
            ${className} ${className?uncap_first}Old = this.baseMapper.selectById(${className?uncap_first}.getId());
            CopyUtils.copyProperties(${className?uncap_first},${className?uncap_first}Old);
            this.updateById(${className?uncap_first}Old);
            return ${className?uncap_first}Old;
        }
    }

    @Override
    @Transactional
    public void enable${className}(String[] ${className?uncap_first}Ids) {
        Arrays.stream(${className?uncap_first}Ids).forEach(${className?uncap_first}Id -> {
            ${className} ${className?uncap_first} = this.baseMapper.selectById(${className?uncap_first}Id);
            ${className?uncap_first}.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(${className?uncap_first});
        });
	}

    @Override
    @Transactional
    public void disable${className}(String[] ${className?uncap_first}Ids) {
        Arrays.stream(${className?uncap_first}Ids).forEach(${className?uncap_first}Id -> {
            ${className} ${className?uncap_first} = this.baseMapper.selectById(${className?uncap_first}Id);
            ${className?uncap_first}.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(${className?uncap_first});
        });
    }

    @Override
    public ${className} find${className}ById(Long ${className?uncap_first}Id){
        return this.baseMapper.selectOne(new QueryWrapper<${className}>().lambda().eq(${className}::getId, ${className?uncap_first}Id));
    }
}
