package com.handturn.bole.front.api.home.vo;

import lombok.Data;

import java.util.List;

@Data
public class AuthAreaPageQueryResponseVo {
    /**
     * 默认发布信息为第一页
     */
    private Integer pageIndex = 1;

    /**
     * 区域权限返回信息
     */
    private List<AuthAreaResponseVo> pageDatas;
}
