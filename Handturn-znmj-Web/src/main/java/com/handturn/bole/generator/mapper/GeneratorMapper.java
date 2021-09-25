package com.handturn.bole.generator.mapper;


import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.generator.entity.Column;
import com.handturn.bole.generator.entity.Table;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Eric
 */
public interface GeneratorMapper {

    List<String> getDatabases(@Param("databaseType") String databaseType);

    ICustomPage<Table> getTables(CustomPage page, @Param("tableName") String tableName, @Param("databaseType") String databaseType, @Param("schemaName") String schemaName);

    List<Column> getColumns(@Param("databaseType") String databaseType, @Param("schemaName") String schemaName, @Param("tableName") String tableName);
}
