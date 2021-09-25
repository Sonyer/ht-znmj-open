package com.ht.znmj.view.manager.equipment;

import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.entity.EquipmentInfoCloudFlag;
import com.ht.znmj.entity.EquipmentInfoInOut;
import com.ht.znmj.view.components.DialogFrame;
import com.ht.znmj.view.manager.ManagerFunctionView;
import com.ht.znmj.view.service.ApplicationUtil;
import com.ht.znmj.view.service.HomeEquipmentUtil;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 设备信息
 */
public class EquipmentInforView extends ManagerFunctionView {

    private TextField areaNameText;
    private TextField seqNumText;
    private CheckboxGroup inoutGroup;

    @Override
    public void initView(Window parentFrame,Panel parentPanel, EquipmentInfo equipmentInfo) {
        super.initView(parentFrame,parentPanel, equipmentInfo);

        initContent();
    }

    private void initContent(){
        //重新获取最新设备
        equipmentInfo = HomeEquipmentUtil.findEquipmentInfoById(equipmentInfo.getId());

        Panel contentPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(250, parentPanel.getHeight());
            }
        };
        contentPanel.setBounds(0,0,250,parentPanel.getHeight());
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        Panel snPanel = new Panel();
        snPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Font font = new Font("SimSun", 1, 15);
        Label snLabel = new Label("*SN:");
        snLabel.setFont(font);
        TextField snText = new TextField(15);
        snText.setText(equipmentInfo.getSn());
        snText.setFont(font);
        snText.setEnabled(false);
        snPanel.add(snLabel);
        snPanel.add(snText);

        Panel ipPanel = new Panel();
        ipPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label ipLabel = new Label("*IP:");
        ipLabel.setFont(font);
        TextField ipText = new TextField(15);
        ipText.setText(equipmentInfo.getIp());
        ipText.setEnabled(false);
        ipText.setFont(font);
        ipPanel.add(ipLabel);
        ipPanel.add(ipText);

        Panel areaNamePanel = new Panel();
        areaNamePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label areaNameLabel = new Label("*区域:");
        areaNameLabel.setFont(font);
        areaNameText = new TextField(15);
        areaNameText.setText(equipmentInfo.getAreaName());
        areaNameText.setFont(font);
        areaNamePanel.add(areaNameLabel);
        areaNamePanel.add(areaNameText);

        Panel seqNumPanel = new Panel();
        seqNumPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label seqNumLabel = new Label("*序号:");
        seqNumLabel.setFont(font);
        seqNumText = new TextField(15);
        seqNumText.setText(equipmentInfo.getSeqNum()+"");
        seqNumText.setFont(font);
        seqNumPanel.add(seqNumLabel);
        seqNumPanel.add(seqNumText);

        Panel inOutPanel = new Panel();
        inOutPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Label inOutLabel = new Label("*出入:");
        inOutLabel.setFont(font);
        inoutGroup = new CheckboxGroup(); // 创建一个选择框的小组

        Checkbox noneCheck = new Checkbox(EquipmentInfoInOut.NONE.getName(), inoutGroup, EquipmentInfoInOut.NONE.getCode().equals(equipmentInfo.getInOut()));
        noneCheck.setName(EquipmentInfoInOut.NONE.getCode());
        noneCheck.setFont(font);
        Checkbox inCheck = new Checkbox(EquipmentInfoInOut.IN.getName(), inoutGroup, EquipmentInfoInOut.IN.getCode().equals(equipmentInfo.getInOut()));
        inCheck.setName(EquipmentInfoInOut.IN.getCode());
        inCheck.setFont(font);
        Checkbox outCheck = new Checkbox(EquipmentInfoInOut.OUT.getName(), inoutGroup, EquipmentInfoInOut.OUT.getCode().equals(equipmentInfo.getInOut()));
        outCheck.setName(EquipmentInfoInOut.OUT.getCode());
        outCheck.setFont(font);
        inOutPanel.add(inOutLabel);
        inOutPanel.add(noneCheck);
        inOutPanel.add(inCheck);
        inOutPanel.add(outCheck);


        Button saveButton = new Button("保存");
        saveClickListener(saveButton, equipmentInfo.getId());
        saveButton.setFont(font);

        contentPanel.add(snPanel);
        contentPanel.add(ipPanel);
        contentPanel.add(areaNamePanel);
        contentPanel.add(seqNumPanel);
        contentPanel.add(inOutPanel);
        if(!ApplicationUtil.getApplicationProperties().getCloudFlag().equals("1")) {
            contentPanel.add(saveButton);
        }

        this.parentPanel.add(contentPanel,BorderLayout.CENTER);
        this.parentPanel.revalidate();
    }

    /**
     * 保存按钮
     * @param equipmentId
     */
    private void saveClickListener(Button saveButton,String equipmentId){
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                equipmentInfo = HomeEquipmentUtil.findEquipmentInfoById(equipmentInfo.getId());

                DialogFrame saveMessage = new DialogFrame();
                if(StringUtils.isEmpty(areaNameText.getText().trim()) || StringUtils.isEmpty(seqNumText.getText().trim())){
                    saveMessage.showMessageDialog(parentFrame,"区域、序号、出入口必填!",DialogFrame.TYPE_ERROR,null);
                    return;
                }
               /* if(!StringUtil.isPositiveIntegerInZero(seqNumText.getText().trim())){
                    saveMessage.showMessageDialog( parentFrame,"序号必须为整数!",DialogFrame.TYPE_ERROR,null);
                    return;
                }*/
                equipmentInfo.setAreaName(areaNameText.getText().trim());
                equipmentInfo.setSeqNum(seqNumText.getText().trim());
                equipmentInfo.setInOut(inoutGroup.getSelectedCheckbox().getName().trim());
                equipmentInfo.setCloudFlag(EquipmentInfoCloudFlag.NO_ASY.getCode());
                HomeEquipmentUtil.saveEquipmentInfo(equipmentInfo);

                saveMessage.showMessageDialog(parentFrame,"保存成功!",DialogFrame.TYPE_INFO,null);
            }
        });
    }

}
