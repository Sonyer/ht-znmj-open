package com.handturn.bole.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 导出反射调用Service
 */
@Slf4j
public class ExportReflectTo<T> {
    public static void exportRun(String queryData, String sortObj, String cols,Object queryObject,
                            HttpServletRequest request, HttpServletResponse response,
                            String beanName, String methodName,Class<?>... parameterTypes){
        try{
            //反射构造
            Object target = SpringContextUtil.getBean(beanName);
            Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);

            QueryRequest queryRequest = JSONObject.parseObject(sortObj, QueryRequest.class);
            JSONArray jsonObj_cols = JSONObject.parseArray(cols);

            List<String> fileds = new ArrayList<String>();
            Map<String,String> titleMap = new HashMap<String,String>();
            Map<String,Map<String,String>> filedDataMap = new HashMap<String,Map<String,String>>();
            jsonObj_cols.forEach(jsonObj_col ->{
                JSONObject jsonObj_colObj = JSONObject.parseObject(jsonObj_col.toString());
                String filed = jsonObj_colObj.get("field") == null?"":jsonObj_colObj.get("field").toString();
                String title = jsonObj_colObj.get("title") == null?"":jsonObj_colObj.get("title").toString();
                String dataMapJsonStr = jsonObj_colObj.get("dataMap") == null?"":jsonObj_colObj.get("dataMap").toString();

                if(!StringUtils.isEmpty(filed)){
                    fileds.add(filed);
                    titleMap.put(filed,title);
                    if(!StringUtils.isEmpty(dataMapJsonStr)){
                        filedDataMap.put(filed,JSONObject.parseObject(dataMapJsonStr,Map.class));
                    }
                }
            });

            queryRequest.setPageNum(1);
            queryRequest.setPageSize(1000);

            Integer pageIndex = 0;

            //映射为int型
            int pageSize = queryRequest.getPageSize();
            int pageNum = queryRequest.getPageNum();
            int startRecord = pageSize*(pageNum-1);

            //构建excel
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
                        pageIndex ++;

                        queryRequest.setPageNum(pageIndex);

                        //ICustomPage<BasItem> pageInfo = this.basItemService.findBasItems(queryRequest, basItem);
                        Object[] params = {queryRequest,queryObject}; //service定义，需要queryRequest前，queryObject后
                        ICustomPage pageInfo = (ICustomPage)exportReflectTo(target,method,params);

                        if(pageIndex == 1){
                            if(pageInfo.getTotal() <= 0){
                                response.setStatus(332);
                                break;
                            }
                            if(pageInfo.getTotal() > 40000){
                                response.setStatus(333);
                                break;
                            }
                        }else{
                            if(pageInfo.getTotal() <= 0){
                                break;
                            }
                        }

                        if(pageInfo.getRecords().size() > 0){
                            for(Object data : pageInfo.getRecords()){
                                Map<String, Object> culumnValueMap = MapTools.beanToMap(data);

                                if (NUM_OF_ROWS % 60000 == 0) {
                                    NUM_OF_SHEETS ++;
                                    sh = wb.createSheet("sheet " + NUM_OF_SHEETS);
                                    rowNum = 0;

                                    //写头信息
                                    row_header = sh.createRow(rowNum);
                                    cellNum_header = 0;
                                    for(String filedCode : fileds){
                                        Cell cell = row_header.createCell(cellNum_header);
                                        cell.setCellValue(titleMap.get(filedCode));
                                        cellNum_header ++;
                                    }
                                }

                                rowNum++;
                                Row row = sh.createRow(rowNum);

                                int cellNum = 0;
                                for(String filedCode : fileds){
                                    Cell cell = row.createCell(cellNum);
                                    //数据字典和集合类型处理
                                    if(filedDataMap.get(filedCode) != null){
                                        if(culumnValueMap.get(filedCode) == null){
                                            cell.setCellValue("");
                                        }else{
                                            if(filedDataMap.get(filedCode).get(culumnValueMap.get(filedCode).toString()) == null){
                                                cell.setCellValue(culumnValueMap.get(filedCode).toString());
                                            }else{
                                                cell.setCellValue(filedDataMap.get(filedCode).get(culumnValueMap.get(filedCode).toString()).toString());
                                            }
                                        }

                                    }else{
                                        if(culumnValueMap.get(filedCode) instanceof Date){
                                            String dateStr = DateUtil.getDateFormat((Date)culumnValueMap.get(filedCode), DateUtil.FULL_TIME_SPLIT_PATTERN);
                                            cell.setCellValue(dateStr.replaceAll(" 00:00:00",""));
                                        }else {
                                            cell.setCellValue(culumnValueMap.get(filedCode) == null ? "" : culumnValueMap.get(filedCode).toString());
                                        }
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
                response.setStatus(333);
            }
        }catch(Exception e){
            e.printStackTrace();
            response.setStatus(333);  //自定义编码-332:没有可导出数据 333:下载错误  334-数据不能超过40000条
        }
    }

    public static Object exportReflectTo( Object target,Method method, Object... args){
        try {
            ReflectionUtils.makeAccessible(method);

            Object result = null;
            if (args != null && args.length > 0) {
                result = method.invoke(target, args);
            } else {
                result = method.invoke(target);
            }
            return result;
        } catch (Exception e) {
            log.error("导出执行失败:"+target.getClass().getName()+"-"+method.getName(), e);
            throw new FebsException("导出执行失败!");
        }
    }
}
