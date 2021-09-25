package com.handturn.bole.master.member.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.member.entity.MemberWxMp;
import com.handturn.bole.master.member.service.IMemberWxMpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 智能门禁-微信小程序会员 Controller
 *
 * @author Eric
 * @date 2020-03-16 13:19:19
 */
@Slf4j
@Validated
@RestController
@RequestMapping("master/member/memberWxMp")
public class MemberWxMpController extends BaseController {

    @Autowired
    private IMemberWxMpService memberWxMpService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("master:member:memberWxMp:view")
    public FebsResponse memberWxMpList(QueryRequest request, MemberWxMp memberWxMp) {
        Map<String, Object> dataTable = getDataTable(this.memberWxMpService.findMemberWxMps(request, memberWxMp));
        return new FebsResponse().success().data(dataTable);
    }


    @PostMapping("enable/{memberWxMpIds}")
    @RequiresPermissions("master:member:memberWxMp:enable")
    @ControllerEndpoint(exceptionMessage = "启用微信小程序会员失败")
    public FebsResponse enableMemberWxMp(@NotBlank(message = "{required}") @PathVariable String memberWxMpIds) {
        String[] memberWxMpIdArr = memberWxMpIds.split(StringPool.COMMA);
        this.memberWxMpService.enableMemberWxMp(memberWxMpIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{memberWxMpIds}")
    @RequiresPermissions("master:member:memberWxMp:disable")
    @ControllerEndpoint(exceptionMessage = "禁用微信小程序会员失败")
    public FebsResponse disableMemberWxMp(@NotBlank(message = "{required}") @PathVariable String memberWxMpIds) {
        String[] memberWxMpIdArr = memberWxMpIds.split(StringPool.COMMA);
        this.memberWxMpService.disableMemberWxMp(memberWxMpIdArr);
        return new FebsResponse().success();
    }

}
