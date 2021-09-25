package com.ht.znmj.view.home.visitorlog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.EquipmentInfoInOut;
import com.ht.znmj.entity.VisitorLog;
import com.ht.znmj.entity.VisitorLogOpenStatus;
import com.ht.znmj.utils.DateUtil;
import com.ht.znmj.view.components.DateChooserJButton;
import com.ht.znmj.view.components.TablePagePanel;
import com.ht.znmj.view.home.HomeMenuItemView;
import com.ht.znmj.view.service.ManagerVisitorLogUtil;
import lombok.Data;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * 人员主界面
 */
public class VisitorLogHomeView extends HomeMenuItemView {
    private Dialog owerDialog;
    private VisitorLogHomeView curentThis;

    @Override
    public void initView(Window parentFrame) {
        super.initView(parentFrame);
        curentThis = this;
        initContent();
    }

    public void initContent(){
        parentFrame.setEnabled(false);

        if(parentFrame instanceof JFrame){
            owerDialog = new Dialog((JFrame)parentFrame,"人员管理",false);
        }else if(parentFrame instanceof Dialog){
            owerDialog = new Dialog((Dialog)parentFrame,"人员管理",false);
        }else{
            owerDialog = new Dialog((Frame)parentFrame,"人员管理",false);
        }

        int x = parentFrame.getX() + (parentFrame.getWidth()-parentFrame.getX())/2-300;
        int y = parentFrame.getY() + (parentFrame.getHeight()-parentFrame.getY())/2-200;
        owerDialog.setBounds(x, y, 1350, 800);//设置大小位置

        JTabbedPane visitorLogTabPane = new JTabbedPane(){
            public Dimension getPreferredSize() {
                return new Dimension(owerDialog.getWidth()-20, owerDialog.getHeight());
            }
        };
        visitorLogTabPane.setBackground(Color.white);

        ImageIcon imageIcon = new ImageIcon("images/common_icon/icon_camera.png");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(25, 25,Image.SCALE_DEFAULT ));
        Panel visitorLogPane  = setVisitorLogPanel();
        visitorLogTabPane.addTab("人员日志查询",imageIcon,visitorLogPane,"人员日志查询");


