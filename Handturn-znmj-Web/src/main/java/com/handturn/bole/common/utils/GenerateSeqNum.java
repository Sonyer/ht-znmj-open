package com.handturn.bole.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 序号生成
 * [$CU]    --客户编号
 * [$ORG]   --组织编号
 * [$OC]    --网点编号
 * [$DATE]  --日期yyMMdd
 * [$TIME]  --时间HH24mmss
 * [$SEQ{6}]  --流水号“{}”内位数
 */
public class GenerateSeqNum {
    public static final String $CU = "[$CU]";  //客户编号
    public static final String $ORG = "[$ORG]";  //组织编号
    public static final String $OC = "[$OC]";  //网点编号
    public static final String $DATETIME = "[$DATETIME]";   //日期时间
    public static final String $DATE = "[$DATE]";  //日期yyMMdd
    public static final String $TIME = "[$TIME]";  //时间HH24mmss
    public static final String $SEQ = "[$SEQ{6}]";  //流水号“{}”内位数

    public static final String CHECK_$SEQ = "$SEQ";  //流水号“{}”内位数

    public static String generate(Map<String,String> params){
        String rule = "ddd[$CU]-[$ORG][$OC][$DATETIME][$DATE][$TIME][$SEQ{6}]bbb";
        String ruleStr = rule;

        //获取规则参数
        Pattern ruleParamP=Pattern.compile("\\[.*?\\]");
        Matcher ruleParamM=ruleParamP.matcher(rule);
        List<String> ruleParamList = new ArrayList<String>();

        String seqStr = "";
        while(ruleParamM.find()){
            String param = ruleParamM.group(0).substring(0,ruleParamM.group(0).length());
            if(params.get(param) != null){
                ruleStr = ruleStr.replace(param,params.get(param));
            }

            if(param.contains(CHECK_$SEQ)){
                seqStr = param;
            }
        }

        //日期时间
        if(ruleStr.contains($DATETIME)) {
            SimpleDateFormat simpleDateFormatDateTime = new SimpleDateFormat("yyMMddHHmmss");
            String dateTime = simpleDateFormatDateTime.format(new Date());
            ruleStr = ruleStr.replace($DATETIME,dateTime);
        }

        //日期
        if(ruleStr.contains($DATE)) {
            SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("yyMMdd");
            String date = simpleDateFormatDate.format(new Date());
            ruleStr = ruleStr.replace($DATE,date);
        }

        //时间
        if(ruleStr.contains($TIME)) {
            SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HHmmss");
            String time = simpleDateFormatTime.format(new Date());
            ruleStr = ruleStr.replace($TIME,time);
        }

        Long seqNum =  10000000000l;

        //流水
        if(StringUtils.isNotEmpty(seqStr)){
            String seqNumStr = "";
            seqNumStr = convertSeqNum(seqNum,seqStr);
            ruleStr = ruleStr.replace(seqStr,seqNumStr);
        }

        return ruleStr;
    }

    /**
     * 流水号转字符串
     * @param seqNum
     * @param seqStr
     * @return
     */
    private static String convertSeqNum(Long seqNum,String seqStr){
        Pattern ruleParamP=Pattern.compile("\\{.*?\\}");
        Matcher ruleParamM=ruleParamP.matcher(seqStr);

        String seqNumStr = "";

        int length = 0;
        while(ruleParamM.find()){
            length = Integer.valueOf(ruleParamM.group(0).substring(1,ruleParamM.group(0).length()-1));
        }
        if (length == 0){
            seqNumStr = seqNum+"";
        }else{
            seqNumStr = autoGenericCode(seqNum.toString(),length);
        }

        return seqNumStr;
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     * @param code
     * @return
     */
    private static String autoGenericCode(String code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", Long.valueOf(code));

        return result;
    }


    public static void main(String[] args){
        Map<String,String> params = new HashMap<String,String>();
        params.put("[$CU]","CU");
        params.put("[$ORG]","ORG");
        params.put("[$OC]","OC");

        System.out.println(generate(params));

        /*String str = "abc[def]deftfh[1]";

        Pattern p=Pattern.compile("[(\\w+)]");
        Matcher m=p.matcher(str);
        while(m.find()){
            System.out.println(m.group(1));

        }*/


    }
}
