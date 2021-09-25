package com.ht.znmj.view.bean;

import lombok.Data;

import java.util.List;

/**
 * 菜单清单
 */
@Data
public class HomeMenuItem {
    public static final String DIR = "DIR";
    public static final String NODE = "NODE";

    private String id;
    private String menuName;
    private String iconPath;
    private String pagePath;
    private String menuType;
    private List<HomeMenuItem> childMenu;
    private String cloudFlag;

    public HomeMenuItem(String id, String menuName, String iconPath, String pagePath,String menuType,List<HomeMenuItem> childMenu,String cloudFlag) {
        this.id = id;
        this.menuName = menuName;
        this.iconPath = iconPath;
        this.pagePath = pagePath;
        this.menuType = menuType;
        this.childMenu = childMenu;
        this.cloudFlag = cloudFlag;
    }
}
