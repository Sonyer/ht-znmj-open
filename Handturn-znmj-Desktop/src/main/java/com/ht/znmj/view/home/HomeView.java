package com.ht.znmj.view.home;

import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.entity.EquipmentInfoConnStatus;
import com.ht.znmj.entity.EquipmentInfoInOut;
import com.ht.znmj.entity.EquipmentInfoStatus;
import com.ht.znmj.sdk.Cust_ZBX_FaceRecoCb_t_Realize;
import com.ht.znmj.sdk.MyFunction;
import com.ht.znmj.sdk.ZBX_Global;
import com.ht.znmj.sdk.libFaceRecognition;
import com.ht.znmj.utils.DESToolUtil;
import com.ht.znmj.view.bean.HomeMenuItem;
import com.ht.znmj.view.components.DialogFrame;
import com.ht.znmj.view.home.equipment.EquipmentRegisterView;
import com.ht.znmj.view.manager.ManagerView;
import com.ht.znmj.view.service.ApplicationUtil;
import com.ht.znmj.view.service.HomeEquipmentUtil;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页
 */
@Slf4j
public class HomeView {

    /**
     * 根页面
     */
    private JFrame rootFrame;
    /**
     * 设备清单内容
     */
    private Panel center ;

    private Button flushButton;

    private Button orderButton;

