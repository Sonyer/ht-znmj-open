package com.ht.znmj.view.manager;

import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.entity.EquipmentInfoInOut;
import com.ht.znmj.entity.EquipmentInfoConnStatus;
import com.ht.znmj.view.bean.EquipmentMenuItem;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagerView {

    private Dialog managerDialog;
    /**
     * 父窗口
     */
    private JFrame parentFrame = null;

    private Panel functionPanel;

    /**
     * 设备管理
     * @param equipmentInfo
     */
    public ManagerView(JFrame parentFrame, EquipmentInfo equipmentInfo) {
        this.parentFrame = parentFrame;
        showView(equipmentInfo);
    }

    public void showView(EquipmentInfo equipmentInfo){
        parentFrame.setEnabled(false);

        managerDialog = new Dialog(parentFrame,"设备管理",false);
        managerDialog.setBounds(150, 150, 1450, 800);//设置大小位置
        //managerDialog.setAlwaysOnTop(true);
        managerDialog.setVisible(true);//使窗口可见
        managerDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                parentFrame.setEnabled(true);
                managerDialog.dispose();
            }
        });

        Panel topPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(managerDialog.getWidth(),35);
            }

            public void paint(Graphics g) {
                super.paint(g);

                Graphics g2=g.create();
                g2.setColor(Color.gray);

                g2.drawLine(0,0,getWidth(), 0);
                g2.drawLine(0,getHeight()-2,getWidth()-2, getHeight()-2);
                g2.drawLine(0,0,0,getHeight());
                g2.drawLine(getWidth()-2,0,getWidth()-2,getHeight()-2);
                g2.dispose();
            }
        };
        topPanel.setBounds(0,0,managerDialog.getWidth(),35);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        Font font = new Font("SimSun", 1, 15);
        topPanel.setFont(font);
        Label snLabel = new Label("SN:"+equipmentInfo.getSn());
        Label ipLabel = new Label("IP:"+equipmentInfo.getIp());
        Label areaNameLabel = new Label("区域:"+equipmentInfo.getAreaName());
        Label inOutLabel = new Label("位置:"+EquipmentInfoInOut.getNameByCode(equipmentInfo.getInOut()));
        Label statusLabel = new Label("状态:"+EquipmentInfoConnStatus.getNameByCode(equipmentInfo.getConnStatus()));
        if(equipmentInfo.getConnStatus().equals(EquipmentInfoConnStatus.CONNECTED.getCode())){
            statusLabel.setForeground(Color.GREEN);
        }else{
            statusLabel.setForeground(Color.RED);
        }

        topPanel.add(snLabel);
        topPanel.add(ipLabel);
        topPanel.add(areaNameLabel);
        topPanel.add(inOutLabel);
        topPanel.add(statusLabel);

        managerDialog.add(topPanel,BorderLayout.NORTH);


        Panel centerPanel = new Panel();
        centerPanel.setLayout(new BorderLayout());
        managerDialog.add(centerPanel,BorderLayout.CENTER);

        Panel menuPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(200,centerPanel.getHeight());
            }

            public void paint(Graphics g) {
                super.paint(g);

                Graphics g2=g.create();
                g2.setColor(Color.gray);

                g2.drawLine(0,0,getWidth(), 0);
                g2.drawLine(0,getHeight()-2,getWidth()-2, getHeight()-2);
                g2.drawLine(0,0,0,getHeight());
                g2.drawLine(getWidth()-2,0,getWidth()-2,getHeight()-2);
                g2.dispose();
            }
        };
        menuPanel.setBounds(0,0,200,centerPanel.getHeight());
        createMenuItem(menuPanel,equipmentInfo);
        centerPanel.add(menuPanel,BorderLayout.WEST);

        functionPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(centerPanel.getWidth()-200,centerPanel.getHeight());
            }

            public void paint(Graphics g) {
                super.paint(g);

                Graphics g2=g.create();
                g2.setColor(Color.gray);

                g2.drawLine(0,0,getWidth(), 0);
                g2.drawLine(0,getHeight()-2,getWidth()-2, getHeight()-2);
                g2.drawLine(0,0,0,getHeight());
                g2.drawLine(getWidth()-2,0,getWidth()-2,getHeight()-2);
                g2.dispose();
            }
        };
        functionPanel.setBounds(0,0,centerPanel.getWidth()-200,centerPanel.getHeight());
        centerPanel.add(functionPanel,BorderLayout.CENTER);
    }

    /**
     * 菜单清单
     */
    java.util.List<EquipmentMenuItem> menuItemList = new ArrayList<EquipmentMenuItem>();
    {
        menuItemList.add(new EquipmentMenuItem("设备设置","设备设置","images/common_icon/icon_menu_equipment.png",
                "com.ht.znmj.view.manager.equipment.EquipmentInforView"));
        menuItemList.add(new EquipmentMenuItem("人员信息","人员信息","images/common_icon/icon_menu_visitor.png",
                "com.ht.znmj.view.manager.visitor.VisitorMainView"));
        menuItemList.add(new EquipmentMenuItem("出入日志","出入日志","images/common_icon/icon_menu_visitorlog.png",
                "com.ht.znmj.view.manager.visitorlog.VisitorLogMainView"));
    }
    static Map<String,JLabel> menuLabelMap = new HashMap<>();

    /**
     * 菜单加载
     */
    public void createMenuItem(Panel parentPanel,EquipmentInfo equipmentInfo){
        parentPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        Font menuFont = new Font("SimSun", 1, 15);
        Border border = BorderFactory.createLineBorder(Color.BLACK);

        menuItemList.forEach(menuItem ->{
            JLabel menuLabel = new JLabel(menuItem.getMenuName()){
                public Dimension getPreferredSize() {
                    return new Dimension(200,40);
                }
                public void paint(Graphics g) {
                    super.paint(g);

                    Graphics g2=g.create();
                    g2.setColor(Color.gray);

                    g2.drawLine(0,0,getWidth(), 0);
                    g2.drawLine(0,getHeight()-2,getWidth()-2, getHeight()-2);
                    g2.drawLine(0,0,0,getHeight());
                    g2.drawLine(getWidth()-2,0,getWidth()-2,getHeight()-2);
                    g2.dispose();
                }
            };

            menuLabel.addMouseListener(new MouseListener() {
                @SneakyThrows
                @Override
                public void mouseClicked(MouseEvent e) {
                    clickMenu(menuItem.getId());

                    functionPanel.removeAll();

                    ManagerFunctionView managerFinctionPanel = (ManagerFunctionView)Class.forName(menuItem.getPagePath()).newInstance();
                    managerFinctionPanel.initView(managerDialog,functionPanel,equipmentInfo);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            menuLabel.setBounds(50,0,200,40);
            menuLabel.setFont(menuFont);
            ImageIcon equipmentIcon = new ImageIcon(menuItem.getIconPath());
            equipmentIcon.setImage(equipmentIcon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT ));
            menuLabel.setIcon(equipmentIcon);
            menuLabel.setBorder(border);

            parentPanel.add(menuLabel);

            menuLabelMap.put(menuItem.getId(),menuLabel);
        });

    }

    private void clickMenu(String id){
        menuLabelMap.keySet().forEach(labelId->{
            if(id.equals(labelId)){
                menuLabelMap.get(labelId).setEnabled(false);
            }else {
                menuLabelMap.get(labelId).setEnabled(true);
            }
        });
    }
}
