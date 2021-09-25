package com.handturn.bole.master.set.controller;

import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.master.set.entity.SitSet;
import com.handturn.bole.master.set.service.ISitSetService;
import com.handturn.bole.sysconf.service.ISysconfPrintService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 智能门禁-站点配置 Controller
 *
 * @author Eric
 * @date 2020-02-28 09:41:18
 */
@Slf4j
@Validated
@RestController
@RequestMapping("master/set/sitSet")
public class SitSetController extends BaseController {

    @Autowired
    private ISysconfPrintService sysconfPrintService;

    @Autowired
    private ISitSetService sitSetService;

    @ControllerEndpoint(operation = "编辑站点设置/按钮", exceptionMessage = "编辑站点设置失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions("master:set:sitSet:update")
    public FebsResponse addSitSetr(@Valid SitSet sitSet) {
        SitSet data = this.sitSetService.saveSitSet(sitSet);
        return new FebsResponse().success().data(data);
    }

    /**
     * 导入
     * @param fileField
     * @return
     */
    @ResponseBody
    @PostMapping("/importUpload")
    @RequiresPermissions("master:set:sitSet:update")
    public FebsResponse importUpload (@RequestParam(value = "fileField", required = false) MultipartFile fileField) {
        try {
            ImportResultBean resultBean = sitSetService.importUpload(fileField);
            return new FebsResponse().success().data(resultBean);
        }catch (Exception e){
            throw new FebsException(e.getMessage());
        }
    }
}
