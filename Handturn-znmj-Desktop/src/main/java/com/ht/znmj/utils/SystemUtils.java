package com.ht.znmj.utils;

import lombok.Data;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Formatter;
import java.util.Locale;
import java.util.Properties;

public class SystemUtils {
    private static Properties properties = System.getProperties();        //系统的所有属性

    /**
     * Key                                    中文描述
     * java.version                    Java 运行时环境版本
     * java.vendor                        Java 运行时环境供应商
     * java.vendor.url                    Java 供应商的 URL
     * java.home                        Java 安装目录
     * java.vm.specification.version    Java 虚拟机规范版本
     * java.vm.specification.vendor    Java 虚拟机规范供应商
     * java.vm.specification.name        Java 虚拟机规范名称
     * java.vm.version                    Java 虚拟机实现版本
     * java.vm.vendor                    Java 虚拟机实现供应商
     * java.vm.name                    Java 虚拟机实现名称
     * java.specification.version        Java 运行时环境规范版本
     * java.specification.vendor        Java 运行时环境规范供应商
     * java.specification.name            Java 运行时环境规范名称
     * java.class.version                Java 类格式版本号
     * java.class.path                    Java 类路径
     * java.library.path                加载库时搜索的路径列表
     * java.io.tmpdir                    默认的临时文件路径
     * java.compiler                    要使用的 JIT 编译器的名称
     * java.ext.dirs                    一个或多个扩展目录的路径
     * os.name                            操作系统的名称
     * os.arch                            操作系统的架构
     * os.version                        操作系统的版本
     * file.separator                    文件分隔符（在 UNIX 系统中是“/”）
     * path.separator                    路径分隔符（在 UNIX 系统中是“:”）
     * line.separator                    行分隔符（在 UNIX 系统中是“/n”）
     * user.name                        用户的账户名称
     * user.home                        用户的主目录
     * user.dir                        用户的当前工作目录
     *
     */

    public static SystemInfo getSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        //获取系统位数
        String bitNumber = properties.getProperty("os.arch");
        String systemName = properties.getProperty("os.name");
        String systemVersion = properties.getProperty("os.version");

        systemInfo.setBitNumer(bitNumber);
        systemInfo.setSystemName(systemName);
        systemInfo.setSystemVersion(systemVersion);

        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            byte[] mac = ni.getHardwareAddress();
            String sIP = address.getHostAddress();
            String sMAC = "";
            Formatter formatter = new Formatter();
            for (int i = 0; i < mac.length; i++) {
                sMAC = formatter.format(Locale.getDefault(), "%02X%s", mac[i],
                        (i < mac.length - 1) ?
                                "-" : "").toString();
            }

            systemInfo.setMacId(sMAC);
            systemInfo.setIp(sIP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemInfo;
    }

    //得到计算机的ip地址和mac地址
    public static void getConfig() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            //ni.getInetAddresses().nextElement().getAddress();
            byte[] mac = ni.getHardwareAddress();
            String sIP = address.getHostAddress();
            String sMAC = "";
            Formatter formatter = new Formatter();
            for (int i = 0; i < mac.length; i++) {
                sMAC = formatter.format(Locale.getDefault(), "%02X%s", mac[i],
                        (i < mac.length - 1) ?
                                "-" : "").toString();
            }
            /*System.out.println("IP：" + sIP);
            System.out.println("MAC：" + sMAC);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 系统数据
     */
    @Data
    public static class SystemInfo {
        private String systemName;
        private String systemVersion;
        private String bitNumer;
        private String macId;
        private String ip;

        public SystemInfo() {
        }
    }
}
