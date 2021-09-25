package com.handturn.bole.main.authArea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.authArea.entity.OcAuthAreaAuthType;
import com.handturn.bole.main.authArea.entity.OcAuthAreaEquipment;
import com.handturn.bole.main.authArea.entity.OcAuthAreaLog;
import com.handturn.bole.main.authArea.entity.OcAuthAreaVisitor;
import com.handturn.bole.main.authArea.mapper.OcAuthAreaEquipmentMapper;
import com.handturn.bole.main.authArea.service.IOcAuthAreaEquipmentService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaLogService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaVisitorService;
import com.handturn.bole.main.equipment.entity.EquipmentInfo;
import com.handturn.bole.main.equipment.service.IEquipmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 网点-设备区域权限 Service实现
 *
 * @author Eric
 * @date 2020-09-22 08:58:18
 */
@Service("OcAuthAreaEquipmentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OcAuthAreaEquipmentServiceImpl extends ServiceImpl<OcAuthAreaEquipmentMapper, OcAuthAreaEquipment> implements IOcAuthAreaEquipmentService {

    @Autowired
    private IOcAuthAreaVisitorService ocAuthAreaVisitorService;

    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    @Autowired
    private IOcAuthAreaLogService ocAuthAreaLogService;

    @Override
    public ICustomPage<OcAuthAreaEquipment> findOcAuthAreaEquipments(QueryRequest request, OcAuthAreaEquipment ocAuthAreaEquipment) {
        CustomPage<OcAuthAreaEquipment> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, ocAuthAreaEquipment);
    }

    @Override
    @Transactional
    public OcAuthAreaEquipment saveOcAuthAreaEquipment(OcAuthAreaEquipment ocAuthAreaEquipment) {
        if(ocAuthAreaEquipment.getId() == null){
            this.save(ocAuthAreaEquipment);
            return ocAuthAreaEquipment;
        }else{
            OcAuthAreaEquipment ocAuthAreaEquipmentOld = this.baseMapper.selectById(ocAuthAreaEquipment.getId());
            CopyUtils.copyProperties(ocAuthAreaEquipment,ocAuthAreaEquipmentOld);
            this.updateById(ocAuthAreaEquipmentOld);
            return ocAuthAreaEquipmentOld;
        }
    }

    @Override
    @Transactional
    public void enableOcAuthAreaEquipment(String[] ocAuthAreaEquipmentIds) {
        Arrays.stream(ocAuthAreaEquipmentIds).forEach(ocAuthAreaEquipmentId -> {
            OcAuthAreaEquipment ocAuthAreaEquipment = this.baseMapper.selectById(ocAuthAreaEquipmentId);
            //ocAuthAreaEquipment.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(ocAuthAreaEquipment);
        });
	}

    @Override
    @Transactional
    public void disableOcAuthAreaEquipment(String[] ocAuthAreaEquipmentIds) {
        Arrays.stream(ocAuthAreaEquipmentIds).forEach(ocAuthAreaEquipmentId -> {
            OcAuthAreaEquipment ocAuthAreaEquipment = this.baseMapper.selectById(ocAuthAreaEquipmentId);
            //ocAuthAreaEquipment.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(ocAuthAreaEquipment);
        });
    }

    @Override
    public OcAuthAreaEquipment findOcAuthAreaEquipmentById(Long ocAuthAreaEquipmentId){
        return this.baseMapper.selectOne(new QueryWrapper<OcAuthAreaEquipment>().lambda().eq(OcAuthAreaEquipment::getId, ocAuthAreaEquipmentId));
    }

    @Override
    public List<OcAuthAreaEquipment> findOcAuthAreaEquipmentByEquipmentIds(Set<String> equipmentIds){
        QueryWrapper<OcAuthAreaEquipment> queryWrapper = new QueryWrapper<OcAuthAreaEquipment>();
        queryWrapper.lambda().in(OcAuthAreaEquipment::getEquipmentId,equipmentIds);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<OcAuthAreaEquipment> findOcAuthAreaEquipmentByAreaIds(Set<String> authAreaIds){
        QueryWrapper<OcAuthAreaEquipment> queryWrapper = new QueryWrapper<OcAuthAreaEquipment>();
        queryWrapper.lambda().in(OcAuthAreaEquipment::getAuthAreaId,authAreaIds);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 授权
     * @param oauthAreaId
     * @param ocEquipmentIdsArr
     */
    @Override
    @Transactional
    public void authOcEquipments(String oauthAreaId,String[] ocEquipmentIdsArr,String orgCode,String ocCode,String orgName,String ocName){
        Set<String> oahthAreaIds = new HashSet<>();
        oahthAreaIds.add(oauthAreaId);
        List<OcAuthAreaVisitor>  ocAuthAreaVisitors = ocAuthAreaVisitorService.findAuthAreaIdByAuthAreaIds(oahthAreaIds);

        Arrays.stream(ocEquipmentIdsArr).forEach(ocEquipmentId ->{
            EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(Long.valueOf(ocEquipmentId));
            if(equipmentInfo != null){
                QueryWrapper<OcAuthAreaEquipment> queryWrapper = new QueryWrapper<OcAuthAreaEquipment>();
                queryWrapper.lambda().eq(OcAuthAreaEquipment::getAuthAreaId,oauthAreaId);
                queryWrapper.lambda().eq(OcAuthAreaEquipment::getEquipmentId,ocEquipmentId);
                queryWrapper.lambda().eq(OcAuthAreaEquipment::getOrgCode,orgCode);
                queryWrapper.lambda().eq(OcAuthAreaEquipment::getOcCode,ocCode);
                List<OcAuthAreaEquipment> equipments = this.baseMapper.selectList(queryWrapper);
                if(equipments == null || equipments.size() <= 0){
                    OcAuthAreaEquipment ocAuthAreaEquipment = new OcAuthAreaEquipment();
                    ocAuthAreaEquipment.setAuthAreaId(oauthAreaId);
                    ocAuthAreaEquipment.setEquipmentId(ocEquipmentId);
                    ocAuthAreaEquipment.setOrgCode(orgCode);
                    ocAuthAreaEquipment.setOrgName(orgName);
                    ocAuthAreaEquipment.setOcCode(ocCode);
                    ocAuthAreaEquipment.setOcName(ocName);
                    this.baseMapper.insert(ocAuthAreaEquipment);

                    ocAuthAreaVisitors.forEach(ocAuthAreaVisitor ->{
                        OcAuthAreaLog ocAuthAreaLog = new OcAuthAreaLog();
                        ocAuthAreaLog.setAuthType(OcAuthAreaAuthType.AUTHTYPE_ADD);
                        ocAuthAreaLog.setEquipmentId(Long.valueOf(ocEquipmentId));
                        ocAuthAreaLog.setEquipmentMac(equipmentInfo.getEquipmentMdCode());
                        ocAuthAreaLog.setOcVisitorId(ocAuthAreaVisitor.getOcVisitorId());
                        ocAuthAreaLog.setOcCode(ocAuthAreaVisitor.getOcCode());
                        ocAuthAreaLog.setOcName(ocAuthAreaVisitor.getOcName());
                        ocAuthAreaLog.setOrgCode(ocAuthAreaVisitor.getOrgCode());
                        ocAuthAreaLog.setOrgName(ocAuthAreaVisitor.getOrgName());
                        ocAuthAreaLogService.getBaseMapper().insert(ocAuthAreaLog);
                    });
                }
            }
        });
    }

    /**
     * 取消授权
     * @param oauthAreaId
     * @param ocEquipmentIdsArr
     */
    @Override
    @Transactional
    public void cancelAuthOcEquipments(String oauthAreaId,String[] ocEquipmentIdsArr,String orgCode,String ocCode,String orgName,String ocName){
        Set<String> oahthAreaIds = new HashSet<>();
        oahthAreaIds.add(oauthAreaId);
        List<OcAuthAreaVisitor>  ocAuthAreaVisitors = ocAuthAreaVisitorService.findAuthAreaIdByAuthAreaIds(oahthAreaIds);

        Arrays.stream(ocEquipmentIdsArr).forEach(ocEquipmentId ->{
            EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(Long.valueOf(ocEquipmentId));
            ocAuthAreaVisitors.forEach(ocAuthAreaVisitor ->{
                OcAuthAreaLog ocAuthAreaLog = new OcAuthAreaLog();
                ocAuthAreaLog.setAuthType(OcAuthAreaAuthType.AUTHTYPE_DELETE);
                ocAuthAreaLog.setEquipmentId(Long.valueOf(ocEquipmentId));
                ocAuthAreaLog.setEquipmentMac(equipmentInfo.getEquipmentMdCode());
                ocAuthAreaLog.setOcVisitorId(ocAuthAreaVisitor.getOcVisitorId());
                ocAuthAreaLog.setOcCode(ocAuthAreaVisitor.getOcCode());
                ocAuthAreaLog.setOcName(ocAuthAreaVisitor.getOcName());
                ocAuthAreaLog.setOrgCode(ocAuthAreaVisitor.getOrgCode());
                ocAuthAreaLog.setOrgName(ocAuthAreaVisitor.getOrgName());
                ocAuthAreaLogService.getBaseMapper().insert(ocAuthAreaLog);
            });

            QueryWrapper<OcAuthAreaEquipment> queryWrapper = new QueryWrapper<OcAuthAreaEquipment>();
            queryWrapper.lambda().eq(OcAuthAreaEquipment::getAuthAreaId,oauthAreaId);
            queryWrapper.lambda().eq(OcAuthAreaEquipment::getEquipmentId,ocEquipmentId);
            queryWrapper.lambda().eq(OcAuthAreaEquipment::getOrgCode,orgCode);
            queryWrapper.lambda().eq(OcAuthAreaEquipment::getOcCode,ocCode);
            this.baseMapper.delete(queryWrapper);
        });
    }
}
