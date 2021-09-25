package com.ht.znmj.view.home.visitor;

import com.alibaba.fastjson.JSONArray;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.VisitorInfo;
import com.ht.znmj.entity.VisitorInfoBakType;
import com.ht.znmj.utils.DateUtil;
import com.ht.znmj.utils.ExportReflectTo;
import com.ht.znmj.utils.LocalStoreUtils;
import com.ht.znmj.utils.ZipUtils;
import com.ht.znmj.view.components.TablePagePanel;
import lombok.Data;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * 人员编辑界面
 */
public class VisitorHomeBakDialog {
    private VisitorHomeView visitorHomeView;
    private Window parentFrame;
    private TablePagePanel tablePagePanel;
    private Dialog owerDialog;

    /**
     * 输入组件
     */
    private JComboBox bakTypeBox;
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

        String tittle = "人员备份";

        if(parentFrame instanceof JFrame){
            owerDialog = new Dialog((JFrame)parentFrame,tittle,false);
        }else if(parentFrame instanceof Dialog){
            owerDialog = new Dialog((Dialog)parentFrame,tittle,false);
        }else{
            owerDialog = new Dialog((Frame)parentFrame,tittle,false);
        }

        Panel centerEntityPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(550, 350);
            }
        };
        centerEntityPanel.setBounds(0,0,550, 350);
        centerEntityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        Font font = new Font("SimSun", 1, 12);

        Panel bakTypePanel = new Panel();
        bakTypePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label bakTypeLabel = new Label("备份类型:");
        bakTypeLabel.setFont(font);
        Vector model = new Vector();
        model.addElement(new Item(VisitorInfoBakType.QUERY.getCode(), "查询备份"));
        model.addElement(new Item(VisitorInfoBakType.ALL.getCode(), "全量备份"));
        bakTypeBox=new JComboBox(model);    //创建JComboBox
        bakTypeBox.setRenderer(new ItemRenderer());
        bakTypePanel.add(bakTypeLabel);
        bakTypePanel.add(bakTypeBox);
        centerEntityPanel.add(bakTypePanel);


        Panel errorPanel = new Panel();
        errorPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label errorLabel = new Label("提 示:");
        errorLabel.setFont(font);
        errorText = new TextArea();
        errorText.setRows(8);
        errorText.setColumns(60);
        errorText.setEnabled(true);
        errorPanel.add(errorLabel);
        errorPanel.add(errorText);
        centerEntityPanel.add(errorPanel);

        Panel bottomEntityPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(parentFrame.getWidth(), 40);
            }
        };
        bottomEntityPanel.setBounds(0,0,parentFrame.getWidth(), 40);

        Button bakButton = new Button("备份");
        bakButton.setFont(font);
        bakButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bakClick();
            }
        });
        bottomEntityPanel.add(bakButton);
        bottomEntityPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        int x = parentFrame.getX() + (parentFrame.getWidth()-parentFrame.getX())/2-300;
        int y = parentFrame.getY() + (parentFrame.getHeight()-parentFrame.getY())/2-200;
        owerDialog.setBounds(x, y, 550, 350);//设置大小位置
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
     * 备份
     */
    public void bakClick(){
        Boolean successFlag = true;
        errorText.setText("");
        errorText.setText(DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-文件备份开始\n");
        errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-处理数据开始\n");

        Item selectItem = (Item)bakTypeBox.getSelectedItem();
        String parentDirName = DateUtil.getDateFormat(new Date(), DateUtil.FULL_TIME_PATTERN)+"_"+selectItem.getText();
        try {

            VisitorInfo visitorInfo_query = null;
            if(selectItem.value.equals(VisitorInfoBakType.ALL.getCode())){
                visitorInfo_query = new VisitorInfo();
            }else {
                //查询条件
                visitorInfo_query = visitorHomeView.getQueryCondition();
            }

            //查询列
            TablePagePanel.ColumnBean columnBean1 = new TablePagePanel.ColumnBean("id", "Id编码", 30, TablePagePanel.ColumnBean.TYPE_STRING, null);
            TablePagePanel.ColumnBean columnBean2 = new TablePagePanel.ColumnBean("name", "姓名", 50, TablePagePanel.ColumnBean.TYPE_STRING, null);
            TablePagePanel.ColumnBean columnBean3 = new TablePagePanel.ColumnBean("phoneNumber", "电话号码", 80, TablePagePanel.ColumnBean.TYPE_STRING, null);
            TablePagePanel.ColumnBean columnBean4 = new TablePagePanel.ColumnBean("department", "部门名", 80, TablePagePanel.ColumnBean.TYPE_STRING, null);
            TablePagePanel.ColumnBean columnBean5 = new TablePagePanel.ColumnBean("positor", "岗位名", 80, TablePagePanel.ColumnBean.TYPE_STRING, null);
            TablePagePanel.ColumnBean columnBean6 = new TablePagePanel.ColumnBean("idCard", "身份证", 120, TablePagePanel.ColumnBean.TYPE_STRING, null);
            TablePagePanel.ColumnBean columnBean7 = new TablePagePanel.ColumnBean("wigan", "韦根号", 100, TablePagePanel.ColumnBean.TYPE_STRING, null);
            TablePagePanel.ColumnBean columnBean8 = new TablePagePanel.ColumnBean("startTime", "生效时间", 125, TablePagePanel.ColumnBean.TYPE_DATE, null);
            TablePagePanel.ColumnBean columnBean9 = new TablePagePanel.ColumnBean("endTime", "失效时间", 125, TablePagePanel.ColumnBean.TYPE_DATE, null);
            TablePagePanel.ColumnBean columnBean10 = new TablePagePanel.ColumnBean("remark", "备注", 100, TablePagePanel.ColumnBean.TYPE_STRING, null);
            TablePagePanel.ColumnBean columnBean11 = new TablePagePanel.ColumnBean("updateTime", "修改时间", 125, TablePagePanel.ColumnBean.TYPE_DATE, null);
            TablePagePanel.ColumnBean columnBean12 = new TablePagePanel.ColumnBean("faceFilePath", "人脸", 40, TablePagePanel.ColumnBean.TYPE_IMG, null);
            java.util.List<TablePagePanel.ColumnBean> listColumn = new ArrayList<>();
            listColumn.add(columnBean1);
            listColumn.add(columnBean2);
            listColumn.add(columnBean3);
            listColumn.add(columnBean4);
            listColumn.add(columnBean5);
            listColumn.add(columnBean6);
            listColumn.add(columnBean7);
            listColumn.add(columnBean8);
            listColumn.add(columnBean9);
            listColumn.add(columnBean10);
            listColumn.add(columnBean11);
            listColumn.add(columnBean12);

            //参数类型
            Class[] paramClassName = {QueryRequest.class, VisitorInfo.class}; //必须按这个循序放置

            String colStr = JSONArray.toJSONString(listColumn);
            //反射调用
            String parentDirPath = LocalStoreUtils.tempFilePath + parentDirName + "/";
            ExportReflectTo.exportRun("{field: 'updateDate', type: null}", colStr, visitorInfo_query,
                    parentDirPath,
                    "VisitoryInfoService", "findVisitorInfos", paramClassName);
            errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-处理数据完成!\n");
        }catch (Exception e){
            e.printStackTrace();
            errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!处理数据失败!"+e.getMessage()+"\n");
            successFlag = false;
        }

        //压缩包
        String zipToPath = null;
        if(successFlag) {
            errorText.setText(errorText.getText() + DateUtil.getDateFormat(new Date(), DateUtil.FULL_TIME_SPLIT_PATTERN) + "-压缩文件开始!\n");
            try {
                String zipSrcDirPath = LocalStoreUtils.tempFilePath + parentDirName;
                zipToPath = LocalStoreUtils.tempFilePath + parentDirName + ".zip";
                ZipUtils.zipFiles(zipSrcDirPath, zipToPath);
                errorText.setText(errorText.getText() + DateUtil.getDateFormat(new Date(), DateUtil.FULL_TIME_SPLIT_PATTERN) + "-压缩文件成功!(临时文件:" + zipToPath + ")\n");
            } catch (Exception e) {
                e.printStackTrace();
                errorText.setText(errorText.getText() + DateUtil.getDateFormat(new Date(), DateUtil.FULL_TIME_SPLIT_PATTERN) + "!!!!!压缩文件失败!\n");
                successFlag = false;
            }

            errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-文件备份完成!\n");
        }


        //转移另存为
        if(successFlag) {
            FileDialog fd = new FileDialog(owerDialog, "文件另存为", FileDialog.SAVE);
            //fd.setAlwaysOnTop(true);
            fd.setVisible(true);

            try {
                errorText.setText(errorText.getText() + DateUtil.getDateFormat(new Date(), DateUtil.FULL_TIME_SPLIT_PATTERN) + "-另存为文件开始!\n");
                File saveFile = new File(fd.getDirectory(), fd.getFile()+".zip");
                FileOutputStream saveFos = new FileOutputStream(saveFile);
                File srcFile = new File(zipToPath);
                FileInputStream srcFis = new FileInputStream(srcFile);
                byte[] bytes = new byte[srcFis.available()];
                int temp;
                while ((temp = srcFis.read(bytes)) != -1) {
                    saveFos.write(bytes, 0, temp);
                }
                saveFos.close();
                srcFis.close();
                errorText.setText(errorText.getText() + DateUtil.getDateFormat(new Date(), DateUtil.FULL_TIME_SPLIT_PATTERN) + "-另存为文件成功!(存放文件:" + saveFile.getPath() + ")\n");
            } catch (Exception e) {
                e.printStackTrace();
                errorText.setText(errorText.getText() + DateUtil.getDateFormat(new Date(), DateUtil.FULL_TIME_SPLIT_PATTERN) + "!!!!!另存为文件失败!\n");
            }
        }
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
