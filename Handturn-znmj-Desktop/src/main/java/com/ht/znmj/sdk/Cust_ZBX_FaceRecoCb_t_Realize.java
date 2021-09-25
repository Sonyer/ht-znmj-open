package com.ht.znmj.sdk;

import com.ht.znmj.entity.EquipmentInfoCloudFlag;
import com.ht.znmj.entity.VisitorLog;
import com.ht.znmj.entity.VisitorLogLogFlag;
import com.ht.znmj.entity.VisitorLogOpenStatus;
import com.ht.znmj.utils.DateUtil;
import com.ht.znmj.utils.LocalStoreUtils;
import com.ht.znmj.utils.PicConverUtil;
import com.ht.znmj.utils.StringRandom;
import com.ht.znmj.view.service.ManagerVisitorLogUtil;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 抓拍回调
 */
@Slf4j
public class Cust_ZBX_FaceRecoCb_t_Realize implements libFaceRecognition.ZBX_FaceRecoCb_t {
    /**
     * SN码
     */
    private String sn;

    public Cust_ZBX_FaceRecoCb_t_Realize(String sn) {
        this.sn = sn;
    }

    @Override
    public void Status(IntByReference cam, libFaceRecognition.FaceRecoInfo.ByReference cb, Pointer usrParam) {
        String imgOpenDirPath = LocalStoreUtils.imgOpenPath;
        File myPath = new File(imgOpenDirPath);
        if (!myPath.exists()) {
            myPath.mkdirs();
        }

        long t = 1000;
        Date openTime = new Date();
        openTime.setTime(cb.tvSec * t);

        VisitorLog visitorLog = new VisitorLog();
        visitorLog.setEquipmentSn(this.sn);
        visitorLog.setOpenTime(openTime);
        visitorLog.setCreateTime(new Date());
        visitorLog.setUpdateTime(new Date());
        visitorLog.setCloudFlag(EquipmentInfoCloudFlag.NO_ASY.getCode());

        String filepath = "";
        if (cb.matched == -1) {
            visitorLog.setOpenStatus(VisitorLogOpenStatus.NONE.getCode());
            filepath = imgOpenDirPath + "\\"+ DateUtil.getDateFormat(new Date(),DateUtil.DATA_PATTERN_8)+"\\none\\";
            File myfilepath = new File(filepath);
            if (!myfilepath.exists()) {
                myfilepath.mkdirs();
            }
        } else {
            visitorLog.setOpenStatus(VisitorLogOpenStatus.OPEN.getCode());
            try {
                String personname = new String(getfullbyte(cb.matchPersonName), StandardCharsets.UTF_8);
                visitorLog.setVisitorName(personname.trim());
                String perid = new String(getfullbyte(cb.matchPersonId),  StandardCharsets.UTF_8);

                visitorLog.setVisitorId(perid.trim());
            } catch (Exception e) {
                e.printStackTrace();
                log.info("设备调用-抓拍回调-数据有误!:(MAC="+sn+";)");
            }

            filepath = imgOpenDirPath + "\\" + DateUtil.getDateFormat(new Date(), DateUtil.DATA_PATTERN_8) + "\\open\\";
            File myfilepath = new File(filepath);
            if (!myfilepath.exists()) {
                myfilepath.mkdirs();
            }
        }

        String desfileName = StringRandom.getStringNumRandom(20);
        String filename = filepath + desfileName  + ".jpg";
        byte[] data = cb.faceImg.getPointer().getByteArray(0, cb.faceImgLen);
        try {
            // 转换成图片
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(data));
            //保存图片
            PicConverUtil.writeImageFile(bi, filename);

            visitorLog.setFaceFilePath(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //保存日志
        visitorLog.setLogFlag(VisitorLogLogFlag.LINE_ON.getCode());
        ManagerVisitorLogUtil.saveVisitorLog(visitorLog);
        return;
    }

    public static byte[] getfullbyte (byte[] orgbyte) {
        int length = 0;
        for (int i = 0; i < orgbyte.length; ++i) {
            if (orgbyte[i] != '\0') ++length;
            else break;
        }
        byte[] temp = new byte[length + 1];
        for (int i = 0; i < length; ++i) {
            temp[i] = orgbyte[i];
        }
        temp[length] = '\0';
        return temp;
    }
}
