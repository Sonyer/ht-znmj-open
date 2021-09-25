package com.handturn.bole.common.utils;

import io.micrometer.core.instrument.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fredy
 * @version 1.0
 * @description com.scm.erp.utils
 * @date 2019/1/23
 */

public class StringUtil {

    /**
     * 去除字符串前中后所有空格以及转小写并且前后拼接%用于模糊查询
     *
     * @param s
     * @return
     */
    public static String strToLikeField(String s) {
        if (StringUtils.isNotBlank(s)) {
            String s1 = s.replaceAll("\\s*", "").toLowerCase();
            return "%" + s1 + "%";
        } else {
            return null;
        }
    }

    /**
     * 去除字符串前中后所有空格以及转小写
     *
     * @param s
     * @return
     */
    public static String removeSpacesAndToLowerCase(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replaceAll("\\s*", "").toLowerCase();
        } else {
            return null;
        }
    }

    /**
     * 去除字符串前中后所有空格以及转大写
     *
     * @param s
     * @return
     */
    public static String removeSpacesAndToUpperCase(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replaceAll("\\s*", "").toUpperCase();
        } else {
            return null;
        }
    }

    /**
     * 去除字符串前中后所有空格
     *
     * @param s
     * @return
     */
    public static String removeAllSpaces(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replaceAll("\\s*", "");
        } else {
            return null;
        }
    }

    /**
     * 去除前后空格
     *
     * @param s
     * @return
     */
    public static String removeOuterSpaces(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.trim();
        } else {
            return null;
        }
    }

    /**
     * 分割字符串转为list集合 可去除所有空格
     *
     * @param str
     * @param splitStr       分割符
     * @param isRemoveSpaces 是否去空格
     * @return
     */
    public static List<String> returnList(String str, String splitStr, Boolean isRemoveSpaces) {
        List<String> list = new ArrayList<>();
        if (str == null || "".equals(str)) {
            return null;
        }
        String[] strArr = str.split(splitStr);
        if (isRemoveSpaces) {
            for (String item : strArr) {
                if (StringUtils.isNotBlank(item)) {
                    list.add(removeAllSpaces(item));
                }
            }
        } else {
            for (String item : strArr) {
                list.add(item);
            }
        }
        if (list.size() == 0) {
            list = null;
        }
        return list;
    }

    /**
     * 是否包括0的整数
     * @param str
     * @return
     */
    public static boolean isPositiveInteger(String str) {
        Pattern pattern = Pattern.compile("^[0-9]{1,}$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为数字（小数或整数）
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");//这个是对的
        //Pattern pattern = Pattern.compile("^[+-]?(0|([1-9]\\d*))(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断包括0的整数
     *
     * @param str
     * @return
     */
    public static boolean isPositiveIntegerInZero(String str) {
        Pattern pattern = Pattern.compile("^(0|\\+?[1-9]\\d*)?(0\\.0*)?((\\+?[1-9]\\d*)(\\.0*))?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断不包括0的整数
     *
     * @param str
     * @return
     */
    public static boolean isPositiveIntegerNoZero(String str) {
        Pattern pattern = Pattern.compile("^\\+?([1-9]\\d*)?(([1-9]\\d*)(\\.0*))?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 空格截取转换成List,处理换行 回车 中英文逗号拆分
     *
     * @param str
     */
    public static List<String> splitStringToList(String str) {
        List<String> resultList = new ArrayList<String>();
        if (str == null) {
            return resultList;
        }
        String[] strTemps = str.split("\\n|\\r|,|，");
        for (String subStr : strTemps) {
            subStr = subStr.trim();
            if (!org.springframework.util.StringUtils.isEmpty(subStr)) {
                resultList.add(subStr);
            }
        }
        return resultList;
    }

    /**
     * 移除非法中文字符
     * @param srcStr
     * @return
     */
    public static String removeIllegalChar(String srcStr){
        if(srcStr == null){
            return "";
        }
        int[] input = new int[]{0x7f, 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89, 0x8a,
                0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90, 0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99,
                0x9a, 0x9b, 0x9c, 0x9d, 0x9e, 0x9f, 0xad, 0x483, 0x484, 0x485, 0x486, 0x487, 0x488, 0x489,
                0x559, 0x55a, 0x58a, 0x591, 0x592, 0x593, 0x594, 0x595, 0x596, 0x597, 0x598, 0x599, 0x59a,
                0x59b, 0x59c, 0x59d, 0x59e, 0x59f, 0x5a0, 0x5a1, 0x5a2, 0x5a3, 0x5a4, 0x5a5, 0x5a6, 0x5a7,
                0x5a8, 0x5a9, 0x5aa, 0x5ab, 0x5ac, 0x5ad, 0x5ae, 0x5af, 0x5b0, 0x5b1, 0x5b2, 0x5b3, 0x5b4,
                0x5b5, 0x5b6, 0x5b7, 0x5b8, 0x5b9, 0x5ba, 0x5bb, 0x5bc, 0x5bd, 0x5bf, 0x5c1, 0x5c2, 0x5c4,
                0x5c5, 0x5c6, 0x5c7, 0x606, 0x607, 0x608, 0x609, 0x60a, 0x63b, 0x63c, 0x63d, 0x63e, 0x63f,
                0x674, 0x6e5, 0x6e6, 0x70f, 0x76e, 0x76f, 0x770, 0x771, 0x772, 0x773, 0x774, 0x775, 0x776,
                0x777, 0x778, 0x779, 0x77a, 0x77b, 0x77c, 0x77d, 0x77e, 0x77f, 0xa51, 0xa75, 0xb44, 0xb62,
                0xb63, 0xc62, 0xc63, 0xce2, 0xce3, 0xd62, 0xd63, 0x135f, 0x200b, 0x200c, 0x200d, 0x200e,
                0x200f, 0x2028, 0x2029, 0x202a, 0x202b, 0x202c, 0x202d, 0x202e, 0x2044, 0x2071, 0xf701,
                0xf702, 0xf703, 0xf704, 0xf705, 0xf706, 0xf707, 0xf708, 0xf709, 0xf70a, 0xf70b, 0xf70c,
                0xf70d, 0xf70e, 0xf710, 0xf711, 0xf712, 0xf713, 0xf714, 0xf715, 0xf716, 0xf717, 0xf718,
                0xf719, 0xf71a, 0xfb1e, 0xfc5e, 0xfc5f, 0xfc60, 0xfc61, 0xfc62, 0xfeff, 0xfffc};
        StringBuilder b = new StringBuilder();
        int lastContinuous = -1;
        int span = 0;
        for (int i = 0; i < input.length; i++) {
            if (lastContinuous == -1 && i < input.length - 1 && input[i] + 1 == input[i + 1]) {
                lastContinuous = input[i];
                span = 1;
            } else {
                if (input[i] == lastContinuous + span) {
                    span++;
                } else if (lastContinuous != -1) {
                    if (b.length() > 0)
                        b.append("|");
                    b.append(String.format("[\\u%s-\\u%s]", zerolize(Integer.toHexString(lastContinuous)),
                            zerolize(Integer.toHexString(lastContinuous + span - 1))));
                    span = 0;
                    lastContinuous = -1;
                    i--;
                } else {
                    b.append("|\\u" + zerolize(Integer.toHexString(input[i])));
                }
            }
        }
        if (lastContinuous != -1) {
            if (b.length() > 0)
                b.append("|");
            b.append(String.format("[\\u%s-\\u%s]", zerolize(Integer.toHexString(lastContinuous)),
                    zerolize(Integer.toHexString(lastContinuous + span - 1))));
        }
        Pattern pattern = Pattern.compile(b.toString());
        Matcher matcher = pattern.matcher(srcStr);
        return matcher.replaceAll("");
    }

    private static String zerolize(String s) {
        if (s.length() < 4) {
            s = "000".substring(0, 4 - s.length()) + s;
        }
        return s;
    }

    /**
     * 计算字符串长度
     * @param str
     * @return
     */
    public static int strLength(String str){
        str = str.replaceAll("[^\\x00-\\xff]", "**");
        int length = str.length();
        return length;
    }

    /**
     * @param text    目标字符串
     * @param length    截取长度
     * @param encode    采用的编码方式
     * @return
     * @throws UnsupportedEncodingException
     */

    public static String substring(String text, int length, String encode){
        if (text == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int currentLength = 0;
        for (char c : text.toCharArray()) {
            try {
                currentLength += String.valueOf(c).getBytes(encode).length;
                if (currentLength <= length) {
                    sb.append(c);
                } else {
                    break;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(removeIllegalChar("BU_ZFQ-WW"));
    }
}
