package com.ht.znmj.view.home.visitor;

import com.ht.znmj.entity.*;
import com.ht.znmj.view.components.DialogFrame;
import com.ht.znmj.view.components.TablePagePanel;
import com.ht.znmj.view.service.HomeEquipmentUtil;
import com.ht.znmj.view.service.ManagerVisitorEquipmentUtil;
import lombok.Data;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.*;

/**
 * 人员编辑界面
 */
public class VisitorHomeAuthDialog {
    private VisitorHomeView visitorHomeView;
    private Window parentFrame;
    private TablePagePanel tablePagePanel;
    private TablePagePanel ownerTablePagePanel;
    private Dialog owerDialog;

    /**
     * 输入组件
     */
    private JComboBox authTypeBox;
    private TextField filePathText;
    private TextArea errorText;

    /**
     * 展示页面
     * @param parentFrame
     */
    public void showDialog(VisitorHomeView visitorHomeView,Window parentFrame, TablePagePanel tablePagePanel){
        parentFrame.setEnabled(false);

        this.visitorHomeView = visitorHomeView;
        this.parentFrame = parentFrame;
        this.tablePagePanel = tablePagePanel;

        String tittle = "人员备份恢复";

        if(parentFrame instanceof JFrame){
            owerDialog = new Dialog((JFrame)parentFrame,tittle,false);
        }else if(parentFrame instanceof Dialog){
            owerDialog = new Dialog((Dialog)parentFrame,tittle,false);
        }else{
            owerDialog = new Dialog((Frame)parentFrame,tittle,false);
        }

        Panel centerEntityPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(600, 450);
            }
        };
        centerEntityPanel.setBounds(0,0,600, 450);
        centerEntityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        Font font = new Font("SimSun", 1, 12);

        Panel authTypePanel = new Panel();
        authTypePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label authTypeLabel = new Label("授权方式:");
        authTypeLabel.setFont(font);
        Vector authTypeModel = new Vector();
        authTypeModel.addElement(new Item(VisitorInfoAuthType.CHECK.getCode(), "按选择人员"));
        authTypeModel.addElement(new Item(VisitorInfoAuthType.QUERY.getCode(), "按查询人员"));
        authTypeModel.addElement(new Item(VisitorInfoAuthType.ALL.getCode(), "按全量人员"));
        authTypeBox=new JComboBox(authTypeModel);    //创建JComboBox
        authTypeBox.setRenderer(new ItemRenderer());
        authTypePanel.add(authTypeLabel);
        authTypePanel.add(authTypeBox);

        centerEntityPanel.add(authTypePanel);


        ownerTablePagePanel = new TablePagePanel<EquipmentInfo>(){
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
        ownerTablePagePanel.setBounds(0,0,600, 350);

        StringBuffer inOutMapJson = new StringBuffer("{");
        inOutMapJson.append("'"+EquipmentInfoInOut.NONE.getCode()+"'").append(":'未知位置',");
        inOutMapJson.append("'"+EquipmentInfoInOut.IN.getCode()+"'").append(":'入口',");
        inOutMapJson.append("'"+EquipmentInfoInOut.OUT.getCode()+"'").append(":'出口'");
        inOutMapJson.append("}");

        StringBuffer statusMapJson = new StringBuffer("{");
        statusMapJson.append("'"+EquipmentInfoConnStatus.NO_CONNECT.getCode()+"'").append(":'未连接',");
        statusMapJson.append("'"+EquipmentInfoConnStatus.CONNECTED.getCode()+"'").append(":'已连接'");
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
        ownerTablePagePanel.initView(parentFrame,false);
        ownerTablePagePanel.setColumnBeanList(columnBeanList);
        //自动加载页面
        ownerTablePagePanel.flushData();
        centerEntityPanel.add(ownerTablePagePanel);


        Panel bottomEntityPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(parentFrame.getWidth(), 40);
            }
        };
        bottomEntityPanel.setBounds(0,0,parentFrame.getWidth(), 40);

        Button authButton = new Button("授权确认");
        authButton.setFont(font);
        authButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authConfirm();
            }
        });
        bottomEntityPanel.add(authButton);

        Button authCancelButton = new Button("取消授权确认");
        authCancelButton.setFont(font);
        authCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authCancelConfirm();
            }
        });
        bottomEntityPanel.add(authCancelButton);
        bottomEntityPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        int x = parentFrame.getX() + (parentFrame.getWidth()-parentFrame.getX())/2-300;
        int y = parentFrame.getY() + (parentFrame.getHeight()-parentFrame.getY())/2-200;
        owerDialog.setBounds(x, y, 600, 450);//设置大小位置
        //owerDialog.setAlwaysOnTop(true);
        owerDialog.setVisible(true);
        owerDialog.add(centerEntityPanel,BorderLayout.CENTER);
        owerDialog.add(bottomEntityPanel,BorderLayout.SOUTH);
        Dialog finalOwerDialog = owerDialog;
        owerDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                parentFrame.setEnabled(true);
                finalOwerDialog.dispose();
            }
        });
    }

    /**
     * 授权确认
     */
    private void authConfirm(){
        Item item = (Item)authTypeBox.getSelectedItem();
        List<EquipmentInfo> equipmentInfoChecks = ownerTablePagePanel.getSelectedRows();

        Set<String> equipmentIdSet = new HashSet<>();
        if(equipmentInfoChecks == null || equipmentInfoChecks.size() <=0){
            DialogFrame dialogFrame = new DialogFrame();
            dialogFrame.showMessageDialog(owerDialog,"请选择连接状态的设备信息!",DialogFrame.TYPE_WARN,null);
            return;
        }else{
            equipmentInfoChecks.forEach(equipmentInfoCheck ->{
                if(!equipmentInfoCheck.getConnStatus().equals(EquipmentInfoConnStatus.CONNECTED.getCode())){
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(owerDialog,"请选择连接状态的设备信息!",DialogFrame.TYPE_WARN,null);
                    return;
                }

                equipmentIdSet.add(equipmentInfoCheck.getId());
            });
        }

        try {
            //全量
            if (item.getValue().equals(VisitorInfoAuthType.ALL.getCode())) {
                ManagerVisitorEquipmentUtil.authAllVisitorEquipments(equipmentIdSet);
            }
            //查询
            if (item.getValue().equals(VisitorInfoAuthType.QUERY.getCode())) {
                VisitorInfo visitoryInfo_query = visitorHomeView.getQueryCondition();
                ManagerVisitorEquipmentUtil.authQueryVisitorEquipments(visitoryInfo_query, equipmentIdSet);
            }
            //选择
            if (item.getValue().equals(VisitorInfoAuthType.CHECK.getCode())) {
                List<VisitorInfo> visitorObjs = tablePagePanel.getSelectedRows();
                if (visitorObjs == null || visitorObjs.size() <= 0) {
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(owerDialog, "请选择人员列表选择信息!", DialogFrame.TYPE_WARN,null);
                    return;
                } else {
                    Set<String> visitorIds = new HashSet<>();
                    visitorObjs.forEach(visitorObj -> {
                        visitorIds.add(visitorObj.getId());
                    });
                    ManagerVisitorEquipmentUtil.authVisitorIdEquipments(visitorIds, equipmentIdSet);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            DialogFrame dialogFrame = new DialogFrame();
            dialogFrame.showMessageDialog(owerDialog, "执行异常!"+e.getMessage(), DialogFrame.TYPE_WARN,null);
            return;
        }

        /**
         * 刷新父表格
         */
        tablePagePanel.flushData();
    }

    /**
     * 取消授权确认
     */
    private void authCancelConfirm(){
        Item item = (Item)authTypeBox.getSelectedItem();
        List<EquipmentInfo> equipmentInfoChecks = ownerTablePagePanel.getSelectedRows();

        Set<String> equipmentIdSet = new HashSet<>();
        if(equipmentInfoChecks == null || equipmentInfoChecks.size() <=0){
            DialogFrame dialogFrame = new DialogFrame();
            dialogFrame.showMessageDialog(owerDialog,"请选择连接状态的设备信息!",DialogFrame.TYPE_WARN,null);
            return;
        }else{
            equipmentInfoChecks.forEach(equipmentInfoCheck ->{
                if(!equipmentInfoCheck.getConnStatus().equals(EquipmentInfoConnStatus.CONNECTED.getCode())){
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(owerDialog,"请选择连接状态的设备信息!",DialogFrame.TYPE_WARN,null);
                    return;
                }

                equipmentIdSet.add(equipmentInfoCheck.getId());
            });
        }

        try {
            //全量
            if (item.getValue().equals(VisitorInfoAuthType.ALL.getCode())) {
                ManagerVisitorEquipmentUtil.authCancelAllVisitorEquipments(equipmentIdSet);
            }
            //查询
            if (item.getValue().equals(VisitorInfoAuthType.QUERY.getCode())) {
                VisitorInfo visitoryInfo_query = visitorHomeView.getQueryCondition();
                ManagerVisitorEquipmentUtil.authCancelQueryVisitorEquipments(visitoryInfo_query, equipmentIdSet);
            }
            //选择
            if (item.getValue().equals(VisitorInfoAuthType.CHECK.getCode())) {
                List<VisitorInfo> visitorObjs = tablePagePanel.getSelectedRows();
                if (visitorObjs == null || visitorObjs.size() <= 0) {
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(owerDialog, "请选择人员列表选择信息!", DialogFrame.TYPE_WARN,null);
                    return;
                } else {
                    Set<String> visitorIds = new HashSet<>();
                    visitorObjs.forEach(visitorObj -> {
                        visitorIds.add(visitorObj.getId());
                    });
                    ManagerVisitorEquipmentUtil.authCancelVisitorIdEquipments(visitorIds, equipmentIdSet);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            DialogFrame dialogFrame = new DialogFrame();
            dialogFrame.showMessageDialog(owerDialog, "执行异常!"+e.getMessage(), DialogFrame.TYPE_WARN,null);
            return;
        }

        /**
         * 刷新父表格
         */
        tablePagePanel.flushData();

    }

    /**
     * 页面刷新
     */
    private void flushPageDate(){
        java.util.List<EquipmentInfo> datas = HomeEquipmentUtil.findEquipmentInfosByStatus(EquipmentInfoStatus.ACTIVE.getCode());

        //表格赋值
        ownerTablePagePanel.setData(datas,datas.size());
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
