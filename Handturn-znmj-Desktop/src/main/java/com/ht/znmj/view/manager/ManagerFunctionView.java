package com.ht.znmj.view.manager;

import com.ht.znmj.entity.EquipmentInfo;
import lombok.Data;

import java.awt.*;

/**
 * 反射加载设备管理菜单功能
 */
@Data
public class ManagerFunctionView{
    public Window parentFrame;

    public Panel parentPanel;

    public EquipmentInfo equipmentInfo;

    public void initView(Window parentFrame,Panel parentPanel,EquipmentInfo equipmentInfo){
        this.parentFrame = parentFrame;
        this.parentPanel = parentPanel;
        this.equipmentInfo = equipmentInfo;
    }
}
