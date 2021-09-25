package com.handturn.bole.main.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.DESToolUtil;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.equipment.entity.EquipmentInOut;
import com.handturn.bole.main.equipment.entity.EquipmentInfo;
import com.handturn.bole.main.equipment.entity.EquipmentInfoErectStatus;
import com.handturn.bole.main.equipment.entity.EquipmentInfoOnlineStatus;
import com.handturn.bole.main.equipment.mapper.EquipmentInfoMapper;
import com.handturn.bole.main.equipment.service.IEquipmentInfoService;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.sysconf.service.SysconfCodeRuleContant;
import com.handturn.bole.websocket.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 基础资料-设备管理 Service实现
 *
 * @author Eric
 * @date 2020-09-11 08:20:12
 */
@Service("EquipmentInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EquipmentInfoServiceImpl extends ServiceImpl<EquipmentInfoMapper, EquipmentInfo> implements IEquipmentInfoService {

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;

    @Override
    public ICustomPage<EquipmentInfo> findEquipmentInfos(QueryRequest request, EquipmentInfo equipmentInfo) {
        CustomPage<EquipmentInfo> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, equipmentInfo);
    }

    @Override
    @Transactional
    public EquipmentInfo saveEquipmentInfo(EquipmentInfo equipmentInfo) {
        if(equipmentInfo.getId() == null){
            checkByUk(equipmentInfo);

            //编码生成
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.ZNMJ_EQUIPMENTINFO_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);
            equipmentInfo.setEquipmentCode(code);

            equipmentInfo.setErectStatus(EquipmentInfoErectStatus.UNERECT);
            equipmentInfo.setOnlineStatus(EquipmentInfoOnlineStatus.OFFLINE);
            this.save(equipmentInfo);
            return equipmentInfo;
        }else{
            checkByUk(equipmentInfo);

            EquipmentInfo equipmentInfoOld = this.baseMapper.selectById(equipmentInfo.getId());
            CopyUtils.copyProperties(equipmentInfo,equipmentInfoOld);
            this.updateById(equipmentInfoOld);
            return equipmentInfoOld;
        }
    }

    /**
     * 安装
     *
     * @param equipmentInfo equipmentInfo
     */
    @Override
    @Transactional
    public EquipmentInfo erect(EquipmentInfo equipmentInfo){
        EquipmentInfo equipmentInfoOld = this.baseMapper.selectById(equipmentInfo.getId());
        if(equipmentInfoOld == null){
            throw new FebsException("设备不存在!");
        }

        CopyUtils.copyProperties(equipmentInfo,equipmentInfoOld);
        equipmentInfoOld.setErectStatus(EquipmentInfoErectStatus.ERECTED);
        this.updateById(equipmentInfoOld);
        return equipmentInfoOld;
    }

    /**
     * 拆卸
     * @param equipmentInfoIds equipmentInfoIds
     */
    @Override
    @Transactional
    public void down(String[] equipmentInfoIds){
        Arrays.stream(equipmentInfoIds).forEach(equipmentInfoId -> {
            EquipmentInfo equipmentInfo = this.baseMapper.selectById(equipmentInfoId);
            equipmentInfo.setSeqNum("99");
            equipmentInfo.setAreaCode("");
            equipmentInfo.setInOutFlag(EquipmentInOut.UNDEFIND);
            equipmentInfo.setErectStatus(EquipmentInfoErectStatus.UNERECT);
            //equipmentInfo.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(equipmentInfo);
        });
	}

    /**
     * 远程开门
     *
     * @param equipmentInfoIds equipmentInfoIds
     */
    @Override
    public void remoteOpen(String[] equipmentInfoIds){
        QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EquipmentInfo::getId,equipmentInfoIds);
        List<EquipmentInfo> equipmentInfos = this.getBaseMapper().selectList(queryWrapper);

        Set<String> lineOffSn = new HashSet<>();
        equipmentInfos.forEach(equipmentInfo ->{
            if(equipmentInfo.getOnlineStatus().equals(EquipmentInfoOnlineStatus.ONLINE)){
                boolean flag = webSocketServer.remoteOpen(equipmentInfo.getEquipmentMdCode(),equipmentInfo.getOcCode());
                if(!flag){
                    lineOffSn.add(equipmentInfo.getEquipmentMdCode());
                }
            }else{
                lineOffSn.add(equipmentInfo.getEquipmentMdCode());
            }
        });
        if(lineOffSn.size() > 0){
            throw new FebsException("所选设备处于离线状态!");
        }
    }

    @Override
    public EquipmentInfo findEquipmentInfoById(Long equipmentInfoId){
        return this.baseMapper.selectOne(new QueryWrapper<EquipmentInfo>().lambda().eq(EquipmentInfo::getId, equipmentInfoId));
    }

    /**
     * 通过mac获取信息
     * @param mac
     * @return
     */
    @Override
    public EquipmentInfo findEquipmentInfoByMac(String mac,String ocCode){
        QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EquipmentInfo::getEquipmentMdCode,mac);
        queryWrapper.lambda().eq(EquipmentInfo::getOcCode,ocCode);
        return this.baseMapper.selectOne(queryWrapper);
    }


    /**
     * 通过mac获取信息
     * @param macs
     * @return
     */
    @Override
    public List<EquipmentInfo> findEquipmentInfoByMacs(Set<String> macs, String ocCode){
        QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EquipmentInfo::getEquipmentMdCode,macs);
        queryWrapper.lambda().eq(EquipmentInfo::getOcCode,ocCode);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public String findEquipmentAuthCodeByEquipmentIds(String[] equipmentInfoIdArr){
        QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EquipmentInfo::getId,equipmentInfoIdArr);
        List<EquipmentInfo> equipmentInfos = this.baseMapper.selectList(queryWrapper);

        StringBuffer result = new StringBuffer();
        equipmentInfos.forEach(equipmentInfo -> {
            try {
                String encryptStr = DESToolUtil.encrypt(equipmentInfo.getEquipmentMdCode(),DESToolUtil.KEY_);
                result.append(encryptStr).append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result.toString();
    }

    public void checkByUk(EquipmentInfo equipmentInfo){
        QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<EquipmentInfo>();
        queryWrapper.lambda().eq(EquipmentInfo::getEquipmentMdCode, equipmentInfo.getEquipmentMdCode());
        if(equipmentInfo.getId() != null && equipmentInfo.getId() != 0){
            queryWrapper.lambda().ne(EquipmentInfo::getId, equipmentInfo.getId());
        }

        EquipmentInfo temp = this.baseMapper.selectOne(queryWrapper);

        if(temp != null){
            throw new FebsException("记录已经存在!请确认!");
        }
    }
}
