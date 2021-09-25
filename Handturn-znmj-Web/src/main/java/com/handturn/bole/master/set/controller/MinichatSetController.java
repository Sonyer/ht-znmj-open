package com.handturn.bole.master.set.controller;

import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.master.set.entity.MinichatSet;
import com.handturn.bole.master.set.service.IMinichatSetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 智能门禁-小程序参数配置 Controller
 *
 * @author Eric
 * @date 2020-02-28 11:05:13
 */
@Slf4j
@Validated
@RestController
@RequestMapping("front/set/minichatSet")
public class MinichatSetController extends BaseController {

    @Autowired
    private IMinichatSetService minichatSetService;

    @ControllerEndpoint(operation = "编辑微信参数设置/按钮", exceptionMessage = "编辑微信参数设置失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions("front:set:minichatSet:update")
    public FebsResponse addMinichatSetr(@Valid MinichatSet minichatSet) {
        MinichatSet data = this.minichatSetService.saveMinichatSet(minichatSet);
        return new FebsResponse().success().data(data);
    }
}
