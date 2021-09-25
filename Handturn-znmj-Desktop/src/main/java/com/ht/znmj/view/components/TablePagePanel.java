package com.ht.znmj.view.components;

import com.alibaba.fastjson.JSONObject;
import com.ht.znmj.utils.DateUtil;
import com.ht.znmj.utils.StringUtil;
import com.ht.znmj.utils.TransObjectUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

/**
 * Table分页组件
 */
@Data
public class TablePagePanel<T> extends Panel{
    /**
     * 数据索引
     */
    private Map<Integer,Object> datas = new HashMap<>();

    private Panel parentPanel;

    /**
     * 分页变量
     */
    public long pageIndex = 1;
    public long pageSize = 10;
    public long totalPageNum = 0;
    public long totalSize = 0;

    /**
     * 组件
     */
    private JTable tableGird = new JTable();
    private TextField currPageText;
    private Label totalPageLabel;
    private Label totalRowsLabel;



    /**
     * 字段列明
     */
    public String[] columnNames = new String[]{};

    /**
     * 索引字段
     */
    private Map<Integer,String> indexMapFiled = new HashMap<>();
    private Map<Integer,Integer> indexMapType = new HashMap<>();
    private Map<Integer,Map<String,Object>> indexMapDic = new HashMap<>();

    /**
     * 字段列
     */
    public java.util.List<ColumnBean> columnBeanList = new ArrayList<>();

    /**
     * 行高
     */
    public int rowHeight = 50;

    /**
     * 是否可编辑单元格
     */
    public Boolean isCelEditable = false;

    /**
     * 刷新页面回调实现
     */
    public void flushData() {

    }

    /**
     * 选择行回调回调实现
     */
    public void checkRow(int row) {

    }

