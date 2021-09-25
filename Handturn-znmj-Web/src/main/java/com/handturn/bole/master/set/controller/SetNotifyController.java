package com.handturn.bole.master.set.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.set.entity.NotifySet;
import com.handturn.bole.master.set.service.INotifySetService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 智能门禁-通知设置 Controller
 *
 * @author Eric
 * @date 2020-06-09 18:43:12
 */
@Slf4j
@Validated
@RestController
@RequestMapping("master/set/notifySet")
public class SetNotifyController extends BaseController {

    @Autowired
    private INotifySetService notifySetService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("master:set:notifySet:view")
    public FebsResponse notifySetList(QueryRequest request, NotifySet notifySet) {
        Map<String, Object> dataTable = getDataTable(this.notifySetService.findNotifySets(request, notifySet));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑消息通知设置/按钮", exceptionMessage = "编辑消息通知设置失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"master:set:notifySet:add","master:set:notifySet:update"})
    public FebsResponse addNotifySetr(@Valid NotifySet notifySet) {
        this.notifySetService.saveNotifySet(notifySet);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{notifySetIds}")
    @RequiresPermissions("master:set:notifySet:enable")
    @ControllerEndpoint(exceptionMessage = "启用消息通知设置失败")
    public FebsResponse enableNotifySet(@NotBlank(message = "{required}") @PathVariable String notifySetIds) {
        String[] notifySetIdArr = notifySetIds.split(StringPool.COMMA);
        this.notifySetService.enableNotifySet(notifySetIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{notifySetIds}")
    @RequiresPermissions("master:set:notifySet:disable")
    @ControllerEndpoint(exceptionMessage = "禁用消息通知设置失败")
    public FebsResponse disableNotifySet(@NotBlank(message = "{required}") @PathVariable String notifySetIds) {
        String[] notifySetIdArr = notifySetIds.split(StringPool.COMMA);
        this.notifySetService.disableNotifySet(notifySetIdArr);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出消息通知设置", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("master:set:notifySet:export")
    public void exportNotifySet(QueryRequest queryRequest, NotifySet notifySet, HttpServletResponse response) {
        List<NotifySet> notifySets = this.notifySetService.findNotifySets(queryRequest, notifySet).getRecords();
        ExcelKit.$Export(NotifySet.class, response).downXlsx(notifySets, false);
    }
}
