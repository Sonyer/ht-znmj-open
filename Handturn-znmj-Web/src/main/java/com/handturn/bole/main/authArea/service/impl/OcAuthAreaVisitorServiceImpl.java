package com.handturn.bole.main.authArea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.authArea.entity.OcAuthAreaAuthType;
import com.handturn.bole.main.authArea.entity.OcAuthAreaEquipment;
import com.handturn.bole.main.authArea.entity.OcAuthAreaLog;
import com.handturn.bole.main.authArea.entity.OcAuthAreaVisitor;
import com.handturn.bole.main.authArea.mapper.OcAuthAreaVisitorMapper;
import com.handturn.bole.main.authArea.service.IOcAuthAreaEquipmentService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaLogService;
import com.handturn.bole.main.authArea.service.IOcAuthAreaVisitorService;
import com.handturn.bole.main.equipment.entity.EquipmentInfo;
import com.handturn.bole.main.equipment.service.IEquipmentInfoService;
import com.handturn.bole.main.visitor.entity.OcVisitorInfo;
import com.handturn.bole.main.visitor.service.IOcVisitorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 网点-访客区域权限 Service实现
 *
 * @author Eric
 * @date 2020-09-22 08:58:06
 */
@Service("OcAuthAreaVisitorService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OcAuthAreaVisitorServiceImpl extends ServiceImpl<OcAuthAreaVisitorMapper, OcAuthAreaVisitor> implements IOcAuthAreaVisitorService {

    @Autowired
    private IOcVisitorInfoService ocVisitorInfoService;
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IOcAuthAreaEquipmentService ocAuthAreaEquipmentService;

    @Autowired
    private IOcAuthAreaLogService ocAuthAreaLogService;

    @Override
    public ICustomPage<OcAuthAreaVisitor> findOcAuthAreaVisitors(QueryRequest request, OcAuthAreaVisitor ocAuthAreaVisitor) {
        CustomPage<OcAuthAreaVisitor> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, ocAuthAreaVisitor);
    }

    @Override
    @Transactional
    public OcAuthAreaVisitor saveOcAuthAreaVisitor(OcAuthAreaVisitor ocAuthAreaVisitor) {
        if(ocAuthAreaVisitor.getId() == null){
            this.save(ocAuthAreaVisitor);
            return ocAuthAreaVisitor;
        }else{
            OcAuthAreaVisitor ocAuthAreaVisitorOld = this.baseMapper.selectById(ocAuthAreaVisitor.getId());
            CopyUtils.copyProperties(ocAuthAreaVisitor,ocAuthAreaVisitorOld);
            this.updateById(ocAuthAreaVisitorOld);
            return ocAuthAreaVisitorOld;
        }
    }

    @Override
    @Transactional
    public void enableOcAuthAreaVisitor(String[] ocAuthAreaVisitorIds) {
        Arrays.stream(ocAuthAreaVisitorIds).forEach(ocAuthAreaVisitorId -> {
            OcAuthAreaVisitor ocAuthAreaVisitor = this.baseMapper.selectById(ocAuthAreaVisitorId);
            ocAuthAreaVisitor.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(ocAuthAreaVisitor);
        });
	}

    @Override
    @Transactional
    public void disableOcAuthAreaVisitor(String[] ocAuthAreaVisitorIds) {
        Arrays.stream(ocAuthAreaVisitorIds).forEach(ocAuthAreaVisitorId -> {
            OcAuthAreaVisitor ocAuthAreaVisitor = this.baseMapper.selectById(ocAuthAreaVisitorId);
            ocAuthAreaVisitor.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(ocAuthAreaVisitor);
        });
    }

    @Override
    public OcAuthAreaVisitor findOcAuthAreaVisitorById(Long ocAuthAreaVisitorId){
        return this.baseMapper.selectOne(new QueryWrapper<OcAuthAreaVisitor>().lambda().eq(OcAuthAreaVisitor::getId, ocAuthAreaVisitorId));
    }

    /**
     * 授权日志
     * @param ocVisitorId
     */
    @Override
    @Transactional
    public void updateAuthOcVisitors(String ocVisitorId,String orgCode,String ocCode,String orgName,String ocName){
        OcVisitorInfo visitorInfo = ocVisitorInfoService.findOcVisitorInfoById(Long.valueOf(ocVisitorId));

        QueryWrapper<OcAuthAreaVisitor> queryWrapper = new QueryWrapper<OcAuthAreaVisitor>();
        queryWrapper.lambda().eq(OcAuthAreaVisitor::getOcVisitorId, ocVisitorId);
        queryWrapper.lambda().eq(OcAuthAreaVisitor::getOrgCode, orgCode);
        queryWrapper.lambda().eq(OcAuthAreaVisitor::getOcCode, ocCode);
        List<OcAuthAreaVisitor> areaVisitors = this.baseMapper.selectList(queryWrapper);
        if (areaVisitors != null && areaVisitors.size() > 0) {
            Set<String> areaIds = new HashSet<>();
            areaVisitors.forEach(areaVisitor ->{
                areaIds.add(areaVisitor.getAuthAreaId().toString());
            });

            if(areaIds != null && areaIds.size() > 0){
                List<OcAuthAreaEquipment> areaEquipments = ocAuthAreaEquipmentService.findOcAuthAreaEquipmentByAreaIds(areaIds);

                areaEquipments.forEach(areaEquipment ->{
                    EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(Long.valueOf(areaEquipment.getEquipmentId()));
                    if(equipmentInfo != null) {
                        OcAuthAreaLog ocAuthAreaLog = new OcAuthAreaLog();
                        ocAuthAreaLog.setAuthType(OcAuthAreaAuthType.AUTHTYPE_UPDATE);
                        ocAuthAreaLog.setEquipmentId(Long.valueOf(equipmentInfo.getId()));
                        ocAuthAreaLog.setEquipmentMac(equipmentInfo.getEquipmentMdCode());
                        ocAuthAreaLog.setOcVisitorId(visitorInfo.getId());
                        ocAuthAreaLog.setOcCode(visitorInfo.getOcCode());
                        ocAuthAreaLog.setOcName(visitorInfo.getOcName());
                        ocAuthAreaLog.setOrgCode(visitorInfo.getOrgCode());
                        ocAuthAreaLog.setOrgName(visitorInfo.getOrgName());
                        ocAuthAreaLogService.getBaseMapper().insert(ocAuthAreaLog);
                    }
                });
            }

        }
    }

    /**
     * 授权
     * @param oauthAreaId
     * @param ocVisitorIdsArr
     */
    @Override
    @Transactional
    public void authOcVisitors(String oauthAreaId,String[] ocVisitorIdsArr,String orgCode,String ocCode,String orgName,String ocName){
        Set<String> oauthAreaIds = new HashSet<>();
        oauthAreaIds.add(oauthAreaId);
        List<OcAuthAreaEquipment> areaEquipments = ocAuthAreaEquipmentService.findOcAuthAreaEquipmentByAreaIds(oauthAreaIds);

        Arrays.stream(ocVisitorIdsArr).forEach(ocVisitorId ->{
            OcVisitorInfo visitorInfo = ocVisitorInfoService.findOcVisitorInfoById(Long.valueOf(ocVisitorId));

            if(visitorInfo != null) {
                QueryWrapper<OcAuthAreaVisitor> queryWrapper = new QueryWrapper<OcAuthAreaVisitor>();
                queryWrapper.lambda().eq(OcAuthAreaVisitor::getAuthAreaId, oauthAreaId);
                queryWrapper.lambda().eq(OcAuthAreaVisitor::getOcVisitorId, ocVisitorId);
                queryWrapper.lambda().eq(OcAuthAreaVisitor::getOrgCode, orgCode);
                queryWrapper.lambda().eq(OcAuthAreaVisitor::getOcCode, ocCode);
                List<OcAuthAreaVisitor> equipments = this.baseMapper.selectList(queryWrapper);
                if (equipments == null || equipments.size() <= 0) {
                    OcAuthAreaVisitor ocAuthAreaVisitor = new OcAuthAreaVisitor();
                    ocAuthAreaVisitor.setAuthAreaId(Long.valueOf(oauthAreaId));
                    ocAuthAreaVisitor.setOcVisitorId(Long.valueOf(ocVisitorId));
                    ocAuthAreaVisitor.setOrgCode(orgCode);
                    ocAuthAreaVisitor.setOrgName(orgName);
                    ocAuthAreaVisitor.setOcCode(ocCode);
                    ocAuthAreaVisitor.setOcName(ocName);
                    this.baseMapper.insert(ocAuthAreaVisitor);

                    areaEquipments.forEach(areaEquipment ->{
                        EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(Long.valueOf(areaEquipment.getEquipmentId()));
                        if(equipmentInfo != null) {

                            OcAuthAreaLog ocAuthAreaLog = new OcAuthAreaLog();
                            ocAuthAreaLog.setAuthType(OcAuthAreaAuthType.AUTHTYPE_ADD);
                            ocAuthAreaLog.setEquipmentId(Long.valueOf(equipmentInfo.getId()));
                            ocAuthAreaLog.setEquipmentMac(equipmentInfo.getEquipmentMdCode());
                            ocAuthAreaLog.setOcVisitorId(visitorInfo.getId());
                            ocAuthAreaLog.setOcCode(visitorInfo.getOcCode());
                            ocAuthAreaLog.setOcName(visitorInfo.getOcName());
                            ocAuthAreaLog.setOrgCode(visitorInfo.getOrgCode());
                            ocAuthAreaLog.setOrgName(visitorInfo.getOrgName());
                            ocAuthAreaLogService.getBaseMapper().insert(ocAuthAreaLog);
                        }
                    });
                }
            }
        });
    }

    /**
     * 授权
     * @param ocAuthAreaIdsArr
     * @param visitorId
     */
    @Override
    @Transactional
    public void authOcVisitors(String[] ocAuthAreaIdsArr,String visitorId,String orgCode,String ocCode,String orgName,String ocName){
        Set<String> oauthAreaIds = new HashSet<>(Arrays.asList(ocAuthAreaIdsArr));
        List<OcAuthAreaEquipment> areaEquipments = ocAuthAreaEquipmentService.findOcAuthAreaEquipmentByAreaIds(oauthAreaIds);

        OcVisitorInfo visitorInfo = ocVisitorInfoService.findOcVisitorInfoById(Long.valueOf(visitorId));

        Arrays.stream(ocAuthAreaIdsArr).forEach(authAreaId ->{
            QueryWrapper<OcAuthAreaVisitor> queryWrapper = new QueryWrapper<OcAuthAreaVisitor>();
            queryWrapper.lambda().eq(OcAuthAreaVisitor::getAuthAreaId,authAreaId);
            queryWrapper.lambda().eq(OcAuthAreaVisitor::getOcVisitorId,visitorId);
            queryWrapper.lambda().eq(OcAuthAreaVisitor::getOrgCode,orgCode);
            queryWrapper.lambda().eq(OcAuthAreaVisitor::getOcCode,ocCode);
            List<OcAuthAreaVisitor> equipments = this.baseMapper.selectList(queryWrapper);
            if(equipments == null || equipments.size() <= 0){
                OcAuthAreaVisitor ocAuthAreaVisitor = new OcAuthAreaVisitor();
                ocAuthAreaVisitor.setAuthAreaId(Long.valueOf(authAreaId));
                ocAuthAreaVisitor.setOcVisitorId(Long.valueOf(visitorId));
                ocAuthAreaVisitor.setOrgCode(orgCode);
                ocAuthAreaVisitor.setOrgName(orgName);
                ocAuthAreaVisitor.setOcCode(ocCode);
                ocAuthAreaVisitor.setOcName(ocName);
                this.baseMapper.insert(ocAuthAreaVisitor);

                areaEquipments.forEach(areaEquipment ->{
                    EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(Long.valueOf(areaEquipment.getEquipmentId()));
                    if(equipmentInfo != null) {

                        OcAuthAreaLog ocAuthAreaLog = new OcAuthAreaLog();
                        ocAuthAreaLog.setAuthType(OcAuthAreaAuthType.AUTHTYPE_ADD);
                        ocAuthAreaLog.setEquipmentId(Long.valueOf(equipmentInfo.getId()));
                        ocAuthAreaLog.setEquipmentMac(equipmentInfo.getEquipmentMdCode());
                        ocAuthAreaLog.setOcVisitorId(visitorInfo.getId());
                        ocAuthAreaLog.setOcCode(visitorInfo.getOcCode());
                        ocAuthAreaLog.setOcName(visitorInfo.getOcName());
                        ocAuthAreaLog.setOrgCode(visitorInfo.getOrgCode());
                        ocAuthAreaLog.setOrgName(visitorInfo.getOrgName());
                        ocAuthAreaLogService.getBaseMapper().insert(ocAuthAreaLog);
                    }
                });
            }
        });
    }

    /**
     * 取消授权
     * @param oauthAreaId
     * @param ocVisitorIdsArr
     */
    @Override
    @Transactional
    public void cancelAuthOcVisitors(String oauthAreaId,String[] ocVisitorIdsArr,String orgCode,String ocCode,String orgName,String ocName){
        Set<String> oauthAreaIds = new HashSet<>();
        oauthAreaIds.add(oauthAreaId);
        List<OcAuthAreaEquipment> areaEquipments = ocAuthAreaEquipmentService.findOcAuthAreaEquipmentByAreaIds(oauthAreaIds);

        Arrays.stream(ocVisitorIdsArr).forEach(ocVisitorId ->{
            OcVisitorInfo visitorInfo = ocVisitorInfoService.findOcVisitorInfoById(Long.valueOf(ocVisitorId));
            if(visitorInfo != null) {
                areaEquipments.forEach(areaEquipment -> {
                    EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(Long.valueOf(areaEquipment.getEquipmentId()));
                    if(equipmentInfo != null) {
                        OcAuthAreaLog ocAuthAreaLog = new OcAuthAreaLog();
                        ocAuthAreaLog.setAuthType(OcAuthAreaAuthType.AUTHTYPE_DELETE);
                        ocAuthAreaLog.setEquipmentId(Long.valueOf(equipmentInfo.getId()));
                        ocAuthAreaLog.setEquipmentMac(equipmentInfo.getEquipmentMdCode());
                        ocAuthAreaLog.setOcVisitorId(visitorInfo.getId());
                        ocAuthAreaLog.setOcCode(visitorInfo.getOcCode());
                        ocAuthAreaLog.setOcName(visitorInfo.getOcName());
                        ocAuthAreaLog.setOrgCode(visitorInfo.getOrgCode());
                        ocAuthAreaLog.setOrgName(visitorInfo.getOrgName());
                        ocAuthAreaLogService.getBaseMapper().insert(ocAuthAreaLog);
                    }
                });

                QueryWrapper<OcAuthAreaVisitor> queryWrapper = new QueryWrapper<OcAuthAreaVisitor>();
                queryWrapper.lambda().eq(OcAuthAreaVisitor::getAuthAreaId, oauthAreaId);
                queryWrapper.lambda().eq(OcAuthAreaVisitor::getOcVisitorId, ocVisitorId);
                queryWrapper.lambda().eq(OcAuthAreaVisitor::getOrgCode, orgCode);
                queryWrapper.lambda().eq(OcAuthAreaVisitor::getOcCode, ocCode);
                this.baseMapper.delete(queryWrapper);
            }
        });
    }

    /**
     * 取消授权
     * @param ocAuthAreaIdsArr
     * @param visitorId
     */
    /**
     * 取消授权
     * @param ocAuthAreaIdsArr
     * @param visitorId
     */
    @Override
    @Transactional
    public void cancelAuthOcVisitors(String[] ocAuthAreaIdsArr,String visitorId,String orgCode,String ocCode,String orgName,String ocName){
        Set<String> oauthAreaIds = new HashSet<>(Arrays.asList(ocAuthAreaIdsArr));
        List<OcAuthAreaEquipment> areaEquipments = ocAuthAreaEquipmentService.findOcAuthAreaEquipmentByAreaIds(oauthAreaIds);

        OcVisitorInfo visitorInfo = ocVisitorInfoService.findOcVisitorInfoById(Long.valueOf(visitorId));

        Arrays.stream(ocAuthAreaIdsArr).forEach(ocAuthAreaId ->{
            areaEquipments.forEach(areaEquipment ->{
                EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(Long.valueOf(areaEquipment.getEquipmentId()));
                if(equipmentInfo != null) {
                    OcAuthAreaLog ocAuthAreaLog = new OcAuthAreaLog();
                    ocAuthAreaLog.setAuthType(OcAuthAreaAuthType.AUTHTYPE_DELETE);
                    ocAuthAreaLog.setEquipmentId(Long.valueOf(equipmentInfo.getId()));
                    ocAuthAreaLog.setEquipmentMac(equipmentInfo.getEquipmentMdCode());
                    ocAuthAreaLog.setOcVisitorId(visitorInfo.getId());
                    ocAuthAreaLog.setOcCode(visitorInfo.getOcCode());
                    ocAuthAreaLog.setOcName(visitorInfo.getOcName());
                    ocAuthAreaLog.setOrgCode(visitorInfo.getOrgCode());
                    ocAuthAreaLog.setOrgName(visitorInfo.getOrgName());
                    ocAuthAreaLogService.getBaseMapper().insert(ocAuthAreaLog);
                }
            });

            QueryWrapper<OcAuthAreaVisitor> queryWrapper = new QueryWrapper<OcAuthAreaVisitor>();
            queryWrapper.lambda().eq(OcAuthAreaVisitor::getAuthAreaId,ocAuthAreaId);
            queryWrapper.lambda().eq(OcAuthAreaVisitor::getOcVisitorId,visitorId);
            queryWrapper.lambda().eq(OcAuthAreaVisitor::getOrgCode,orgCode);
            queryWrapper.lambda().eq(OcAuthAreaVisitor::getOcCode,ocCode);
            this.baseMapper.delete(queryWrapper);
        });
    }

    /**
     * 通过VisitorId获取所有访问授权
     * @param visitorIds
     */
    @Override
    @Transactional
    public Set<Long> findAuthAreaIdByVisitorIds(List<Long> visitorIds){
        QueryWrapper<OcAuthAreaVisitor> queryWrapper = new QueryWrapper<OcAuthAreaVisitor>();
        queryWrapper.lambda().in(OcAuthAreaVisitor::getOcVisitorId,visitorIds);
        queryWrapper.lambda().select(OcAuthAreaVisitor::getAuthAreaId);
        List<OcAuthAreaVisitor> areaVisitors = this.baseMapper.selectList(queryWrapper);

        Set<Long> result = new HashSet<>();
        areaVisitors.forEach(areaVisitor ->{
            result.add(areaVisitor.getAuthAreaId());
        });
        return result;
    }

    /**
     * 通过VisitorId获取所有访问授权
     * @param authAreaIds
     */
    @Override
    public List<OcAuthAreaVisitor> findAuthAreaIdByAuthAreaIds(Set<String> authAreaIds){
        QueryWrapper<OcAuthAreaVisitor> queryWrapper = new QueryWrapper<OcAuthAreaVisitor>();
        queryWrapper.lambda().in(OcAuthAreaVisitor::getAuthAreaId,authAreaIds);
        List<OcAuthAreaVisitor> areaVisitors = this.baseMapper.selectList(queryWrapper);
        return areaVisitors;
    }
}
