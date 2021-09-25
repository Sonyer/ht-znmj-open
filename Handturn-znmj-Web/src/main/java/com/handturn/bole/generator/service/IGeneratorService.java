package com.handturn.bole.generator.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.generator.entity.Column;
import com.handturn.bole.generator.entity.Table;

import java.util.List;

/**
 * @author Eric
 */
public interface IGeneratorService {

    List<String> getDatabases(String databaseType);

    ICustomPage<Table> getTables(String tableName, QueryRequest request, String databaseType, String schemaName);

    List<Column> getColumns(String databaseType, String schemaName, String tableName);
}
