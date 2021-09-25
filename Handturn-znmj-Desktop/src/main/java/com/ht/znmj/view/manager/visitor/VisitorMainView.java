package com.ht.znmj.view.manager.visitor;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.entity.VisitorInfo;
import com.ht.znmj.entity.VisitorInfoAuthType;
import com.ht.znmj.entity.VisitorInfoIsActive;
import com.ht.znmj.sdk.ZBX_Global;
import com.ht.znmj.sdk.MyFunction;
import com.ht.znmj.view.components.DialogFrame;
import com.ht.znmj.view.components.TablePagePanel;
import com.ht.znmj.view.manager.ManagerFunctionView;
import com.ht.znmj.view.service.ApplicationUtil;
import com.ht.znmj.view.service.HomeEquipmentUtil;
import com.ht.znmj.view.service.ManagerVisitorEquipmentUtil;
import com.ht.znmj.view.service.ManagerVisitorUtil;
import com.sun.jna.ptr.IntByReference;
import lombok.Data;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

/**
 * 人员主界面
 */
public class VisitorMainView extends ManagerFunctionView {

    private VisitorMainView curentThis;

    /**
     * 组件
     */
    private TablePagePanel tablePagePanel;
    private JLabel topPictureLabel;

    /**
     * 查询输入
     */
    private TextField visitorIdText;
    private TextField visitorNameText;
    private TextField visitorPhoneNumberText;
    private TextField visitorIdCardText;
    private TextField wiganText;
    private TextField departmentText;
    private TextField positorText;
    private JComboBox isActiveBox;

    @Override
    public void initView(Window parentFrame, Panel parentPanel, EquipmentInfo equipmentInfo) {
        super.initView(parentFrame,parentPanel, equipmentInfo);
        curentThis = this;
        initContent();
    }

