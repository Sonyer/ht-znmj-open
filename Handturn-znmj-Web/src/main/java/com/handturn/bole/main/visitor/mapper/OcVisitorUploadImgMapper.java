package com.handturn.bole.main.visitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.main.visitor.entity.OcVisitorUploadImg;
import org.springframework.data.repository.query.Param;

/**
 * 网点-访客上传图片信息 Mapper
 *
 * @author Eric
 * @date 2020-09-26 08:34:23
 */
public interface OcVisitorUploadImgMapper extends BaseMapper<OcVisitorUploadImg> {

    ICustomPage<OcVisitorUploadImg> findForPage(CustomPage page, @Param("ocVisitorUploadImg") OcVisitorUploadImg ocVisitorUploadImg);

}
