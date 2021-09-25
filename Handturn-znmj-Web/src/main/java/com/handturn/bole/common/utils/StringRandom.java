package com.handturn.bole.common.utils;

import java.util.Random;

/**
 * 随机字符串生成
 */
public class StringRandom {

    //生成随机数字和字母,
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    //生成随机数字
    public static String getStringNumRandom(int length){
        Random random = new Random();
        String result="";
        for (int i=0;i<length;i++) {
            result+=random.nextInt(10);
        }
        return result;
    }

    public static void  main(String[] args) {
        StringRandom test = new StringRandom();
        //测试
        //System.out.println(test.getStringRandom(20));

        System.out.println(test.getStringNumRandom(20));
    }
}