    /**
     * 初始化组件
     */
    public void initView(Window parentFrame,Boolean needPage){
        parentPanel = this;

        tableGird = new JTable();
        tableGird.setCellSelectionEnabled(false);
        tableGird.setColumnSelectionAllowed(false);
        tableGird.setRowSelectionAllowed(true);
        /*Font tableFont = new Font("SimSun", 1, 12);
        tableGird.setFont(tableFont);*/
        tableGird.setRowHeight(rowHeight);
        tableGird.setLayout(new FlowLayout(FlowLayout.LEFT));
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        cr.setHorizontalAlignment(JLabel.CENTER);
        tableGird.setDefaultRenderer(Object.class,cr);
        tableGird.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                int row = tableGird.getSelectedRow();
                if(row != -1){
                    checkRow(row);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tableGird) {
            public Dimension getPreferredSize() {
                return new Dimension(parentPanel.getWidth(), parentPanel.getHeight()-40);
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
        scroll.setSize(parentPanel.getWidth(), parentPanel.getHeight()-40);

        this.add(scroll,BorderLayout.CENTER);

        Panel pageToolsPanel = new Panel(){
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
        pageToolsPanel.setBounds(0,0,parentPanel.getWidth(), 35);
        pageToolsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JComboBox pageSizeBox=new JComboBox();    //创建JComboBox
        pageSizeBox.addItem("10");
        pageSizeBox.addItem("40");
        pageSizeBox.addItem("100");
        pageSizeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageSize = Integer.valueOf(pageSizeBox.getSelectedItem().toString());

                flushData();
            }
        });
        pageToolsPanel.add(pageSizeBox);
        Button firstpageButton = new Button("第一页");
        pageToolsPanel.add(firstpageButton);
        firstpageButton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  pageIndex = 1;
                  currPageText.setText(pageIndex+"");

                  flushData();
              }
        });
        Button uppageButton = new Button("上一页");
        uppageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pageIndex-1 < 1){
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(parentFrame,"已经是第一页!",DialogFrame.TYPE_WARN,null);
                }else {
                    pageIndex --;
                    currPageText.setText(pageIndex + "");

                    flushData();
                }
            }
        });
        pageToolsPanel.add(uppageButton);

        currPageText = new TextField(2);
        currPageText.setText(pageIndex+"");
        currPageText.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!StringUtil.isPositiveIntegerNoZero(currPageText.getText())){
                    currPageText.setText(pageIndex+"");
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(parentFrame,"页码请填写数字!",DialogFrame.TYPE_WARN,null);
                }else if(Integer.valueOf(currPageText.getText()) > totalPageNum){
                    currPageText.setText(pageIndex+"");
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(parentFrame,"页码超过最大页数!",DialogFrame.TYPE_WARN,null);
                }

                flushData();
            }
        });
        pageToolsPanel.add(currPageText);
        Label splitLabel = new Label("/");
        pageToolsPanel.add(splitLabel);
        totalPageLabel = new Label(totalPageNum+"");
        totalPageLabel.setAlignment(Label.LEFT);
        pageToolsPanel.add(totalPageLabel);

        Button downpageButton = new Button("下一页");
        downpageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pageIndex >= totalPageNum){
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(parentFrame,"已经是最后一页!",DialogFrame.TYPE_WARN,null);
                }else {
                    pageIndex ++;
                    currPageText.setText(pageIndex + "");

                    flushData();
                }
            }
        });
        pageToolsPanel.add(downpageButton);
        Button lastButton = new Button("最后一页");
        lastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageIndex = totalPageNum;
                currPageText.setText(pageIndex + "");

                flushData();
            }
        });
        pageToolsPanel.add(lastButton);

        Label totalRowsTittleLabel = new Label("共:");
        totalRowsTittleLabel.setAlignment(Label.RIGHT);
        pageToolsPanel.add(totalRowsTittleLabel);
        totalRowsLabel = new Label(totalSize+"");
        pageToolsPanel.add(totalRowsLabel);
        Label totalRowsUnitLabel = new Label("条");
        totalRowsUnitLabel.setAlignment(Label.LEFT);
        pageToolsPanel.add(totalRowsUnitLabel);

        if(!needPage){
            pageToolsPanel.setVisible(false);
        }else{
            pageToolsPanel.setVisible(true);
        }
        this.add(pageToolsPanel,BorderLayout.SOUTH);

    }

    /**
     *
     * @param dates
     * @param totalSize
     */
    public void setData(List<T> dates,long totalSize){
        String[][] objs = new String[dates.size()][indexMapFiled.size()+1];
        for(int i = 0; i < dates.size(); i ++){
            Object obj = dates.get(i);
            //数据缓存
            datas.put(i,obj);

            Map<String,Object> maps = null;
            try {
                maps = TransObjectUtil.getObjectToMap(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            //Map<String,Object> maps = JSONObject.parseObject(JSONObject.toJSONString(obj), Map.class);
            int finalI = i;
            objs[finalI][0] = String.valueOf(i+1);
            Map<String, Object> finalMaps = maps;
            indexMapFiled.keySet().forEach(index ->{
                Object value = finalMaps.get(indexMapFiled.get(index)==null?"":indexMapFiled.get(index));
                if(value == null){
                    objs[finalI][index] = "";
                }else {
                    if (value instanceof Date) {
                        objs[finalI][index] = DateUtil.getDateFormat((Date) value, DateUtil.FULL_TIME_SPLIT_PATTERN);
                    } else {
                        if(indexMapDic.get(index) != null){
                            Map<String,Object> dataMap = indexMapDic.get(index);
                            objs[finalI][index] = dataMap.get(value.toString()) == null?value.toString():dataMap.get(value.toString()).toString();
                        }else {
                            objs[finalI][index] = value.toString();
                        }
                    }
                }
            });
        }

        DefaultTableModel model = new DefaultTableModel(objs, columnNames){
            public boolean isCellEditable(int row, int column) {
                return isCelEditable;
            }
        };
        //清空数据
        if(totalSize <= 0){
            model.setRowCount(0);
        }

        tableGird.setModel(model);

        tableGird.getColumnModel().getColumn(0).setPreferredWidth(20);
        for(int i = 0; i < columnBeanList.size(); i ++) {
            ColumnBean columnBean = columnBeanList.get(i);
            tableGird.getColumnModel().getColumn(i+1).setPreferredWidth(columnBean.getWidth());
            if(columnBean.getType() != null && columnBean.getType() == ColumnBean.TYPE_IMG){
                tableGird.getColumnModel().getColumn(i+1).setCellRenderer(new JTableImgCellRender());
            }
        }

        Long newTotalPageNum = totalSize%pageSize > 0? totalSize/pageSize+1:totalSize/pageSize;
        if(newTotalPageNum < this.totalPageNum){
            if(pageIndex > this.totalPageNum){
                this.pageIndex = newTotalPageNum;
                this.currPageText.setText(this.pageIndex+"");
            }
        }

        this.totalPageNum = newTotalPageNum;
        this.totalSize = totalSize;
        this.totalPageLabel.setText(this.totalPageNum+"");
        this.totalRowsLabel.setText( this.totalSize+"");
    }


    /**
     * 图片显示
     */
    class JTableImgCellRender extends JLabel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel imgLabel = new JLabel();
            if(!StringUtils.isEmpty(value.toString())){
                ImageIcon imageIcon = new ImageIcon(value.toString());
                imageIcon.setImage(imageIcon.getImage().getScaledInstance(35, 45,Image.SCALE_DEFAULT ));
                imgLabel.setIcon(imageIcon);
            }
            return imgLabel;
        }
    }
    /**
     * 获取选择行数据
     * @return
     */
    public List<Object> getSelectedRows(){
        int[] indexArray = tableGird.getSelectedRows();
        List<Object> selectObjs = new ArrayList<>();
        for(int i = 0; i < indexArray.length; i++){
            int rowIndex = indexArray[i];
            selectObjs.add(datas.get(rowIndex));
        }
        return selectObjs;
    }

    /**
     * 获取选择行数据
     * @return
     */
    public Object getSelectedRow(int row){
        return datas.get(row) == null?null:datas.get(row);
    }

    /**
     * 设置显示字段
     * @param columnBeanList
     */
    public void setColumnBeanList(List<ColumnBean> columnBeanList) {
        this.columnBeanList = columnBeanList;

        List<String> columnNameList = new ArrayList<>();
        columnNameList.add("序号");
        for(int i = 0; i < columnBeanList.size(); i ++){
            ColumnBean columnBean = columnBeanList.get(i);
            columnNameList.add(columnBean.getTitle());
            indexMapFiled.put(i+1,columnBean.getField());
            indexMapType.put(i+1,columnBean.getType());
            if(!StringUtils.isEmpty(columnBean.getDataMap())){
                try {
                    Map<String, Object> maps = JSONObject.parseObject(columnBean.getDataMap(), Map.class);
                    indexMapDic.put(i + 1, maps);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        String[] strings = new String[columnNameList.size()];
        columnNameList.toArray(strings);
        columnNames = strings;

        DefaultTableModel model = new DefaultTableModel(null, columnNames){
            public boolean isCellEditable(int row, int column) {
                return isCelEditable;
            }
        };
        tableGird.setModel(model);

        tableGird.getColumnModel().getColumn(0).setPreferredWidth(20);
        for(int i = 0; i < columnBeanList.size(); i ++) {
            ColumnBean columnBean = columnBeanList.get(i);
            tableGird.getColumnModel().getColumn(i+1).setPreferredWidth(columnBean.getWidth());
        }
    }

    @Data
    public static class ColumnBean{
        public final static int TYPE_STRING = 1;
        public final static int TYPE_NUMBER = 2;
        public final static int TYPE_DATE = 3;
        public final static int TYPE_IMG = 4;

        private String field;
        private String title;
        private int width;
        private Integer type;
        private String dataMap;

        public ColumnBean(String field, String title, int width, int type,String dataMap) {
            this.field = field;
            this.title = title;
            this.width = width;
            this.type = type;
            this.dataMap = dataMap;
        }
    }
}
