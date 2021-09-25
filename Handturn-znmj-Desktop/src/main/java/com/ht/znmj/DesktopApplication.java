package com.ht.znmj;

import com.ht.znmj.app.WelcomeView;
import com.ht.znmj.app.ZnmjMain;
import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.entity.EquipmentInfoConnStatus;
import com.ht.znmj.sdk.MyFunction;
import com.ht.znmj.sdk.libFaceRecognition;
import com.ht.znmj.utils.NetUtil;
import com.ht.znmj.view.service.ApplicationUtil;
import com.ht.znmj.view.service.HomeEquipmentUtil;
import com.ht.znmj.view.service.WebSocketUtil;
import com.sun.jna.Native;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.util.List;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@MapperScan("com.ht.znmj.**.mapper")
public class DesktopApplication {
    public static void main(String[] args) {
        WelcomeView welcomeView = new WelcomeView();

        welcomeView.setInitMessage("初始化开始...");

        Boolean isRunning = NetUtil.isLoclePortUsing(9111);
        if(isRunning){
            welcomeView.setInitMessage("程序已启动,请检查右下侧工具栏!");
            sleep(4000);

            welcomeView.setEnabled(true);
            welcomeView.setVisible(false);
            System.exit(0);
            return;
        }

        try {
            welcomeView.setInitMessage("启动程序开始...");
            //加载上下文
            SpringApplicationBuilder builder = new SpringApplicationBuilder(DesktopApplication.class);
            ApplicationContext applicationContext = builder.headless(false).run(args);

            String bit = ApplicationUtil.getApplicationProperties().getBit();
            //加载接口包
            if (bit.equals("64")) {
                MyFunction.m_FaceRecognition = (libFaceRecognition) Native.loadLibrary(
                        new File("").getAbsolutePath() + "\\dlls\\x64\\libFaceRecognitionSDK_x64.dll", libFaceRecognition.class);
            } else {
                MyFunction.m_FaceRecognition = (libFaceRecognition) Native.loadLibrary(
                        new File("").getAbsolutePath() + "\\dlls\\x86\\libFaceRecognitionSDK_x86.dll", libFaceRecognition.class);
            }

            try{
                //关闭所有设备连接 避免加载云端数据错乱
                List<EquipmentInfo> equipmentInfos = HomeEquipmentUtil.findAllEquipmentInfos();
                equipmentInfos.forEach(equipmentInfo ->{
                    HomeEquipmentUtil.updateConnStatus(equipmentInfo.getSn(),EquipmentInfoConnStatus.NO_CONNECT.getCode());
                });
            }catch(Exception e){
                e.printStackTrace();
            }

            try {
                //加载云端接口
                WebSocketUtil.initWebSocketClient();
            }catch (Exception e){

            }
        }catch (Exception e){
            welcomeView.setInitMessage("启动程序失败,请检查日志...");
            sleep(4000);

            welcomeView.setEnabled(true);
            welcomeView.setVisible(false);
            System.exit(0);
            return;
        }



        welcomeView.setInitMessage("启动程序完成!");
        sleep(2000);
        welcomeView.setVisible(false);
        ZnmjMain swing = new ZnmjMain();
    }

    private static void sleep(int second){
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
