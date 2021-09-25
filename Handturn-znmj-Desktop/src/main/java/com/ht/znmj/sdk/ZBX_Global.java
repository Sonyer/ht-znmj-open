package com.ht.znmj.sdk;

import com.ht.znmj.entity.EquipmentInfo;
import com.sun.jna.ptr.IntByReference;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ZBX_Global {
    //有效连接相机数量
    public final String QUIQPMENT_COUNT_ACTIVE = "QUIQPMENT_COUNT_ACTIVE";
    public final List<EquipmentInfo> EQUIPMENT_LIST_ACTIVE_MAP = new ArrayList<>();
    //设备连接成功标记 键值对
    public static Map<String, IntByReference> EQUIPMENT_CONNECT_REFERENCE_MAP = new HashMap<>();
    public static Map<String, String> EQUIPMENT_IP_SN_MAP = new HashMap<>();
    //无效连接相机数量
    public final String QUIQPMENT_COUNT_UNACTIVE = "QUIQPMENT_COUNT_UNACTIVE";
    public final List<EquipmentInfo> EQUIPMENT_LIST_UNACTIVE_MAP = new ArrayList<>();
    //其他参数
    public final Map<String,Object> otherGlobalMap = new HashMap<>();
}
