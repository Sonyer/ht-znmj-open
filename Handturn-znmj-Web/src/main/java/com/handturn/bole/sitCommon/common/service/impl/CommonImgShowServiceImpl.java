package com.handturn.bole.sitCommon.common.service.impl;

import com.handturn.bole.sitCommon.common.service.ICommonImgShowService;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.util.Iterator;

/**
 * Service实现
 *
 * @author MrBird
 * @date 2019-12-08 10:00:07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CommonImgShowServiceImpl implements ICommonImgShowService {

    @Autowired
    private ISysconfGlobalParamService sysconfGlobalParamService;

    @Value("${localStore.physicalPath}")
    private String physicalPath;

    @Value("${localStore.requestDomain}")
    private String requestDomain;

    @Value("${localStore.requestFileDomain}")
    private String requestFileDomain;


    /**
     * 图片展现
     * @return
     */
    @Override
    public void imgShow(String typeName,String dateStr, String fileName,HttpServletRequest request, HttpServletResponse response) throws IOException {

        String filePath = physicalPath + "/"+typeName+"/"+dateStr+"/"+fileName;
        Image src = Toolkit.getDefaultToolkit().getImage(filePath);
        BufferedImage image= toBufferedImage(src);

        // 得到指定Format图片的writer
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");// 得到迭代器
        ImageWriter writer = (ImageWriter) iter.next(); // 得到writer

        // 得到指定writer的输出参数设置(ImageWriteParam )
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
        iwp.setCompressionQuality(0.6f); // 设置压缩质量参数
        iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

        ColorModel colorModel = image.getColorModel();
        // 指定压缩时使用的色彩模式
        iwp.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));

        // 用response获得字节输出流
        ServletOutputStream sos = response.getOutputStream();
        // 获得服务器上的图片
        //FileInputStream fis = new FileInputStream(file);
        IIOImage iIamge = new IIOImage(image, null, null);

        try {
            // 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput
            // 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput
            writer.setOutput(ImageIO.createImageOutputStream(sos));
            writer.write(null, iIamge, iwp);

        } catch (IOException e) {
            System.out.println("write errro");
            e.printStackTrace();
        } finally {
            sos.close();
        }
    }

    /**
     *  避免变色
     */
    public BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    /**
     * 文件展现
     * @return
     */
    @Override
    public void fileShow(String typeName,String dateStr, String fileName,HttpServletRequest request, HttpServletResponse response) throws IOException{
        String filePath = physicalPath + "/"+typeName+"/"+dateStr+"/"+fileName;

        File file = new File(filePath);

        // 读取文件内容 (输入流)
        InputStream in = new BufferedInputStream(new FileInputStream(file), 4096);
        OutputStream os = new BufferedOutputStream(response.getOutputStream());

        byte[] bytes = new byte[4096];
        int i = 0;
        while ((i = in.read(bytes)) > 0) {
            os.write(bytes, 0, i);
        }
        os.flush();
        os.close();
    }
}
