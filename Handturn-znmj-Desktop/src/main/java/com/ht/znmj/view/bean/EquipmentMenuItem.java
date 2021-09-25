package com.ht.znmj.view.bean;

import lombok.Data;

/**
 * 菜单清单
 */
@Data
public class EquipmentMenuItem {
    private String id;
    private String menuName;
    private String iconPath;
    private String pagePath;

    public EquipmentMenuItem(String id,String menuName, String iconPath, String pagePath) {
        this.id = id;
        this.menuName = menuName;
        this.iconPath = iconPath;
        this.pagePath = pagePath;
    }
}
