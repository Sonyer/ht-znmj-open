package com.ht.znmj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ht.znmj.entity.CloudInterfaceInfo;
import com.ht.znmj.mapper.CloudInterfaceInfoMapper;
import com.ht.znmj.service.ICloudInterfaceInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 设备信息 Service实现
 *
 * @author Eric
 * @date 2020-01-29 14:34:06
 */
@Service("CloudInterfaceInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CloudInterfaceInfoServiceImpl extends ServiceImpl<CloudInterfaceInfoMapper, CloudInterfaceInfo> implements ICloudInterfaceInfoService {

    /**
     * 通过ID查询
     */
    @Override
    public CloudInterfaceInfo findCloudInterfaceInfoById(String id){
        QueryWrapper<CloudInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CloudInterfaceInfo::getId,id);
        return this.baseMapper.selectOne(queryWrapper);
    }


    /**
     * 保存设备信息
     * @param cloudInterfaceInfo
     */
    @Override
    @Transactional
    public CloudInterfaceInfo saveCloudInterfaceInfo(CloudInterfaceInfo cloudInterfaceInfo){
        if(cloudInterfaceInfo.getId() == null){
            cloudInterfaceInfo.setCreateTime(new Date());
            cloudInterfaceInfo.setUpdateTime(new Date());
            this.save(cloudInterfaceInfo);
            return cloudInterfaceInfo;
        }else{
            cloudInterfaceInfo.setUpdateTime(new Date());
            this.updateById(cloudInterfaceInfo);
            return cloudInterfaceInfo;
        }
    }

}