        /*Panel dailyTimePane = new Panel();
        visitorLogTabPane.addTab("考勤记录查询",imageIcon,dailyTimePane,"考勤记录查询");*/
        //owerDialog.setAlwaysOnTop(true);
        owerDialog.setVisible(true);
        owerDialog.add(visitorLogTabPane,BorderLayout.CENTER);
        owerDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                parentFrame.setEnabled(true);
                owerDialog.dispose();
            }
        });
    }

    /**
     * 组件
     */
    private TablePagePanel<VisitorLog> tablePagePanel;
    private JLabel topPictureLabel;

    /**
     * 查询输入
     */
    private TextField visitorNameText;
    private TextField visitorPhoneNumberText;
    private TextField visitorIdCardText;
    private TextField wiganText;
    private TextField departmentText;
    private TextField areaNameText;
    private JComboBox inOutBox;
    private JComboBox openStatusBox;
    private DateChooserJButton startOpenTimeText;
    private DateChooserJButton endOpenTimeText;

    /**
     * 访客日志
     * @return
     */
    private Panel setVisitorLogPanel(){
        Panel contentPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(owerDialog.getWidth(), owerDialog.getHeight());
            }
        };

        Panel topPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(owerDialog.getWidth(), 150);
            }
        };
        topPanel.setBounds(0,0,owerDialog.getWidth(),150);

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

        Font font = new Font("SimSun", 1, 12);

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

        Panel areaNamePanel = new Panel();
        areaNamePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label areaNameLabel = new Label("区域名:");
        areaNameLabel.setFont(font);
        areaNameText = new TextField(15);
        areaNameText.setFont(font);
        areaNameText.setEnabled(true);
        areaNamePanel.add(areaNameLabel);
        areaNamePanel.add(areaNameText);
        topConditionPanel.add(areaNamePanel);

        Panel inOutPanel = new Panel();
        inOutPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label inOutLabel = new Label("出入口:");
        inOutLabel.setFont(font);
        Vector model = new Vector();
        model.addElement(new Item("", "-全部--"));
        model.addElement(new Item(EquipmentInfoInOut.NONE.getCode(), EquipmentInfoInOut.NONE.getName()));
        model.addElement(new Item(EquipmentInfoInOut.IN.getCode(), EquipmentInfoInOut.IN.getName()));
        model.addElement(new Item(EquipmentInfoInOut.OUT.getCode(), EquipmentInfoInOut.OUT.getName()));
        inOutBox=new JComboBox(model);    //创建JComboBox
        inOutBox.setRenderer(new ItemRenderer());
        inOutPanel.add(inOutLabel);
        inOutPanel.add(inOutBox);
        topConditionPanel.add(inOutPanel);

        Panel openStatusPanel = new Panel();
        openStatusPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label openStatusLabel = new Label("通行状态:");
        openStatusLabel.setFont(font);
        Vector openStatusModel = new Vector();
        openStatusModel.addElement(new Item("", "-全部--"));
        openStatusModel.addElement(new Item(VisitorLogOpenStatus.NONE.getCode(), VisitorLogOpenStatus.NONE.getName()));
        openStatusModel.addElement(new Item(VisitorLogOpenStatus.OPEN.getCode(), VisitorLogOpenStatus.OPEN.getName()));
        openStatusBox=new JComboBox(openStatusModel);    //创建JComboBox
        openStatusBox.setRenderer(new ItemRenderer());
        openStatusPanel.add(openStatusLabel);
        openStatusPanel.add(openStatusBox);
        topConditionPanel.add(openStatusPanel);

        Panel startOpenTimePanel = new Panel();
        startOpenTimePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label startOpenTimeLabel = new Label("开闸时间:");
        startOpenTimeLabel.setFont(font);
        startOpenTimePanel.add(startOpenTimeLabel);
        startOpenTimeText = new DateChooserJButton(DateUtil.getDateFormat(DateUtil.days(new Date(),-31),DateUtil.FULL_TIME_SPLIT_PATTERN));
        startOpenTimeText.setFont(font);
        startOpenTimePanel.add(startOpenTimeText);
        Label splitLabel = new Label("-");
        splitLabel.setFont(font);
        startOpenTimePanel.add(splitLabel);
        endOpenTimeText = new DateChooserJButton(DateUtil.getDateFormat(DateUtil.days(new Date(),1),DateUtil.FULL_TIME_SPLIT_PATTERN));
        endOpenTimeText.setFont(font);
        startOpenTimePanel.add(endOpenTimeText);
        topConditionPanel.add(startOpenTimePanel);

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

        tablePagePanel = new TablePagePanel<VisitorLog>(){
            @Override
            public void flushData() {
                super.flushData();

                //实现分页查询数据
                flushPageDate();
            }

            @Override
            public void checkRow(int row) {
                VisitorLog visitorLog = (VisitorLog)getSelectedRow(row);
                if(visitorLog != null){
                    ImageIcon imageIcon = new ImageIcon(visitorLog.getFaceFilePath());
                    imageIcon.setImage(imageIcon.getImage().getScaledInstance(120, 150,Image.SCALE_DEFAULT ));
                    topPictureLabel.setIcon(imageIcon);
                }
            }

            public Dimension getPreferredSize() {
                return new Dimension(owerDialog.getWidth()-40, owerDialog.getHeight()-240);
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
        tablePagePanel.setBounds(0,0,owerDialog.getWidth()-40, owerDialog.getHeight()-240);

        StringBuffer inOutMapJson = new StringBuffer("{");
        inOutMapJson.append("'"+EquipmentInfoInOut.NONE.getCode()+"'").append(":'未知位置',");
        inOutMapJson.append("'"+EquipmentInfoInOut.IN.getCode()+"'").append(":'入口',");
        inOutMapJson.append("'"+EquipmentInfoInOut.OUT.getCode()+"'").append(":'出口'");
        inOutMapJson.append("}");

        StringBuffer openStatusMapJson = new StringBuffer("{");
        openStatusMapJson.append("'"+VisitorLogOpenStatus.NONE.getCode()+"'").append(":'未识别人员',");
        openStatusMapJson.append("'"+VisitorLogOpenStatus.OPEN.getCode()+"'").append(":'已识别人员'");
        openStatusMapJson.append("}");

        TablePagePanel.ColumnBean columnBean1 = new TablePagePanel.ColumnBean("faceFilePath", "人脸", 35,TablePagePanel.ColumnBean.TYPE_IMG,null);
        TablePagePanel.ColumnBean columnBean2 = new TablePagePanel.ColumnBean("visitorName", "姓名", 50,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean3 = new TablePagePanel.ColumnBean("phoneNumber", "电话号码", 80,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean4 = new TablePagePanel.ColumnBean("department", "部门名", 80,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean5 = new TablePagePanel.ColumnBean("positor", "岗位名", 80,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean6 = new TablePagePanel.ColumnBean("idCard", "身份证", 120,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean7 = new TablePagePanel.ColumnBean("wigan", "韦根号", 100,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean8 = new TablePagePanel.ColumnBean("areaName", "区域名称", 80,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean9 = new TablePagePanel.ColumnBean("seqNum", "设备编号", 40,TablePagePanel.ColumnBean.TYPE_STRING,null);
        TablePagePanel.ColumnBean columnBean10 = new TablePagePanel.ColumnBean("inOut", "进出口", 50,TablePagePanel.ColumnBean.TYPE_STRING,inOutMapJson.toString());
        TablePagePanel.ColumnBean columnBean11 = new TablePagePanel.ColumnBean("openStatus", "通行状态", 50,TablePagePanel.ColumnBean.TYPE_STRING,openStatusMapJson.toString());
        TablePagePanel.ColumnBean columnBean12 = new TablePagePanel.ColumnBean("openTime", "开闸时间", 100,TablePagePanel.ColumnBean.TYPE_DATE,null);
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
        columnBeanList.add(columnBean12);
        tablePagePanel.initView(parentFrame,true);
        tablePagePanel.setColumnBeanList(columnBeanList);

        //自动加载页面
        tablePagePanel.flushData();

        contentPanel.add(tablePagePanel,BorderLayout.SOUTH);

        return contentPanel;
    }

    /**
     * 页面刷新
     */
    private void flushPageDate(){
        QueryRequest request = new QueryRequest();
        request.setPageNum((int)tablePagePanel.getPageIndex());
        request.setPageSize((int)tablePagePanel.getPageSize());

        VisitorLog visitorLog_query = getQueryCondition();

        IPage<VisitorLog> pageDate = ManagerVisitorLogUtil.findVisitorLogs(request,visitorLog_query);

        //表格赋值
        tablePagePanel.setData(pageDate.getRecords(),pageDate.getTotal());
    }

    public VisitorLog getQueryCondition(){
        VisitorLog visitorLog = new VisitorLog();
        visitorLog.setVisitorName(visitorNameText.getText());
        visitorLog.setPhoneNumber(visitorPhoneNumberText.getText());
        visitorLog.setIdCard(visitorIdCardText.getText());
        visitorLog.setWigan(wiganText.getText());
        visitorLog.setDepartment(departmentText.getText());

        visitorLog.setAreaName(areaNameText.getText());
        Item item = (Item)inOutBox.getSelectedItem();
        visitorLog.setInOut(item.getValue());
        Item openStatusItem = (Item)openStatusBox.getSelectedItem();
        visitorLog.setOpenStatus(openStatusItem.getValue());
        visitorLog.setStartOpenTime(startOpenTimeText.getDate().getTime());
        visitorLog.setEndOpenTime(endOpenTimeText.getDate().getTime());

        return visitorLog;
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
