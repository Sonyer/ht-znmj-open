package com.handturn.bole.front.api.visitorApply.vo;

import lombok.Data;

import java.util.List;

@Data
public class VisitorApplyPageQueryResponseVo {
    /**
     * 默认发布信息为第一页
     */
    private Integer pageIndex = 1;

    private List<String> wxNotifyTemplatIds;

    /**
     * 区域权限返回信息
     */
    private List<VisitorApplyResponseVo> pageDatas;
}
