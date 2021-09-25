package com.ht.znmj.app;

import com.ht.znmj.sdk.MyFunction;
import com.ht.znmj.view.home.HomeView;
import com.ht.znmj.view.service.ApplicationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ZnmjMain {
    JFrame rootFrame = new JFrame();
    private TrayIcon trayIcon = null;
    SystemTray tray = SystemTray.getSystemTray();

    public ZnmjMain() {   //在构造器中直接运行建立窗口的函数
        //数据库连接初始化
        //DbGlobal.init();
        //数据表更新
        //DbGlobal.updateDB();
        //加载人脸机接口
        MyFunction.Init();
        //加载主窗体
        initMainView();
    }

   /* public void init(){
        //加载人脸机接口
        FaceEquipmentApi.Init();
        //加载主窗体
        initMainView();
    }*/

    public void initMainView(){
        String version = ApplicationUtil.getApplicationProperties().getVersion();
        String bit = ApplicationUtil.getApplicationProperties().getBit();
        String bitName = bit+"位";
        String versionName = ApplicationUtil.getApplicationProperties().getCloudFlag().equals("1")?"云版":"基版";
        rootFrame.setTitle("大诺门禁 (V-"+version+"-"+versionName+"-"+bitName+")");
        rootFrame.setBounds(100, 100, 1200, 800);//设置大小位置
        //rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit toolkit = rootFrame.getToolkit();
        Image image = toolkit.createImage("images/logo.png");
        rootFrame.setIconImage(image);
        rootFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                miniTray();
                //MyFunction.Close();
            }
        });

        HomeView homeView = new HomeView();
        homeView.setView(rootFrame);

        rootFrame.setVisible(true);//使窗口可见
    }

    private void miniTray() {  //窗口最小化到任务栏托盘
        ImageIcon trayImg = new ImageIcon("images/logo.png");//托盘图标

        PopupMenu pop = new PopupMenu();  //增加托盘右击菜单
        MenuItem show = new MenuItem("还原");
        MenuItem exit = new MenuItem("退出");

        show.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { // 按下还原键
                tray.remove(trayIcon);
                rootFrame.setVisible(true);
                rootFrame.setExtendedState(JFrame.NORMAL);
                rootFrame.toFront();
            }
        });

        exit.addActionListener(new ActionListener() { // 按下退出键
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                MyFunction.Close();
                System.gc();
                System.exit(0);
            }
        });

        pop.add(show);
        pop.add(exit);

        trayIcon = new TrayIcon(trayImg.getImage(), "大诺门禁", pop);
        trayIcon.setImageAutoSize(true);

        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { // 鼠标器双击事件
                if (e.getClickCount() == 2) {

                    tray.remove(trayIcon); // 移去托盘图标
                    rootFrame.setVisible(true);
                    rootFrame.setExtendedState(JFrame.NORMAL); // 还原窗口
                    rootFrame.toFront();
                }
            }
        });

        try {
            tray.add(trayIcon);
        } catch (AWTException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ZnmjMain(); //创建一个窗口
    }
}
