package com.ht.znmj.view.home.system;

import com.ht.znmj.entity.CloudInterfaceInfo;
import com.ht.znmj.entity.EquipmentInfoConnStatus;
import com.ht.znmj.view.components.DialogFrame;
import com.ht.znmj.view.home.HomeMenuItemView;
import com.ht.znmj.view.service.CloudInterfaceInfoUtil;
import com.ht.znmj.view.service.WebSocketUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 人员主界面
 */
public class InterfaceConfView extends HomeMenuItemView {
    private Dialog owerDialog;
    private InterfaceConfView curentThis;

    /**
     * 组件
     */
    private TextField interfaceUrlText;
    private TextField ocCodeText;
    private TextField accountCodeText;
    private TextField passwordText;
    private Label connStatusText;


    private Button connectButton;
    private Button closeButton;
    private Button reAsyButton;

    @Override
    public void initView(Window parentFrame) {
        super.initView(parentFrame);
        curentThis = this;
        initContent();
    }

    public void initContent(){
        parentFrame.setEnabled(false);

        //获取第一个
        CloudInterfaceInfo cloudInterfaceInfo = CloudInterfaceInfoUtil.findCloudInterfaceInfoById("1");

        if(parentFrame instanceof JFrame){
            owerDialog = new Dialog((JFrame)parentFrame,"接口配置",false);
        }else if(parentFrame instanceof Dialog){
            owerDialog = new Dialog((Dialog)parentFrame,"接口配置",false);
        }else{
            owerDialog = new Dialog((Frame)parentFrame,"接口配置",false);
        }

        int x = parentFrame.getX() + (parentFrame.getWidth()-parentFrame.getX())/2-300;
        int y = parentFrame.getY() + (parentFrame.getHeight()-parentFrame.getY())/2-200;
        owerDialog.setBounds(x, y, 500, 400);//设置大小位置

        Panel centerEntityPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(500, 400);
            }
        };
        centerEntityPanel.setBounds(0,0,500, 400);
        centerEntityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        Font font = new Font("SimSun", 1, 12);
        Panel interfaceUrlPanel = new Panel();
        interfaceUrlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label interfaceUrlLabel = new Label("*接口URL:");
        interfaceUrlLabel.setFont(font);
        interfaceUrlText = new TextField(40);
        interfaceUrlText.setFont(font);
        interfaceUrlText.setText(cloudInterfaceInfo == null?"": StringUtils.isEmpty(cloudInterfaceInfo.getUrl())?"":cloudInterfaceInfo.getUrl());
        interfaceUrlText.setEnabled(true);
        interfaceUrlPanel.add(interfaceUrlLabel);
        interfaceUrlPanel.add(interfaceUrlText);
        centerEntityPanel.add(interfaceUrlPanel);

        Panel ocCodePanel = new Panel();
        ocCodePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label ocCodeLabel = new Label("*网点密钥:");
        ocCodeLabel.setFont(font);
        ocCodeText = new TextField(40);
        ocCodeText.setFont(font);
        ocCodeText.setText(cloudInterfaceInfo == null?"": StringUtils.isEmpty(cloudInterfaceInfo.getOcCode())?"":cloudInterfaceInfo.getOcCode());
        ocCodeText.setEnabled(true);
        ocCodePanel.add(ocCodeLabel);
        ocCodePanel.add(ocCodeText);
        centerEntityPanel.add(ocCodePanel);

        Panel accountCodePanel = new Panel();
        accountCodePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label accountCodeLabel = new Label("*账        号:");
        accountCodeLabel.setFont(font);
        accountCodeText = new TextField(40);
        accountCodeText.setFont(font);
        accountCodeText.setText(cloudInterfaceInfo == null?"": StringUtils.isEmpty(cloudInterfaceInfo.getAccountCode())?"":cloudInterfaceInfo.getAccountCode());
        accountCodeText.setEnabled(true);
        accountCodePanel.add(accountCodeLabel);
        accountCodePanel.add(accountCodeText);
        centerEntityPanel.add(accountCodePanel);

        Panel passwordPanel = new Panel();
        passwordPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label passwordLabel = new Label("*密        码:");
        passwordLabel.setFont(font);
        passwordText = new TextField(40);
        passwordText.setFont(font);
        passwordText.setText(cloudInterfaceInfo == null?"": StringUtils.isEmpty(cloudInterfaceInfo.getPassword())?"":cloudInterfaceInfo.getPassword());
        passwordText.setEchoChar("*".charAt(0));
        passwordText.setEnabled(true);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordText);
        centerEntityPanel.add(passwordPanel);

        Panel connStatusPanel = new Panel();
        connStatusPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label connStatusLabel = new Label("连接状态:");
        connStatusLabel.setFont(font);
        connStatusText = new Label("未连接");
        connStatusText.setFont(font);
        connStatusText.setText(cloudInterfaceInfo == null?"未连接": StringUtils.isEmpty(cloudInterfaceInfo.getPassword())?"未连接":cloudInterfaceInfo.getPassword());
        connStatusText.setEnabled(true);
        connStatusPanel.add(connStatusLabel);
        connStatusPanel.add(connStatusText);
        centerEntityPanel.add(connStatusPanel);

        Panel bottomEntityPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(parentFrame.getWidth(), 40);
            }
        };
        bottomEntityPanel.setBounds(0,0,parentFrame.getWidth(), 40);

        connectButton = new Button("连接");
        connectClickListener(connectButton);
        connectButton.setFont(font);
        bottomEntityPanel.add(connectButton);
        closeButton = new Button("断开");
        closeClickListener(closeButton);
        closeButton.setFont(font);
        bottomEntityPanel.add(closeButton);
        reAsyButton = new Button("重新同步");
        reAsyClickListener(reAsyButton);
        reAsyButton.setFont(font);
        bottomEntityPanel.add(reAsyButton);
        bottomEntityPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        connectStatusShow(cloudInterfaceInfo,null);


        owerDialog.setVisible(true);
        owerDialog.add(centerEntityPanel,BorderLayout.CENTER);
        owerDialog.add(bottomEntityPanel,BorderLayout.SOUTH);
        owerDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                parentFrame.setEnabled(true);
                owerDialog.dispose();
            }
        });
    }

    /**
     * 连接按钮
     */
    private void connectClickListener(Button connectButton){
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(StringUtils.isEmpty(interfaceUrlText.getText().trim()) || StringUtils.isEmpty(ocCodeText.getText().trim()) || StringUtils.isEmpty(accountCodeText.getText().trim()) || StringUtils.isEmpty(passwordText.getText().trim())){
                    DialogFrame saveMessage = new DialogFrame();
                    saveMessage.showMessageDialog(owerDialog,"连接URL、网点密钥、账号、密码必填!",DialogFrame.TYPE_WARN,null);
                    return;
                }

                connectButton.setEnabled(false);
                connStatusText.setText("连接中...");

                CloudInterfaceInfo cloudInterfaceInfo = CloudInterfaceInfoUtil.findCloudInterfaceInfoById("1");
                if(cloudInterfaceInfo == null){
                    cloudInterfaceInfo = new CloudInterfaceInfo();
                }
                cloudInterfaceInfo.setUrl(interfaceUrlText.getText().trim());
                cloudInterfaceInfo.setOcCode(ocCodeText.getText().trim());
                cloudInterfaceInfo.setAccountCode(accountCodeText.getText().trim());
                cloudInterfaceInfo.setPassword(passwordText.getText().trim());

                //连接服务端
                WebSocketUtil.connectWebSocketServer(cloudInterfaceInfo);

                sleep(4000);

                //重新获取最新
                CloudInterfaceInfo cloudInterfaceInfonew = CloudInterfaceInfoUtil.findCloudInterfaceInfoById("1");
                connectStatusShow(cloudInterfaceInfonew,null);

                connectButton.setEnabled(true);
            }
        });
    }

    private static void sleep(int second){
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开按钮
     */
    private void closeClickListener(Button closeButton){
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                closeButton.setEnabled(false);
                connStatusText.setText("中断中...");

                CloudInterfaceInfo cloudInterfaceInfo = CloudInterfaceInfoUtil.findCloudInterfaceInfoById("1");
                if(cloudInterfaceInfo == null){
                    cloudInterfaceInfo = new CloudInterfaceInfo();
                }
                cloudInterfaceInfo.setUrl(interfaceUrlText.getText().trim());
                cloudInterfaceInfo.setAccountCode(accountCodeText.getText().trim());
                cloudInterfaceInfo.setPassword(passwordText.getText().trim());

                //连接服务端
                WebSocketUtil.closeWebSocketServer(cloudInterfaceInfo);

                sleep(4000);

                connectStatusShow(cloudInterfaceInfo,null);

                closeButton.setEnabled(true);
            }
        });
    }
    /**
     * 重新同步
     */
    private void reAsyClickListener(Button connectButton){
        connectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CloudInterfaceInfo cloudInterfaceInfo = CloudInterfaceInfoUtil.findCloudInterfaceInfoById("1");
                if(cloudInterfaceInfo == null){
                    DialogFrame saveMessage = new DialogFrame();
                    saveMessage.showMessageDialog(owerDialog,"云端还未连接!",DialogFrame.TYPE_WARN,null);
                    return;
                }
                WebSocketUtil.sendAsyApply();
            }
        });
    }

    /**
     * 连接状态
     * @param cloudInterfaceInfo
     */
    private void connectStatusShow(CloudInterfaceInfo cloudInterfaceInfo,String message){
        if(cloudInterfaceInfo != null && !StringUtils.isEmpty(cloudInterfaceInfo.getConnStatus()) && cloudInterfaceInfo.getConnStatus().equals(EquipmentInfoConnStatus.CONNECTED.getCode())){
            interfaceUrlText.setEnabled(false);
            ocCodeText.setEnabled(false);
            accountCodeText.setEnabled(false);
            passwordText.setEnabled(false);

            connStatusText.setText("已连接");
            connStatusText.setForeground(Color.GREEN);
            connectButton.setVisible(false);
            closeButton.setVisible(true);
            reAsyButton.setVisible(true);
        }else{
            interfaceUrlText.setEnabled(true);
            ocCodeText.setEnabled(true);
            accountCodeText.setEnabled(true);
            passwordText.setEnabled(true);

            connStatusText.setText(StringUtils.isEmpty(message)?"未连接":message);
            connStatusText.setForeground(Color.RED);
            connectButton.setVisible(true);
            closeButton.setVisible(false);
            reAsyButton.setVisible(false);
        }
        owerDialog.revalidate();
    }

    /**
     * 下拉框键值
     */
    class ItemRenderer extends BasicComboBoxRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value != null) {
                Item item = (Item) value;
                setText(item.getValue()+"-"+ item.getText());
            }

            if (index == -1) {
                Item item = (Item) value;
                setText(item.getValue()+"-"+ item.getText());
            }

            return this;
        }
    }
    /**
     * 下拉框对象
     */
    @Data
    private class Item{
        private String value;
        private String text;

        public Item(String value, String text) {
            this.value = value;
            this.text = text;
        }
    }

}
