package com.handturn.bole.common.configure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.system.entity.SysUser;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * MybatisPlus自定义填充公共字段
 * @author by songyequan on 2018/2/6.
 */
@Component
public class MybatisMetaObjectHandler implements MetaObjectHandler {
    private final static String USER_CODE = "USER_CODE";
    private final static String USER_NAME = "USER_NAME";

    //TODO: 获取当前用户,格式uid:userCode
    private Map<String,String> getOperator() {
        Map<String,String> userMap = new HashMap<String,String>();
        SysUser sysUser = UserInfoHolder.getUserInfo();
        if(sysUser == null){
            userMap.put(USER_CODE,"") ;
            userMap.put(USER_NAME,"");
        }else{
            userMap.put(USER_CODE,sysUser.getUserCode().toString()) ;
            userMap.put(USER_NAME,sysUser.getUserName());
        }

        return userMap;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        Map<String,String> userMap = getOperator();
        setFieldValByName("createUserCode",userMap.get(USER_CODE),metaObject);
        setFieldValByName("createUserName",userMap.get(USER_NAME),metaObject);
        setFieldValByName("createDate",new Date(),metaObject);
        setFieldValByName("lastUpdUserCode",userMap.get(USER_CODE),metaObject);
        setFieldValByName("lastUpdUserName",userMap.get(USER_NAME),metaObject);
        setFieldValByName("lastUpdDate",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Map<String,String> userMap = getOperator();
        setFieldValByName("lastUpdUserCode",userMap.get(USER_CODE),metaObject);
        setFieldValByName("lastUpdUserName",userMap.get(USER_NAME),metaObject);
        setFieldValByName("lastUpdDate",new Date(),metaObject);
        /*Object object = metaObject.getValue("recordVersion");
        if (object != null) {
            Long recordVersion = (Long)object;
            recordVersion++;
            setFieldValByName("recordVersion",recordVersion,metaObject);
        }*/
    }
}
