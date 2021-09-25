package com.handturn.bole.master.member.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.member.entity.Member;
import com.handturn.bole.master.member.service.IMemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 智能门禁-平台会员 Controller
 *
 * @author Eric
 * @date 2020-03-13 20:42:35
 */
@Slf4j
@Validated
@RestController
@RequestMapping("master/member/member")
public class MemberController extends BaseController {

    @Autowired
    private IMemberService memberService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("master:member:member:view")
    public FebsResponse memberList(QueryRequest request, Member member) {
        Map<String, Object> dataTable = getDataTable(this.memberService.findMembers(request, member));
        return new FebsResponse().success().data(dataTable);
    }


    @PostMapping("specialUser/{memberIds}")
    @RequiresPermissions("master:member:member:specialUser")
    @ControllerEndpoint(exceptionMessage = "特约平台会员失败")
    public FebsResponse specialUserMember(@NotBlank(message = "{required}") @PathVariable String memberIds) {
        String[] memberIdArr = memberIds.split(StringPool.COMMA);
        this.memberService.specialUserMember(memberIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("cancelSpecialUser/{memberIds}")
    @RequiresPermissions("master:member:member:cancelSpecialUser")
    @ControllerEndpoint(exceptionMessage = "取消特约平台会员失败")
    public FebsResponse cancelSpecialUserMember(@NotBlank(message = "{required}") @PathVariable String memberIds) {
        String[] memberIdArr = memberIds.split(StringPool.COMMA);
        this.memberService.cancelSpecialUserMember(memberIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("enable/{memberIds}")
    @RequiresPermissions("master:member:member:enable")
    @ControllerEndpoint(exceptionMessage = "启用平台会员失败")
    public FebsResponse enableMember(@NotBlank(message = "{required}") @PathVariable String memberIds) {
        String[] memberIdArr = memberIds.split(StringPool.COMMA);
        this.memberService.enableMember(memberIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("disable/{memberIds}")
    @RequiresPermissions("master:member:member:disable")
    @ControllerEndpoint(exceptionMessage = "禁用平台会员失败")
    public FebsResponse disableMember(@NotBlank(message = "{required}") @PathVariable String memberIds) {
        String[] memberIdArr = memberIds.split(StringPool.COMMA);
        this.memberService.disableMember(memberIdArr);
        return new FebsResponse().success();
    }

}
