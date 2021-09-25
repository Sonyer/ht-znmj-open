package com.ht.znmj.view.home;

import lombok.Data;

import java.awt.*;

@Data
public class HomeMenuItemView {
    public Window parentFrame;

    public void initView(Window parentFrame){
        this.parentFrame = parentFrame;
    }
}
