package com.ht.znmj.common.customPage;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 分页 CustomPage 对象接口
 *
 * @author hubin
 * @since 2018-06-09
 */
public interface ICustomPage<T> extends IPage<T> {

    Map<String, BigDecimal> getTotalRow();

    void setTotalRow(Map<String, BigDecimal> fieldsSumMap);

    List<String> getTotalRowNames();

    void setTotalRowNames(List<String> totalRowNames);
}
