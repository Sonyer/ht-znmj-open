package com.ht.znmj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ht.znmj.common.customPage.CustomPage;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.common.entity.FebsConstant;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.entity.*;
import com.ht.znmj.mapper.EquipmentInfoMapper;
import com.ht.znmj.service.IEquipmentInfoService;
import com.ht.znmj.utils.DESToolUtil;
import com.ht.znmj.utils.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备信息 Service实现
 *
 * @author Eric
 * @date 2020-01-29 14:34:06
 */
@Service("EquipmentInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EquipmentInfoServiceImpl extends ServiceImpl<EquipmentInfoMapper, EquipmentInfo> implements IEquipmentInfoService {

    @Override
    public ICustomPage<EquipmentInfo> findEquipmentInfos(QueryRequest request, EquipmentInfo equipmentInfo) {
        CustomPage<EquipmentInfo> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, equipmentInfo);
    }


    /**
     * 通过状态查询
     */
    @Override
    public List<EquipmentInfo> findEquipmentInfosByStatus(String status){
        QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EquipmentInfo::getStatus,status);
        queryWrapper.lambda().orderByAsc(EquipmentInfo::getAreaName);
        queryWrapper.lambda().orderByAsc(EquipmentInfo::getSeqNum);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 通过连接状态查询
     */
    @Override
    public List<EquipmentInfo> findEquipmentInfosByConnStatus(String connStatus){
        QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EquipmentInfo::getConnStatus,connStatus);
        queryWrapper.lambda().orderByAsc(EquipmentInfo::getAreaName);
        queryWrapper.lambda().orderByAsc(EquipmentInfo::getSeqNum);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询所有
     */
    @Override
    public List<EquipmentInfo> findAllEquipmentInfos(){
        QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(EquipmentInfo::getAreaName);
        queryWrapper.lambda().orderByAsc(EquipmentInfo::getSeqNum);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询Map
     */
    @Override
    public Map<String,EquipmentInfo> findMapEquipmentInfos(){
        List<EquipmentInfo> equipmentInfos = findEquipmentInfosByStatus(EquipmentInfoStatus.ACTIVE.getCode());
        Map<String,EquipmentInfo> resultMap = new HashMap<>();
        equipmentInfos.forEach(equipmentInfo ->{
            resultMap.put(equipmentInfo.getSn(),equipmentInfo);
        });
        return resultMap;
    }

    /**
     * 通过ID查询
     */
    @Override
    public EquipmentInfo findEquipmentInfoById(String id){
        QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EquipmentInfo::getId,id);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 通过ID查询
     */
    @Override
    public EquipmentInfo findEquipmentInfoBySn(String sn){
        QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EquipmentInfo::getSn,sn);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 保存设备信息
     * @param equipmentInfo
     */
    @Override
    @Transactional
    public EquipmentInfo saveEquipmentInfo(EquipmentInfo equipmentInfo){
        if(equipmentInfo.getId() == null){
            equipmentInfo.setCreateTime(new Date());
            equipmentInfo.setUpdateTime(new Date());
            this.save(equipmentInfo);
            return equipmentInfo;
        }else{
            /*EquipmentInfo equipmentInfoOld = this.baseMapper.selectById(equipmentInfo.getId());
            CopyUtils.copyProperties(equipmentInfo,equipmentInfoOld);*/
            equipmentInfo.setUpdateTime(new Date());
            this.updateById(equipmentInfo);
            return equipmentInfo;
        }
    }

    /**
     * 修改状态
     */
    @Override
    @Transactional
    public EquipmentInfo updateConnStatus(String sn,String connStatus){
        EquipmentInfo equipment = findEquipmentInfoBySn(sn);
        equipment.setConnStatus(connStatus);
        this.baseMapper.updateById(equipment);
        return equipment;
    }

    /**
     * 修改日志最大
     */
    @Override
    @Transactional
    public void updateMaxLogSeq(String sn,Integer maxLogSeq){
        EquipmentInfo equipment = new EquipmentInfo();
        equipment.setMaxLogSeq(maxLogSeq);
        QueryWrapper<EquipmentInfo> equipmentInfoQueryWrapper = new QueryWrapper<>();
        equipmentInfoQueryWrapper.lambda().eq(EquipmentInfo::getSn,sn);
        this.baseMapper.update(equipment,equipmentInfoQueryWrapper);
    }

    /**
     * 注册
     */
    @Override
    @Transactional
    public String registerEquipmentInfo(String license){
        int successCount = 0;
        int errorCount = 0;
        String errorMessage = "";
        String[] licenseArr = license.split("\n");
        for(int i = 0; i < licenseArr.length; i++){
            String licenseSingle = licenseArr[i].trim();
            if(!StringUtils.isEmpty(licenseSingle)){
                try {
                    String equipmentSn = DESToolUtil.decrypt(licenseSingle,DESToolUtil.KEY_);
                    EquipmentInfo equipmentInfo = findEquipmentInfoBySn(equipmentSn);
                    if(equipmentInfo == null){
                        equipmentInfo = new EquipmentInfo();
                        equipmentInfo.setAreaName("未知区域");
                        equipmentInfo.setInOut(EquipmentInfoInOut.NONE.getCode());
                        equipmentInfo.setConnStatus(EquipmentInfoConnStatus.NO_CONNECT.getCode());
                        equipmentInfo.setVersion("未知");
                        equipmentInfo.setSeqNum("99");
                        equipmentInfo.setCloudFlag(EquipmentInfoCloudFlag.NO_ASY.getCode());
                    }

                    equipmentInfo.setLicense(licenseSingle);
                    equipmentInfo.setSn(equipmentSn);
                    equipmentInfo.setStatus(EquipmentInfoStatus.ACTIVE.getCode());
                    equipmentInfo.setCreateTime(new Date());
                    equipmentInfo.setUpdateTime(new Date());
                    saveEquipmentInfo(equipmentInfo);
                    successCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                    errorCount++;
                }
            }
        }

        if(errorCount > 0){
            errorMessage = "注册成功:"+successCount+"个,失败:"+errorCount+"个!";
        }

        return errorMessage;
    }

}
