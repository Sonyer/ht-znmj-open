package com.ht.znmj.view.home.visitor;

import com.ht.znmj.common.exception.FebsException;
import com.ht.znmj.entity.VisitorInfo;
import com.ht.znmj.entity.VisitorInfoBakRestType;
import com.ht.znmj.utils.DateUtil;
import com.ht.znmj.utils.*;
import com.ht.znmj.view.components.DialogFrame;
import com.ht.znmj.view.components.TablePagePanel;
import com.ht.znmj.view.service.ManagerVisitorUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.Font;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * 人员编辑界面
 */
public class VisitorHomeBakRestDialog {
    private VisitorHomeView visitorHomeView;
    private Window parentFrame;
    private TablePagePanel tablePagePanel;
    private Dialog owerDialog;

    /**
     * 输入组件
     */
    private JComboBox bakRestTypeBox;
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
                return new Dimension(550, 350);
            }
        };
        centerEntityPanel.setBounds(0,0,550, 350);
        centerEntityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        Font font = new Font("SimSun", 1, 12);

        Panel bakRestTypePanel = new Panel();
        bakRestTypePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label bakRestTypeLabel = new Label("备份类型:");
        bakRestTypeLabel.setFont(font);
        Vector bakRestTypeModel = new Vector();
        bakRestTypeModel.addElement(new Item(VisitorInfoBakRestType.ADD.getCode(), "增量恢复"));
        bakRestTypeModel.addElement(new Item(VisitorInfoBakRestType.CLEAN.getCode(), "覆盖恢复"));
        bakRestTypeBox=new JComboBox(bakRestTypeModel);    //创建JComboBox
        bakRestTypeBox.setRenderer(new ItemRenderer());
        bakRestTypePanel.add(bakRestTypeLabel);
        bakRestTypePanel.add(bakRestTypeBox);

        centerEntityPanel.add(bakRestTypePanel);


        Panel filePathPanel = new Panel();
        filePathPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label filePathLabel = new Label("文件路径:");
        filePathLabel.setFont(font);
        filePathPanel.add(filePathLabel);
        filePathText = new TextField(48);
        filePathText.setFont(font);
        filePathText.setEnabled(false);
        filePathPanel.add(filePathText);
        Button importButton = new Button("选择");
        importButton.setFont(font);
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventOnImport(importButton);
            }
        });
        filePathPanel.add(importButton);
        centerEntityPanel.add(filePathPanel);


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

        Button bakButton = new Button("备份恢复");
        bakButton.setFont(font);
        bakButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bakRestClick();
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
     * 备份恢复
     */
    public void bakRestClick(){
        String filePath = filePathText.getText().trim();
        if(StringUtils.isEmpty(filePath)){
            DialogFrame dialogMessage = new DialogFrame();
            dialogMessage.showMessageDialog(owerDialog,"请选择备份文件!",DialogFrame.TYPE_ERROR,null);
            return;
        }else{
            errorText.setText("");
            Boolean successFlag = true;
            Item bakRestTypeItem = (Item)bakRestTypeBox.getSelectedItem();
            if(bakRestTypeItem.getValue().equals(VisitorInfoBakRestType.CLEAN)){
                errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-清理人员数据开始!\n");
                try {
                    ManagerVisitorUtil.deleteAllVisitor();
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-清理人员数据完成!\n");
                }catch (Exception e){
                    e.printStackTrace();
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!清理人员数据异常结束!"+e.getMessage()+"\n");
                    successFlag = false;
                }
            }

            String zipFileDir = "";
            if(successFlag){
                errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-备份文件处理开始!\n");
                try {
                    String tempZipFilePath = tranImgBak2Temp(filePath);
                    zipFileDir = tempZipFilePath.replace(".zip","");
                    ZipUtils.unZiFiles(tempZipFilePath,zipFileDir);
                }catch (Exception e){
                    e.printStackTrace();
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!备份文件处理异常!"+e.getMessage()+"\n");
                    successFlag = false;
                }

                if(successFlag){
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-备份文件处理完成!\n");
                }else{
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-备份文件处理异常结束!\n");
                }

            }

            if(successFlag){
                errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-备份文件读取开始!\n");
                String dataExcelFilePath = zipFileDir+"/data/数据信息.xlsx";
                String imgFileDirPath = zipFileDir+"/";
                File zipFile = new File(zipFileDir);
                File tempFile = new File(dataExcelFilePath);
                InputStream is = null;
                try {
                    //根据新建的文件实例化输入流
                    is = new FileInputStream(tempFile);
                    Workbook wb = new XSSFWorkbook(is);
                    readExcelValue(wb,imgFileDirPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!备份文件读取异常!"+e.getMessage()+"\n");
                    successFlag = false;
                } finally {
                    if(is != null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (zipFile != null && zipFile.exists()) {
                        zipFile.delete();
                    }
                }

                if(successFlag){
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-备份文件读取完成!\n");
                }else{
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-备份文件读取异常结束!\n");
                }
            }
            if(successFlag){
                errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-备份文件恢复完成!\n");
            }else{
                errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-备份文件恢复异常结束!\n");
            }
        }

        //刷新表格
        if(tablePagePanel != null){
            tablePagePanel.flushData();
        }
    }

    /**
     * Exccel读取
     * @param wb
     */
    private void readExcelValue(Workbook wb,String imgFileDirPath){
        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        //总列数
        int cells = 0;
        if (rows >= 2 && sheet.getRow(0) != null) {
            cells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        java.util.List<VisitorInfo> visitorInfoList = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            if (i == 0) {
                for (int j = 0; j < cells; j++) {
                    //头不做处理
                }
            } else {
                VisitorInfo visitorInfo = new VisitorInfo();
                //组装Bean对象
                visitorInfo.setName(getCellValue(row.getCell(1)));
                visitorInfo.setPhoneNumber(getCellValue(row.getCell(2)));
                if(StringUtils.isEmpty(visitorInfo.getName()) || StringUtils.isEmpty(visitorInfo.getPhoneNumber())){
                    throw new FebsException("备份文件存在问题!");
                }
                visitorInfo.setDepartment(getCellValue(row.getCell(3)));
                visitorInfo.setPositor(getCellValue(row.getCell(4)));
                visitorInfo.setIdCard(getCellValue(row.getCell(5)));
                visitorInfo.setWigan(getCellValue(row.getCell(6)));
                try {
                    Date startTime = StringUtils.isEmpty(getCellValue(row.getCell(7)))?new Date():DateUtil.formatStr2Date(getCellValue(row.getCell(7)),DateUtil.FULL_TIME_SPLIT_PATTERN);
                    visitorInfo.setStartTime(startTime);

                    Date endTime = StringUtils.isEmpty(getCellValue(row.getCell(8)))?new Date():DateUtil.formatStr2Date(getCellValue(row.getCell(8)),DateUtil.FULL_TIME_SPLIT_PATTERN);
                    visitorInfo.setEndTime(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new FebsException("备份文件存在问题!");
                }

                visitorInfo.setRemark(getCellValue(row.getCell(9)));
                visitorInfo.setFaceFilePath(getCellValue(row.getCell(11)));

                visitorInfoList.add(visitorInfo);
            }
        }

        /**
         * 数据存储
         */
        visitorInfoList.forEach(visitorInfo ->{
            try {
                String srcImgFilePath = imgFileDirPath+visitorInfo.getFaceFilePath();
                String toImgFilePath = PicConverUtil.toJPG(srcImgFilePath,LocalStoreUtils.imgFacePath);
                visitorInfo.setFaceFilePath(toImgFilePath);
                ManagerVisitorUtil.saveVisitorInfo(visitorInfo);
            }catch (Exception e){
                e.printStackTrace();
                throw new FebsException("人员信息存储出现异常!");
            }
        });
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }else{
            return StringUtil.removeIllegalChar(cell.getStringCellValue().trim());
        }
    }

    /**
     * 备份文件转临时文件
     */
    public String tranImgBak2Temp(String filePath){
        String resultPath =  PicConverUtil.moveImgPathNdelete(filePath,LocalStoreUtils.tempFileRestPath,DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_PATTERN)+".zip");
        return resultPath;
    }

    /**
     * 文件上传功能
     *
     * @param developer
     *            按钮控件名称
     */
    public void eventOnImport(Button developer) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        /** 过滤文件类型 * */
        FileNameExtensionFilter filter = new FileNameExtensionFilter("人员备份文件",
                "zip");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(developer);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            /** 得到选择的文件* */
            File file = chooser.getSelectedFile();
            if (file == null) {
                return;
            }
            filePathText.setText(file.getPath());
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
