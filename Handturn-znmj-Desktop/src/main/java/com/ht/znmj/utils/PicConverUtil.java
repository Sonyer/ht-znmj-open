package com.ht.znmj.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class PicConverUtil {

    /**
     * 转成JPG
     * @param srcImgPath
     * @param desImgDirPath
     * @return
     */
    public static String toJPG(String srcImgPath,String desImgDirPath){
        String desFilePath ="";
        try {
            File desImgDirFile = new File(desImgDirPath);
            if(!desImgDirFile.exists()){
                desImgDirFile.mkdirs();
            }
            File file = new File(srcImgPath);
            Image img = ImageIO.read(file);
            BufferedImage tag = new BufferedImage(img.getWidth(null), img.getHeight(null),    BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(img.getScaledInstance(img.getWidth(null), img.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);

            String desfileName = StringRandom.getStringNumRandom(20);
            desFilePath = desImgDirPath+desfileName+".jpg";
            FileOutputStream out = new FileOutputStream(desFilePath);
            // JPEGImageEncoder可适用于其他图片类型的转换
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return desFilePath;
    }

    /**
     * 转成JPG
     * @param imgByte
     * @param desImgDirPath
     * @return
     */
    public static String toJPG(byte[] imgByte,String desImgDirPath){
        String desFilePath ="";
        try {
            File desImgDirFile = new File(desImgDirPath);
            if(!desImgDirFile.exists()){
                desImgDirFile.mkdirs();
            }

            InputStream inputStream = new ByteArrayInputStream(imgByte);

            Image img = ImageIO.read(inputStream);
            BufferedImage tag = new BufferedImage(img.getWidth(null), img.getHeight(null),    BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(img.getScaledInstance(img.getWidth(null), img.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);

            String desfileName = StringRandom.getStringNumRandom(20);
            desFilePath = desImgDirPath+desfileName+".jpg";
            FileOutputStream out = new FileOutputStream(desFilePath);
            // JPEGImageEncoder可适用于其他图片类型的转换
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return desFilePath;
    }

    /**
     * 路径转移
     * @param srcImgPath
     * @param desImgDirPath
     * @return
     */
    public static String moveImgPath(String srcImgPath,String desImgDirPath){
        File srcfile = new File(srcImgPath);

        FileInputStream input = null;
        FileOutputStream out = null;
        String desImgPath = "";
        try {
            input = new FileInputStream(srcfile);
            byte[] buffer = new byte[1024];

            File dir = new File(desImgDirPath);
            if(!dir.exists()){
                dir.mkdirs();
            }

            File des = new File(desImgDirPath, srcfile.getName());
            out = new FileOutputStream(des);
            int len = 0;
            while (-1 != (len = input.read(buffer))) {
                out.write(buffer, 0, len);
            }

            out.close();
            input.close();

            desImgPath = des.getPath();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        srcfile.delete();

        return desImgPath;
    }

    /**
     * 路径转移
     * @param srcImgPath
     * @param desImgDirPath
     * @return
     */
    public static String moveImgPathNdelete(String srcImgPath,String desImgDirPath,String fileName){
        File srcfile = new File(srcImgPath);

        FileInputStream input = null;
        FileOutputStream out = null;
        String desImgPath = "";
        try {
            input = new FileInputStream(srcfile);
            byte[] buffer = new byte[1024];

            File dir = new File(desImgDirPath);
            if(!dir.exists()){
                dir.mkdirs();
            }

            File des = new File(desImgDirPath, StringUtils.isEmpty(fileName)?srcfile.getName():fileName);
            out = new FileOutputStream(des);
            int len = 0;
            while (-1 != (len = input.read(buffer))) {
                out.write(buffer, 0, len);
            }

            out.close();
            input.close();

            desImgPath = des.getPath();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return desImgPath;
    }

    //  读取图片转换成byte[]
    public static byte[] imageToByteArray(String imgPath) {
        BufferedInputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(imgPath));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int size = 0;
            byte[] temp = new byte[1024];
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            in.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //图像保存
    public static  void writeImageFile(BufferedImage bi,String filename) throws IOException {
        File outputfile = new File(filename);
        ImageIO.write(bi, "jpg", outputfile);
    }
}
