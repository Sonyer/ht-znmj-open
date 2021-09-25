package com.handturn.bole.master.set.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.master.set.service.IImgStoreSetService;
import com.handturn.bole.master.set.vo.ImgStoreSetVo;
import com.handturn.bole.sysconf.constant.SysconfGlobalConstant;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
* 智能门禁购-小程序参数配置 View Controller
*
* @author Eric
* @date 2020-02-28 11:05:13
*/
@Slf4j
@Validated
@Controller
public class ImgStoreSetViewController extends BaseController {
    @Autowired
    private IImgStoreSetService imgStoreSetService;

    @Autowired
    private ISysconfGlobalParamService sysconfGlobalParamService;

    @Autowired
    ISysconfDictCodeService sysconfDictCodeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "master/set/imgStoreSet")
    @RequiresPermissions("master:set:imgStoreSet:view")
    public String view(Model model) {
        String storeMethod = sysconfGlobalParamService.getParamValueByGroupCodeAndOrg(SysconfGlobalConstant.SysconfGlobalGroupCode.FILE_STORE_METHOD,
                SysconfGlobalConstant.FILE_STORE_METHOD.FILE_STORE_METHOD, UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode(),
                UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());

        ImgStoreSetVo imgStoreSetVo = imgStoreSetService.getImgStoreSet();
        imgStoreSetVo.setStoreMethod(storeMethod);

        //获取数据字典
        List<OptionVo> fileStoreMethodOption = sysconfDictCodeService.getDictOptionVoByTypeCode("FileStoreMethod");

        model.addAttribute("fileStoreMethodOption",fileStoreMethodOption);
        model.addAttribute("imgStoreSet",imgStoreSetVo);
        return FebsUtil.view("master/set/imgStoreSet/imgStoreSetEdit");

    }
}
