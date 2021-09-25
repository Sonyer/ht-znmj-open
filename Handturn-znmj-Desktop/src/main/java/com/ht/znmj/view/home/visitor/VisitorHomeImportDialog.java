package com.ht.znmj.view.home.visitor;

import com.ht.znmj.entity.VisitorInfo;
import com.ht.znmj.utils.DateUtil;
import com.ht.znmj.utils.LocalStoreUtils;
import com.ht.znmj.utils.PicConverUtil;
import com.ht.znmj.utils.StringUtil;
import com.ht.znmj.view.components.DateChooserJButton;
import com.ht.znmj.view.components.DialogFrame;
import com.ht.znmj.view.components.TablePagePanel;
import com.ht.znmj.view.service.ManagerVisitorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * 人员编辑界面
 */
public class VisitorHomeImportDialog {
    private Window parentFrame;
    private TablePagePanel tablePagePanel;
    private Dialog owerDialog;

    /**
     * 输入组件
     */
    private TextField filePathText;
    private DateChooserJButton startTimeText;
    private DateChooserJButton endTimeText;
    private TextArea remarkText;
    private JTextArea noticeText;
    private TextArea errorText;

    /**
     * 展示页面
     * @param parentFrame
     */
    public void showDialog(Window parentFrame, TablePagePanel tablePagePanel){
        parentFrame.setEnabled(false);

        this.parentFrame = parentFrame;
        this.tablePagePanel = tablePagePanel;

        String tittle = "人员导入";

        if(parentFrame instanceof JFrame){
            owerDialog = new Dialog((JFrame)parentFrame,tittle,false);
        }else if(parentFrame instanceof Dialog){
            owerDialog = new Dialog((Dialog)parentFrame,tittle,false);
        }else{
            owerDialog = new Dialog((Frame)parentFrame,tittle,false);
        }

        Panel centerEntityPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(550, 570);
            }
        };
        centerEntityPanel.setBounds(0,0,550, 570);
        centerEntityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        Font font = new Font("SimSun", 1, 12);
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

        Panel startOpenTimePanel = new Panel();
        startOpenTimePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label startOpenTimeLabel = new Label("有效时间:");
        startOpenTimeLabel.setFont(font);
        startOpenTimePanel.add(startOpenTimeLabel);
        startTimeText = new DateChooserJButton(DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN));
        startTimeText.setFont(font);
        startOpenTimePanel.add(startTimeText);
        Label splitLabel = new Label("-");
        splitLabel.setFont(font);
        startOpenTimePanel.add(splitLabel);
        endTimeText = new DateChooserJButton("2099-12-31 23:59:59");
        endTimeText.setFont(font);
        startOpenTimePanel.add(endTimeText);
        centerEntityPanel.add(startOpenTimePanel);


        Panel remarkPanel = new Panel();
        remarkPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label remarkLabel = new Label("备 注:");
        remarkLabel.setFont(font);
        remarkText = new TextArea();
        remarkText.setRows(4);
        remarkText.setColumns(60);
        remarkText.setFont(font);
        remarkText.setEnabled(true);
        remarkPanel.add(remarkLabel);
        remarkPanel.add(remarkText);
        centerEntityPanel.add(remarkPanel);

        String noteText = "<html><p>1.可导入文件或文件夹;</p>" +
                "<p>2.文件和文件夹中图片必须为<font color='red'>(.jpg .png)</font>格式);</p>" +
                "<p>3.图片文件名格式必须为<font color='red'>“姓名_手机号码_部门名_岗位名_身份证_韦根号.jpg”(其中姓名与手机号码必填,其他信息没有的填写“无”,每个信息使用“_”隔开)。</font></p></html>";
        JLabel noticeLabel = new JLabel(noteText){
            public Dimension getPreferredSize() {
                return new Dimension(500, 120);
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
        noticeLabel.setFont(font);
        centerEntityPanel.add(noticeLabel);

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

        Button saveButton = new Button("导入");
        saveClickListener(saveButton);
        saveButton.setFont(font);
        bottomEntityPanel.add(saveButton);
        bottomEntityPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        int x = parentFrame.getX() + (parentFrame.getWidth()-parentFrame.getX())/2-300;
        int y = parentFrame.getY() + (parentFrame.getHeight()-parentFrame.getY())/2-200;
        owerDialog.setBounds(x, y, 550, 570);//设置大小位置
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
     * 文件上传
     */
    private void uploadFiles(){
        String filePaths = filePathText.getText().trim();
        if(StringUtils.isEmpty(filePaths)){
            DialogFrame dialogMessage = new DialogFrame();
            dialogMessage.showMessageDialog(owerDialog,"请选择人脸文件或文件夹!",DialogFrame.TYPE_ERROR,null);
            return;
        }else{
            errorText.setText("");
            errorText.setText(DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-文件扫描检测开始!\n");
            String[] filePathArr = filePaths.split("\\|");

            Integer errorCount = 0;
            java.util.List<VisitorInfo> visitorInfoList = new ArrayList<VisitorInfo>();

            try {
                for (int i = 0; i < filePathArr.length; i++) {
                    filesScan(filePathArr[i].trim(), errorCount, visitorInfoList);
                }
            }catch (Exception e){
                errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!扫描异常，导入全部失败!\n");
                return;
            }

            errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!校验错误文件个数:"+errorCount+"!\n");
            errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-文件扫描检测结束!\n");

            if(errorCount > 0){
                errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!扫描异常，导入全部失败!\n");
            }else{
                if(visitorInfoList.size() <= 0){
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-无可导入的图片,导入完成!\n");
                }else{
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-图片数据导入开始!\n");
                    visitorInfoList.forEach(visitorInfo ->{
                        try {
                            //图片转存
                            String faceImgPathSave = "";
                            if(!StringUtils.isEmpty(visitorInfo.getFaceFilePath())){
                                faceImgPathSave = tranImgTemp2Face(visitorInfo.getFaceFilePath());
                            }
                            visitorInfo.setFaceFilePath(faceImgPathSave);

                            ManagerVisitorUtil.saveVisitorInfo(visitorInfo);
                        }catch(Exception e){
                            errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!数据处理失败!:"+e.getMessage()+"("+visitorInfo.getName()+"-"+visitorInfo.getPhoneNumber()+")\n");
                        }
                    });
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-图片数据导入完成!\n");
                }
            }
        }
    }

    /**
     * 文件扫描
     * @param filePath
     * 姓名_手机号码_职位名_岗位名_身份证_韦根号.jpg
     */
    private void filesScan(String filePath, Integer errorCount, java.util.List<VisitorInfo> visitorInfoList){
        File file = new File(filePath);
        if(file.isDirectory()){
            errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-文件夹:"+filePath+"\n");
            for(int i =0 ;i < file.listFiles().length; i++){
                String childFileName = file.listFiles()[i].getAbsolutePath();
                filesScan(childFileName,errorCount,visitorInfoList);
            }
        }else{
            String lastPre = filePath.substring(filePath.lastIndexOf("."),filePath.length());
            if(lastPre.toUpperCase().contentEquals(".JPG") || lastPre.toUpperCase().contentEquals(".PNG")){
                errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"-人脸图片:"+filePath+"\n");

                String fileNme = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.lastIndexOf(".")-1);
                String[] fileFields = fileNme.split("_");
                if(fileFields.length < 3){
                    errorCount ++;
                    errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!错误格式:"+filePath+"\n");
                }else{
                    boolean successFlag = true;
                    String visitorName = fileFields[0];
                    String phoneNumer = fileFields[1];
                    String department = null;
                    if(fileFields.length > 2){
                        department = fileFields[2];
                    }
                    String positor = null;
                    if(fileFields.length > 3){
                        positor = fileFields[3];
                    }
                    String idCard = null;
                    if(fileFields.length > 4){
                        idCard = fileFields[4];
                    }
                    String wigen = null;
                    if(fileFields.length > 5){
                        wigen = fileFields[5];
                        if(!StringUtil.isPositiveIntegerInZero(wigen.trim())){
                            successFlag = false;
                            errorCount ++;
                            errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!错误格式:"+filePath+"(韦根号为10位以内整数!)\n");
                        }else{
                            if(wigen.trim().length() > 10){
                                successFlag = false;
                                errorCount ++;
                                errorText.setText(errorText.getText()+DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN)+"!!!!!错误格式:"+filePath+"(韦根号为10位以内整数!)\n");
                            }
                        }
                    }

                    if(successFlag){
                        VisitorInfo visitorInfo = new VisitorInfo();
                        visitorInfo.setName(visitorName);
                        visitorInfo.setPhoneNumber(phoneNumer);
                        visitorInfo.setDepartment(department);
                        visitorInfo.setPositor(positor);
                        visitorInfo.setIdCard(idCard);
                        visitorInfo.setWigan(wigen);
                        visitorInfo.setFaceFilePath(filePath);
                        visitorInfo.setStartTime(startTimeText.getDate());
                        visitorInfo.setEndTime(endTimeText.getDate());
                        visitorInfoList.add(visitorInfo);
                    }
                }
            }
        }
    }

    /**
     * 文件上传功能
     *
     * @param developer
     *            按钮控件名称
     */
    public void eventOnImport(Button developer) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled(true);
        /** 过滤文件类型 * */
        /*FileNameExtensionFilter filter = new FileNameExtensionFilter("人脸图片");
        chooser.setFileFilter(filter);*/
        int returnVal = chooser.showOpenDialog(developer);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            /** 得到选择的文件* */
            File[] files = chooser.getSelectedFiles();
            if (files == null) {
                return;
            }
            StringBuffer filePaths = new StringBuffer();
            for (int i = 0; i < files.length; i++){
                filePaths.append(files[i].getAbsolutePath()+"|");
            }
            filePathText.setText(filePaths.toString());
        }
    }

    /**
     * 保存按钮
     */
    private void saveClickListener(Button saveButton){
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadFiles();

                //重置数据
                resetForm();

                //刷新表格
                if(tablePagePanel != null){
                    tablePagePanel.flushData();
                }
            }
        });
    }

    /**
     * 重置数据
     */
    public void resetForm(){

    }

    /**
     * 临时文件转人脸图片
     */
    public String tranImgTemp2Face(String filePath){
        String resultPath =  PicConverUtil.toJPG(filePath,LocalStoreUtils.imgFacePath);
        return resultPath;
    }
}
