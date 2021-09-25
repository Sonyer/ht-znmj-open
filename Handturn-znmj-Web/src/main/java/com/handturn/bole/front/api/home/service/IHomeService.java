package com.handturn.bole.front.api.home.service;

import com.handturn.bole.front.api.home.vo.AuthAreaPageQueryResponseVo;

/**
 * 微信相关
 */
public interface IHomeService {

    /**
     * 获取区域权限分页查询
     * @return
     */
    AuthAreaPageQueryResponseVo authAreaPageQuery(String pageIndex,String searchValue, String curAccountCode);





}
