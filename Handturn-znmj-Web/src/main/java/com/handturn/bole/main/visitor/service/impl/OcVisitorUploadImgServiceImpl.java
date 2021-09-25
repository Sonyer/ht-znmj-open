package com.handturn.bole.main.visitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.DateUtil;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.common.utils.StringRandom;
import com.handturn.bole.main.visitor.entity.*;
import com.handturn.bole.main.visitor.mapper.OcVisitorUploadImgMapper;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import com.handturn.bole.main.visitor.service.IOcVisitorUploadImgService;
import com.handturn.bole.master.set.service.IImgStoreSetService;
import com.handturn.bole.sysconf.constant.SysconfGlobalConstant;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 网点-访客上传图片信息 Service实现
 *
 * @author Eric
 * @date 2020-09-26 08:34:23
 */
@Service("OcVisitorUploadImgService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OcVisitorUploadImgServiceImpl extends ServiceImpl<OcVisitorUploadImgMapper, OcVisitorUploadImg> implements IOcVisitorUploadImgService {

    @Autowired
    private IImgStoreSetService imgStoreSetService;

    @Autowired
    private IOcVisitorInfoService ocVisitorInfoService;

    @Autowired
    private ISysconfGlobalParamService sysconfGlobalParamService;

    @Override
    public ICustomPage<OcVisitorUploadImg> findOcVisitorUploadImgs(QueryRequest request, OcVisitorUploadImg ocVisitorUploadImg) {
        CustomPage<OcVisitorUploadImg> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, ocVisitorUploadImg);
    }

    @Override
    @Transactional
    public OcVisitorUploadImg saveOcVisitorUploadImg(OcVisitorUploadImg ocVisitorUploadImg) {
        if(ocVisitorUploadImg.getId() == null){
            this.save(ocVisitorUploadImg);
            return ocVisitorUploadImg;
        }else{
            OcVisitorUploadImg ocVisitorUploadImgOld = this.baseMapper.selectById(ocVisitorUploadImg.getId());
            CopyUtils.copyProperties(ocVisitorUploadImg,ocVisitorUploadImgOld);
            this.updateById(ocVisitorUploadImgOld);
            return ocVisitorUploadImgOld;
        }
    }

    @Override
    public OcVisitorUploadImg findOcVisitorUploadImgById(Long ocVisitorUploadImgId){
        return this.baseMapper.selectOne(new QueryWrapper<OcVisitorUploadImg>().lambda().eq(OcVisitorUploadImg::getId, ocVisitorUploadImgId));
    }

    /**
     * 通过网点编码获取信息
     * @param ocCode
     * @param orgCode
     * @return
     */
    @Override
    public List<OcVisitorUploadImg> findOcVisitorUploadImgByOcCode(String ocCode, String orgCode, Set<String> statues,String idCardNameSearch){
        QueryWrapper<OcVisitorUploadImg> queryWrapper = new QueryWrapper<OcVisitorUploadImg>();
        queryWrapper.lambda().eq(OcVisitorUploadImg::getOcCode,ocCode);
        queryWrapper.lambda().eq(OcVisitorUploadImg::getOrgCode,orgCode);
        queryWrapper.lambda().in(OcVisitorUploadImg::getStatus,statues);
        if(!StringUtils.isEmpty(idCardNameSearch)){
            queryWrapper.lambda().and(wrapper ->
                    wrapper.like(OcVisitorUploadImg::getIdCardName, idCardNameSearch)
                            .or().like(OcVisitorUploadImg::getIdCard, idCardNameSearch)
                            .or().like(OcVisitorUploadImg::getPhoneNumber, idCardNameSearch)
                            .or().like(OcVisitorUploadImg::getDepName, idCardNameSearch)
                            .or().like(OcVisitorUploadImg::getPositionName, idCardNameSearch)
                            .or().eq(OcVisitorUploadImg::getVisitorType, StringUtils.isEmpty(idCardNameSearch)?"":idCardNameSearch.contains("内部")?
                            OcVisitorType.INNER_USER:idCardNameSearch.contains("外来")?OcVisitorType.OUTER_USER:""));
        }

        List<OcVisitorUploadImg> result = this.baseMapper.selectList(queryWrapper);
        return result;
    }

    /**
     * 上传人脸图片
     * @param files
     * @return
     */
    @Override
    public List<ImportResultBean> visitorImgSingleUpload(MultipartFile files[], String orgCode, String ocCode){
        List<ImportResultBean> imgRequstList = new ArrayList<>();
        Arrays.asList(files).forEach(uploadFile->{
            try {
                ImportResultBean resultBean = imgStoreSetService.importUpload(uploadFile, IImgStoreSetService.IMGS_VISITOR_ITEM+"-"+orgCode+"-"+ocCode);
                imgRequstList.add(resultBean);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        return imgRequstList;
    }

    /**
     * 上传人脸图片
     * @param files
     * @param orgCode
     * @param ocCode
     * @return
     */
    @Override
    @Transactional
    public List<OcVisitorUploadImg> visitorImgsUpload(MultipartFile files[], String orgCode,String orgName, String ocCode,String ocName){
        //文件名组成:(访客姓名_手机号码_部门名_岗位名_身份证_韦根号.jpg

        List<ImportResultBean> imgRequstList = new ArrayList<>();
        Arrays.asList(files).forEach(uploadFile->{
            try {
                ImportResultBean resultBean = imgStoreSetService.importUpload(uploadFile, IImgStoreSetService.IMGS_VISITOR_ITEM+"-"+orgCode+"-"+ocCode);
                imgRequstList.add(resultBean);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        List<OcVisitorUploadImg> result = new ArrayList<OcVisitorUploadImg>();
        imgRequstList.forEach(imgVo ->{
            String[] visitorStrs = imgVo.getOldFileName().split("_");
            OcVisitorUploadImg itemImg = new OcVisitorUploadImg();
            itemImg.setOcCode(ocCode);
            itemImg.setOcName(ocName);
            itemImg.setOrgCode(orgCode);
            itemImg.setOrgName(orgName);
            itemImg.setFaceImgAttchment(imgVo.getFilePath());
            itemImg.setFaceImgRequest(imgVo.getFileRequestUrl());

            StringBuffer errorMessage = new StringBuffer("");
            if(visitorStrs.length != 6){
                errorMessage.append("文件名组成:(访客姓名_手机号码_部门名_岗位名_身份证_韦根号.jpg!\n");
                saveOcVisitorUploadImg(itemImg);
            }else{
               /* if(!visitorStrs[1].trim().equals("内部员工") && !visitorStrs[1].trim().equals("外来人员")){
                    errorMessage.append("访客类型只能为:内部员工或外来人员!\n");
                }else{
                    if(visitorStrs[1].trim().equals("内部员工")){
                        itemImg.setVisitorType(OcVisitorType.INNER_USER);
                    }else{
                        itemImg.setVisitorType(OcVisitorType.OUTER_USER);
                    }
                }*/
                itemImg.setIdCardName(visitorStrs[0]);
                itemImg.setPhoneNumber(visitorStrs[1]);
                itemImg.setDepName(visitorStrs[2]);
                itemImg.setPositionName(visitorStrs[3]);
                itemImg.setIdCard(visitorStrs[4]);
                itemImg.setWegan(visitorStrs[5]);

                itemImg.setFaceImgAttchment(imgVo.getFilePath());
                itemImg.setFaceImgRequest(imgVo.getFileRequestUrl());

                if(errorMessage.length() <= 0){
                    itemImg.setStatus(OcVisitorUploadImgStatus.BINDED);
                    itemImg = saveOcVisitorUploadImg(itemImg);

                    OcVisitorInfo ocVisitorInfo = ocVisitorInfoService.findOcVisitorInfoByIdCard(itemImg.getIdCardName(),itemImg.getPhoneNumber(),ocCode,orgCode);
                    if(ocVisitorInfo == null){
                        ocVisitorInfo = new OcVisitorInfo();
                    }
                    ocVisitorInfo.setVisitorType(itemImg.getVisitorType());
                    ocVisitorInfo.setOcCode(ocCode);
                    ocVisitorInfo.setOcName(ocName);
                    ocVisitorInfo.setOrgCode(orgCode);
                    ocVisitorInfo.setOrgName(orgName);
                    ocVisitorInfo.setFaceImgAttchment(imgVo.getFilePath());
                    ocVisitorInfo.setFaceImgRequest(imgVo.getFileRequestUrl());
                    ocVisitorInfo.setIdCard(itemImg.getIdCard());
                    ocVisitorInfo.setIdCardName(itemImg.getIdCardName());
                    ocVisitorInfo.setPhoneNumber(itemImg.getPhoneNumber());
                    ocVisitorInfo.setDepName(itemImg.getDepName());
                    ocVisitorInfo.setPositionName(itemImg.getPositionName());
                    ocVisitorInfo.setWegan(itemImg.getWegan());
                    ocVisitorInfo.setFreezeStatus(OcVisitorFreezeStatus.NORMAL);
                    ocVisitorInfo.setFaceUploadImgId(itemImg.getId());
                    ocVisitorInfo.setCreateType(OcVisitorCreateType.IMPORT);
                    ocVisitorInfoService.saveOcVisitorInfo(ocVisitorInfo);
                }else{
                    itemImg.setStatus(OcVisitorUploadImgStatus.INIT);
                    saveOcVisitorUploadImg(itemImg);
                }
            }
        });
        return result;
    }

    /**
     * 删除人脸图片
     * @param orgCode
     * @param ocCode
     * @return
     */
    @Override
    @Transactional
    public void visitorImgDelete(String ocCode,String orgCode, Set<String> statues,String[] visitorImgIds){
        QueryWrapper<OcVisitorUploadImg> queryWrapper = new QueryWrapper<OcVisitorUploadImg>();
        queryWrapper.lambda().in(OcVisitorUploadImg::getId,visitorImgIds);
        queryWrapper.lambda().eq(OcVisitorUploadImg::getOcCode,ocCode);
        queryWrapper.lambda().eq(OcVisitorUploadImg::getOrgCode,orgCode);
        queryWrapper.lambda().in(OcVisitorUploadImg::getStatus,statues);

        this.baseMapper.delete(queryWrapper);
    }

    /**
     * 上传人脸图片
     * @param orgCode
     * @param ocCode
     * @return
     */
    @Override
    @Transactional
    public void visitorImgDeleteAll(String ocCode,String orgCode, Set<String> statues){
        QueryWrapper<OcVisitorUploadImg> queryWrapper = new QueryWrapper<OcVisitorUploadImg>();
        queryWrapper.lambda().eq(OcVisitorUploadImg::getOcCode,ocCode);
        queryWrapper.lambda().eq(OcVisitorUploadImg::getOrgCode,orgCode);
        queryWrapper.lambda().in(OcVisitorUploadImg::getStatus,statues);

        this.baseMapper.delete(queryWrapper);
    }
}
