package com.handturn.bole.sysconf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.*;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.entity.*;
import com.handturn.bole.sysconf.mapper.SysconfQueryReportMapper;
import com.handturn.bole.sysconf.service.*;
import com.handturn.bole.system.entity.SysResource;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统-报表 Service实现
 *
 * @author Eric
 * @date 2020-02-12 09:06:44
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysconfQueryReportServiceImpl extends ServiceImpl<SysconfQueryReportMapper, SysconfQueryReport> implements ISysconfQueryReportService {

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;

    @Autowired
    private ISysconfQueryReportColumnService sysconfQueryReportColumnService;

    @Autowired
    private ISysconfDictCodeService sysconfDictCodeService;

    /**
     * 查找所有的报表/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public CommonTree<SysconfQueryReport> findReportTree(SysconfQueryReport sysconfQueryReport){
        QueryWrapper<SysconfQueryReport> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(sysconfQueryReport.getReportName())) {
            queryWrapper.lambda().like(SysconfQueryReport::getReportName, sysconfQueryReport.getReportName());
        }
        queryWrapper.lambda().orderByAsc(SysconfQueryReport::getSortNo);
        List<SysconfQueryReport> sysconfQueryReports = this.baseMapper.selectList(queryWrapper);
        List<CommonTree<SysconfQueryReport>> trees = this.convertCommonTree(sysconfQueryReports,new HashSet<Long>());

        return CommonTree.buildTree(trees,false,null);
    }

    /**
     * 查找用户的报表/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public CommonTree<SysconfQueryReport> findUserReport(String rootReportId,String userCode){
        List<SysconfQueryReport> sysconfQueryReports = this.baseMapper.findUserReport(rootReportId,userCode);
        List<CommonTree<SysconfQueryReport>> trees = this.convertCommonTree(sysconfQueryReports,new HashSet<Long>());

        return CommonTree.buildTree(trees,false,null);
    }

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public CommonTree<SysconfQueryReport> findReportsByRootId(String rootReportId, Set<Long> permReportIds){
        QueryWrapper<SysconfQueryReport> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(rootReportId)) {
            queryWrapper.lambda().like(SysconfQueryReport::getRootReportId, rootReportId);
        }
        queryWrapper.lambda().orderByAsc(SysconfQueryReport::getSortNo);
        List<SysconfQueryReport> sysResources = this.baseMapper.selectList(queryWrapper);
        List<CommonTree<SysconfQueryReport>> trees = this.convertCommonTree(sysResources,permReportIds);

        return CommonTree.buildTree(trees,false,null);
    }

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public CommonTree<SysconfQueryReport> findReportsByRootIdOrgId(String rootReportId,String orgId, Set<Long> permReportIds){
        List<SysconfQueryReport> reports = this.baseMapper.findReportsByRootIdOrgId(rootReportId,orgId);
        List<CommonTree<SysconfQueryReport>> trees = this.convertCommonTree(reports,permReportIds);
        return CommonTree.buildTree(trees,false,null);
    }

    /**
     * 查找所有的菜单/按钮 （树形结构）
     *
     * @return MenuTree<Menu>
     */
    @Override
    public CommonTree<SysconfQueryReport> findReportsByRootIdOcId(String rootReportId,String ocId, Set<Long> permReportIds){
        List<SysconfQueryReport> reports = this.baseMapper.findReportsByRootIdOcId(rootReportId,ocId);
        List<CommonTree<SysconfQueryReport>> trees = this.convertCommonTree(reports,permReportIds);
        return CommonTree.buildTree(trees,false,null);
    }


    private List<CommonTree<SysconfQueryReport>> convertCommonTree(List<SysconfQueryReport> sysconfQueryReports, Set<Long> permReportIds) {
        List<CommonTree<SysconfQueryReport>> trees = new ArrayList<>();
        sysconfQueryReports.forEach(sysconfQueryReport -> {  //除去存在有子节点的资源
            permReportIds.remove(sysconfQueryReport.getParentReportId());
        });

        sysconfQueryReports.forEach(sysconfQueryReport -> {
            CommonTree<SysconfQueryReport> tree = new CommonTree<>();
            tree.setId(String.valueOf(sysconfQueryReport.getId()));
            tree.setParentId(String.valueOf(sysconfQueryReport.getParentReportId()));
            tree.setTitle(sysconfQueryReport.getReportName());
            sysconfQueryReport.setSqlScript("");  //清空，避免传输时间
            sysconfQueryReport.setSqlSortScript("");
            tree.setData(sysconfQueryReport);
            if(permReportIds.contains(sysconfQueryReport.getId())){
                tree.setChecked(true);
            }
            trees.add(tree);
        });
        return trees;
    }

    /**
     * 按照条件查询table展示
     * @param sysconfQueryReport
     * @return
     */
    @Override
    public List<SysconfQueryReport> findSysconfQueryReport4Table(SysconfQueryReport sysconfQueryReport){
        QueryWrapper<SysconfQueryReport> queryWrapper = new QueryWrapper<SysconfQueryReport>();
        if (sysconfQueryReport.getId() != null) {
            queryWrapper.lambda().and(wrapper -> wrapper.eq(SysconfQueryReport::getParentReportId, sysconfQueryReport.getId()).or().eq(SysconfQueryReport::getId, sysconfQueryReport.getId()));
        }
        if (StringUtils.isNotEmpty(sysconfQueryReport.getReportName())) {
            queryWrapper.lambda().like(SysconfQueryReport::getReportName, sysconfQueryReport.getReportName());
        }
        queryWrapper.lambda().orderByAsc(SysconfQueryReport::getParentReportId).orderByAsc(SysconfQueryReport::getSortNo);
        List<SysconfQueryReport> listSysResource = this.baseMapper.selectList(queryWrapper);
        List<SysconfQueryReport> menus = convertReport4List(listSysResource);
        return menus;
    }

    private List<SysconfQueryReport> convertReport4List(List<SysconfQueryReport> listSysconfQueryReport) {
        List<SysconfQueryReport> result = new ArrayList<SysconfQueryReport>();
        Map<Long, List<SysconfQueryReport>> parentIdMap = new HashMap<Long, List<SysconfQueryReport>>();
        listSysconfQueryReport.forEach(sysconfQueryReport ->{
            if (sysconfQueryReport.getParentReportId() == null || sysconfQueryReport.getParentReportId() == 0) {
                List<SysconfQueryReport> sysconfQueryReports = null;
                if (parentIdMap.get(0L) == null) {
                    sysconfQueryReports = new ArrayList<SysconfQueryReport>();
                } else {
                    sysconfQueryReports = parentIdMap.get(0L);
                }
                sysconfQueryReports.add(sysconfQueryReport);

                parentIdMap.put(0L, sysconfQueryReports);
            } else {
                List<SysconfQueryReport> sysconfQueryReports = null;
                if (parentIdMap.get(sysconfQueryReport.getParentReportId()) == null) {
                    sysconfQueryReports = new ArrayList<SysconfQueryReport>();
                } else {
                    sysconfQueryReports = parentIdMap.get(sysconfQueryReport.getParentReportId());
                }
                sysconfQueryReports.add(sysconfQueryReport);

                parentIdMap.put(sysconfQueryReport.getParentReportId(), sysconfQueryReports);
            }
        });

        result = iterationMunue(listSysconfQueryReport,parentIdMap,new ArrayList<SysconfQueryReport>(),new HashMap<>());

        return result;
    }

    private List<SysconfQueryReport> iterationMunue(List<SysconfQueryReport> sysconfQueryReports,Map<Long,List<SysconfQueryReport>> parentMenuK,List<SysconfQueryReport> result,Map<Long,Long> exitMap){
        if(sysconfQueryReports == null){
            return result;
        }
        for(SysconfQueryReport sysconfQueryReport : sysconfQueryReports){
            if(exitMap.get(sysconfQueryReport.getId()) != null){
                continue;
            }else{
                exitMap.put(sysconfQueryReport.getId(),sysconfQueryReport.getId());
            }
            result.add(sysconfQueryReport);
            if(parentMenuK.get(sysconfQueryReport.getId()) != null){
                result = iterationMunue(parentMenuK.get(sysconfQueryReport.getId()),parentMenuK,result,exitMap);
            }
        }
        return result;
    }

    @Override
    public ICustomPage<SysconfQueryReport> findSysconfQueryReports(QueryRequest request, SysconfQueryReport sysconfQueryReport) {
        CustomPage<SysconfQueryReport> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysconfQueryReport);
    }

    @Override
    @Transactional
    public SysconfQueryReport saveSysconfQueryReport(SysconfQueryReport sysconfQueryReport) {
        if(sysconfQueryReport.getReportNodeType().equals(QueryReportType.REPORT)
                && (StringUtils.isEmpty(sysconfQueryReport.getDataSource())
                || StringUtils.isEmpty(sysconfQueryReport.getSqlScript())
                ||StringUtils.isEmpty(sysconfQueryReport.getSqlSortScript()))){
            throw new FebsException("报表类型,SQL Script和数据源必填!");
        }

        if(sysconfQueryReport.getId() == null){
            //编码生成
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.SYSCONF_QUERY_REPORT_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);
            sysconfQueryReport.setReportCode(code);
            sysconfQueryReport.setStatus(BaseStatus.ENABLED);
            this.save(sysconfQueryReport);
            if(sysconfQueryReport.getParentReportId() == 0 && sysconfQueryReport.getReportNodeType().equals(QueryReportType.REPORT_ROOT)){
                sysconfQueryReport.setRootReportId(sysconfQueryReport.getId());
                this.updateById(sysconfQueryReport);
            }

            if(sysconfQueryReport.getReportNodeType().equals(QueryReportType.REPORT)){
                createReportQueryColumnBySql(sysconfQueryReport.getId(),sysconfQueryReport.getReportCode(), sysconfQueryReport.getDataSource(), sysconfQueryReport.getSqlScript());
            }
            return sysconfQueryReport;
        }else{
            SysconfQueryReport sysconfQueryReportOld = this.baseMapper.selectById(sysconfQueryReport.getId());
            CopyUtils.copyProperties(sysconfQueryReport,sysconfQueryReportOld);
            this.updateById(sysconfQueryReportOld);

            if(sysconfQueryReport.getReportNodeType().equals(QueryReportType.REPORT)){
                createReportQueryColumnBySql(sysconfQueryReportOld.getId(),sysconfQueryReport.getReportCode(), sysconfQueryReportOld.getDataSource(), sysconfQueryReportOld.getSqlScript());
            }

            return sysconfQueryReportOld;
        }
    }

    /**
     * 删除
     *
     * @param sysconfQueryReportIds sysconfQueryReportIds
     */
    @Override
    @Transactional
    public void deleteSysconfQueryReport(String[] sysconfQueryReportIds){
        this.delete(Arrays.asList(sysconfQueryReportIds));
    }

    private void delete(List<String> reportIds) {
        removeByIds(reportIds);

        QueryWrapper<SysconfQueryReportColumn> deleteColumnQueryWrapper = new QueryWrapper<SysconfQueryReportColumn>();
        deleteColumnQueryWrapper.lambda().in(SysconfQueryReportColumn::getReportId,reportIds);
        sysconfQueryReportColumnService.getBaseMapper().delete(deleteColumnQueryWrapper);

        LambdaQueryWrapper<SysconfQueryReport> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysconfQueryReport::getParentReportId, reportIds);
        List<SysconfQueryReport> reports = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(reports)) {
            List<String> reportIdList = new ArrayList<>();
            reports.forEach(m -> reportIdList.add(String.valueOf(m.getId())));
            this.delete(reportIdList);

            deleteColumnQueryWrapper.lambda().in(SysconfQueryReportColumn::getReportId,reportIdList);
            sysconfQueryReportColumnService.getBaseMapper().delete(deleteColumnQueryWrapper);
        }
    }

    @Override
    @Transactional
    public void enableSysconfQueryReport(String[] sysconfQueryReportIds) {
        Arrays.stream(sysconfQueryReportIds).forEach(sysconfQueryReportId -> {
            SysconfQueryReport sysconfQueryReport = this.baseMapper.selectById(sysconfQueryReportId);
            sysconfQueryReport.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysconfQueryReport);
        });
	}

    @Override
    @Transactional
    public void disableSysconfQueryReport(String[] sysconfQueryReportIds) {
        Arrays.stream(sysconfQueryReportIds).forEach(sysconfQueryReportId -> {
            SysconfQueryReport sysconfQueryReport = this.baseMapper.selectById(sysconfQueryReportId);
            sysconfQueryReport.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysconfQueryReport);
        });
    }

    @Override
    public SysconfQueryReport findSysconfQueryReportById(Long sysconfQueryReportId){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfQueryReport>().lambda().eq(SysconfQueryReport::getId, sysconfQueryReportId));
    }

    /**
     * 查找所有根节点
     *
     * @return 用户菜单集合
     */
    @Override
    public List<SysconfQueryReport> findAllRootSysReports(){
        List<SysconfQueryReport> rootModule = this.baseMapper.findAllRootReports();
        return rootModule;
    }

    /**
     * 通过根节点查询报表
     * @param orgId
     * @return
     */
    @Override
    public List<SysconfQueryReport> findAllRootSysReportsByOrgId(Long orgId){
        List<SysconfQueryReport> rootModule = this.baseMapper.findAllRootReportsByOrgId(String.valueOf(orgId));
        return rootModule;
    }

    /**
     * 查找所有根节点
     *
     * @return 用户菜单集合
     */
    @Override
    public List<SysconfQueryReport> findAllRootSysReportsByOcId(Long ocId){
        List<SysconfQueryReport> rootModule = this.baseMapper.findAllRootReportsByOcId(String.valueOf(ocId));
        return rootModule;
    }

    /**
     * 通过根节点查询报表
     * @param rootReportId
     * @return
     */
    @Override
    public List<SysconfQueryReport> findSysconfReportsByRootId(String rootReportId){
        QueryWrapper<SysconfQueryReport> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(rootReportId)) {
            queryWrapper.lambda().like(SysconfQueryReport::getRootReportId, rootReportId);
        }
        queryWrapper.lambda().orderByAsc(SysconfQueryReport::getSortNo);
        List<SysconfQueryReport> sysResources = this.baseMapper.selectList(queryWrapper);
        return sysResources;
    }


    /**
     * 根据SQL语句创建报表查询字段明细
     * @param reportId
     * @param sql
     */
    @Transactional
    public void createReportQueryColumnBySql(Long reportId,String reportCode, String dataSource, String sql ){
        //中文逗号装换
        sql = sql.replace("，", ",");
        //根据SQL获取查询列表
        Map<String,String> queryColumns = getSqlColumns(sql,dataSource);
        if(queryColumns.size() > 0){

            QueryWrapper<SysconfQueryReportColumn> queryWrapper = new  QueryWrapper<SysconfQueryReportColumn>();
            queryWrapper.lambda().eq(SysconfQueryReportColumn::getReportId,reportId);
            List<SysconfQueryReportColumn>  sysconfQueryReportColumns = sysconfQueryReportColumnService.getBaseMapper().selectList(queryWrapper);
            Map<String,SysconfQueryReportColumn> columnMap = new HashMap<String,SysconfQueryReportColumn>();
            sysconfQueryReportColumns.forEach(sysconfQueryReportColumn ->{
                columnMap.put(sysconfQueryReportColumn.getColumnCode(),sysconfQueryReportColumn);
            });

            queryColumns.keySet().forEach(key ->{
                String type = queryColumns.get(key);
                String sequenceNumberStr = key.substring(0,key.indexOf("_"));
                Integer sequenceNumber = Integer.valueOf(sequenceNumberStr);
                String column = key.substring(key.indexOf("_")+1);
                String columnType = "";
                if("DATETIME".equalsIgnoreCase(type) || "TIMESTAMP".equalsIgnoreCase(type)){
                    columnType = QueryReportColumnType.DATE;
                }else if("BIGINT".equalsIgnoreCase(type) || "INT".equalsIgnoreCase(type) || "SMALLINT".equalsIgnoreCase(type)){
                    columnType = QueryReportColumnType.INTEGER;
                }else{
                    columnType = QueryReportColumnType.STRING;
                }

                if(columnMap.get(column) == null){
                    //查询字段不存在原查询字段列表中则新建
                    SysconfQueryReportColumn queryColumn = new SysconfQueryReportColumn();
                    queryColumn.setReportCode(reportCode);
                    queryColumn.setColumnCode(column);
                    queryColumn.setColumnName(column);
                    queryColumn.setColumnSeqNum(sequenceNumber);
                    queryColumn.setReportId(reportId);
                    queryColumn.setColumnType(columnType);
                    queryColumn.setIsQuery(BaseBoolean.TRUE);
                    queryColumn.setColumnWidth("110");
                    queryColumn.setQueryMethod(QueryReportColumnQueryMethod.EQUALS);
                    sysconfQueryReportColumnService.getBaseMapper().insert(queryColumn);
                }else{
                    //存在则忽略
                    SysconfQueryReportColumn queryColumn = columnMap.get(column);
                    queryColumn.setColumnCode(column);
                    queryColumn.setColumnSeqNum(sequenceNumber);
                    sysconfQueryReportColumnService.getBaseMapper().updateById(queryColumn);
                    columnMap.remove(column);
                }
            });

            if(columnMap.size() > 0){
                columnMap.keySet().forEach(column ->{
                    SysconfQueryReportColumn sysconfQueryReportColumn = columnMap.get(column) ;
                    sysconfQueryReportColumnService.getBaseMapper().deleteById(sysconfQueryReportColumn);
                });
            }
        }
    }

    /**
     * 获取SQL语句的查询列
     * @param sql
     * @return
     */
    public Map<String,String> getSqlColumns(String sql, String connName) {
        Map<String,String> columns = new HashMap<String,String>();
        DataSource dataSource = dynamicRoutingDataSource.getDataSource(connName);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn =dataSource.getConnection();
            String newSql = "SELECT * FROM ( "+sql +")T WHERE 1<>1";
            pstmt= conn.prepareStatement(newSql);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int j = 0;
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                String columnLabel = rsmd.getColumnLabel(i + 1);
                if(!StringUtils.isBlank(columnLabel) && !columns.containsKey(columnLabel)){
                    j++;
                    String type = rsmd.getColumnTypeName(i+ 1);
                    columns.put(j+"_"+columnLabel,type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FebsException("SQL语句解析出错！");
        } finally {
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    throw new FebsException("SQL语句解析出错！");
                }
            }
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new FebsException("SQL语句解析出错！");
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new FebsException("SQL语句解析出错！");
                }
            }
        }
        return columns;
    }


    /**
     * 报表查询
     * @param request
     * @param reportId
     * @return
     */
    @Override
    public ICustomPage reportList(String reportId, QueryRequest queryRequest, HttpServletRequest request, HttpServletResponse response,Map<String,String> exitQueryParams){
        SysconfQueryReport queryReport = findSysconfQueryReportById(Long.valueOf(reportId));
        if(queryReport == null){
            throw new FebsException("报表模板未找到");
        }
        String sql = queryReport.getSqlScript();
        String sortSql = queryReport.getSqlSortScript();

        Map<String,Object> queryParameters_method = new HashMap<String,Object>();
        Map<String,Object> queryParameters = new HashMap<String,Object>();
        Map<String,Object> queryParameters_date = new HashMap<String,Object>();
        Set<String> queryTotalRowFields = new HashSet<String>();

        StringBuffer dateSqlStr = new StringBuffer();
        Map<String,SysconfQueryReportColumn> columnMap = sysconfQueryReportColumnService.findColumnsMapByReportId(Long.valueOf(reportId));
        columnMap.keySet().forEach(columnCode ->{
            SysconfQueryReportColumn sysconfQueryReportColumn = columnMap.get(columnCode);
            if(sysconfQueryReportColumn != null){
                if(sysconfQueryReportColumn.getIsTotalRowField().equals(BaseBoolean.TRUE)) {
                    queryTotalRowFields.add(columnCode);
                }

                if(sysconfQueryReportColumn.getIsQuery().equals(BaseBoolean.TRUE)){
                    if(!StringUtils.isEmpty(request.getParameter(columnCode))){
                        queryParameters_method.put(columnCode,sysconfQueryReportColumn.getQueryMethod());
                        if(sysconfQueryReportColumn.getQueryMethod().equals(QueryReportColumnQueryMethod.IN)){
                            String[] conditionStrs = request.getParameter(columnCode).split("\n|\r|,|，");
                            StringBuffer conditionStr = new StringBuffer();
                            Arrays.stream(conditionStrs).forEach(condition ->{
                                conditionStr.append(",'"+condition+"'");
                            });
                            queryParameters.put(columnCode, conditionStr.substring(2,conditionStr.length()-1));
                        }else{
                            queryParameters.put(columnCode, request.getParameter(columnCode));
                        }
                    }

                    //系统数据限制
                    if(!StringUtils.isEmpty(exitQueryParams.get(columnCode))){
                        queryParameters_method.put(columnCode,sysconfQueryReportColumn.getQueryMethod());
                        if(sysconfQueryReportColumn.getQueryMethod().equals(QueryReportColumnQueryMethod.IN)){
                            String[] conditionStrs = exitQueryParams.get(columnCode).split("\n|\r|,|，");
                            StringBuffer conditionStr = new StringBuffer();
                            Arrays.stream(conditionStrs).forEach(condition ->{
                                conditionStr.append(",'"+condition+"'");
                            });
                            queryParameters.put(columnCode, conditionStr.substring(2,conditionStr.length()-1));
                        }else{
                            queryParameters.put(columnCode, exitQueryParams.get(columnCode));
                        }
                    }

                    if(sysconfQueryReportColumn.getColumnType().equals(QueryReportColumnType.DATE)){
                        if(!StringUtils.isEmpty(request.getParameter("START_"+columnCode))){
                            queryParameters_date.put("START_"+columnCode, request.getParameter("START_"+columnCode).trim());
                            dateSqlStr.append(" AND "+columnCode+" >= '" +request.getParameter("START_"+columnCode).trim()+"'");
                        }
                        if(!StringUtils.isEmpty(request.getParameter("END_"+columnCode))){
                            queryParameters_date.put("END_"+columnCode, request.getParameter("END_"+columnCode).trim());
                            dateSqlStr.append(" AND "+columnCode+" <= '" +request.getParameter("END_"+columnCode).trim()+"'");
                        }
                    }
                }
            }
        });

        sql = StringEscapeUtils.unescapeXml(sql);
        sql = replaceParamSql(sql,queryParameters);
        sql = replaceParamSql(sql,queryParameters_date);

        Map<String,String> exitSqlMap = new HashMap<String,String>();
        exitSqlMap.put("exitSql",dateSqlStr.toString());
        queryParameters.keySet().forEach(queryParameter ->{
            if(queryParameters_method.get(queryParameter) == null){
                exitSqlMap.put("exitSql",exitSqlMap.get("exitSql")+" AND "+queryParameter+ " = '"+queryParameters.get(queryParameter)+"' ");
            }else{
                if(queryParameters_method.get(queryParameter).equals(QueryReportColumnQueryMethod.EQUALS)){
                    exitSqlMap.put("exitSql",exitSqlMap.get("exitSql")+" AND "+queryParameter+ " = '"+queryParameters.get(queryParameter)+"' ");
                }
                if(queryParameters_method.get(queryParameter).equals(QueryReportColumnQueryMethod.IN)){
                    exitSqlMap.put("exitSql",exitSqlMap.get("exitSql")+" AND "+queryParameter+ " IN ('"+queryParameters.get(queryParameter)+"') ");
                }
                if(queryParameters_method.get(queryParameter).equals(QueryReportColumnQueryMethod.LIKE)){
                    exitSqlMap.put("exitSql",exitSqlMap.get("exitSql")+" AND "+queryParameter+ " LIKE '%"+queryParameters.get(queryParameter)+"%' ");
                }
            }
        });

        //映射为int型
        int pageSize = queryRequest.getPageSize();
        int pageNum = queryRequest.getPageNum();
        int startRecord = pageSize*(pageNum-1);
        int recordCount = 0;
        int pageCount = 0;

        DataSource dataSource = dynamicRoutingDataSource.getDataSource(queryReport.getDataSource());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //汇总列
        Map<String,BigDecimal> totalRowMap = new HashMap<String,BigDecimal>();
        if (queryTotalRowFields.size() > 0){
            StringBuffer totalRowSql = new StringBuffer();
            totalRowSql.append("SELECT ");

            StringBuffer totalRowSelectSql = new StringBuffer();
            StringBuffer finalTotalRowSelectSql = totalRowSelectSql;
            queryTotalRowFields.forEach(queryTotalRowField ->{
                finalTotalRowSelectSql.append("SUM("+queryTotalRowField+") AS "+queryTotalRowField+",");
            });
            totalRowSelectSql = new StringBuffer(finalTotalRowSelectSql.substring(0,finalTotalRowSelectSql.length()-1));
            totalRowSql.append(totalRowSelectSql).append(" FROM ( "+sql +") T WHERE 1 = 1 ");
            totalRowSql.append(exitSqlMap.get("exitSql"));

            List list = jdbcTemplate.queryForList(totalRowSql.toString());
            totalRowMap = (Map)list.get(0);
        }


        //总条数
        StringBuffer countSql = new StringBuffer();
        countSql.append("SELECT count(*) FROM ( "+sql +") T WHERE 1 = 1 ");
        countSql.append(exitSqlMap.get("exitSql"));
        List list = jdbcTemplate.queryForList(countSql.toString());
        Map objarr = (Map)list.get(0);
        recordCount = Integer.valueOf(objarr.get("count(*)").toString());
        if(recordCount == 0){
            pageCount = 1;
        }else{
            pageCount = recordCount/pageSize+(recordCount%pageSize>0?1:0);
        }
        //如果当前页超过了总页数，则重新设置当前页及开始记录
        if(pageNum>pageCount && pageNum != 1){
            pageNum = pageCount;
            startRecord = pageSize*(pageNum-1);
        }

        StringBuffer newSql = new StringBuffer();
        newSql.append("SELECT * FROM ( "+sql +") T WHERE 1 = 1 ");
        newSql.append(exitSqlMap.get("exitSql")).append(" "+sortSql);
        newSql.append(" limit ").append(startRecord).append(", ").append(pageSize);

        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(newSql.toString());

        ICustomPage ipage = new CustomPage();
        ipage.setRecords(dataList);
        ipage.setCurrent(pageNum);
        ipage.setPages(recordCount/pageSize);
        ipage.setTotal(recordCount);
        ipage.setSize(pageSize);
        ipage.setTotalRow(totalRowMap);
        return ipage;
    }


    /**
     * 报表导出
     * @param request
     * @param reportId
     * @return
     */
    public void reportExport(String reportId, QueryRequest queryRequest, HttpServletRequest request, HttpServletResponse response,Map<String,String> exitQueryParams){
        //报表参数处理
        SysconfQueryReport queryReport = findSysconfQueryReportById(Long.valueOf(reportId));
        if(queryReport == null){
            throw new FebsException("报表模板未找到");
        }
        String sql = queryReport.getSqlScript();
        String sortSql = queryReport.getSqlSortScript();

        Map<String,Object> queryParameters_method = new HashMap<String,Object>();
        Map<String,Object> queryParameters = new HashMap<String,Object>();
        Map<String,Object> queryParameters_date = new HashMap<String,Object>();

        StringBuffer dateSqlStr = new StringBuffer();
        Map<String,SysconfQueryReportColumn> columnMap = sysconfQueryReportColumnService.findColumnsMapByReportId(Long.valueOf(reportId));
        columnMap.keySet().forEach(columnCode ->{
            SysconfQueryReportColumn sysconfQueryReportColumn = columnMap.get(columnCode);
            if(sysconfQueryReportColumn != null){
                if(sysconfQueryReportColumn.getIsQuery().equals(BaseBoolean.TRUE)){
                    if(!StringUtils.isEmpty(request.getParameter(columnCode))){
                        queryParameters_method.put(columnCode,sysconfQueryReportColumn.getQueryMethod());
                        if(sysconfQueryReportColumn.getQueryMethod().equals(QueryReportColumnQueryMethod.IN)){
                            String[] conditionStrs = request.getParameter(columnCode).split("\n|\r|,|，");
                            StringBuffer conditionStr = new StringBuffer();
                            Arrays.stream(conditionStrs).forEach(condition ->{
                                conditionStr.append(",'"+condition+"'");
                            });
                            queryParameters.put(columnCode, conditionStr.substring(2,conditionStr.length()-1));
                        }else{
                            queryParameters.put(columnCode, request.getParameter(columnCode));
                        }
                    }

                    //系统数据限制
                    if(!StringUtils.isEmpty(exitQueryParams.get(columnCode))){
                        queryParameters_method.put(columnCode,sysconfQueryReportColumn.getQueryMethod());
                        if(sysconfQueryReportColumn.getQueryMethod().equals(QueryReportColumnQueryMethod.IN)){
                            String[] conditionStrs = exitQueryParams.get(columnCode).split("\n|\r|,|，");
                            StringBuffer conditionStr = new StringBuffer();
                            Arrays.stream(conditionStrs).forEach(condition ->{
                                conditionStr.append(",'"+condition+"'");
                            });
                            queryParameters.put(columnCode, conditionStr.substring(2,conditionStr.length()-1));
                        }else{
                            queryParameters.put(columnCode, exitQueryParams.get(columnCode));
                        }
                    }

                    if(sysconfQueryReportColumn.getColumnType().equals(QueryReportColumnType.DATE)){
                        if(!StringUtils.isEmpty(request.getParameter("START_"+columnCode))){
                            queryParameters_date.put("START_"+columnCode, request.getParameter("START_"+columnCode).trim());
                            dateSqlStr.append(" AND "+columnCode+" >= '" +request.getParameter("START_"+columnCode).trim()+"'");
                        }
                        if(!StringUtils.isEmpty(request.getParameter("END_"+columnCode))){
                            queryParameters_date.put("END_"+columnCode, request.getParameter("END_"+columnCode).trim());
                            dateSqlStr.append(" AND "+columnCode+" <= '" +request.getParameter("END_"+columnCode).trim()+"'");
                        }
                    }
                }
            }
        });

        sql = StringEscapeUtils.unescapeXml(sql);
        sql = replaceParamSql(sql,queryParameters);
        sql = replaceParamSql(sql,queryParameters_date);

        Map<String,String> exitSqlMap = new HashMap<String,String>();
        exitSqlMap.put("exitSql",dateSqlStr.toString());
        queryParameters.keySet().forEach(queryParameter ->{
            if(queryParameters_method.get(queryParameter) == null){
                exitSqlMap.put("exitSql",exitSqlMap.get("exitSql")+" AND "+queryParameter+ " = '"+queryParameters.get(queryParameter)+"' ");
            }else{
                if(queryParameters_method.get(queryParameter).equals(QueryReportColumnQueryMethod.EQUALS)){
                    exitSqlMap.put("exitSql",exitSqlMap.get("exitSql")+" AND "+queryParameter+ " = '"+queryParameters.get(queryParameter)+"' ");
                }
                if(queryParameters_method.get(queryParameter).equals(QueryReportColumnQueryMethod.IN)){
                    exitSqlMap.put("exitSql",exitSqlMap.get("exitSql")+" AND "+queryParameter+ " IN ('"+queryParameters.get(queryParameter)+"') ");
                }
                if(queryParameters_method.get(queryParameter).equals(QueryReportColumnQueryMethod.LIKE)){
                    exitSqlMap.put("exitSql",exitSqlMap.get("exitSql")+" AND "+queryParameter+ " LIKE '%"+queryParameters.get(queryParameter)+"%' ");
                }
            }
        });

        /**
         * 数据字典和集合类型
         */
        List<SysconfQueryReportColumn> columnList = sysconfQueryReportColumnService.findColumnsByReportId(Long.valueOf(reportId));
        Map<String,Map<String,Object>> columnValueMap = new HashMap<>();
        columnList.forEach(column ->{
            if(column.getColumnType().equals(QueryReportColumnType.DATA_DICT)){
                Map<String,Object> dataDictMap = sysconfDictCodeService.getDictMapByTypeCode(column.getColumnInitValue());
                columnValueMap.put(column.getColumnCode(),dataDictMap);
            }
            if(column.getColumnType().equals(QueryReportColumnType.LIST)){
                try {
                    Map<String,Object> stringToMap = JSONObject.parseObject(column.getColumnInitValue());
                    columnValueMap.put(column.getColumnCode(),stringToMap);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //映射为int型
        int pageSize = queryRequest.getPageSize();
        int pageNum = queryRequest.getPageNum();
        int startRecord = pageSize*(pageNum-1);

        DataSource dataSource = dynamicRoutingDataSource.getDataSource(queryReport.getDataSource());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        try {
             int NUM_OF_SHEETS = 0;
             int NUM_OF_ROWS = 0;

            SXSSFWorkbook wb = null;
            OutputStream os = null;
            try {
                wb = new SXSSFWorkbook();
                wb.setCompressTempFiles(true); //压缩临时文件，很重要，否则磁盘很快就会被写满

                Sheet sh = null;
                int rowNum = 0;
                Row row_header = null;
                int cellNum_header = 0;

                while(true){
                    StringBuffer newSql = new StringBuffer();
                    newSql.append("SELECT * FROM ( "+sql +") T WHERE 1 = 1 ");
                    newSql.append(exitSqlMap.get("exitSql")).append(" "+sortSql);
                    newSql.append(" limit ").append(startRecord).append(", ").append(pageSize);
                    List<Map<String, Object>> dataList = jdbcTemplate.queryForList(newSql.toString());
                    if(dataList.size() > 0){
                        for(Map<String, Object> data : dataList){
                            if (NUM_OF_ROWS % 60000 == 0) {
                                NUM_OF_SHEETS ++;
                                sh = wb.createSheet("sheet " + NUM_OF_SHEETS);
                                rowNum = 0;

                                //写头信息
                                row_header = sh.createRow(rowNum);
                                cellNum_header = 0;
                                for(SysconfQueryReportColumn sysconfQueryReportColumn : columnList){
                                    Cell cell = row_header.createCell(cellNum_header);
                                    cell.setCellValue(sysconfQueryReportColumn.getColumnName());
                                    cellNum_header ++;
                                }
                            }

                            rowNum++;
                            Row row = sh.createRow(rowNum);

                            int cellNum = 0;
                            for(SysconfQueryReportColumn sysconfQueryReportColumn : columnList){
                                Cell cell = row.createCell(cellNum);
                                //数据字典和集合类型处理
                                if(sysconfQueryReportColumn.getColumnType().equals(QueryReportColumnType.DATA_DICT)
                                        || sysconfQueryReportColumn.getColumnType().equals(QueryReportColumnType.DATA_DICT)){
                                    if(data.get(sysconfQueryReportColumn.getColumnCode()) == null){
                                        cell.setCellValue("");
                                    }else{
                                        if(columnValueMap.get(sysconfQueryReportColumn.getColumnCode()) == null){
                                            cell.setCellValue(data.get(sysconfQueryReportColumn.getColumnCode()).toString());
                                        }else{
                                            if(columnValueMap.get(sysconfQueryReportColumn.getColumnCode()).get(data.get(sysconfQueryReportColumn.getColumnCode()).toString()) == null){
                                                cell.setCellValue(data.get(sysconfQueryReportColumn.getColumnCode()).toString());
                                            }else{
                                                cell.setCellValue(columnValueMap.get(sysconfQueryReportColumn.getColumnCode()).get(data.get(sysconfQueryReportColumn.getColumnCode()).toString()).toString());
                                            }
                                        }
                                    }

                                }else{
                                    cell.setCellValue(data.get(sysconfQueryReportColumn.getColumnCode()) == null?"":data.get(sysconfQueryReportColumn.getColumnCode()).toString());
                                }
                                cellNum ++;
                            }

                            NUM_OF_ROWS ++;
                        }
                    }else{
                        break;
                    }

                    startRecord += pageSize;
                }

                response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode("导出_" + NUM_OF_SHEETS + ".xlsx", "utf-8"));
                response.setContentType("multipart/form-data");
                response.setCharacterEncoding("utf-8");

                os = response.getOutputStream();
                wb.write(os);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (wb != null) {
                    wb.dispose();// 删除临时文件，很重要，否则磁盘可能会被写满
                }
                if(os != null){
                    os.flush();
                    os.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new FebsException("导出出错!");
        }
    }

    /**
     * 替换带参数的sql
     */
    public String replaceParamSql(String paramSql, Map<String, Object> queryParameters){
        //获取注释SQL语句
        Pattern p = Pattern.compile("/\\*.*?\\*/");
        Matcher m = p.matcher(paramSql);
        ArrayList<String> strs = new ArrayList<String>();
        while (m.find()) {
            if(!strs.contains(m.group(0))){
                strs.add(m.group(0));
            }
        }

        //去注释，参数替换
        for(String key : queryParameters.keySet()){
            if(key.endsWith("_format") || "".equals(MapUtils.getString(queryParameters, key, "").trim()) || "null".equals(MapUtils.getString(queryParameters, key, "").trim())){
                continue;
            }else{
                Object value = queryParameters.get(key);
                String field = "";
                if(key.startsWith("ge_")){
                    field = key.replaceFirst("ge_", "START_");
                }else if(key.startsWith("le_")){
                    field = key.replaceFirst("le_", "END_");
                }else{
                   /* field = key.substring(key.indexOf("_")+1, key.length());*/
                    field = key;
                }
                String fieldParam = "${"+field+"}";
                for (String s : strs){
                    if(s.contains(fieldParam)){
                        String repalceStr = s.replace("/*", " ");
                        repalceStr = repalceStr.replace("*/", " ");
                        repalceStr = repalceStr.replace(fieldParam, "'"+value+"'");
                        paramSql = paramSql.replace(s, repalceStr);
                    }
                }
            }
        }
        return paramSql;
    }

    /**
     * 将ResultSet结果集中的记录映射到Map对象中.
     *
     * @param fieldClassName 是JDBC API中的类型名称,
     * @param fieldName 是字段名，
     * @param rs 是一个ResultSet查询结果集,
     * @param fieldValue Map对象,用于存贮一条记录.
     * @throws SQLException
     */
    private void _recordMappingToMap(String fieldClassName, String fieldName, ResultSet rs, Map fieldValue)
            throws SQLException
    {
        fieldName = fieldName.toLowerCase();
        // 优先规则：常用类型靠前
        if (fieldClassName.equals("java.lang.String"))
        {
            String s = rs.getString(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.lang.Integer"))
        {
            int s = rs.getInt(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);// 早期jdk需要包装，jdk1.5后不需要包装
            }
        }
        else if (fieldClassName.equals("java.lang.Long"))
        {
            long s = rs.getLong(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.lang.Boolean"))
        {
            boolean s = rs.getBoolean(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.lang.Short"))
        {
            short s = rs.getShort(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.lang.Float"))
        {
            float s = rs.getFloat(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.lang.Double"))
        {
            double s = rs.getDouble(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.sql.Timestamp"))
        {
            java.sql.Timestamp s = rs.getTimestamp(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.sql.Date") || fieldClassName.equals("java.util.Date"))
        {
            java.util.Date s = rs.getDate(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.sql.Time"))
        {
            java.sql.Time s = rs.getTime(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.lang.Byte"))
        {
            byte s = rs.getByte(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, new Byte(s));
            }
        }
        else if (fieldClassName.equals("[B") || fieldClassName.equals("byte[]"))
        {
            // byte[]出现在SQL Server中
            byte[] s = rs.getBytes(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.math.BigDecimal"))
        {
            BigDecimal s = rs.getBigDecimal(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.lang.Object") || fieldClassName.equals("oracle.sql.STRUCT"))
        {
            Object s = rs.getObject(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.sql.Array") || fieldClassName.equals("oracle.sql.ARRAY"))
        {
            java.sql.Array s = rs.getArray(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.sql.Clob"))
        {
            java.sql.Clob s = rs.getClob(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else if (fieldClassName.equals("java.sql.Blob"))
        {
            java.sql.Blob s = rs.getBlob(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
        else
        {// 对于其它任何未知类型的处理
            Object s = rs.getObject(fieldName);
            if (rs.wasNull())
            {
                fieldValue.put(fieldName, null);
            }
            else
            {
                fieldValue.put(fieldName, s);
            }
        }
    }
}
