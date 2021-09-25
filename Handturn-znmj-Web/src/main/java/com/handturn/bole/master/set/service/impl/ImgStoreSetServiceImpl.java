package com.handturn.bole.master.set.service.impl;

import com.google.gson.Gson;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.qiniu.UploadFactory;
import com.handturn.bole.common.qiniu.UploadUtil;
import com.handturn.bole.common.utils.DateUtil;
import com.handturn.bole.common.utils.StringRandom;
import com.handturn.bole.master.set.service.IImgStoreSetService;
import com.handturn.bole.master.set.vo.ImgStoreSetVo;
import com.handturn.bole.sysconf.constant.SysconfGlobalConstant;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

/**
 * 智能门禁-站点配置 Service实现
 *
 * @author Eric
 * @date 2020-02-28 09:41:18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ImgStoreSetServiceImpl implements IImgStoreSetService {

    //引入第一步的七牛配置
    @Value("${qiniu.accessKey}")
    private String accesskey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucketName}")
    private String bucketName;

    @Value("${qiniu.fileDomain}")
    private String bucketHostName;

    @Value("${localStore.physicalPath}")
    private String physicalPath;

    @Value("${localStore.requestDomain}")
    private String requestDomain;

    @Value("${localStore.requestFileDomain}")
    private String requestFileDomain;

    @Autowired
    private ISysconfGlobalParamService sysconfGlobalParamService;


    /**
     * 获取图潘存储配置
     * @return
     */
    @Override
    public ImgStoreSetVo getImgStoreSet(){
        ImgStoreSetVo frontImgStoreSetVo = new ImgStoreSetVo();
        frontImgStoreSetVo.setAccess_key(accesskey);
        frontImgStoreSetVo.setSecret_key(secretKey);
        frontImgStoreSetVo.setBucket(bucketName);
        frontImgStoreSetVo.setPhysical_path(physicalPath);
        frontImgStoreSetVo.setRequest_url(requestDomain);
        return frontImgStoreSetVo;
    }

    /**
     * 导入图片
     * @param uploadFile
     * @return
     */
    @Override
    public ImportResultBean importUpload(MultipartFile uploadFile, String typeName){
        ImportResultBean resultBean = new ImportResultBean();

        String ocCode = null;
        String orgCode = null;
        if(UserInfoHolder.getUserInfo() != null && UserInfoHolder.getUserInfo().getCurrentOc() != null && UserInfoHolder.getUserInfo().getCurrentOrg() != null){
            ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
            orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        }
        // 获取临时上传文件目录
        String fileStoreMethod = sysconfGlobalParamService.getParamValueByGroupCode(SysconfGlobalConstant.SysconfGlobalGroupCode.FILE_STORE_METHOD,
                SysconfGlobalConstant.FILE_STORE_METHOD.FILE_STORE_METHOD,
                ocCode,orgCode);

        // 新建一个文件
        String originalFileName = uploadFile.getOriginalFilename();
        originalFileName.replaceAll("/", "\\");

        String oldFileName = originalFileName.substring(0,originalFileName.lastIndexOf("."));
        String lastPrex = originalFileName.substring(originalFileName.lastIndexOf("."),originalFileName.length());

        String filePath = "";
        String fileName = StringRandom.getStringRandom(20)+lastPrex;

        filePath = "/"+typeName+"/"+ DateUtil.getDateFormat(new Date(), DateUtil.DATA_PATTERN_8)+"/";

        //七牛存储
        if(fileStoreMethod.equals(SysconfGlobalConstant.SysconfGlobalGroupCode.QINIU_STORE_SECRET)){
            try{
                String requestUrl = uploadImage(uploadFile,filePath,fileName);
                resultBean.setSuccess(true);
                resultBean.setFileName(fileName);
                resultBean.setFilePath(requestUrl);
                resultBean.setFileRequestUrl(requestUrl);
            }catch (Exception e){
                e.printStackTrace();
                throw new FebsException("文件存储错误");
            }
        }

        //本地存储
        if(fileStoreMethod.equals(SysconfGlobalConstant.SysconfGlobalGroupCode.LOCATION_STORE_CONF)){

            String pyfilePath = this.physicalPath + filePath+ fileName;
            String requestUrl = this.requestDomain + filePath + fileName;

            File dest = new File(pyfilePath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                //将上传的文件写入新建的文件中
                uploadFile.transferTo(dest);
                //获取长度
                resultBean.setFileSize(dest.length());

                if(lastPrex.equals(".bmp") || lastPrex.equals(".jpg") || lastPrex.equals(".png") || lastPrex.equals(".tif") || lastPrex.equals(".gif")){
                    BufferedImage sourceImg = ImageIO.read(new FileInputStream(dest));
                    resultBean.setImgWidth(sourceImg.getWidth());
                    resultBean.setImgHeight(sourceImg.getHeight());
                }

                resultBean.setSuccess(true);
                resultBean.setOldFileName(oldFileName);
                resultBean.setFileName(fileName);
                resultBean.setFilePath(pyfilePath);
                resultBean.setFileRequestUrl(requestUrl);
            } catch (Exception e) {
                e.printStackTrace();
                resultBean.setSuccess(false);
                resultBean.setReturnInfo("Import Progress exception! "+e.getMessage());
            } finally {

            }
        }

        return resultBean;
    }

    /**
     * 导入文件
     * @param uploadFile
     * @return
     */
    @Override
    public ImportResultBean importFileUpload(MultipartFile uploadFile, String typeName){
        ImportResultBean resultBean = new ImportResultBean();

        String ocCode = null;
        String orgCode = null;
        if(UserInfoHolder.getUserInfo() != null && UserInfoHolder.getUserInfo().getCurrentOc() != null && UserInfoHolder.getUserInfo().getCurrentOrg() != null){
            ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
            orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        }
        // 获取临时上传文件目录
        String fileStoreMethod = sysconfGlobalParamService.getParamValueByGroupCode(SysconfGlobalConstant.SysconfGlobalGroupCode.FILE_STORE_METHOD,
                SysconfGlobalConstant.FILE_STORE_METHOD.FILE_STORE_METHOD,
                ocCode,orgCode);

        // 新建一个文件
        String originalFileName = uploadFile.getOriginalFilename();
        originalFileName.replaceAll("/", "\\");

        String lastPrex = originalFileName.substring(originalFileName.lastIndexOf("."),originalFileName.length());

        String filePath = "";
        String fileName = StringRandom.getStringRandom(20)+lastPrex;

        filePath = "/"+typeName+"/"+ DateUtil.getDateFormat(new Date(), DateUtil.DATA_PATTERN_8)+"/";

        //七牛存储
        if(fileStoreMethod.equals(SysconfGlobalConstant.SysconfGlobalGroupCode.QINIU_STORE_SECRET)){
            try{
                String requestUrl = uploadImage(uploadFile,filePath,fileName);
                resultBean.setSuccess(true);

                resultBean.setFilePath(requestUrl);
                resultBean.setFileRequestUrl(requestUrl);
            }catch (Exception e){
                e.printStackTrace();
                throw new FebsException("文件存储错误");
            }
        }

        //本地存储
        if(fileStoreMethod.equals(SysconfGlobalConstant.SysconfGlobalGroupCode.LOCATION_STORE_CONF)){

            String pyfilePath = this.physicalPath + filePath+ fileName;
            String requestUrl = this.requestFileDomain + filePath + fileName;

            File dest = new File(pyfilePath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                //将上传的文件写入新建的文件中
                uploadFile.transferTo(dest);
                //获取长度
                resultBean.setFileSize(dest.length());

                resultBean.setSuccess(true);

                resultBean.setFilePath(pyfilePath);
                resultBean.setFileRequestUrl(requestUrl);
            } catch (Exception e) {
                e.printStackTrace();
                resultBean.setSuccess(false);
                resultBean.setReturnInfo("Import Progress exception! "+e.getMessage());
            } finally {

            }
        }

        return resultBean;
    }

    /**
     * 导入图片
     * @param imgByte
     * @return
     */
    @Override
    public ImportResultBean importUpload(byte[] imgByte, String typeName) {
        ImportResultBean resultBean = new ImportResultBean();

        String ocCode = null;
        String orgCode = null;
        if (UserInfoHolder.getUserInfo() != null && UserInfoHolder.getUserInfo().getCurrentOc() != null && UserInfoHolder.getUserInfo().getCurrentOrg() != null) {
            ocCode = UserInfoHolder.getUserInfo().getCurrentOc().getOcCode();
            orgCode = UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode();
        }
        // 获取临时上传文件目录
        String fileStoreMethod = sysconfGlobalParamService.getParamValueByGroupCode(SysconfGlobalConstant.SysconfGlobalGroupCode.FILE_STORE_METHOD,
                SysconfGlobalConstant.FILE_STORE_METHOD.FILE_STORE_METHOD,
                ocCode, orgCode);

        String filePath = "";
        String fileName = StringRandom.getStringRandom(20) + ".jpg";

        filePath = "/" + typeName + "/" + DateUtil.getDateFormat(new Date(), DateUtil.DATA_PATTERN_8) + "/";

        //本地存储
        if (fileStoreMethod.equals(SysconfGlobalConstant.SysconfGlobalGroupCode.LOCATION_STORE_CONF)) {

            String pyfilePath = this.physicalPath + filePath + fileName;
            String requestUrl = this.requestDomain + filePath + fileName;

            try {
                File dest = new File(pyfilePath);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }

                InputStream inputStream = new ByteArrayInputStream(imgByte);
                Image img = ImageIO.read(inputStream);
                BufferedImage tag = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(img.getScaledInstance(img.getWidth(null), img.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);

                FileOutputStream out = new FileOutputStream(pyfilePath);
                // JPEGImageEncoder可适用于其他图片类型的转换
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(tag);
                out.close();

                resultBean.setSuccess(true);
                resultBean.setOldFileName(fileName);
                resultBean.setFileName(fileName);
                resultBean.setFilePath(pyfilePath);
                resultBean.setFileRequestUrl(requestUrl);
            } catch (IOException e) {
                e.printStackTrace();
                resultBean.setSuccess(false);
                resultBean.setReturnInfo("Import Progress exception! " + e.getMessage());
            }
        }
        return resultBean;
    }

    public String uploadImage(MultipartFile image,String filePath,String fileName) {
        UploadUtil uploadUtil = UploadFactory.createUpload(this.accesskey, this.secretKey,
                this.bucketHostName, this.bucketName);
        return uploadUtil.uploadFile(image,fileName/*, filePath*/);
    }

    public static void main(String[] args) {
        String accessKey = "y1TQwSwQNmN1y3kerbbE1pbB1kDuv-QIhdcJ4fgT";
        String secretKey = "ebaEVd01lrsYNItIF9vziSA02takXalhGouW926l";
        String bucket = "linlingo";

        Configuration configuration = new Configuration(Zone.zone0());
        UploadManager manager = new UploadManager(configuration);

        String key = "bbbb.jpg";

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        String localFilePath = "D:\\timg.jpg";


        Response response = null;
        try {
            response = manager.put(localFilePath, key, upToken);
            DefaultPutRet set = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(set.key);
            System.out.println(set.hash);

        } catch (QiniuException e) {
            Response r = e.response;
            System.err.println(r.toString());
        }
    }
}
