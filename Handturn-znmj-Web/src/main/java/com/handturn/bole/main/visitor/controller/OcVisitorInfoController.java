package com.handturn.bole.main.visitor.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.ExportReflectTo;
import com.handturn.bole.main.authArea.entity.OcAuthArea;
import com.handturn.bole.main.authArea.service.IOcAuthAreaService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaVisitorService;
import com.handturn.bole.main.visitor.entity.*;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import com.handturn.bole.main.visitor.service.IOcVisitorUploadImgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 网点-会员访客信息 Controller
 *
 * @author Eric
 * @date 2020-09-22 08:57:38
 */
@Slf4j
@Validated
@RestController
@RequestMapping("main/visitor/ocVisitorInfo")
public class OcVisitorInfoController extends BaseController {

    @Autowired
    private IOcVisitorInfoService ocVisitorInfoService;

    @Autowired
    private IOcVisitorUploadImgService ocVisitorUploadImgService;

    @Autowired
    private IOcAuthAreaVisitorService ocAuthAreaVisitorService;

    @Autowired
    private IOcAuthAreaService ocAuthAreaService;


    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("main:visitor:ocVisitorInfo:view")
    public FebsResponse ocVisitorInfoList(QueryRequest request, OcVisitorInfo ocVisitorInfo) {
        ocVisitorInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisitorInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        Map<String, Object> dataTable = getDataTable(this.ocVisitorInfoService.findOcVisitorInfos(request, ocVisitorInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("export")
    @RequiresPermissions("main:visitor:ocVisitorInfo:view")
    public void exportOcVisitorInfo(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        OcVisitorInfo ocVisitorInfo = JSONObject.parseObject(queryData,OcVisitorInfo.class);
        //-------控制导出权限-------begin
        ocVisitorInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisitorInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, OcVisitorInfo.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,ocVisitorInfo,
        request,response,
        "OcVisitorInfoService","findOcVisitorInfos",paramClassName);
    }

    @ControllerEndpoint(operation = "编辑/按钮", exceptionMessage = "编辑失败")
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions({"main:visitor:ocVisitorInfo:add","main:visitor:ocVisitorInfo:update"})
    public FebsResponse addOcVisitorInfor(@Valid OcVisitorInfo ocVisitorInfo) {
        ocVisitorInfo.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocVisitorInfo.setOrgName(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName());
        ocVisitorInfo.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        ocVisitorInfo.setOcName(UserInfoHolder.getUserInfo().getCurrentOc().getOcName());

        OcVisitorUploadImg new_visitorUploadImg = null;
        if(ocVisitorInfo.getFaceUploadImgId() == null || ocVisitorInfo.getFaceUploadImgId() == 0L) {
            if (StringUtils.isEmpty(ocVisitorInfo.getFaceImgAttchment())) {
                throw new FebsException("请从人脸池中选择图片或上传人脸图片");
            }
        }else{
            new_visitorUploadImg = ocVisitorUploadImgService.findOcVisitorUploadImgById(ocVisitorInfo.getFaceUploadImgId());
            if(new_visitorUploadImg == null){
                throw new FebsException("请从人脸池中选择图片或上传人脸图片");
            }
        }

        if(ocVisitorInfo.getId() != null && ocVisitorInfo.getId() != 0L){
            OcVisitorInfo old_ocVisitorInfo = ocVisitorInfoService.findOcVisitorInfoById(ocVisitorInfo.getId());
            if(old_ocVisitorInfo.getFaceUploadImgId() != null && ocVisitorInfo.getFaceUploadImgId() != 0L){
                if(ocVisitorInfo.getFaceUploadImgId() != null && ocVisitorInfo.getFaceUploadImgId() != 0L && ocVisitorInfo.getFaceUploadImgId() != old_ocVisitorInfo.getFaceUploadImgId()){
                    OcVisitorUploadImg old_visitorUploadImg = ocVisitorUploadImgService.findOcVisitorUploadImgById(old_ocVisitorInfo.getFaceUploadImgId());
                    if(old_visitorUploadImg != null){
                        old_visitorUploadImg.setStatus(OcVisitorUploadImgStatus.INIT);
                        old_visitorUploadImg.setUploadErrorMessage("已被替换!");
                        ocVisitorUploadImgService.getBaseMapper().updateById(old_visitorUploadImg);
                    }
                }
            }else{
                if(old_ocVisitorInfo.getFaceUploadImgId() != null && (ocVisitorInfo.getFaceUploadImgId() == null || ocVisitorInfo.getFaceUploadImgId() == 0L)){
                    OcVisitorUploadImg old_visitorUploadImg = ocVisitorUploadImgService.findOcVisitorUploadImgById(old_ocVisitorInfo.getFaceUploadImgId());
                    if(old_visitorUploadImg != null){
                        old_visitorUploadImg.setStatus(OcVisitorUploadImgStatus.INIT);
                        old_visitorUploadImg.setUploadErrorMessage("已被替换!");
                        ocVisitorUploadImgService.getBaseMapper().updateById(old_visitorUploadImg);
                    }
                }
            }
        }else{
            ocVisitorInfo.setCreateType(OcVisitorCreateType.PAGE_INPUT);
        }

        if(ocVisitorInfo.getFaceUploadImgId() == null || ocVisitorInfo.getFaceUploadImgId() == 0L){
            OcVisitorUploadImg  ocVisitorUploadImg = new OcVisitorUploadImg();
            ocVisitorUploadImg.setOrgCode(ocVisitorInfo.getOrgCode());
            ocVisitorUploadImg.setOrgName(ocVisitorInfo.getOrgName());
            ocVisitorUploadImg.setOcCode(ocVisitorInfo.getOcCode());
            ocVisitorUploadImg.setOcName(ocVisitorInfo.getOcName());
            ocVisitorUploadImg.setVisitorType(ocVisitorInfo.getVisitorType());
            ocVisitorUploadImg.setIdCard(ocVisitorInfo.getIdCard());
            ocVisitorUploadImg.setIdCardName(ocVisitorInfo.getIdCardName());
            ocVisitorUploadImg.setPhoneNumber(ocVisitorInfo.getPhoneNumber());
            ocVisitorUploadImg.setPositionName(ocVisitorInfo.getPositionName());
            ocVisitorUploadImg.setWegan(ocVisitorInfo.getWegan());
            ocVisitorUploadImg.setFaceImgRequest(ocVisitorInfo.getFaceImgRequest());
            ocVisitorUploadImg.setFaceImgAttchment(ocVisitorInfo.getFaceImgAttchment());
            ocVisitorUploadImg.setStatus(OcVisitorUploadImgStatus.BINDED);
            ocVisitorUploadImgService.getBaseMapper().insert(ocVisitorUploadImg);

            ocVisitorInfo.setFaceUploadImgId(ocVisitorUploadImg.getId());
        }else{
            new_visitorUploadImg.setOrgCode(ocVisitorInfo.getOrgCode());
            new_visitorUploadImg.setOrgName(ocVisitorInfo.getOrgName());
            new_visitorUploadImg.setOcCode(ocVisitorInfo.getOcCode());
            new_visitorUploadImg.setOcName(ocVisitorInfo.getOcName());
            new_visitorUploadImg.setVisitorType(ocVisitorInfo.getVisitorType());
            new_visitorUploadImg.setIdCard(ocVisitorInfo.getIdCard());
            new_visitorUploadImg.setIdCardName(ocVisitorInfo.getIdCardName());
            new_visitorUploadImg.setPhoneNumber(ocVisitorInfo.getPhoneNumber());
            new_visitorUploadImg.setPositionName(ocVisitorInfo.getPositionName());
            new_visitorUploadImg.setWegan(ocVisitorInfo.getWegan());
            new_visitorUploadImg.setFaceImgRequest(new_visitorUploadImg.getFaceImgRequest());
            new_visitorUploadImg.setFaceImgAttchment(new_visitorUploadImg.getFaceImgAttchment());
            new_visitorUploadImg.setStatus(OcVisitorUploadImgStatus.BINDED);
            new_visitorUploadImg.setUploadErrorMessage("");
            ocVisitorUploadImgService.getBaseMapper().updateById(new_visitorUploadImg);

            ocVisitorInfo.setFaceUploadImgId(new_visitorUploadImg.getId());
            ocVisitorInfo.setFaceImgAttchment(new_visitorUploadImg.getFaceImgAttchment());
            ocVisitorInfo.setFaceImgRequest(new_visitorUploadImg.getFaceImgRequest());

        }
        this.ocVisitorInfoService.saveOcVisitorInfo(ocVisitorInfo);
        return new FebsResponse().success();
    }

    @PostMapping("freeze/{ocVisitorInfoIds}")
    @RequiresPermissions("main:visitor:ocVisitorInfo:freeze")
    @ControllerEndpoint(exceptionMessage = "冻结失败")
    public FebsResponse freeze(@NotBlank(message = "{required}") @PathVariable String ocVisitorInfoIds) {
        String[] ocVisitorInfoIdArr = ocVisitorInfoIds.split(StringPool.COMMA);
        this.ocVisitorInfoService.freeze(ocVisitorInfoIdArr);
        return new FebsResponse().success();
    }

    @PostMapping("normal/{ocVisitorInfoIds}")
    @RequiresPermissions("main:visitor:ocVisitorInfo:normal")
    @ControllerEndpoint(exceptionMessage = "解冻失败")
    public FebsResponse normal(@NotBlank(message = "{required}") @PathVariable String ocVisitorInfoIds) {
        String[] ocVisitorInfoIdArr = ocVisitorInfoIds.split(StringPool.COMMA);
        this.ocVisitorInfoService.normal(ocVisitorInfoIdArr);
        return new FebsResponse().success();
    }

    /**
     * 导入图片
     * @param files
     * @return
     */
    @ResponseBody
    @PostMapping("/ocVisitorInfoSingleImport")
    @RequiresPermissions({"main:visitor:ocVisitorInfo:add","main:visitor:ocVisitorInfo:update"})
    public FebsResponse ocVisitorInfoSingleImport (@RequestParam(value = "file") MultipartFile files[], String storeCode) {
        try {
            String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
            String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
            List<ImportResultBean> resultBean = ocVisitorUploadImgService.visitorImgSingleUpload(files,orgCode,ocCode);
            return new FebsResponse().success().data(resultBean);
        }catch (Exception e){
            throw new FebsException(e.getMessage());
        }
    }

    /**
     * 导入图片
     * @param files
     * @return
     */
    @ResponseBody
    @PostMapping("/ocVisitorInfoImport")
    @RequiresPermissions("main:visitor:ocVisitorInfo:import")
    public FebsResponse ocVisitorInfoImport (@RequestParam(value = "file") MultipartFile files[], String storeCode) {
        try {
            String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
            String ocName = UserInfoHolder.getUserInfo().getCurrentOc().getOcName();
            String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
            String orgName = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName();
            List<OcVisitorUploadImg> resultBean = ocVisitorUploadImgService.visitorImgsUpload(files,orgCode,orgName,ocCode,ocName);

            /*Set<String> statues = new HashSet<>();
            statues.add(OcVisitorUploadImgStatus.INIT);
            List<OcVisitorUploadImg> resultImgs = ocVisitorUploadImgService.findOcVisitorUploadImgByOcCode(ocCode,orgCode,statues);*/
            return new FebsResponse().success();
        }catch (Exception e){
            throw new FebsException(e.getMessage());
        }
    }

    /**
     * 展示人脸池
     * @return
     */
    @ResponseBody
    @PostMapping("/ocVisitorImgsShow")
    @RequiresPermissions("main:visitor:ocVisitorInfo:view")
    public FebsResponse ocVisitorImgsShow (String idCardNameSearch) {
        String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
        String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        Set<String> statues = new HashSet<>();
        statues.add(OcVisitorUploadImgStatus.INIT);
        List<OcVisitorUploadImg> resultImgs = ocVisitorUploadImgService.findOcVisitorUploadImgByOcCode(ocCode,orgCode,statues,idCardNameSearch);
        return new FebsResponse().success().data(resultImgs);
    }

    /**
     * 展示人脸池
     * @return
     */
    @ResponseBody
    @PostMapping("/visitorImgDelete")
    @RequiresPermissions("main:visitor:ocVisitorInfo:update")
    public FebsResponse visitorImgDelete (String visitorImgIds) {
        String[] ocVisitorInfoIdArr = visitorImgIds.split(StringPool.COMMA);
        String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
        String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        Set<String> statues = new HashSet<>();
        statues.add(OcVisitorUploadImgStatus.INIT);
        ocVisitorUploadImgService.visitorImgDelete(ocCode,orgCode,statues,ocVisitorInfoIdArr);
        return new FebsResponse().success();
    }


    /**
     * 清空图片
     * @return
     */
    @ResponseBody
    @PostMapping("/visitorImgDeleteAll")
    @RequiresPermissions("main:visitor:ocVisitorInfo:update")
    public FebsResponse visitorImgDeleteAll () {
        try {
            String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
            String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
            Set<String> statues = new HashSet<>();
            statues.add(OcVisitorUploadImgStatus.INIT);
            ocVisitorUploadImgService.visitorImgDeleteAll(ocCode,orgCode,statues);

            return new FebsResponse().success();
        }catch (Exception e){
            throw new FebsException(e.getMessage());
        }
    }


    @GetMapping("ocAuthAreaList")
    @ResponseBody
    @RequiresPermissions("main:visitor:ocVisitorInfo:view")
    public FebsResponse ocAuthAreaList(QueryRequest request, OcAuthArea ocAuthArea) {
        ocAuthArea.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocAuthArea.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        ocAuthArea.setAuthCheck(true);
        Map<String, Object> dataTable = getDataTable(this.ocAuthAreaService.findOcAuthAreas(request, ocAuthArea));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "导出", exceptionMessage = "导出Excel失败")
    @GetMapping("ocAuthAreaExport")
    @RequiresPermissions("main:visitor:ocVisitorInfo:view")
    public void ocAuthAreaExport(String queryData, String sortObj, String cols, HttpServletRequest request, HttpServletResponse response) {
        OcAuthArea ocAuthArea = JSONObject.parseObject(queryData,OcAuthArea.class);
        //-------控制导出权限-------begin
        ocAuthArea.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
        ocAuthArea.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());
        ocAuthArea.setAuthCheck(true);
        //-------控制导出权限-------end

        //参数类型
        Class[] paramClassName = {QueryRequest.class, OcAuthArea.class}; //必须按这个循序放置

        //反射调用
        ExportReflectTo.exportRun(queryData,sortObj,cols,ocAuthArea,
                request,response,
                "OcAuthAreaService","findOcAuthAreas",paramClassName);
    }

    @PostMapping("authOcAuthAreas")
    @RequiresPermissions("main:visitor:ocVisitorInfo:auth")
    @ControllerEndpoint(exceptionMessage = "授权失败")
    public FebsResponse authOcAuthAreas(String visitorId,String ocAuthAreaIds) {
        String[] ocAuthAreaIdsArr = ocAuthAreaIds.split(StringPool.COMMA);
        String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
        String orgName = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName();
        String ocName = UserInfoHolder.getUserInfo().getCurrentOc().getOcName();
        this.ocAuthAreaVisitorService.authOcVisitors(ocAuthAreaIdsArr,visitorId,orgCode,ocCode,orgName,ocName);
        return new FebsResponse().success();
    }

    @PostMapping("cancelVisitorsOcAuths")
    @RequiresPermissions("main:visitor:ocVisitorInfo:auth")
    @ControllerEndpoint(exceptionMessage = "取消授权失败")
    public FebsResponse cancelVisitorsOcAuths(String visitorId,String ocAuthAreaIds) {
        String[] ocAuthAreaIdsArr = ocAuthAreaIds.split(StringPool.COMMA);
        String orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        String ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
        String orgName = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgName();
        String ocName = UserInfoHolder.getUserInfo().getCurrentOc().getOcName();
        this.ocAuthAreaVisitorService.cancelAuthOcVisitors(ocAuthAreaIdsArr,visitorId,orgCode,ocCode,orgName,ocName);
        return new FebsResponse().success();
    }

}
