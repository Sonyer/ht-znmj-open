package com.handturn.bole.master.set.controller;

import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.master.set.vo.ImgStoreSetVo;
import com.handturn.bole.sysconf.constant.SysconfGlobalConstant;
import com.handturn.bole.sysconf.entity.SysconfGlobalParam;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
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
@RequestMapping("master/set/imgStoreSet")
public class ImgStoreSetController extends BaseController {

    @Autowired
    private ISysconfGlobalParamService sysconfGlobalParamService;

    @ControllerEndpoint(operation = "编辑文件存储参数设置/按钮", exceptionMessage = "编辑文件存储参数设置失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions("master:set:imgStoreSet:update")
    public FebsResponse addImgStoreSetSet(@Valid ImgStoreSetVo imgStoreSetVo) {
        SysconfGlobalParam storeMethod = sysconfGlobalParamService.getParamByGroupCodeByOrg(SysconfGlobalConstant.SysconfGlobalGroupCode.FILE_STORE_METHOD,
                SysconfGlobalConstant.FILE_STORE_METHOD.FILE_STORE_METHOD, UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode(),
                UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());

        if(storeMethod == null){
            storeMethod = new SysconfGlobalParam();
            storeMethod.setGroupCode(SysconfGlobalConstant.SysconfGlobalGroupCode.FILE_STORE_METHOD);
            storeMethod.setParamKey(SysconfGlobalConstant.FILE_STORE_METHOD.FILE_STORE_METHOD);
            storeMethod.setParamValue(imgStoreSetVo.getStoreMethod());
            storeMethod.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
            storeMethod.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
            storeMethod.setStatus(BaseStatus.ENABLED);
        }else{
            storeMethod.setParamValue(imgStoreSetVo.getStoreMethod());
            storeMethod.setStatus(BaseStatus.ENABLED);
        }

        sysconfGlobalParamService.saveSysconfGlobalParam(storeMethod);

        return new FebsResponse().success().data(1);
    }
}
