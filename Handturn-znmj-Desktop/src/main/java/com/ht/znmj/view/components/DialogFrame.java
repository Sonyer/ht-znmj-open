package com.ht.znmj.view.components;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 信息窗口
 */
public class DialogFrame{

    private Dialog dialog;

    public final static String TYPE_ERROR = "TYPE_ERROR";  //错误信息
    public final static String TYPE_WARN = "TYPE_WARN";   //警告信息
    public final static String TYPE_INFO = "TYPE_INFO";   //提示信息

    /**
     * 提示信息窗口
     * @param parentFrame
     * @param message
     */
   public void showMessageDialog(Window parentFrame, String message,String type,ActionListener confirmListener){
       parentFrame.setEnabled(false);

       if(parentFrame instanceof JFrame){
           dialog = new Dialog((JFrame)parentFrame,"提示信息",false);
       }else if(parentFrame instanceof Dialog){
           dialog = new Dialog((Dialog)parentFrame,"提示信息",false);
       }else{
           dialog = new Dialog((Frame)parentFrame,"提示信息",false);
       }

       String imgIconPath = "images/logo.png";
       if(StringUtils.isEmpty(type)){
           imgIconPath = "images/logo.png";
       }else if(type.equals(TYPE_ERROR)){
           imgIconPath = "images/common_icon/icon_message_error.png";
       }else if(type.equals(TYPE_WARN)){
           imgIconPath = "images/common_icon/icon_message_warn.png";
       }else if(type.equals(TYPE_INFO)){
           imgIconPath = "images/common_icon/icon_message_success.png";
       }else{
           imgIconPath = "images/logo.png";
       }
       ImageIcon imageIcon = new ImageIcon(imgIconPath);
       imageIcon.setImage(imageIcon.getImage().getScaledInstance(40, 40,Image.SCALE_DEFAULT ));

       Panel contentPanel = new Panel();

       Panel messagePanel = new Panel(){
           public Dimension getPreferredSize() {
               return new Dimension(300, 170);
           }
       };
       messagePanel.setBounds(0,0,300,170);

       JLabel messageLabel = new JLabel();
       messageLabel.setSize(300,170);
       Font font = new Font("SimSun", 1, 12);
       messageLabel.setFont(font);
       messageLabel.setIcon(imageIcon);
       jlabelSetText(messageLabel,message);
       messagePanel.add(messageLabel);
       messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
       contentPanel.add(messageLabel,BorderLayout.CENTER);

       Panel bottomPanel = new Panel(){
           public Dimension getPreferredSize() {
               return new Dimension(300, 30);
           }
       };

       bottomPanel.setBounds(0,0,300,30);
       Button okButton = new Button("确认");
       okButton.setFont(font);
       Dialog finalDialog = dialog;
       if(confirmListener != null){
           okButton.addActionListener(confirmListener);
       }else{
           okButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   parentFrame.setEnabled(true);
                   finalDialog.dispose();
               }
           });
       }
       bottomPanel.add(okButton);
       bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
       contentPanel.add(bottomPanel,BorderLayout.SOUTH);

       dialog.add(contentPanel,BorderLayout.CENTER);
       int x = parentFrame.getX() + (parentFrame.getWidth()-parentFrame.getX())/2-150;
       int y = parentFrame.getY() + (parentFrame.getHeight()-parentFrame.getY())/2-150;
       dialog.setBounds(x, y, 300, 200);//设置大小位置
       //dialog.setAlwaysOnTop(true);
       dialog.setVisible(true);
       Dialog finalDialog1 = dialog;
       dialog.addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent e) {
               parentFrame.setEnabled(true);
               finalDialog1.dispose();
           }
       });
   }

    /**
     * 关闭提示窗口
     */
    public void dispose(){
        dialog.dispose();
    }

    /**
     * 确认信息窗口
     * @param parentFrame
     * @param message
     */
    public void showConfirmMessageDialog(JFrame parentFrame, String message,ActionListener confirmListener,ActionListener cancelListener){
        parentFrame.setEnabled(false);

        dialog = new Dialog(parentFrame,"确认信息",false);

        String imgIconPath = "images/common_icon/icon_message_success.png";
        ImageIcon imageIcon = new ImageIcon(imgIconPath);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(40, 40,Image.SCALE_DEFAULT ));

        Panel contentPanel = new Panel();

        Panel messagePanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(300, 170);
            }
        };
        messagePanel.setBounds(0,0,300,170);

        JLabel messageLabel = new JLabel();
        messageLabel.setSize(300,170);
        Font font = new Font("SimSun", 1, 15);
        messageLabel.setFont(font);
        messageLabel.setIcon(imageIcon);
        jlabelSetText(messageLabel,message);
        messagePanel.add(messageLabel);
        messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        contentPanel.add(messageLabel,BorderLayout.CENTER);

        Panel bottomPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(300, 30);
            }
        };

        bottomPanel.setBounds(0,0,300,30);
        Button okButton = new Button("确认");
        okButton.setFont(font);
        okButton.addActionListener(confirmListener);
        bottomPanel.add(okButton);

        Button cancelButton = new Button("取消");
        cancelButton.setFont(font);
        cancelButton.addActionListener(cancelListener);
        bottomPanel.add(cancelButton);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentPanel.add(bottomPanel,BorderLayout.SOUTH);

        dialog.add(contentPanel,BorderLayout.CENTER);
        int x = parentFrame.getX() + (parentFrame.getWidth()-parentFrame.getX())/2;
        int y = parentFrame.getY() + (parentFrame.getHeight()-parentFrame.getY())/2;
        dialog.setBounds(x, y, 300, 200);//设置大小位置
        //dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                parentFrame.setEnabled(true);
                dialog.dispose();
            }
        });
    }

    public void jlabelSetText(JLabel jLabel, String longString) {
        StringBuilder builder = new StringBuilder("<html>");
        char[] chars = longString.toCharArray();
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        int diffWidth = jLabel.getWidth()-jLabel.getIcon().getIconWidth() -50;
        int start = 0;
        int len = 0;
        while (start + len < longString.length()) {
            while (true) {
                len++;
                if (start + len > longString.length())break;
                    if (fontMetrics.charsWidth(chars, start, len) > diffWidth) {
                        break;
                    }
            }
            builder.append(chars, start, len-1).append("<br/>");
            start = start + len - 1;
            len = 0;
        }
        builder.append(chars, start, longString.length()-start);
        builder.append("</html>");
        jLabel.setText(builder.toString());
    }
}
