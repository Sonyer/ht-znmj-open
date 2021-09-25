package com.handturn.bole.sysconf.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.monitor.contant.RedisContant;
import com.handturn.bole.monitor.service.IRedisService;
import com.handturn.bole.sysconf.entity.SysconfDictCode;
import com.handturn.bole.sysconf.mapper.SysconfDictCodeMapper;
import com.handturn.bole.sysconf.service.ISysconfDictCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统-数据字典明细 Service实现
 *
 * @author Eric
 * @date 2019-12-01 10:59:43
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysconfDictCodeServiceImpl extends ServiceImpl<SysconfDictCodeMapper, SysconfDictCode> implements ISysconfDictCodeService {
    @Autowired
    private IRedisService redisService;

    @Override
    public ICustomPage<SysconfDictCode> findSysconfDictCodes(QueryRequest request, SysconfDictCode sysconfDictCode) {
        CustomPage<SysconfDictCode> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysconfDictCode);
    }

    @Override
    @Transactional
    public void saveSysconfDictCode(SysconfDictCode sysconfDictCode) {
        if(sysconfDictCode.getId() == null){
            sysconfDictCode.setStatus(BaseStatus.ENABLED);
            this.save(sysconfDictCode);
        }else{

            SysconfDictCode sysconfDictCodeOld = this.baseMapper.selectById(sysconfDictCode.getId());
            CopyUtils.copyProperties(sysconfDictCode,sysconfDictCodeOld);
            this.updateById(sysconfDictCodeOld);

            //清理redis
            this.cleanRedisDictTypeCode(sysconfDictCode.getTypeCode());
        }
    }

    @Override
    @Transactional
    public void enableSysconfDictCode(String[] sysconfDictCodeIds) {
        Arrays.stream(sysconfDictCodeIds).forEach(sysconfDictCodeId -> {
            SysconfDictCode sysconfDictCode = this.baseMapper.selectById(sysconfDictCodeId);
            sysconfDictCode.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysconfDictCode);

            //清理redis
            this.cleanRedisDictTypeCode(sysconfDictCode.getTypeCode());
        });
    }

    @Override
    @Transactional
    public void disableSysconfDictCode(String[] sysconfDictCodeIds) {
        Arrays.stream(sysconfDictCodeIds).forEach(sysconfDictCodeId -> {
            SysconfDictCode sysconfDictCode = this.baseMapper.selectById(sysconfDictCodeId);
            sysconfDictCode.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysconfDictCode);

            //清理redis
            this.cleanRedisDictTypeCode(sysconfDictCode.getTypeCode());
        });
    }

    @Override
    public SysconfDictCode findSysconfDictCodeById(Long sysconfDictCodeId){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfDictCode>().lambda().eq(SysconfDictCode::getId, sysconfDictCodeId));
    }

    public List<OptionVo> getDictOptionVoByTypeCode(String typeCode){
        String jsonStr = "";
        try {
            jsonStr = redisService.get(RedisContant.PREFIX_DIC_TYPE_OPTION + typeCode);
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }

        List<OptionVo> optionVos = new ArrayList<OptionVo>();
        if(!StringUtils.isEmpty(jsonStr)){
            try {
                List<JSONObject> optionObjs = new ArrayList<JSONObject>();
                optionObjs = JSONArray.parseObject(jsonStr, List.class);
                optionObjs.forEach(optionObj ->{
                    OptionVo optionVo = JSONObject.parseObject(optionObj.toString(),OptionVo.class);
                    optionVos.add(optionVo);
                });
            }catch(Exception e){
                log.error("数据字典-OPTION获取Redis异常:"+"["+typeCode+"]"+e.getMessage());
                e.printStackTrace();
            }
        }

        if(optionVos == null || optionVos.size() <= 0){
            QueryWrapper<SysconfDictCode> queryWrapper = new QueryWrapper<SysconfDictCode>();

            if (StringUtils.isNotBlank(typeCode)) {
                queryWrapper.lambda().eq(SysconfDictCode::getTypeCode, typeCode);
                queryWrapper.lambda().orderByAsc(SysconfDictCode::getOrderNum);
                List<SysconfDictCode> dictCodes = this.baseMapper.selectList(queryWrapper);
                for(SysconfDictCode dictCode : dictCodes){
                    OptionVo optionvo = new OptionVo();
                    optionvo.setValue(dictCode.getCodeValue());
                    optionvo.setText(dictCode.getDisplayValueCn());
                    optionVos.add(optionvo);
                }

                jsonStr =JSONArray.toJSONString(optionVos);
                try {
                    redisService.set(RedisContant.PREFIX_DIC_TYPE_OPTION + typeCode,jsonStr);
                } catch (RedisConnectException e) {
                    log.error("数据字典-OPTION存储Redis异常:"+"["+typeCode+"]"+e.getMessage());
                    e.printStackTrace();
                }

            }
        }
        return optionVos;
    }

    /**
     * 查询显示名
     * @param typeCode
     * @return
     */
    public Map<String,Object> getDictMapByTypeCode(String typeCode){
        String jsonStr = "";
        try {
            jsonStr = redisService.get(RedisContant.PREFIX_DIC_TYPE_MAP + typeCode);
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }

        Map<String,Object> result = new HashMap<String,Object>();
        if(!StringUtils.isEmpty(jsonStr)){
            try {
                result = JSONArray.parseObject(jsonStr, Map.class);
            }catch(Exception e){
                log.error("数据字典-MAP获取Redis异常:"+"["+typeCode+"]"+e.getMessage());
                e.printStackTrace();
            }
        }


        if(result == null || result.size() <= 0){
            QueryWrapper<SysconfDictCode> queryWrapper = new QueryWrapper<SysconfDictCode>();
            if (StringUtils.isNotBlank(typeCode)) {
                queryWrapper.lambda().eq(SysconfDictCode::getTypeCode, typeCode);
                queryWrapper.lambda().orderByAsc(SysconfDictCode::getOrderNum);
                List<SysconfDictCode> dictCodes = this.baseMapper.selectList(queryWrapper);
                for(SysconfDictCode dictCode : dictCodes){
                    result.put(dictCode.getCodeValue(),dictCode.getDisplayValueCn());
                }
            }
            jsonStr =JSONArray.toJSONString(result);
            try {
                redisService.set(RedisContant.PREFIX_DIC_TYPE_MAP + typeCode,jsonStr);
            } catch (RedisConnectException e) {
                log.error("数据字典-MAP存储Redis异常:"+"["+typeCode+"]"+e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 查询显示名
     * @param typeCode
     * @return
     */
    public Map<String,String> getDictNameByTypeCode(String typeCode){
        Map<String,String> result = new HashMap<String,String>();
        QueryWrapper<SysconfDictCode> queryWrapper = new QueryWrapper<SysconfDictCode>();
        queryWrapper.lambda().eq(SysconfDictCode::getTypeCode,typeCode);
        List<SysconfDictCode> dictCodes = this.baseMapper.selectList(queryWrapper);
        for(SysconfDictCode dictCode : dictCodes){
            result.put(dictCode.getDisplayValueCn(),dictCode.getCodeValue());
        }
        return result;
    }

    /**
     * 指定查询
     * @param typeCode
     * @return
     */
    public List<OptionVo> getDictOptionVoByTypeCodeContains(String typeCode, Set<String> dictCodeTemp){
        List<OptionVo> optionVos = this.getDictOptionVoByTypeCode(typeCode);
        if(dictCodeTemp != null && dictCodeTemp.size() > 0){
            Set<Integer> idContainSet = new HashSet<>();
            for(int i = 0; i < optionVos.size();i++){
                OptionVo optionVo = JSONObject.parseObject(JSONObject.toJSONString(optionVos.get(i)),OptionVo.class);
                if(!dictCodeTemp.contains(optionVo.getValue())){
                    idContainSet.add(i);
                }
            }
            idContainSet.forEach(idContain ->{
                optionVos.remove(idContain);
            });

        }
        return optionVos;
    }

    /**
     * 指定查询
     * @param typeCode
     * @return
     */
    public List<OptionVo> getDictOptionVoByTypeCodeExit(String typeCode,Set<String> dictCodeTemp){
        List<OptionVo> optionVos = this.getDictOptionVoByTypeCode(typeCode);
        if(dictCodeTemp != null && dictCodeTemp.size() > 0){
            Set<Integer> idExitSet = new HashSet<>();
            for(int i = 0; i < optionVos.size();i++){
                OptionVo optionVo = JSONObject.parseObject(JSONObject.toJSONString(optionVos.get(i)),OptionVo.class);
                if(dictCodeTemp.contains(optionVo.getValue())){
                    idExitSet.add(i);
                }
            }
            idExitSet.forEach(idExit ->{
                optionVos.remove(idExit);
            });

        }
        return optionVos;
    }

    /**
     * 清理redis缓存
     * @param typeCode
     */
    private void cleanRedisDictTypeCode(String typeCode){
        //清理redis
        try {
            redisService.del(RedisContant.PREFIX_DIC_TYPE_OPTION + typeCode);
        } catch (RedisConnectException e) {
            e.printStackTrace();
            log.error("数据字典-OPTION清理Redis异常:"+"["+typeCode+"]"+e.getMessage());
            e.printStackTrace();
        }

        //清理redis
        try {
            redisService.del(RedisContant.PREFIX_DIC_TYPE_MAP + typeCode);
        } catch (RedisConnectException e) {
            e.printStackTrace();
            log.error("数据字典-MAP清理Redis异常:"+"["+typeCode+"]"+e.getMessage());
            e.printStackTrace();
        }
    }
}
