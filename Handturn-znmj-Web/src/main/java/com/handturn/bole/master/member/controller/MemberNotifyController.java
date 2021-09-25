package com.handturn.bole.master.member.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.member.entity.MemberNotify;
import com.handturn.bole.master.member.service.IMemberNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 智能门禁-会员通知 Controller
 *
 * @author Eric
 * @date 2020-05-19 09:22:40
 */
@Slf4j
@Validated
@RestController
@RequestMapping("master/member/memberNotify")
public class MemberNotifyController extends BaseController {

    @Autowired
    private IMemberNotifyService memberNotifyService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("memberNotify:view")
    public FebsResponse memberNotifyList(QueryRequest request, MemberNotify memberNotify) {
        Map<String, Object> dataTable = getDataTable(this.memberNotifyService.findMemberNotifys(request, memberNotify));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "编辑消息通知/按钮", exceptionMessage = "编辑消息通知失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"master:member:memberNotify:add","master:member:memberNotify:update"})
    public FebsResponse addMemberNotifyr(@Valid MemberNotify memberNotify) {
        this.memberNotifyService.saveMemberNotify(memberNotify);
        return new FebsResponse().success();
    }


    @PostMapping("enable/{memberNotifyIds}")
    @RequiresPermissions("master:member:memberNotify:enable")
    @ControllerEndpoint(exceptionMessage = "启用消息通知失败")
    public FebsResponse enableMemberNotify(@NotBlank(message = "{required}") @PathVariable String memberNotifyIds) {
        String[] memberNotifyIdArr = memberNotifyIds.split(StringPool.COMMA);
        this.memberNotifyService.enableMemberNotify(memberNotifyIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{memberNotifyIds}")
    @RequiresPermissions("master:member:memberNotify:disable")
    @ControllerEndpoint(exceptionMessage = "禁用消息通知失败")
    public FebsResponse disableMemberNotify(@NotBlank(message = "{required}") @PathVariable String memberNotifyIds) {
        String[] memberNotifyIdArr = memberNotifyIds.split(StringPool.COMMA);
        this.memberNotifyService.disableMemberNotify(memberNotifyIdArr);
        return new FebsResponse().success();
    }
}
