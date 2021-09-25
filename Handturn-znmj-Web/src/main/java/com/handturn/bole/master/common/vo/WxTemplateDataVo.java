package com.handturn.bole.master.common.vo;

import lombok.Data;

/**
 * 微信模板
 */
@Data
public class WxTemplateDataVo {
    //字段值例如：keyword1：订单类型，keyword2：下单金额，keyword3：配送地址，keyword4：取件地址，keyword5备注
    private String value;//依次排下去
}
