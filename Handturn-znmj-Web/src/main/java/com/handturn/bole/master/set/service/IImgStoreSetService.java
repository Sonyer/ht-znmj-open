package com.handturn.bole.master.set.service;

import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.master.set.vo.ImgStoreSetVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 智能门禁-站点配置 Service接口
 *
 * @author Eric
 * @date 2020-02-28 09:41:18
 */
public interface IImgStoreSetService {

    //轮播图
    static final String IMGS_ROTATION = "imgs_rotation";
    static final String IMGS_PUBLISH = "imgs_publish";
    static final String IMGS_ME_AVATAR = "imgs_me_avatar";
    static final String IMGS_ME_CERTIFICATION = "imgs_me_certification";

    static final String IMGS_ITEM = "imgs_item";

    static final String FILE_PLATFORM = "file_platform";

    static final String IMGS_VISITOR_ITEM = "imgs_visitor_item";

    static final String IMGS_ORG_LOGO = "imgs_org_logo";
    static final String IMGS_VISITOR_APPLY_FACE = "imgs_visitor_apply_face";
    static final String IMGS_VISITOR_LOG_FACE = "imgs_visitor_log_face";

    /**
     * 获取图潘存储配置
     * @return
     */
    ImgStoreSetVo getImgStoreSet();
    /**
     * 导入图片
     * @param uploadFile
     * @return
     */
    ImportResultBean importUpload(MultipartFile uploadFile, String typeName);

    /**
     * 导入文件
     * @param uploadFile
     * @return
     */
    ImportResultBean importFileUpload(MultipartFile uploadFile, String typeName);

    /**
     * 导入图片
     * @param imgByte
     * @return
     */
    ImportResultBean importUpload(byte[] imgByte, String typeName);
}
