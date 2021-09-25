package com.handturn.bole.common.ireport;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.view.AbstractView;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 * IReport打印显示PDF工具类
 * 重写AbstractView试图类
 * @author esqabc
 * @Create 2018-08-08 08:08:08
 * @Website www.esqabc.com
 * @WeChat 110
 */
public class IReportView extends AbstractView{
	private static final String CONTENT_TYPE = "application/pdf";

	 private String templatePath;
	 private List<JasperPrint> jasperPrintList;
	 /**
	  * 构造函数
	  * @param templatePath 模板路径
	  * @param exportFileName 导出名称
	  * @param jasperPrintList 多个模板集合
	  */
	 public IReportView(String templatePath,List<JasperPrint> jasperPrintList) {
	    this.templatePath = templatePath;
	    this.jasperPrintList = jasperPrintList;
	    setContentType(CONTENT_TYPE);
	 }
	 /**
	  * 重写返回视图
	  */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request,HttpServletResponse response){
		try{
			List<JasperPrint> jasperPrints =jasperPrintList;
			if(jasperPrints==null || jasperPrints.size()<1) {
				ServletOutputStream servletOutputStream = response.getOutputStream();
				File cfgFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX +templatePath);
				InputStream reportStream =new FileInputStream(cfgFile);
				JasperRunManager.runReportToPdfStream(reportStream,servletOutputStream,getParamsMap(map),getDataSource(map));
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/pdf");
				servletOutputStream.flush();
				servletOutputStream.close();
			}else {
			     ServletOutputStream servletOutputStream = response.getOutputStream();
				 JRPdfExporter exporter = new JRPdfExporter();
				 exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				 exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(servletOutputStream));
				 //exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,jasperPrints);
				 //exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
				 //出现乱码时请加上，下面的代码
				 //exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "GB2312");
				 exporter.exportReport();
				 response.setCharacterEncoding("UTF-8");
				 response.setContentType("application/pdf");

				 servletOutputStream.flush();
				 servletOutputStream.close();
			}
		}catch(Exception e){
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			try {
				response.getOutputStream().print(stringWriter.toString());
			} catch (Exception a) {
				a.printStackTrace();
			}
		}
		
	}
	 //获取表头数据
	 private Map<String, Object> getParamsMap(Map<String, Object> map) throws Exception {
	    Map<String, Object> params = new HashMap<>();
	    for (String key : map.keySet()) {
	        Object val = map.get(key);
	        if (val instanceof JRDataSource) {
	            continue;
	        } else {
	           params.put(key, val);
	        }
	     }
	     return params;
	 }
	 //获取列表数据源
	 private JRDataSource getDataSource(Map<String, Object> map) throws Exception {
	    for (String key : map.keySet()) {
	       Object val = map.get(key);
	       if (val instanceof JRDataSource) {
	           return (JRDataSource) map.get(key);
	       }
	    }
	    return new JREmptyDataSource();
	 }
}
