package com.ht.znmj.utils;

import com.ht.znmj.common.exception.FebsException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtils {

    /**
     * 压缩文件操作
     * @param filePath 要压缩的文件路径
     * @param descDir 压缩文件的保存路径
     * @throws IOException
     */
    public static void zipFiles(String filePath,String descDir){
        ZipOutputStream zos=null;
        try {
            //创建一个zip输出流
            zos=new ZipOutputStream(new FileOutputStream(descDir));
            //启动压缩
            startZip(zos,"",filePath);
            log.info("=============压缩完毕=============");
        } catch (FileNotFoundException e) {
            //压缩失败，则删除创建的文件
            File zipFile=new File(descDir);
            if(zipFile.exists()){
                zipFile.delete();
            }
            e.printStackTrace();
            log.error("本地压缩："+filePath+",目录："+descDir+",压缩失败！"+e.getMessage());
            throw new FebsException("本地压缩："+filePath+",目录："+descDir+",压缩失败！"+e.getMessage());
        } catch(IOException e){
            //压缩失败，则删除创建的文件
            File zipFile=new File(descDir);
            if(zipFile.exists()){
                zipFile.delete();
            }
            e.printStackTrace();
            log.error("本地压缩："+filePath+",目录："+descDir+",压缩失败！"+e.getMessage());
            throw new FebsException("本地压缩："+filePath+",目录："+descDir+",压缩失败！"+e.getMessage());
        }finally{
            if(zos!=null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 对目录中所有文件递归遍历进行压缩
     * @param zos 压缩输出流
     * @param oppositePath 在zip文件中的相对路径
     * @param filePath 要压缩的文件路径
     */
    private static void startZip(ZipOutputStream zos, String oppositePath,
                                 String filePath) throws IOException{
        File file=new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        if(file.isDirectory()){//如果是压缩目录
            File[] files=file.listFiles();//列出所有目录
            for(int i=0;i<files.length;i++){
                File aFile=files[i];
                if(aFile.isDirectory()){//如果是目录，修改相对地址
                    String newoppositePath=oppositePath+aFile.getName()+"/";
                    //压缩目录，这是关键，创建一个目录的条目时，需要在目录名后面加多一个"/"
                    ZipEntry entry=new ZipEntry(newoppositePath);
                    zos.putNextEntry(entry);
                    zos.closeEntry();
                    startZip(zos, newoppositePath, aFile.getPath());
                }else{//如果不是目录，则进行压缩
                    zipFile(zos,oppositePath,aFile);
                }
            }
        }else{//如果是压缩文件，直接调用压缩方法进行压缩
            zipFile(zos, oppositePath, file);
        }
    }
    /**
     * 压缩单个文件到目录中
     * @param zos zip输出流
     * @param oppositePath 在zip文件中的相对路径
     * @param file 要压缩的文件
     */
    private static void zipFile(ZipOutputStream zos, String oppositePath, File file){
        //创建一个zip条目，每个zip条目都必须是相对于跟路径
        InputStream is=null;

        try {
            ZipEntry entry=new ZipEntry(oppositePath+file.getName());
            //将条目保存到zip压缩文件当中
            zos.putNextEntry(entry);
            //从文件输入流当中读取数据，并将数据写到输出流当中
            is=new FileInputStream(file);
            //====这种压缩速度很快
            int length=0;
            int bufferSize=1024;
            byte[] buffer=new byte[bufferSize];

            while((length=is.read(buffer, 0, bufferSize))>=0){
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("单文件压缩："+file.getName()+",失败！");
            throw new FebsException("单文件压缩："+file.getName()+",失败！");
        }finally{
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 解压文件操作
     * @param zipFilePath zip文件路径
     * @param descDir 解压出来的文件保存的目录
     */
    public static void unZiFiles(String zipFilePath,String descDir){
        File zipFile=new File(zipFilePath);
        File pathFile=new File(descDir);

        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile zip=null;
        InputStream in=null;
        OutputStream out=null;

        try {
            zip=new ZipFile(zipFile);
            Enumeration<?> entries=zip.entries();
            while(entries.hasMoreElements()){
                ZipEntry entry=(ZipEntry) entries.nextElement();
                String zipEntryName=entry.getName();
                in=zip.getInputStream(entry);

                String outPath=(descDir+"/"+zipEntryName).replace("\\*", "/");
                //判断路径是否存在，不存在则创建文件路径
                File file=new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if(!file.exists()){
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经创建,不需要解压
                if(new File(outPath).isDirectory()){
                    continue;
                }
                out=new FileOutputStream(outPath);

                byte[] buf=new byte[4*1024];
                int len;
                while((len=in.read(buf))>=0){
                    out.write(buf, 0, len);
                }
                in.close();

                log.info("==================解压完毕==================");
            }
        } catch (Exception e) {
            log.info("==================解压失败==================");
            e.printStackTrace();
            log.error("本地解压缩："+zipFilePath+",目录："+descDir+",解压缩失败！"+e.getMessage());
            throw new FebsException("本地解压缩："+zipFilePath+",目录："+descDir+",解压缩失败！"+e.getMessage());
        }finally{
            try {
                if(zip!=null){
                    zip.close();
                }
                if(in!=null){
                    in.close();
                }
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressWarnings("static-access")
    public static void main(String args[]) throws IOException{
//      long startTimes=System.currentTimeMillis();
//      new ZipUtil().zipFiles("f:"+File.separator+"Vashon2Xiaoai", "f:"+File.separator+"Vashon2Xiaoai_vvv.zip");
//      long times=System.currentTimeMillis()-startTimes;
//      System.out.println("耗时："+times/1000+"秒");
        //unZiFiles("D:\\TEST\\20170424-LSM(MA)-l000001.zip", "D:\\TEST\\20170424-LSM(MA)-l000001");
        zipFiles("D:\\temp\\20170424-LSD(ASN)-l000001","D:\\temp\\20170424-LSM(MA)-l000001.zip");

    }

}