    public void setView(JFrame rootFrame){
        this.rootFrame = rootFrame;

        Panel top = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(rootFrame.getWidth(),27);
            }
        };
        top.setLayout(new BorderLayout());

        Panel topLeft = new Panel();
        flushButton = new Button();
        flushButton.setLabel("连接/刷新");
        Font font = new Font("SimSun", 1, 12);
        flushButton.setFont(font);
        flushClickListener();
        topLeft.add(flushButton);

        orderButton = new Button();
        orderButton.setLabel("排序");
        orderButton.setFont(font);
        topLeft.add(orderButton);
        orderClickListener();

        top.add(topLeft,BorderLayout.WEST);

        Panel topRight = new Panel();
        createMenuRootItem(topRight);
        top.add(topRight,BorderLayout.EAST);

        rootFrame.add(top,BorderLayout.NORTH);

        center = new Panel(){
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
        center.setLayout(new FlowLayout(FlowLayout.LEADING));
        ScrollPane centerScrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        centerScrollPane.setPreferredSize(new Dimension(rootFrame.getWidth(),rootFrame.getHeight()));
        centerScrollPane.setBounds(0,0,rootFrame.getWidth(),rootFrame.getHeight());
        centerScrollPane.add(center,BorderLayout.CENTER);
        center.addComponentListener(new ComponentAdapter(){
            public void componentResized(java.awt.event.ComponentEvent evt) {
                realseScrollPanel(center,350,420);
                center.revalidate();
                //centerScrollPane.revalidate();
            }

        });
        rootFrame.add(centerScrollPane,BorderLayout.CENTER);

        Panel bottom = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(rootFrame.getWidth(),30);
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
        bottom.setLayout(new BorderLayout());


        Label licenseButton = new Label();
        Font licenseFont = new Font("SimSun", Font.ITALIC, 12);
        licenseButton.setFont(licenseFont);
        licenseButton.setText("设备许可证注册");
        licenseButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EquipmentRegisterView equipmentRegisterView = new EquipmentRegisterView();
                equipmentRegisterView.initView(rootFrame);
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
        bottom.add(licenseButton,BorderLayout.WEST);

        Panel copyrightPanel = new Panel(){
            public Dimension getPreferredSize() {
                return new Dimension(bottom.getWidth()-30, 30);
            }
        };
        copyrightPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        Label copyrightButton = new Label();

        copyrightButton.setText("©copyright 深圳大诺科技有限公司");
        copyrightPanel.add(copyrightButton);
        bottom.add(copyrightPanel,BorderLayout.CENTER);

        rootFrame.add(bottom,BorderLayout.SOUTH);

        //初始化设备列表
        initEquipmentItem();
    }

    /**
     * 初始化
     */
    private void initEquipmentItem(){
        flushButton.setEnabled(false);

        try {
            //终止所有视频流
            ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.keySet().forEach(sn ->{
                try {
                    MyFunction.m_FaceRecognition.ZBX_StopStream(ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(sn));
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

            //清除所有首页视频
            center.removeAll();

            //获取数据Map
            Map<String, EquipmentInfo> equipmentMap = HomeEquipmentUtil.findMapEquipmentInfos();


            //SDK接口 -搜索设备
            libFaceRecognition m_FaceRecognition = MyFunction.m_FaceRecognition;
            m_FaceRecognition.ZBX_RegDiscoverIpscanCb(new libFaceRecognition.discover_ipscan_cb_t() {
                @Override
                public void Status(libFaceRecognition.ipscan_t.ByReference ips, int usr_param) {
                    try {
                        //  输出查到的ip
                        String ip = MyFunction.deCode(new String(ips.ip)).trim();
                        String mac = MyFunction.deCode(new String(ips.mac)).trim();
                        String manufacturer = MyFunction.deCode(new String(ips.manufacturer)).trim();
                        log.info("设备搜索到:(IP="+ip+";MAC="+mac+";)");

                        boolean flag = true;
                        EquipmentInfo equipmentInfo = null;
                        if (equipmentMap.get(mac) == null) {
                           //不做处理，已交给注册
                            flag = false;
                        } else {
                            equipmentInfo = equipmentMap.get(mac);
                            //判断许可证是否有效
                            try {
                                String equipmentSn = DESToolUtil.decrypt(equipmentInfo.getLicense(), DESToolUtil.KEY_);
                                if(!equipmentSn.equals(equipmentInfo.getSn())){
                                    flag = false;
                                }
                            }catch (Exception e){
                               flag = false;
                            }

                            equipmentInfo.setIp(ip);
                        }

                        EquipmentItemLabel itemLabel = new EquipmentItemLabel();

                        if(flag && !StringUtils.isEmpty(mac)){
                            itemLabel.addEquipmentItemLabel(center, equipmentInfo);
                        }

                        flushButton.setEnabled(true);
                    }catch(Exception e){
                        e.printStackTrace();
                        flushButton.setEnabled(true);
                    }
                }
            }, 0);
            m_FaceRecognition.ZBX_DiscoverIpscan();
        }catch(Exception ex){
            ex.printStackTrace();
            flushButton.setEnabled(true);
        }
    }

    /**
     * 排序设备
     */
    private void orderEquipmentItem(){
        orderButton.setEnabled(false);

        try {
            //终止所有视频流
            ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.keySet().forEach(sn ->{
                try {
                    MyFunction.m_FaceRecognition.ZBX_StopStream(ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(sn));
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

            //清除所有首页视频
            center.removeAll();

            //获取数据Map
            java.util.List<EquipmentInfo> equipmentList = HomeEquipmentUtil.findEquipmentInfosByStatus(EquipmentInfoStatus.ACTIVE.getCode());

            equipmentList.forEach(equipmentInfo ->{
                try {
                    //判断许可证是否有效
                    boolean flag = true;
                    try {
                        String equipmentSn = DESToolUtil.decrypt(equipmentInfo.getLicense(), DESToolUtil.KEY_);
                        if(!equipmentSn.equals(equipmentInfo.getSn())){
                            flag = false;
                        }
                    }catch (Exception e){
                        flag = false;
                    }

                    if(flag){
                        EquipmentItemLabel itemLabel = new EquipmentItemLabel();
                        itemLabel.addEquipmentItemLabel(center, equipmentInfo);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            });

            orderButton.setEnabled(true);
        }catch(Exception ex){
            ex.printStackTrace();
            orderButton.setEnabled(true);
        }
    }

    /**
     * 刷新按钮事件
     */
    private void flushClickListener(){
        flushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initEquipmentItem();
            }
        });
    }

    /**
     * 排序按钮事件
     */
    private void orderClickListener() {
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderEquipmentItem();
            }
        });
    }


    /**
     * 设备清单
     */
    public class EquipmentItemLabel {
        /**
         * 添加设备
         *
         * @param equipmentInfo
         */
        public void addEquipmentItemLabel(Panel parentPane, EquipmentInfo equipmentInfo) {

            Panel contentPanel = new Panel(){
                public Dimension getPreferredSize() {
                    return new Dimension(350, 420);
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
            contentPanel.setLayout(new BorderLayout(5,5));
            contentPanel.setBounds(0,0,350,420);
            parentPane.add(contentPanel);


            Panel itemTop = new Panel() {
                public Dimension getPreferredSize() {
                    return new Dimension(350, 30);
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
            itemTop.setBounds(0,0,350,30);
            //创建一个label 并将url参数传递给label,并居中显示，查看源码可以得出有3个参数，String Icon 和对齐方式
            ImageIcon imageIcon = new ImageIcon("images/common_icon/icon_camera.png");
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT ));
            JLabel label = new JLabel(equipmentInfo.getAreaName()+"-"+EquipmentInfoInOut.getNameByCode(equipmentInfo.getInOut()));
            Font tittleFont = new Font("SimSun", 1, 15);
            label.setFont(tittleFont);
            label.setIcon(imageIcon);
            itemTop.setLayout(new FlowLayout(FlowLayout.LEFT));
            itemTop.add(label);
            contentPanel.add(itemTop, BorderLayout.NORTH);

            Label itemCenter = new Label() {
                public Dimension getPreferredSize() {
                    return new Dimension(350, 320);
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
            itemCenter.setBounds(0,0,350,320);

            contentPanel.add(itemCenter, BorderLayout.CENTER);

            Panel itemBottom = new Panel() {
                public Dimension getPreferredSize() {
                    return new Dimension(350, 30);
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
            Button mngButton = new Button();
            mngButton.setLabel("管理");
            Font font = new Font("SimSun", 1, 12);
            mngButton.setFont(font);
            clickManegerButton(equipmentInfo,mngButton);
            itemBottom.add(mngButton);

            Button openButton = new Button();
            openButton.setLabel("开门");
            openButton.setFont(font);
            clickOpenButton(equipmentInfo.getSn(),openButton);
            itemBottom.add(openButton);


            itemBottom.setLayout(new FlowLayout(FlowLayout.LEFT));
            itemBottom.setBounds(0,0,350,30);
            contentPanel.add(itemBottom, BorderLayout.SOUTH);


            connectEquipment(itemCenter,equipmentInfo);  //设备流输出

            //重绘面板
            realseScrollPanel(parentPane,contentPanel.getSize().width,contentPanel.getSize().height);

            parentPane.revalidate();
        }
    }

    private void clickOpenButton(final String sn,Button openButton){
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                libFaceRecognition m_FaceRecognition = MyFunction.m_FaceRecognition;
                boolean isConnect = false;
                IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(sn);
                if(cameraPoint == null){
                    isConnect = false;
                }else{
                    if (m_FaceRecognition.ZBX_Connected(cameraPoint) == 1) {
                        isConnect = true;
                    }else{
                        isConnect = false;
                    }
                }

                if(isConnect){
                    m_FaceRecognition.ZBX_WhiteListAlarm(cameraPoint,1,1);
                }else{
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(rootFrame,"该设备没有连接，请确认!",DialogFrame.TYPE_WARN,null);
                }
            }
        });
    }

    private void clickManegerButton(final EquipmentInfo equipmentInfo,Button mngButton){
        mngButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(equipmentInfo.getSn());
                if(MyFunction.m_FaceRecognition.ZBX_Connected(cameraPoint) != 1){
                    DialogFrame dialogFrame = new DialogFrame();
                    dialogFrame.showMessageDialog(rootFrame,"设备未连接,请先在首页刷新设备列表!",DialogFrame.TYPE_ERROR,null);
                    return;
                }else{
                    //保存设备信息
                    equipmentInfo.setConnStatus(EquipmentInfoConnStatus.CONNECTED.getCode());
                    HomeEquipmentUtil.saveEquipmentInfo(equipmentInfo);
                }
                //打开页面
                ManagerView managerView = new ManagerView(rootFrame,equipmentInfo);
            }
        });
    }

    /**
     * 设备流输出页面
     * @param panel
     * @param equipmentInfo
     */
    private void connectEquipment(Label panel,EquipmentInfo equipmentInfo){
        /**-------------------相机连接-----------------begin***/
        libFaceRecognition m_FaceRecognition = MyFunction.m_FaceRecognition;
        boolean reConnect = false;
        IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(equipmentInfo.getSn());
        if(cameraPoint == null){
            reConnect = true;
        }else{
            if (m_FaceRecognition.ZBX_Connected(cameraPoint) == 1) {
                reConnect = false;
            }else{
                reConnect = true;
            }
        }

        IntByReference err_code = new IntByReference(-1);

        if(reConnect){
            //相机句柄为空时 重新连接 否则只要校验就可以
            if(cameraPoint == null || m_FaceRecognition.ZBX_Connected(cameraPoint) != 1){
                cameraPoint = m_FaceRecognition.ZBX_ConnectEx(equipmentInfo.getIp(), (short) 8099, "", "", err_code,
                        0, 1);
            }

            if (m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
                m_FaceRecognition.ZBX_StartSecondStream(cameraPoint, new WinDef.HWND(Native.getComponentPointer(panel)));
                log.info("相机连接失败:(IP="+equipmentInfo.getIp()+";MAC="+equipmentInfo.getSn()+";)");
                equipmentInfo.setConnStatus(EquipmentInfoConnStatus.NO_CONNECT.getCode());
            } else {
                m_FaceRecognition.ZBX_StartSecondStream(cameraPoint, new WinDef.HWND(Native.getComponentPointer(panel)));
                log.info("连接相机成功-新连接:(IP="+equipmentInfo.getIp()+";MAC="+equipmentInfo.getSn()+";)");
                equipmentInfo.setConnStatus(EquipmentInfoConnStatus.CONNECTED.getCode());

                //新连接开启日志通知
                Cust_ZBX_FaceRecoCb_t_Realize m_RecoCb_t = new Cust_ZBX_FaceRecoCb_t_Realize(equipmentInfo.getSn());
                m_FaceRecognition.ZBX_RegFaceRecoCb(cameraPoint, m_RecoCb_t, Pointer.NULL);
                m_FaceRecognition.ZBX_RegFaceOffLineRecoCb(cameraPoint, m_RecoCb_t, Pointer.NULL);
            }

            ZBX_Global.EQUIPMENT_IP_SN_MAP.put(equipmentInfo.getIp(),equipmentInfo.getSn());
        }else{
            m_FaceRecognition.ZBX_StartSecondStream(cameraPoint, new WinDef.HWND(Native.getComponentPointer(panel)));
            log.info("连接相机成功-已有连接:(IP="+equipmentInfo.getIp()+";MAC="+equipmentInfo.getSn()+";)");
            equipmentInfo.setConnStatus(EquipmentInfoConnStatus.CONNECTED.getCode());

            //新连接开启日志通知
            Cust_ZBX_FaceRecoCb_t_Realize m_RecoCb_t = new Cust_ZBX_FaceRecoCb_t_Realize(equipmentInfo.getSn());
            m_FaceRecognition.ZBX_RegFaceRecoCb(cameraPoint, m_RecoCb_t, Pointer.NULL);
            m_FaceRecognition.ZBX_RegFaceOffLineRecoCb(cameraPoint, m_RecoCb_t, Pointer.NULL);

            ZBX_Global.EQUIPMENT_IP_SN_MAP.put(equipmentInfo.getIp(),equipmentInfo.getSn());

        }

        //保存设备信息
        HomeEquipmentUtil.saveEquipmentInfo(equipmentInfo);
        /**-------------------相机连接-----------------end***/
    }

    private void realseScrollPanel(Panel parentPane,int contentWidht,int contentHeight){
        BigDecimal clumnCount = new BigDecimal(parentPane.getSize().width).divide(new BigDecimal(contentWidht+10),0,BigDecimal.ROUND_DOWN);
        if(clumnCount.compareTo(new BigDecimal(0)) <= 0){
            clumnCount = new BigDecimal(1);
        }
        BigDecimal rowCount = new BigDecimal(parentPane.getComponents().length).divide(clumnCount,0,BigDecimal.ROUND_UP);
        Dimension dim = new Dimension((contentWidht+10)*clumnCount.intValue(),    // 尺寸改变后可能会出现垂直滚动条，需减去其宽度
                (contentHeight+10)*rowCount.intValue());
        parentPane.setPreferredSize(dim);
    }

    /**
     * 菜单清单
     */
    java.util.List<HomeMenuItem> menuItemList = new ArrayList<HomeMenuItem>();
    {
        menuItemList.add(new HomeMenuItem("人员信息","人员信息","images/common_icon/icon_menu_visitor.png",
                "com.ht.znmj.view.home.visitor.VisitorHomeView",HomeMenuItem.NODE,null,"0"));
        menuItemList.add(new HomeMenuItem("出入日志","出入日志","images/common_icon/icon_menu_visitorlog.png",
                "com.ht.znmj.view.home.visitorlog.VisitorLogHomeView",HomeMenuItem.NODE,null,"0"));

        java.util.List<HomeMenuItem> systemItemList = new ArrayList<HomeMenuItem>();
        HomeMenuItem systemChildItem1 = new HomeMenuItem("接口配置","接口配置","images/common_icon/icon_menu_interface.png",
                "com.ht.znmj.view.home.system.InterfaceConfView",HomeMenuItem.NODE,null,"1");
        systemItemList.add(systemChildItem1);

        menuItemList.add(new HomeMenuItem("系统管理","系统管理","images/common_icon/icon_menu_system.png",
                null,HomeMenuItem.DIR,systemItemList,"1"));
    }
    static Map<String,JLabel> menuLabelMap = new HashMap<>();

    /**
     * 菜单加载
     */
    public void createMenuRootItem(Panel parentPanel){
        parentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Font menuFont = new Font("SimSun", 1, 18);
        //Border border = BorderFactory.createLineBorder(Color.BLACK);

        menuItemList.forEach(menuItem ->{
            if(menuItem.getCloudFlag().equals(ApplicationUtil.getApplicationProperties().getCloudFlag())){
                JLabel menuLabel = new JLabel(menuItem.getMenuName()){
                    public Dimension getPreferredSize() {
                        return new Dimension(100,parentPanel.getHeight()-10);
                    }
                };

                if(menuItem.getMenuType().equals(HomeMenuItem.NODE)){
                    menuLabel.addMouseListener(new MouseListener() {
                        @SneakyThrows
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if(!StringUtils.isEmpty(menuItem.getPagePath())) {
                                HomeMenuItemView homeMenuItemView = (HomeMenuItemView) Class.forName(menuItem.getPagePath()).newInstance();
                                homeMenuItemView.initView(rootFrame);
                            }
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
                }
                menuLabel.setBounds(50,0,100,parentPanel.getHeight()-10);
                //menuLabel.setFont(menuFont);
                ImageIcon equipmentIcon = new ImageIcon(menuItem.getIconPath());
                equipmentIcon.setImage(equipmentIcon.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT ));
                menuLabel.setIcon(equipmentIcon);
                //menuLabel.setBorder(border);

                parentPanel.add(menuLabel);

                menuLabelMap.put(menuItem.getId(),menuLabel);

                /**
                 * 增加子节点
                 */
                if(menuItem.getMenuType().equals(HomeMenuItem.DIR)){
                    if(menuItem.getChildMenu() != null && menuItem.getChildMenu().size() > 0){
                        JPopupMenu parentPop = new JPopupMenu();
                        createMenuNodeItem(parentPop,menuItem.getChildMenu());

                        menuLabel.addMouseListener(new MouseListener() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                parentPop.show(menuLabel,0,menuLabel.getHeight());
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
                        parentPanel.add(parentPop);
                    }
                }
            }
        });
    }

    /**
     * 菜单node节点
     */
    public void createMenuNodeItem(JPopupMenu parentPop,java.util.List<HomeMenuItem> menuItemList){
        for (HomeMenuItem menuItem : menuItemList) {
            if (menuItem.getCloudFlag().equals(ApplicationUtil.getApplicationProperties().getCloudFlag())) {
                JMenuItem nodeMenu = new JMenuItem(menuItem.getMenuName());
                //创建一个label 并将url参数传递给label,并居中显示，查看源码可以得出有3个参数，String Icon 和对齐方式
                ImageIcon imageIcon = new ImageIcon(menuItem.getIconPath());
                imageIcon.setImage(imageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
                nodeMenu.setIcon(imageIcon);

                if (menuItem.getMenuType().equals(HomeMenuItem.NODE)) {
                    nodeMenu.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (!StringUtils.isEmpty(menuItem.getPagePath())) {
                                    HomeMenuItemView homeMenuItemView = (HomeMenuItemView) Class.forName(menuItem.getPagePath()).newInstance();
                                    homeMenuItemView.initView(rootFrame);
                                }
                            } catch (InstantiationException ex) {
                                ex.printStackTrace();
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            } catch (ClassNotFoundException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }

                parentPop.add(nodeMenu);

                if (menuItem.getMenuType().equals(HomeMenuItem.DIR)) {
                    if (menuItem.getChildMenu() != null && menuItem.getChildMenu().size() > 0) {
                        JPopupMenu parentNodePop = new JPopupMenu();
                        createMenuNodeItem(parentNodePop, menuItem.getChildMenu());

                        nodeMenu.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                parentNodePop.show(nodeMenu, nodeMenu.getWidth(), 0);
                            }
                        });
                        parentPop.add(parentNodePop);
                    }
                }
            }
        }

    }

}
