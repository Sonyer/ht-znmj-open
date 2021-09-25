package com.handturn.bole.master.set.vo;

import lombok.Data;

@Data
public class ImgStoreSetVo {
    public String storeMethod;

    //七牛
    public String access_key;

    public String secret_key;

    public String bucket;

    //本地
    public String physical_path;
    public String request_url;
}
