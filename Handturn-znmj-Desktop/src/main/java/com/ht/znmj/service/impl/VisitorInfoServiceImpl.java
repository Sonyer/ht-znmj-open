package com.ht.znmj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ht.znmj.common.customPage.CustomPage;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.common.entity.FebsConstant;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.common.exception.FebsException;
import com.ht.znmj.entity.*;
import com.ht.znmj.mapper.VisitorInfoMapper;
import com.ht.znmj.sdk.ZBX_Global;
import com.ht.znmj.sdk.MyFunction;
import com.ht.znmj.service.IEquipmentInfoService;
import com.ht.znmj.service.IVisitorEquipmentService;
import com.ht.znmj.service.IVisitorInfoService;
import com.ht.znmj.utils.CopyUtils;
import com.ht.znmj.utils.SortUtil;
import com.sun.jna.ptr.IntByReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 人员信息 Service实现
 *
 * @author Eric
 * @date 2020-01-29 14:34:06
 */
@Service("VisitoryInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VisitorInfoServiceImpl extends ServiceImpl<VisitorInfoMapper, VisitorInfo> implements IVisitorInfoService {
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IVisitorEquipmentService visitorEquipmentService;

    @Override
    public ICustomPage<VisitorInfo> findVisitorInfos(QueryRequest request, VisitorInfo visitorInfo) {
        CustomPage<VisitorInfo> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, visitorInfo);
    }

    /**
     * 通过ID查询
     */
    @Override
    public VisitorInfo findVisitorInfoById(String id){
        QueryWrapper<VisitorInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VisitorInfo::getId,id);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 通过云端ID查询
     */
    @Override
    public List<VisitorInfo> findVisitorInfoByCloudId(String cloudId){
        QueryWrapper<VisitorInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VisitorInfo::getCloudId,cloudId);
        List<VisitorInfo> visitorInfos = this.baseMapper.selectList(queryWrapper);
        return visitorInfos;
    }

    /**
     * 通过姓名、手机号查询
     */
    @Override
    public VisitorInfo findVisitorInfoByUK(String name,String phoneNumber,String id){
        QueryWrapper<VisitorInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VisitorInfo::getName,name);
        queryWrapper.lambda().eq(VisitorInfo::getPhoneNumber,phoneNumber);
        if(!StringUtils.isEmpty(id)){
            queryWrapper.lambda().ne(VisitorInfo::getId,id);
        }
        List<VisitorInfo> visitorInfos = this.baseMapper.selectList(queryWrapper);
        return visitorInfos == null || visitorInfos.size() <=0 ?null:visitorInfos.get(0);
    }

    /**
     * 保存设备信息
     * @param visitorInfo
     */
    @Override
    @Transactional
    public VisitorInfo saveVisitorInfo(VisitorInfo visitorInfo){
        VisitorInfo visitorInfoOld = null;
        if(visitorInfo.getId() == null){
            visitorInfoOld = findVisitorInfoByUK(visitorInfo.getName(),visitorInfo.getPhoneNumber(),visitorInfo.getId());
            if(visitorInfoOld == null){
                visitorInfo.setCloudFlag(EquipmentInfoCloudFlag.NO_ASY.getCode());
                visitorInfo.setCreateTime(new Date());
                visitorInfo.setUpdateTime(new Date());
                this.save(visitorInfo);
                return visitorInfo;
            }
        }else{
            visitorInfoOld = this.baseMapper.selectById(visitorInfo.getId());
        }

        CopyUtils.copyProperties(visitorInfo,visitorInfoOld);
        visitorInfoOld.setCloudFlag(EquipmentInfoCloudFlag.NO_ASY.getCode());
        visitorInfoOld.setUpdateTime(new Date());
        this.updateById(visitorInfoOld);

        QueryWrapper<VisitorEquipment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VisitorEquipment::getVisitorId,visitorInfo.getId());
        List<VisitorEquipment> visitorEquipments = visitorEquipmentService.getBaseMapper().selectList(queryWrapper);
        visitorEquipments.forEach(visitorEquipment ->{
            visitorEquipmentService.saveVisitorEquipment(visitorEquipment.getVisitorId(),visitorEquipment.getEquipmentId());
        });
        return visitorInfoOld;
    }

    /**
     * 删除设备访客
     * @param visitorIds
     */
    @Override
    @Transactional
    public void deleteVisitors(Set<String> visitorIds){
        List<EquipmentInfo> equipments = equipmentInfoService.findEquipmentInfosByStatus(EquipmentInfoStatus.ACTIVE.getCode());
        equipments.forEach(equipment ->{
            IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(equipment.getSn());
            if(MyFunction.m_FaceRecognition.ZBX_Connected(cameraPoint) != 1){
                throw new FebsException("设备未全部连接,请先在首页刷新设备列表");
            }
            visitorEquipmentService.deleteVisitorEquipments(visitorIds,equipment.getId());
        });

        QueryWrapper<VisitorInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(VisitorInfo::getId,visitorIds);
        List<VisitorInfo> visitorInfos = this.getBaseMapper().selectList(queryWrapper);
        visitorInfos.forEach(visitorInfo ->{
            try {
                File imgFile = new File(visitorInfo.getFaceFilePath());
                imgFile.delete();
            }catch (Exception e){
                e.printStackTrace();
            }
            this.getBaseMapper().deleteById(visitorInfo.getId());
        });

    }

    @Override
    @Transactional
    public void deleteAllVisitors(){
        CustomPage<VisitorInfo> page = new CustomPage<>(1, 200);
        QueryWrapper<VisitorInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(VisitorInfo::getId);


        while (true){
            IPage<VisitorInfo> visitorInfos = this.getBaseMapper().selectPage(page,queryWrapper);

            if(visitorInfos.getRecords().size() <= 0){
                break;
            }

            Set<String> visitorIds = new HashSet<>();
            visitorInfos.getRecords().forEach(visitorInfo ->{
                visitorIds.add(visitorInfo.getId());
            });

            deleteVisitors(visitorIds);
        }

    }
}
