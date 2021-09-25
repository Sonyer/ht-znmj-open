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
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Date;

/**
 * 人员编辑界面
 */
public class VisitorHomeEditDialog {
    /**
     * 变量
     */
    private String tempImgPath;
    private String faceImgPath;
    private VisitorInfo visitorInfo;
    private String editType;


    private Window parentFrame;
    private TablePagePanel tablePagePanel;
    private Dialog owerDialog;
    private JLabel faceShowLabel;

    public final static String UPDATE_TYPE = "UPDATE_TYPE";  //修改类型
    public final static String NEW_TYPE = "NEW_TYPE";   //新增类型

    /**
     * 输入组件
     */
    private TextField visitorNameText;
    private TextField visitorPhoneNumberText;
    private TextField visitorIdCardText;
    private TextField wiganText;
    private TextField departmentText;
    private TextField positorText;
    private DateChooserJButton startTimeText;
    private DateChooserJButton endTimeText;
    private TextArea remarkText;

    /**
     * 展示页面
     * @param parentFrame
     * @param editType
     * @param visitorInfo
     */
    public void showEditDialog(Window parentFrame, TablePagePanel tablePagePanel, String editType, VisitorInfo visitorInfo){
        parentFrame.setEnabled(false);

        this.visitorInfo = visitorInfo;
        this.editType = editType;
        this.parentFrame = parentFrame;
        this.tablePagePanel = tablePagePanel;

        String tittle = "人员编辑";
        if(editType.equals(UPDATE_TYPE)){
            tittle = "人员修改";
        }else{
            tittle = "人员新增";
        }

        if(parentFrame instanceof JFrame){
            owerDialog = new Dialog((JFrame)parentFrame,tittle,false);
        }else if(parentFrame instanceof Dialog){
            owerDialog = new Dialog((Dialog)parentFrame,tittle,false);
        }else{
            owerDialog = new Dialog((Frame)parentFrame,tittle,false);
        }

        Panel centerEntityPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(500, 400);
            }
        };
        centerEntityPanel.setBounds(0,0,500, 400);
        centerEntityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        Font font = new Font("SimSun", 1, 12);
        Panel visitorNamePanel = new Panel();
        visitorNamePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label visitorNameLabel = new Label("*人员名:");
        visitorNameLabel.setFont(font);
        visitorNameText = new TextField(15);
        visitorNameText.setFont(font);
        visitorNameText.setText(visitorInfo.getName());
        visitorNameText.setEnabled(true);
        visitorNamePanel.add(visitorNameLabel);
        visitorNamePanel.add(visitorNameText);
        centerEntityPanel.add(visitorNamePanel);

        Panel visitorPhoneNumberPanel = new Panel();
        visitorPhoneNumberPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label visitorPhoneNumberLabel = new Label("*手机号:");
        visitorPhoneNumberLabel.setFont(font);
        visitorPhoneNumberText = new TextField(15);
        visitorPhoneNumberText.setFont(font);
        visitorPhoneNumberText.setText(visitorInfo.getPhoneNumber());
        visitorPhoneNumberText.setEnabled(true);
        visitorPhoneNumberPanel.add(visitorPhoneNumberLabel);
        visitorPhoneNumberPanel.add(visitorPhoneNumberText);
        centerEntityPanel.add(visitorPhoneNumberPanel);

        Panel visitorIdCardPanel = new Panel();
        visitorIdCardPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label visitorIdCardLabel = new Label(" 身份证:");
        visitorIdCardLabel.setFont(font);
        visitorIdCardText = new TextField(15);
        visitorIdCardText.setFont(font);
        visitorIdCardText.setText(visitorInfo.getIdCard());
        visitorIdCardText.setEnabled(true);
        visitorIdCardPanel.add(visitorIdCardLabel);
        visitorIdCardPanel.add(visitorIdCardText);
        centerEntityPanel.add(visitorIdCardPanel);

        Panel wiganPanel = new Panel();
        wiganPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label wiganLabel = new Label(" 韦根号:");
        wiganLabel.setFont(font);
        wiganText = new TextField(15);
        wiganText.setFont(font);
        wiganText.setText(visitorInfo.getWigan());
        wiganText.setEnabled(true);
        wiganPanel.add(wiganLabel);
        wiganPanel.add(wiganText);
        centerEntityPanel.add(wiganPanel);

        Panel departmentPanel = new Panel();
        departmentPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label departmentLabel = new Label(" 部门名:");
        departmentLabel.setFont(font);
        departmentText = new TextField(15);
        departmentText.setFont(font);
        departmentText.setText(visitorInfo.getDepartment());
        departmentText.setEnabled(true);
        departmentPanel.add(departmentLabel);
        departmentPanel.add(departmentText);
        centerEntityPanel.add(departmentPanel);

        Panel positorPanel = new Panel();
        positorPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label positorLabel = new Label(" 岗位名:");
        positorLabel.setFont(font);
        positorText = new TextField(15);
        positorText.setFont(font);
        positorText.setText(visitorInfo.getPositor());
        positorText.setEnabled(true);
        positorPanel.add(positorLabel);
        positorPanel.add(positorText);
        centerEntityPanel.add(positorPanel);

        Panel startOpenTimePanel = new Panel();
        startOpenTimePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label startOpenTimeLabel = new Label("有效时间:");
        startOpenTimeLabel.setFont(font);
        startOpenTimePanel.add(startOpenTimeLabel);
        if(visitorInfo.getStartTime() == null){
            startTimeText = new DateChooserJButton(DateUtil.getDateFormat(new Date(),DateUtil.FULL_TIME_SPLIT_PATTERN));
        }else{
            startTimeText = new DateChooserJButton(visitorInfo.getStartTime());
        }
        startTimeText.setFont(font);
        startOpenTimePanel.add(startTimeText);
        Label splitLabel = new Label("-");
        splitLabel.setFont(font);
        startOpenTimePanel.add(splitLabel);
        if(visitorInfo.getEndTime() == null){
            endTimeText = new DateChooserJButton("2099-12-31 23:59:59");
        }else{
            endTimeText = new DateChooserJButton(visitorInfo.getEndTime());
        }
        endTimeText.setFont(font);
        startOpenTimePanel.add(endTimeText);
        centerEntityPanel.add(startOpenTimePanel);

        faceShowLabel = new JLabel(){
            public Dimension getPreferredSize() {
                return new Dimension(120, 170);
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
        faceImgPath = visitorInfo.getFaceFilePath();
        faceShowLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                eventOnImport(faceShowLabel);
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
        faceShowLabel.setBounds(0,0,120,170);
        centerEntityPanel.add(faceShowLabel);

        Panel remarkPanel = new Panel();
        remarkPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label remarkLabel = new Label("备 注:");
        remarkLabel.setFont(font);
        remarkText = new TextArea();
        remarkText.setText(visitorInfo.getRemark());
        remarkText.setRows(8);
        remarkText.setColumns(32);
        remarkText.setFont(font);
        remarkText.setEnabled(true);
        remarkPanel.add(remarkLabel);
        remarkPanel.add(remarkText);
        centerEntityPanel.add(remarkPanel);


        Panel bottomEntityPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(parentFrame.getWidth(), 40);
            }
        };
        bottomEntityPanel.setBounds(0,0,parentFrame.getWidth(), 40);

        Button saveButton = new Button("保存");
        saveClickListener(saveButton,visitorInfo.getId());
        saveButton.setFont(font);
        bottomEntityPanel.add(saveButton);
        bottomEntityPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        int x = parentFrame.getX() + (parentFrame.getWidth()-parentFrame.getX())/2-300;
        int y = parentFrame.getY() + (parentFrame.getHeight()-parentFrame.getY())/2-200;
        owerDialog.setBounds(x, y, 500, 450);//设置大小位置
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

        //展示图片
        showUploadFaceImg();
    }

    /**
     * 保存按钮
     */
    private void saveClickListener(Button saveButton,String visitorId){
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogFrame saveMessage = new DialogFrame();

                if(StringUtils.isEmpty(visitorNameText.getText().trim())
                        || StringUtils.isEmpty(visitorPhoneNumberText.getText().trim())
                        || StringUtils.isEmpty(startTimeText.getText().trim())
                        || StringUtils.isEmpty(endTimeText.getText().trim())){
                    saveMessage.showMessageDialog(owerDialog,"人员名、手机号码、有效时间必填!",DialogFrame.TYPE_ERROR,null);
                    return;
                }

                if(!StringUtils.isEmpty(wiganText.getText().trim())){
                    if(!StringUtil.isPositiveIntegerInZero(wiganText.getText().trim())){
                        saveMessage.showMessageDialog(owerDialog,"韦根号为10位以内整数!",DialogFrame.TYPE_ERROR,null);
                        return;
                    }else{
                        if(wiganText.getText().trim().length() > 10){
                            saveMessage.showMessageDialog(owerDialog,"韦根号为10位以内整数!",DialogFrame.TYPE_ERROR,null);
                            return;
                        }
                    }
                }

                String faceImgPathSave = "";
                if(StringUtils.isEmpty(tempImgPath) && StringUtils.isEmpty(faceImgPath)){
                    saveMessage.showMessageDialog(owerDialog,"请选择人脸图片!",DialogFrame.TYPE_ERROR,null);
                    return;
                }else{
                    if(!StringUtils.isEmpty(tempImgPath)){
                        faceImgPathSave = tranImgTemp2Face(tempImgPath,faceImgPath);
                    }else{
                        faceImgPathSave = faceImgPath;
                    }
                }

                VisitorInfo visitorInfo = null;
                if(StringUtils.isEmpty(visitorId)){
                    visitorInfo = ManagerVisitorUtil.findVisitorInfoByUK(visitorNameText.getText().trim(),visitorPhoneNumberText.getText().trim(),visitorId);
                    if(visitorInfo == null){
                        visitorInfo = new VisitorInfo();
                    }

                }else{
                    visitorInfo = ManagerVisitorUtil.findVisitorInfoByUK(visitorNameText.getText().trim(),visitorPhoneNumberText.getText().trim(),visitorId);
                    if(visitorInfo == null){
                        visitorInfo = ManagerVisitorUtil.findVisitorInfoById(visitorId);
                        if(visitorInfo == null){
                            visitorInfo = new VisitorInfo();
                        }
                    }
                }

                visitorInfo.setFaceFilePath(faceImgPathSave);
                visitorInfo.setName(visitorNameText.getText().trim());
                visitorInfo.setPhoneNumber(visitorPhoneNumberText.getText().trim());
                visitorInfo.setIdCard(visitorIdCardText.getText().trim());
                visitorInfo.setWigan(wiganText.getText().trim());
                visitorInfo.setDepartment(departmentText.getText().trim());
                visitorInfo.setPositor(positorText.getText().trim());
                visitorInfo.setStartTime(startTimeText.getDate());
                visitorInfo.setEndTime(endTimeText.getDate());
                visitorInfo.setRemark(remarkText.getText());

                //人员信息保存
                VisitorInfo newVisitorInfo = ManagerVisitorUtil.saveVisitorInfo(visitorInfo);

                saveMessage.showMessageDialog(parentFrame,"保存成功!",DialogFrame.TYPE_INFO,null);

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
        if(editType.equals(UPDATE_TYPE)){
            tempImgPath = "";
        }else{
            visitorInfo = null;
            tempImgPath = "";
            faceImgPath = "";
            visitorNameText.setText("");
            visitorPhoneNumberText.setText("");
            visitorIdCardText.setText("");
            wiganText.setText("");
            departmentText.setText("");
            positorText.setText("");
            remarkText.setText("");
        }
    }

    /**
     * 文件上传功能
     *
     * @param developer
     *            按钮控件名称
     */
    public void eventOnImport(Container developer) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        /** 过滤文件类型 * */
        FileNameExtensionFilter filter = new FileNameExtensionFilter("人脸图片",
                "jpg", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(developer);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            /** 得到选择的文件* */
            File file = chooser.getSelectedFile();
            if (file == null) {
                return;
            }
            tempImgPath = PicConverUtil.toJPG(file.getPath(),LocalStoreUtils.imgTempPath);
            //展示图片
            this.showUploadFaceImg();
        }
    }

    /**
     * 临时文件转人脸图片
     */
    public String tranImgTemp2Face(String tempImgPath,String faceImgPath){

        String resultPath = PicConverUtil.moveImgPath(tempImgPath,LocalStoreUtils.imgFacePath);

        if(!StringUtils.isEmpty(tempImgPath) && StringUtils.isEmpty(faceImgPath)){
            this.tempImgPath = "";
            this.faceImgPath = "";
        }

        if(!StringUtils.isEmpty(tempImgPath) && !StringUtils.isEmpty(faceImgPath)){
            this.tempImgPath = "";
            this.faceImgPath = resultPath;
        }

        //展示图片
        this.showUploadFaceImg();
        return resultPath;
    }

    /**
     * 展示图片
     */
    public void showUploadFaceImg(){
        if(!StringUtils.isEmpty(tempImgPath)){
            ImageIcon imageIcon = new ImageIcon(tempImgPath);
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(120, 170,Image.SCALE_DEFAULT ));
            faceShowLabel.setIcon(imageIcon);
        }else{
            if(!StringUtils.isEmpty(faceImgPath)){
                ImageIcon imageIcon = new ImageIcon(faceImgPath);
                imageIcon.setImage(imageIcon.getImage().getScaledInstance(120, 170,Image.SCALE_DEFAULT ));
                faceShowLabel.setIcon(imageIcon);
            }else{
                ImageIcon imageIcon = new ImageIcon("images/common_icon/empty-img.png");
                imageIcon.setImage(imageIcon.getImage().getScaledInstance(120, 170,Image.SCALE_DEFAULT ));
                faceShowLabel.setIcon(imageIcon);
            }
        }
        faceShowLabel.revalidate();
    }
}
