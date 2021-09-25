package com.handturn.bole.front.api.visitorApply.service;

import com.handturn.bole.front.api.visitorApply.vo.VisitorApplyPageQueryResponseVo;
import com.handturn.bole.front.api.visitorApply.vo.VisitorApplySubmitRequestVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 访客申请
 */
public interface IVisitorApplyService {

    /**
     * 获取访客申请分页查询
     * @return
     */
    VisitorApplyPageQueryResponseVo initVisitorApply(String curAccountCode,String orgCode,String ocCode,String authAreaCode);

    /**
     * 上传图片
     * @param file
     * @param accountCode
     * @param token
     * @return
     */
    String uploadVisitorImg(MultipartFile file, String accountCode, String token);

    /**
     * 提交审核
     * @param request
     * @param requestVo
     * @return
     */
    Boolean submitVisitorApply(HttpServletRequest request, VisitorApplySubmitRequestVo requestVo);

}
