package com.handturn.bole.master.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.DateUtil;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.common.utils.StringUtil;
import com.handturn.bole.master.common.service.ISmsSendService;
import com.handturn.bole.master.common.service.IWxNoticeService;
import com.handturn.bole.master.member.entity.*;
import com.handturn.bole.master.member.mapper.MemberNotifyMapper;
import com.handturn.bole.master.member.service.IMemberNotifyService;
import com.handturn.bole.master.set.entity.NotifySet;
import com.handturn.bole.master.set.service.INotifySetService;
import com.handturn.bole.monitor.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * 智能门禁-会员通知 Service实现
 *
 * @author Eric
 * @date 2020-05-19 09:22:40
 */
@Service("memberNotifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MemberNotifyServiceImpl extends ServiceImpl<MemberNotifyMapper, MemberNotify> implements IMemberNotifyService {
    @Autowired
    private IRedisService redisService;

    @Autowired
    private INotifySetService setNotifyService;

    @Autowired
    private IWxNoticeService wxNoticeService;

    @Autowired
    private ISmsSendService smsSendService;

    @Override
    public ICustomPage<MemberNotify> findMemberNotifys(QueryRequest request, MemberNotify memberNotify) {
        CustomPage<MemberNotify> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, memberNotify);
    }

    @Override
    @Transactional
    public MemberNotify saveMemberNotify(MemberNotify memberNotify) {
        if(memberNotify.getId() == null){
            this.save(memberNotify);
            return memberNotify;
        }else{
            MemberNotify memberNotifyOld = this.baseMapper.selectById(memberNotify.getId());
            CopyUtils.copyProperties(memberNotify,memberNotifyOld);
            this.updateById(memberNotifyOld);
            return memberNotifyOld;
        }
    }

    @Override
    @Transactional
    public void enableMemberNotify(String[] memberNotifyIds) {
        Arrays.stream(memberNotifyIds).forEach(memberNotifyId -> {
            MemberNotify memberNotify = this.baseMapper.selectById(memberNotifyId);
            /*memberNotify.setStatus(BaseStatus.ENABLED);*/
            this.baseMapper.updateById(memberNotify);
        });
	}

    @Override
    @Transactional
    public void disableMemberNotify(String[] memberNotifyIds) {
        Arrays.stream(memberNotifyIds).forEach(memberNotifyId -> {
            MemberNotify memberNotify = this.baseMapper.selectById(memberNotifyId);
            /*memberNotify.setStatus(BaseStatus.DISABLED);*/
            this.baseMapper.updateById(memberNotify);
        });
    }

    @Override
    public MemberNotify findMemberNotifyById(Long memberNotifyId){
        return this.baseMapper.selectOne(new QueryWrapper<MemberNotify>().lambda().eq(MemberNotify::getId, memberNotifyId));
    }

    /**
     * 异步 用户通知
     * @param memberId
     * @param notifyType
     */
    @Transactional
    public void notifyMessage(Long memberId,String accountCode,String notifyType, Object messageDataDto){
        Set<String> statuese = new HashSet<String>();
        statuese.add(BaseStatus.ENABLED);
        NotifySet setNotify = setNotifyService.findNotifySetByType(notifyType,statuese);
        if(setNotify != null){
            MemberNotify notify = new MemberNotify();
            notify.setMemberId(memberId);
            notify.setAccountCode(accountCode);
            notify.setNotifyType(notifyType);
            notify.setInnerNotifyToPage(setNotify.getInnerNotifyToPage());
            notify.setInnerNotifyMessage(setNotify.getInnerNotifyMessage());
            notify.setWxNotifyTemplateId(setNotify.getWxNotifyTemplateId());
            notify.setWxNotifyToPage(setNotify.getWxNotifyToPage());
            notify.setWxNotifyDataJson(setNotify.getWxNotifyDataJson());
            notify.setSmsNotifyTemplateCode(setNotify.getSmsNotifyTemplateCode());
            notify.setSmsNotifyDataJson(setNotify.getSmsNotifyDataJson());
            notify.setSmsPhoneNumber(setNotify.getSmsPhoneNumber());

            if(messageDataDto != null){
                Map<String,Object> objDataMap = null;
                try {
                    if( messageDataDto instanceof Map){
                        objDataMap = (Map<String,Object>)messageDataDto;
                    }else{
                        objDataMap = objectToMap(messageDataDto);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    log.error("消息通知:数据转换错误!");
                }


                Map<String, Object> finalObjDataMap = objDataMap;
                objDataMap.keySet().forEach(key->{
                    String value = "";
                    if(finalObjDataMap.get(key) instanceof Date){
                        if(finalObjDataMap.get(key) != null){
                            value = DateUtil.getDateFormat((Date) finalObjDataMap.get(key), DateUtil.CN_TIMS_SPLIT_PATTERN);
                        }
                    }else if(finalObjDataMap.get(key) instanceof BigDecimal){
                        if(finalObjDataMap.get(key) != null) {
                            value = ((BigDecimal) finalObjDataMap.get(key)).stripTrailingZeros().toPlainString();
                        }
                    }else{
                        if(finalObjDataMap.get(key) != null) {
                            value = finalObjDataMap.get(key).toString();
                        }
                    }

                    log.error("消息通知:对象属性值,"+key+",value:"+value);

                    if(setNotify.getIsOnInner().equals(BaseBoolean.TRUE)){
                        notify.setInnerNotifyToPage(notify.getInnerNotifyToPage().replaceAll("\\$\\{"+key+"\\}",value));
                        notify.setInnerNotifyMessage(notify.getInnerNotifyMessage().replaceAll("\\$\\{"+key+"\\}",value));
                        notify.setCacheStatus(MemberNotifyCacheStatus.UNCACHE);
                        notify.setReadStatus(MemberNotifyReadStatus.UNREAD);
                    }
                    if(setNotify.getIsOnWx().equals(BaseBoolean.TRUE)){
                        notify.setWxNotifyToPage(notify.getWxNotifyToPage().replaceAll("\\$\\{"+key+"\\}",value));
                        notify.setWxNotifyDataJson(notify.getWxNotifyDataJson().replaceAll("\\$\\{"+key+"\\}",value));
                    }
                    if(setNotify.getIsOnSms().equals(BaseBoolean.TRUE)){
                        notify.setSmsPhoneNumber(notify.getSmsPhoneNumber().replaceAll("\\$\\{"+key+"\\}",value));
                        notify.setSmsNotifyDataJson(notify.getSmsNotifyDataJson().replaceAll("\\$\\{"+key+"\\}",value));
                    }
                });
            }

            //消息发送
            if(setNotify.getIsOnInner().equals(BaseBoolean.TRUE)){  //内部消息为异步，可以不处理

            }

            if(setNotify.getIsOnWx().equals(BaseBoolean.TRUE)){  //微信消息直发
                //需要校验各各字段长度，并截取
                Map<String,Map<String,String>> mapData = wxNotifyDataAgainDeal(notify.getWxNotifyDataJson());
                try {
                    wxNoticeService.pushNoticeToUser(memberId, notify.getWxNotifyTemplateId(), notify.getWxNotifyToPage(),mapData);
                }catch(Exception e){
                    //不强制要求执行
                    e.printStackTrace();
                }
            }
            if(setNotify.getIsOnSms().equals(BaseBoolean.TRUE)){  //SMS消息直发
                SendSmsResponse response = null;
                try {
                    response = smsSendService.sendSms(notify.getSmsNotifyTemplateCode(), notify.getSmsPhoneNumber(), notify.getSmsNotifyDataJson(), "");
                    if(response.getCode().equals("OK")){

                    }else{
                        log.error("SMS消息发送问题!");
                    }
                } catch (ClientException e) {
                    log.error("SMS消息发送问题!");
                    e.printStackTrace();
                }

            }

            notify.setSendStatus(MemberNotifySendStatus.SEND);
            saveMemberNotify(notify);

        }

    }

    /**
     * 微信通知再处理
     * @param json
     * @return
     */
    private Map<String,Map<String,String>> wxNotifyDataAgainDeal(String json){
        Map<String,Map<String,String>> result = new HashMap<>();
        Map<String,JSONObject> dataMap = JSONObject.parseObject(json,Map.class);
        dataMap.keySet().forEach(key ->{
            Map<String,String> dataValueMap = JSONObject.parseObject(dataMap.get(key).toJSONString(),Map.class);
           /* Map<String,String> dataValueStringMap = new HashMap<>();*/
            if(key.contains("thing")){
                if(dataValueMap.get("value") != null){
                    if(dataValueMap.get("value").toString().length() > 20){
                       dataValueMap.put("value", StringUtil.substring(dataValueMap.get("value").toString(),40,"UTF-8"));
                       /*dataValueStringMap.put("value",StringUtil.substring(dataValueMap.get("value").toString(),40,"UTF-8"));*/
                    }
                }
            }

            result.put(key,dataValueMap);
        });

        return result;
    }

    /**
     * 通过类型获取
     * @return
     */
    public List<MemberNotify> findMemberNotifyByType(String accountCode,Set<String> types,Set<String> cacheStatuses){
        QueryWrapper<MemberNotify> queryWrapper = new QueryWrapper<MemberNotify>();
        queryWrapper.lambda().eq(MemberNotify::getAccountCode,accountCode);
        if(types != null && types.size() > 0){
            queryWrapper.lambda().in(MemberNotify::getNotifyType,types);
        }
        if(cacheStatuses != null && cacheStatuses.size() > 0){
            queryWrapper.lambda().in(MemberNotify::getCacheStatus,cacheStatuses);
        }
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 异步 用户触发缓存通知
     * @param accountCode
     */
    @Transactional
    public void cacheNotifyMessage(String accountCode){
        Set<String> statuese = new HashSet<>();
        statuese.add(BaseStatus.ENABLED);
        List<NotifySet> setNotifys = setNotifyService.findNotifySetByIs(null, BaseBoolean.TRUE,null,null,statuese);
        Set<String> notifyTypes = new HashSet<>();
        setNotifys.forEach(setNotify ->{
            notifyTypes.add(setNotify.getNotifyType());
        });

        try {
            String notifyTypesJson = JSONArray.toJSONString(notifyTypes);
            redisService.set(MemberNotifyType.getRedisInnerNotifyTypesKey(), notifyTypesJson);  //永久有效
        } catch (RedisConnectException e) {
            log.error("内部消息通知(存储消息通知类型-REDIS):异常错误!"+e.getLocalizedMessage());
            e.printStackTrace();
        }

        Set<String> cacheStatuses = new HashSet<>();
        cacheStatuses.add(MemberNotifyCacheStatus.UNCACHE);
        List<MemberNotify> notifys = findMemberNotifyByType(accountCode,notifyTypes,cacheStatuses);

        Map<String,String> newNotifyMap = new HashMap<String,String>();
        notifys.forEach(notify ->{
            String redisKey = MemberNotifyType.getRedisKey(notify.getNotifyType(),notify.getAccountCode());
            //保存状态
            notify.setCacheStatus(MemberNotifyCacheStatus.CACHED);
            saveMemberNotify(notify);
            newNotifyMap.put(redisKey,"1");
        });

        //发缓存数据
        newNotifyMap.keySet().forEach(key ->{
            try {
                redisService.set(key,"1",1000*60*60*24L);  //保留一天
            } catch (RedisConnectException e) {
                log.error("内部消息通知(存储消息通知内容-REDIS):异常错误!"+e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    public static void main(String[] args){
        /*BuyerOrder order = new BuyerOrder();
        order.setPayTime(null);
        Map<String,Object> params = null;
        try {
            params = objectToMap(order);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Map<String, Object> finalParams = params;
        params.keySet().forEach(key-> {
            String value = "";
            if (finalParams.get(key) instanceof Date) {

                value = DateUtil.getDateFormat((Date) finalParams.get(key),DateUtil.FULL_TIME_SPLIT_PATTERN);
                System.out.println("进入转换"+value);
            }
        });*/

        Set<String> set = new HashSet<>();
        set.add("bbbb");
        set.add("ccc");
        String notifyTypesJson = JSONArray.toJSONString(set);
        Set<String> innerNotifyTypesSet = JSON.parseObject(notifyTypesJson,Set.class);
        System.out.println("");
    }


}
