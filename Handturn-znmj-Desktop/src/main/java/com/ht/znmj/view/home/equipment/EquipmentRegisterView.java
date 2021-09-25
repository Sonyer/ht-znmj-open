package com.ht.znmj.view.home.equipment;

import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.entity.EquipmentInfoConnStatus;
import com.ht.znmj.entity.EquipmentInfoInOut;
import com.ht.znmj.view.components.DialogFrame;
import com.ht.znmj.view.components.TablePagePanel;
import com.ht.znmj.view.home.HomeMenuItemView;
import com.ht.znmj.view.service.HomeEquipmentUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * 人员主界面
 */
public class EquipmentRegisterView extends HomeMenuItemView {

    private EquipmentRegisterView curentThis;

    private Dialog owerDialog;
    /**
     * 组件
     */
    private TablePagePanel<EquipmentInfo> tablePagePanel;
    private TextArea licenseText;


    @Override
    public void initView(Window parentFrame) {
        super.initView(parentFrame);
        curentThis = this;
        initContent();
    }

    public void initContent(){
        parentFrame.setEnabled(false);

        owerDialog = null;
        if(parentFrame instanceof JFrame){
            owerDialog = new Dialog((JFrame)parentFrame,"设备许可证",false);
        }else if(parentFrame instanceof Dialog){
            owerDialog = new Dialog((Dialog)parentFrame,"设备许可证",false);
        }else{
            owerDialog = new Dialog((Frame)parentFrame,"设备许可证",false);
        }

        int x = parentFrame.getX() + (parentFrame.getWidth()-parentFrame.getX())/2-300;
        int y = parentFrame.getY() + (parentFrame.getHeight()-parentFrame.getY())/2-200;
        owerDialog.setBounds(x, y, 950, 370);//设置大小位置

        Panel contentPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(owerDialog.getWidth()-20, owerDialog.getHeight());
            }
        };

        Panel leftPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(320, owerDialog.getHeight());
            }
        };
        leftPanel.setLayout(new FlowLayout());

        Panel licensePanel = new Panel();
        Font font = new Font("SimSun", 1, 12);
        licensePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label remarkLabel = new Label("许可证:");
        remarkLabel.setFont(font);
        licenseText = new TextArea();
        licenseText.setRows(13);
        licenseText.setColumns(30);
        licenseText.setFont(font);
        licenseText.setEnabled(true);
        licensePanel.add(remarkLabel);
        licensePanel.add(licenseText);
        leftPanel.add(licensePanel);

        Button registerButton = new Button("注册");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String license = licenseText.getText();
                if(StringUtils.isEmpty(license.trim())){
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(owerDialog,"请填写许可证!",DialogFrame.TYPE_WARN,null);
                    return;
                }
                try {
                    String errorMessage = HomeEquipmentUtil.registerEquipmentInfo(license);
                    if(!StringUtils.isEmpty(errorMessage)){
                        DialogFrame dialogFrame = new DialogFrame();
                        dialogFrame.showMessageDialog(owerDialog,errorMessage,DialogFrame.TYPE_WARN,null);
                    }else{
                        DialogFrame dialogFrame = new DialogFrame();
                        dialogFrame.showMessageDialog(owerDialog,"注册成功!",DialogFrame.TYPE_INFO,null);
                    }

                    tablePagePanel.flushData();
                }catch (Exception e1){
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(owerDialog,e1.getMessage(),DialogFrame.TYPE_ERROR,null);
                }
            }
        });
        leftPanel.add(registerButton);

        String noteText = "<html><p>1.请联系厂商获取许可证;</p>" +
                "<p>2.多许可证注册,需要换行。</p>" ;
        JLabel noticeLabel = new JLabel(noteText){
            public Dimension getPreferredSize() {
                return new Dimension(300, 40);
            }
        };
        noticeLabel.setFont(font);
        leftPanel.add(noticeLabel);
        owerDialog.add(leftPanel,BorderLayout.WEST);


        tablePagePanel = new TablePagePanel<EquipmentInfo>(){
            @Override
            public void flushData() {
                //实现分页查询数据
                flushPageDate();
            }

            public Dimension getPreferredSize() {
                return new Dimension(600, 350);
            }

            public void paint(Graphics g) {
                super.paint(g);

                Graphics g2 = g.create();
                g2.setColor(Color.gray);

                g2.drawLine(0, 0, getWidth(), 0);
                g2.drawLine(0, getHeight() - 2, getWidth() - 2, getHeight() - 2);
                g2.drawLine(0, 0, 0, getHeight());
                g2.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight() - 2);
                g2.dispose();
            }
        };
        tablePagePanel.setBounds(0,0,600, 350);

        StringBuffer inOutMapJson = new StringBuffer("{");
        inOutMapJson.append("'"+ EquipmentInfoInOut.NONE.getCode()+"'").append(":'未知位置',");
        inOutMapJson.append("'"+EquipmentInfoInOut.IN.getCode()+"'").append(":'入口',");
        inOutMapJson.append("'"+EquipmentInfoInOut.OUT.getCode()+"'").append(":'出口'");
        inOutMapJson.append("}");

        StringBuffer statusMapJson = new StringBuffer("{");
        statusMapJson.append("'"+ EquipmentInfoConnStatus.NO_CONNECT.getCode()+"'").append(":'未连接',");
        statusMapJson.append("'"+ EquipmentInfoConnStatus.CONNECTED.getCode()+"'").append(":'已连接'");
        statusMapJson.append("}");
        TablePagePanel.ColumnBean columnBean1 = new TablePagePanel.ColumnBean("sn", "设备号", 80,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean2 = new TablePagePanel.ColumnBean("areaName", "区域名称", 50,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean3 = new TablePagePanel.ColumnBean("seqNum", "编号", 20,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean4 = new TablePagePanel.ColumnBean("inOut", "出入口", 30,TablePagePanel.ColumnBean.TYPE_STRING,inOutMapJson.toString());
        TablePagePanel.ColumnBean columnBean5 = new TablePagePanel.ColumnBean("connStatus", "连接状态", 40,TablePagePanel.ColumnBean.TYPE_STRING,statusMapJson.toString());
        java.util.List<TablePagePanel.ColumnBean> columnBeanList = new ArrayList<>();
        columnBeanList.add(columnBean1);
        columnBeanList.add(columnBean2);
        columnBeanList.add(columnBean3);
        columnBeanList.add(columnBean4);
        columnBeanList.add(columnBean5);
        tablePagePanel.initView(parentFrame,false);
        tablePagePanel.setColumnBeanList(columnBeanList);
        //自动加载页面
        tablePagePanel.flushData();

        contentPanel.add(tablePagePanel,BorderLayout.SOUTH);

        //owerDialog.setAlwaysOnTop(true);
        //owerDialog.setResizable(false);
        owerDialog.setVisible(true);
        owerDialog.add(contentPanel,BorderLayout.CENTER);
        Dialog finalOwerDialog = owerDialog;
        owerDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                parentFrame.setEnabled(true);
                finalOwerDialog.dispose();
            }
        });
    }

    /**
     * 页面刷新
     */
    private void flushPageDate(){
        java.util.List<EquipmentInfo> datas = HomeEquipmentUtil.findAllEquipmentInfos();

        //表格赋值
        tablePagePanel.setData(datas,datas.size());
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
