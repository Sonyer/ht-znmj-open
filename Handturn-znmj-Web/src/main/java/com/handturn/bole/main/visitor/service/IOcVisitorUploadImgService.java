package com.handturn.bole.main.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.main.visitor.entity.OcVisitorUploadImg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * 网点-访客上传图片信息 Service接口
 *
 * @author Eric
 * @date 2020-09-26 08:34:23
 */
public interface IOcVisitorUploadImgService extends IService<OcVisitorUploadImg> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param ocVisitorUploadImg ocVisitorUploadImg
     * @return ICustomPage<OcVisitorUploadImg>
     */
    ICustomPage<OcVisitorUploadImg> findOcVisitorUploadImgs(QueryRequest request, OcVisitorUploadImg ocVisitorUploadImg);

    /**
     * 修改
     *
     * @param ocVisitorUploadImg ocVisitorUploadImg
     */
    OcVisitorUploadImg saveOcVisitorUploadImg(OcVisitorUploadImg ocVisitorUploadImg);


    /**
     * 通过Id获取信息
     * @param ocVisitorUploadImgId
     * @return
     */
    OcVisitorUploadImg findOcVisitorUploadImgById(Long ocVisitorUploadImgId);

    /**
     * 通过网点编码获取信息
     * @param ocCode
     * @param orgCode
     * @return
     */
    List<OcVisitorUploadImg> findOcVisitorUploadImgByOcCode(String ocCode, String orgCode, Set<String> statues,String idCardNameSearch);

    /**
     * 上传人脸图片
     * @param files
     * @return
     */
    List<ImportResultBean> visitorImgSingleUpload(MultipartFile files[], String orgCode, String ocCode);

    /**
     * 上传人脸图片
     * @param files
     * @param orgCode
     * @param ocCode
     * @return
     */
    List<OcVisitorUploadImg> visitorImgsUpload(MultipartFile files[], String orgCode,String orgName, String ocCode,String ocName);


    /**
     * 删除人脸图片
     * @param orgCode
     * @param ocCode
     * @return
     */
    void visitorImgDelete(String ocCode,String orgCode, Set<String> statues,String[] visitorImgIds);

    /**
     * 删除人脸图片
     * @param orgCode
     * @param ocCode
     * @return
     */
    void visitorImgDeleteAll(String ocCode,String orgCode, Set<String> statues);
}