    public void initContent(){
        //重新获取最新设备
        equipmentInfo = HomeEquipmentUtil.findEquipmentInfoById(equipmentInfo.getId());
        Panel contentPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(parentPanel.getWidth(), parentPanel.getHeight());
            }
        };

        Panel topPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(parentPanel.getWidth(), 150);
            }
        };
        topPanel.setBounds(0,0,parentPanel.getWidth(),150);

        topPictureLabel = new JLabel(){
            public Dimension getPreferredSize() {
                return new Dimension(120, topPanel.getHeight());
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
        topPictureLabel.setBounds(0,0,120,topPanel.getHeight());
        topPanel.add(topPictureLabel,BorderLayout.WEST);

        Panel topConditionPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(topPanel.getWidth()-250, topPanel.getHeight());
            }
        };
        topConditionPanel.setBounds(0,0,topPanel.getWidth()-250, topPanel.getHeight());
        topConditionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        Panel visitorIdPanel = new Panel();
        visitorIdPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Font font = new Font("SimSun", 1, 12);
        Label visitorIdLabel = new Label("ID编码:");
        visitorIdLabel.setFont(font);
        visitorIdText = new TextField(15);
        visitorIdText.setFont(font);
        visitorIdText.setEnabled(true);
        visitorIdPanel.add(visitorIdLabel);
        visitorIdPanel.add(visitorIdText);
        topConditionPanel.add(visitorIdPanel);

        Panel visitorNamePanel = new Panel();
        visitorNamePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label visitorNameLabel = new Label("人员名:");
        visitorNameLabel.setFont(font);
        visitorNameText = new TextField(15);
        visitorNameText.setFont(font);
        visitorNameText.setEnabled(true);
        visitorNamePanel.add(visitorNameLabel);
        visitorNamePanel.add(visitorNameText);
        topConditionPanel.add(visitorNamePanel);

        Panel visitorPhoneNumberPanel = new Panel();
        visitorPhoneNumberPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label visitorPhoneNumberLabel = new Label("手机号:");
        visitorPhoneNumberLabel.setFont(font);
        visitorPhoneNumberText = new TextField(15);
        visitorPhoneNumberText.setFont(font);
        visitorPhoneNumberText.setEnabled(true);
        visitorPhoneNumberPanel.add(visitorPhoneNumberLabel);
        visitorPhoneNumberPanel.add(visitorPhoneNumberText);
        topConditionPanel.add(visitorPhoneNumberPanel);

        Panel visitorIdCardPanel = new Panel();
        visitorIdCardPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label visitorIdCardLabel = new Label("身份证:");
        visitorIdCardLabel.setFont(font);
        visitorIdCardText = new TextField(15);
        visitorIdCardText.setFont(font);
        visitorIdCardText.setEnabled(true);
        visitorIdCardPanel.add(visitorIdCardLabel);
        visitorIdCardPanel.add(visitorIdCardText);
        topConditionPanel.add(visitorIdCardPanel);

        Panel wiganPanel = new Panel();
        wiganPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label wiganLabel = new Label("韦根号:");
        wiganLabel.setFont(font);
        wiganText = new TextField(15);
        wiganText.setFont(font);
        wiganText.setEnabled(true);
        wiganPanel.add(wiganLabel);
        wiganPanel.add(wiganText);
        topConditionPanel.add(wiganPanel);

        Panel departmentPanel = new Panel();
        departmentPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label departmentLabel = new Label("部门名:");
        departmentLabel.setFont(font);
        departmentText = new TextField(15);
        departmentText.setFont(font);
        departmentText.setEnabled(true);
        departmentPanel.add(departmentLabel);
        departmentPanel.add(departmentText);
        topConditionPanel.add(departmentPanel);

        Panel positorPanel = new Panel();
        positorPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label positorLabel = new Label("岗位名:");
        positorLabel.setFont(font);
        positorText = new TextField(15);
        positorText.setFont(font);
        positorText.setEnabled(true);
        positorPanel.add(positorLabel);
        positorPanel.add(positorText);
        topConditionPanel.add(positorPanel);

        Panel isActivePanel = new Panel();
        isActivePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label isActiveLabel = new Label("是否有效:");
        isActiveLabel.setFont(font);
        Vector model = new Vector();
        model.addElement(new Item("", "-全部--"));
        model.addElement(new Item(VisitorInfoIsActive.ACTIVE.getCode(), VisitorInfoIsActive.ACTIVE.getName()));
        model.addElement(new Item(VisitorInfoIsActive.UNACTIVE.getCode(), VisitorInfoIsActive.UNACTIVE.getName()));
        isActiveBox=new JComboBox(model);    //创建JComboBox
        isActiveBox.setFont(font);
        isActiveBox.setRenderer(new ItemRenderer());
        isActivePanel.add(isActiveLabel);
        isActivePanel.add(isActiveBox);
        topConditionPanel.add(isActivePanel);

        topPanel.add(topConditionPanel,BorderLayout.CENTER);


        Panel searchButtonPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(100, topPanel.getHeight());
            }
        };
        searchButtonPanel.setBounds(0,0,100, topPanel.getHeight());
        Button searchButton = new Button("查询");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //刷新表格数据
                flushPageDate();
            }
        });
        Font buttonFont = new Font("SimSun", 1, 12);
        searchButton.setFont(buttonFont);
        searchButtonPanel.add(searchButton);
        topPanel.add(searchButton,BorderLayout.EAST);
        contentPanel.add(topPanel,BorderLayout.NORTH);

        Panel topToolsPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(parentPanel.getWidth(), 35);
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
        topToolsPanel.setBounds(0,0,parentPanel.getWidth(), 35);

        topToolsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        Button createButton = new Button("添加授权");
        createButton.setFont(buttonFont);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(equipmentInfo.getSn());
                if(MyFunction.m_FaceRecognition.ZBX_Connected(cameraPoint) != 1){
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(parentFrame,"设备未连接,请先在首页刷新设备列表!",DialogFrame.TYPE_ERROR,null);
                    return;
                }

                VisitorAddDialog addDialog = new VisitorAddDialog();
                addDialog.showDialog(curentThis,parentFrame,tablePagePanel,equipmentInfo);
            }
        });


        Button checkCancelButton = new Button("选择取消授权");
        checkCancelButton.setFont(buttonFont);
        checkCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(equipmentInfo.getSn());
                if(MyFunction.m_FaceRecognition.ZBX_Connected(cameraPoint) != 1){
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(parentFrame,"设备未连接,请先在首页刷新设备列表!",DialogFrame.TYPE_ERROR,null);
                    return;
                }

                authCancelConfirm(VisitorInfoAuthType.CHECK.getCode());

                //刷新数据
                tablePagePanel.flushData();
            }
        });


        Button queryCancelButton = new Button("查询取消授权");
        queryCancelButton.setFont(buttonFont);
        queryCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(equipmentInfo.getSn());
                if(MyFunction.m_FaceRecognition.ZBX_Connected(cameraPoint) != 1){
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(parentFrame,"设备未连接,请先在首页刷新设备列表!",DialogFrame.TYPE_ERROR,null);
                    return;
                }

                authCancelConfirm(VisitorInfoAuthType.QUERY.getCode());

                //刷新数据
                tablePagePanel.flushData();
            }
        });

        //是否云端开启
        if(!ApplicationUtil.getApplicationProperties().getCloudFlag().equals("1")) {
            topToolsPanel.add(createButton);
            topToolsPanel.add(checkCancelButton);
            topToolsPanel.add(queryCancelButton);
        }
        contentPanel.add(topToolsPanel,BorderLayout.CENTER);

        tablePagePanel = new TablePagePanel<VisitorInfo>(){
            @Override
            public void flushData() {
                //实现分页查询数据
                flushPageDate();
            }

            @Override
            public void checkRow(int row) {
               VisitorInfo visitorInfo = (VisitorInfo)getSelectedRow(row);
               if(visitorInfo != null){
                   ImageIcon imageIcon = new ImageIcon(visitorInfo.getFaceFilePath());
                   imageIcon.setImage(imageIcon.getImage().getScaledInstance(120, 150,Image.SCALE_DEFAULT ));
                   topPictureLabel.setIcon(imageIcon);
               }
            }

            public Dimension getPreferredSize() {
                return new Dimension(parentPanel.getWidth(), parentPanel.getHeight()-210);
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
        tablePagePanel.setBounds(0,0,parentPanel.getWidth(), parentPanel.getHeight()-210);

        TablePagePanel.ColumnBean columnBean1 = new TablePagePanel.ColumnBean("faceFilePath", "人脸", 35,TablePagePanel.ColumnBean.TYPE_IMG,null);
        TablePagePanel.ColumnBean columnBean2 = new TablePagePanel.ColumnBean("id", "Id编码", 30,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean3 = new TablePagePanel.ColumnBean("name", "姓名", 50,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean4 = new TablePagePanel.ColumnBean("phoneNumber", "电话号码", 80,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean5 = new TablePagePanel.ColumnBean("department", "部门名", 80,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean6 = new TablePagePanel.ColumnBean("positor", "岗位名", 80,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean7 = new TablePagePanel.ColumnBean("idCard", "身份证", 120,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean8 = new TablePagePanel.ColumnBean("wigan", "韦根号", 100,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean9 = new TablePagePanel.ColumnBean("startTime", "生效时间", 125,TablePagePanel.ColumnBean.TYPE_DATE,null);
        TablePagePanel.ColumnBean columnBean10 = new TablePagePanel.ColumnBean("endTime", "失效时间", 125,TablePagePanel.ColumnBean.TYPE_DATE,null);
        TablePagePanel.ColumnBean columnBean11 = new TablePagePanel.ColumnBean("remark", "备注", 100,TablePagePanel.ColumnBean.TYPE_STRING,null);
        java.util.List<TablePagePanel.ColumnBean> columnBeanList = new ArrayList<>();
        columnBeanList.add(columnBean1);
        columnBeanList.add(columnBean2);
        columnBeanList.add(columnBean3);
        columnBeanList.add(columnBean4);
        columnBeanList.add(columnBean5);
        columnBeanList.add(columnBean6);
        columnBeanList.add(columnBean7);
        columnBeanList.add(columnBean8);
        columnBeanList.add(columnBean9);
        columnBeanList.add(columnBean10);
        columnBeanList.add(columnBean11);
        tablePagePanel.initView(parentFrame,true);
        tablePagePanel.setColumnBeanList(columnBeanList);

        //自动加载页面
        tablePagePanel.flushData();

        contentPanel.add(tablePagePanel,BorderLayout.SOUTH);

        this.parentPanel.add(contentPanel,BorderLayout.CENTER);
        this.parentPanel.revalidate();
    }


    /**
     * 取消授权确认
     */
    private void authCancelConfirm(String visitorInfoAuthType){
        //查询
        if (visitorInfoAuthType.equals(VisitorInfoAuthType.QUERY.getCode())) {
            VisitorInfo visitoryInfo_query = getQueryCondition();
            Set<String> equipmentIdSet = new HashSet<>();
            equipmentIdSet.add(equipmentInfo.getId());
            ManagerVisitorEquipmentUtil.authCancelQueryVisitorEquipments(visitoryInfo_query, equipmentIdSet);
        }
        //选择
        if (visitorInfoAuthType.equals(VisitorInfoAuthType.CHECK.getCode())) {
            List<VisitorInfo> visitorObjs = tablePagePanel.getSelectedRows();
            if (visitorObjs == null || visitorObjs.size() <= 0) {
                DialogFrame dialogFrame = new DialogFrame();
                dialogFrame.showMessageDialog(parentFrame, "请选择人员列表选择信息!", DialogFrame.TYPE_WARN,null);
                return;
            } else {
                Set<String> visitorIds = new HashSet<>();
                visitorObjs.forEach(visitorObj -> {
                    visitorIds.add(visitorObj.getId());
                });

                Set<String> equipmentIdSet = new HashSet<>();
                equipmentIdSet.add(equipmentInfo.getId());
                ManagerVisitorEquipmentUtil.authCancelVisitorIdEquipments(visitorIds, equipmentIdSet);
            }
        }

        tablePagePanel.flushData();
    }

    /**
     * 页面刷新
     */
    private void flushPageDate(){
        QueryRequest request = new QueryRequest();
        request.setPageNum((int)tablePagePanel.getPageIndex());
        request.setPageSize((int)tablePagePanel.getPageSize());

        VisitorInfo visitoryInfo_query = getQueryCondition();

        IPage<VisitorInfo> pageDate = ManagerVisitorUtil.findVisitorInfos(request,visitoryInfo_query);

        //表格赋值
        tablePagePanel.setData(pageDate.getRecords(),pageDate.getTotal());
    }

    public VisitorInfo getQueryCondition(){
        VisitorInfo visitoryInfo = new VisitorInfo();
        visitoryInfo.setId(visitorIdText.getText());
        visitoryInfo.setName(visitorNameText.getText());
        visitoryInfo.setPhoneNumber(visitorPhoneNumberText.getText());
        visitoryInfo.setIdCard(visitorIdCardText.getText());
        visitoryInfo.setWigan(wiganText.getText());
        visitoryInfo.setDepartment(departmentText.getText());
        visitoryInfo.setPositor(positorText.getText());
        Item isActiveItem = (Item)isActiveBox.getSelectedItem();
        visitoryInfo.setIsActive(isActiveItem.value);

        //额外使用条件
        visitoryInfo.setEquipmentId(equipmentInfo.getId());
        return visitoryInfo;
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
