package com.handturn.bole.sysconf.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.common.utils.MD5Util;import lombok.extern.slf4j.Slf4j;
import com.handturn.bole.system.entity.SysUser;
import com.handturn.bole.system.service.ISysUserService;
import com.handturn.bole.sysconf.entity.SysconfQueryReportColumn;
import com.handturn.bole.sysconf.service.ISysconfQueryReportColumnService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 系统-报表查询列 Controller
 *
 * @author Eric
 * @date 2020-02-12 09:14:32
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sysconf/sysconfQueryReportColumn")
public class SysconfQueryReportColumnController extends BaseController {

    @Autowired
    private ISysconfQueryReportColumnService sysconfQueryReportColumnService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("sysconfQueryReport:view")
    public FebsResponse sysconfQueryReportColumnList(QueryRequest request, SysconfQueryReportColumn sysconfQueryReportColumn) {
        Map<String, Object> dataTable = getDataTable(this.sysconfQueryReportColumnService.findSysconfQueryReportColumns(request, sysconfQueryReportColumn));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑报表字段/按钮", exceptionMessage = "编辑报表字段失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"sysconfQueryReport:add","sysconfQueryReport:update"})
    public FebsResponse addSysconfQueryReportColumnr(@Valid SysconfQueryReportColumn sysconfQueryReportColumn) {
        this.sysconfQueryReportColumnService.saveSysconfQueryReportColumn(sysconfQueryReportColumn);
        return new FebsResponse().success();
    }
}
