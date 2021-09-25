package com.ht.znmj.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 欢迎页
 */
public class WelcomeView extends JFrame {
    private String messageHeader = "欢迎进入【大诺门禁】...";

    private Label welcomeLabel;

    public WelcomeView() throws HeadlessException {
        this.setBounds(200, 200, 800, 470);//设置大小位置
        this.setLayout(null);
        this.setUndecorated(true);  //无标题栏


        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0,0,this.getWidth(),this.getHeight());

        ImageIcon background = new ImageIcon("images/welcome_img.jpg");//背景图片
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0,0,background.getIconWidth(),background.getIconHeight());
        backgroundPanel.add(backgroundLabel,new Integer(Integer.MIN_VALUE));
        this.add(backgroundPanel,new Integer(Integer.MIN_VALUE));

        JPanel textPanel = new JPanel();
        textPanel.setBackground(new Color(0, 0, 0, 0));
        textPanel.setLayout(null);
        textPanel.setBounds(0,0,this.getWidth(),35);
        Font font = new Font("SimSun", Font.ITALIC, 15);
        welcomeLabel = new Label();
        welcomeLabel.setBackground(new Color(0, 0, 0, 0));
        welcomeLabel.setBounds(0,0,this.getWidth(),35);
        welcomeLabel.setForeground(new Color(227, 255, 235));
        welcomeLabel.setFont(font);
        welcomeLabel.setText(messageHeader);
        //this.getLayeredPane().add(welcomeLabel,new Integer(Integer.MAX_VALUE));
        textPanel.add(welcomeLabel);
        this.add(textPanel,null);

        Toolkit toolkit = this.getToolkit();
        Image image = toolkit.createImage("images/logo.png");
        this.setIconImage(image);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //System.exit(0);
                setVisible(false);
            }
        });
        this.setVisible(true);//使窗口可见
    }


    public void setInitMessage(String message){
        welcomeLabel.setText(messageHeader+message);
    }

}
