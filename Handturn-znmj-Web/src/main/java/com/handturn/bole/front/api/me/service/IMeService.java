package com.handturn.bole.front.api.me.service;

import com.handturn.bole.front.api.me.vo.InitMeResponseVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 我的
 */
public interface IMeService {
    /**
     * 获取我的信息
     * @param request
     * @return
     */
    InitMeResponseVo initMe(HttpServletRequest request);

    /**
     * 修改昵称
     * @param request
     * @param nickName
     * @return
     */
    Boolean updateNickName(HttpServletRequest request, String nickName);

    /**
     * 上传个人图片
     * @param file
     * @param accountCode
     * @param token
     * @return
     */
    String uploadMeAvatar(MultipartFile file, String accountCode, String token);

}
