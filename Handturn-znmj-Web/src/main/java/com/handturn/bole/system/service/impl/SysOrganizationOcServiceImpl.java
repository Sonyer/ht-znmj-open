package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.*;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleService;
import com.handturn.bole.sysconf.service.SysconfCodeRuleContant;
import com.handturn.bole.system.entity.*;
import com.handturn.bole.system.mapper.SysOrganizationOcMapper;
import com.handturn.bole.system.service.ISysOrganizationDepService;
import com.handturn.bole.system.service.ISysOrganizationOcService;
import com.handturn.bole.system.service.ISysOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统-公司操作运营中心 Service实现
 *
 * @author MrBird
 * @date 2019-12-08 19:36:28
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysOrganizationOcServiceImpl extends ServiceImpl<SysOrganizationOcMapper, SysOrganizationOc> implements ISysOrganizationOcService {

    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;
    @Autowired
    private ISysOrganizationService sysOrganizationService;
    @Autowired
    private ISysOrganizationDepService sysOrganizationDepService;

    @Override
    public ICustomPage<SysOrganizationOc> findSysOrganizationOcs(QueryRequest request, SysOrganizationOc sysOrganizationOc) {
        if(UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_CN)
           ||UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.SYS)
            ||UserInfoHolder.getUserInfo().getCurrentOc().getOcType().equals(OcType.ORG_OC)){
            sysOrganizationOc.setOrgId(UserInfoHolder.getUserInfo().getCurrentOrg().getId());
        }else{
            sysOrganizationOc.setId(UserInfoHolder.getUserInfo().getCurrentOc().getId());
        }
        CustomPage<SysOrganizationOc> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysOrganizationOc);
    }

    @Override
    @Transactional
    public SysOrganizationOc saveSysOrganizationOc(SysOrganizationOc sysOrganizationOc,boolean isSystem) {
        if(sysOrganizationOc.getId() == null){
            //编码生成
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.SYS_ORGANIZATION_OC_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);
            sysOrganizationOc.setOcCode(code);
            sysOrganizationOc.setStatus(BaseStatus.ENABLED);
            this.save(sysOrganizationOc);
            if(!isSystem){
                saveRelateOtherInfo(sysOrganizationOc);
            }
            return sysOrganizationOc;
        }else{
            SysOrganizationOc sysOrganizationOcOld = this.baseMapper.selectById(sysOrganizationOc.getId());
            if(!isSystem && (sysOrganizationOcOld.getOcType().equals(OcType.SYS) || sysOrganizationOcOld.getOcType().equals(OcType.ORG_CN))){
                throw new FebsException("不允许对系统数据进行调整!");
            }
            CopyUtils.copyProperties(sysOrganizationOc,sysOrganizationOcOld);
            this.updateById(sysOrganizationOcOld);
            return sysOrganizationOcOld;
        }
    }

    @Override
    @Transactional
    public SysOrganizationOc saveSysOrganizationOcNoneExtend(SysOrganizationOc sysOrganizationOc,boolean isSystem) {
        if(sysOrganizationOc.getId() == null){
            //编码生成
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.SYS_ORGANIZATION_OC_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);
            sysOrganizationOc.setOcCode(code);
            sysOrganizationOc.setStatus(BaseStatus.ENABLED);
            this.save(sysOrganizationOc);
            return sysOrganizationOc;
        }else{
            SysOrganizationOc sysOrganizationOcOld = this.baseMapper.selectById(sysOrganizationOc.getId());
            if(!isSystem && (sysOrganizationOcOld.getOcType().equals(OcType.SYS) || sysOrganizationOcOld.getOcType().equals(OcType.ORG_CN))){
                throw new FebsException("不允许对系统数据进行调整!");
            }
            CopyUtils.copyProperties(sysOrganizationOc,sysOrganizationOcOld);
            this.updateById(sysOrganizationOcOld);
            return sysOrganizationOcOld;
        }
    }

    private void saveRelateOtherInfo(SysOrganizationOc sysOrganizationOc){
        //保存运营部门
        SysOrganizationDep sysOrganizationDep = new SysOrganizationDep();
        sysOrganizationDep.setDepName(sysOrganizationOc.getOcName());
        sysOrganizationDep.setDepShortName(sysOrganizationOc.getOcShortName());
        sysOrganizationDep.setOrgId(sysOrganizationOc.getOrgId());
        sysOrganizationDep.setOcId(sysOrganizationOc.getId());
        sysOrganizationDep.setAddress(sysOrganizationOc.getAddress());
        sysOrganizationDep.setAttentionTo(sysOrganizationOc.getAttentionTo());
        sysOrganizationDep.setCell(sysOrganizationOc.getCell());
        sysOrganizationDep.setCity(sysOrganizationOc.getCity());
        sysOrganizationDep.setCountry(sysOrganizationOc.getCountry());
        sysOrganizationDep.setEmail(sysOrganizationOc.getEmail());
        sysOrganizationDep.setFax(sysOrganizationOc.getFax());
        sysOrganizationDep.setPostCode(sysOrganizationOc.getPostCode());
        sysOrganizationDep.setProvince(sysOrganizationOc.getProvince());
        sysOrganizationDep.setRegion(sysOrganizationOc.getRegion());
        sysOrganizationDep.setTel(sysOrganizationOc.getTel());
        sysOrganizationDepService.saveSysOrganizationDep(sysOrganizationDep,false);
    }

    @Override
    @Transactional
    public void enableSysOrganizationOc(String[] sysOrganizationOcIds) {
        Arrays.stream(sysOrganizationOcIds).forEach(sysOrganizationOcId -> {
            SysOrganizationOc sysOrganizationOc = this.baseMapper.selectById(sysOrganizationOcId);
            sysOrganizationOc.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysOrganizationOc);
        });
	}

    @Override
    @Transactional
    public void disableSysOrganizationOc(String[] sysOrganizationOcIds) {
        Arrays.stream(sysOrganizationOcIds).forEach(sysOrganizationOcId -> {
            SysOrganizationOc sysOrganizationOc = this.baseMapper.selectById(sysOrganizationOcId);
            sysOrganizationOc.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysOrganizationOc);
        });
    }

    @Override
    public SysOrganizationOc findSysOrganizationOcById(Long sysOrganizationOcId){
        return this.baseMapper.selectOne(new QueryWrapper<SysOrganizationOc>().lambda().eq(SysOrganizationOc::getId, sysOrganizationOcId));
    }

    @Override
    public SysOrganizationOc findSysOrganizationOcByCode(String ocCode){
        return this.baseMapper.selectOne(new QueryWrapper<SysOrganizationOc>().lambda().eq(SysOrganizationOc::getOcCode, ocCode));
    }

    /**
     * 获取当前用户的组织选项
     * @param user
     * @param currentOrgId
     * @return
     */
    @Override
    public List<SysOrganizationOc> getOrgOcByCurrentUser(SysUser user, Long currentOrgId){
        SysOrganization org = sysOrganizationService.findSysOrganizationById(user.getOrgId());
        List<SysOrganizationOc> ocs = null;
        if(org.getOrgType().equals(OrgType.SYS)){
            ocs = this.baseMapper.selectList(new QueryWrapper<SysOrganizationOc>().lambda().eq(SysOrganizationOc::getOrgId,currentOrgId));
        }else{
            ocs = this.baseMapper.getOrgOcOptionVoByCurrentUser(String.valueOf(user.getId()), String.valueOf(currentOrgId));
        }
        return ocs;
    }

    /**
     * 获取当前用户的组织选项
     * @param user
     * @param currentOrgId
     * @return
     */
    @Override
    public List<OptionVo> getOrgOcOptionVoByCurrentUser(SysUser user, Long currentOrgId){
        SysOrganization org = sysOrganizationService.findSysOrganizationById(user.getOrgId());

        List<OptionVo> result = new ArrayList<OptionVo>();

        List<SysOrganizationOc> ocs = null;
        if(org.getOrgType().equals(OrgType.SYS)){
            ocs = this.baseMapper.selectList(new QueryWrapper<SysOrganizationOc>().lambda().eq(SysOrganizationOc::getOrgId,currentOrgId));
        }else {
            ocs = this.baseMapper.getOrgOcOptionVoByCurrentUser(String.valueOf(user.getId()), String.valueOf(currentOrgId));
        }

        for(SysOrganizationOc oc : ocs){
            OptionVo optionvo = new OptionVo();
            optionvo.setValue(oc.getId().toString());
            optionvo.setText(oc.getOcShortName());
            result.add(optionvo);
        }
        return result;
    }


    /**
     * 获取当前用户的组织选项
     * @return
     */
    @Override
    public List<OptionVo> getOrgOcKeyOptionVoByOcType(List<String> ocTypes){
        List<OptionVo> result = new ArrayList<OptionVo>();
        List<SysOrganizationOc> ocs = this.baseMapper.selectList(new QueryWrapper<SysOrganizationOc>().lambda().in(SysOrganizationOc::getOcType,ocTypes));

        ocs.forEach(oc ->{
            OptionVo optionvo = new OptionVo();
            optionvo.setValue(oc.getOcCode().toString());
            optionvo.setText(oc.getOcShortName());
            result.add(optionvo);
        });
        return result;
    }

    @Override
    public CommonTree<SysOrganizationOc> findSysOcByOrgId(Long orgId, Set<Long> ocIds){
        List<SysOrganizationOc> ocList = this.baseMapper.selectList(new QueryWrapper<SysOrganizationOc>().lambda()
                .eq(SysOrganizationOc::getOrgId, orgId));
        List<CommonTree<SysOrganizationOc>> trees = this.convertCommonTree(ocList,ocIds);
        return CommonTree.buildTree(trees,false,null);
    }

    @Override
    public CommonTree<SysOrganizationOc> findSysOcByOcId(Long ocId, Set<Long> ocIds){
        List<SysOrganizationOc> ocList = this.baseMapper.selectList(new QueryWrapper<SysOrganizationOc>().lambda()
                .eq(SysOrganizationOc::getId, ocId));
        List<CommonTree<SysOrganizationOc>> trees = this.convertCommonTree(ocList,ocIds);
        return CommonTree.buildTree(trees,false,null);
    }

    private List<CommonTree<SysOrganizationOc>> convertCommonTree(List<SysOrganizationOc> sysOcs, Set<Long> permOcIds) {
        List<CommonTree<SysOrganizationOc>> trees = new ArrayList<>();

        sysOcs.forEach(sysOc -> {
            CommonTree<SysOrganizationOc> tree = new CommonTree<>();
            tree.setId(String.valueOf(sysOc.getId()));
            tree.setParentId(String.valueOf(0));
            tree.setTitle(sysOc.getOcShortName());
            tree.setData(sysOc);
            if(permOcIds.contains(sysOc.getId())){
                tree.setChecked(true);
            }
            trees.add(tree);
        });
        return trees;
    }

    @Override
    public List<SysOrganizationOc> findClientByOcType(String ocType,Long orgId){
        QueryWrapper<SysOrganizationOc> queryWrapper = new QueryWrapper<SysOrganizationOc>();
        queryWrapper.lambda().eq(SysOrganizationOc::getOcType,ocType);
        queryWrapper.lambda().eq(SysOrganizationOc::getOrgId,orgId);
        List<SysOrganizationOc> ocList = this.baseMapper.selectList(queryWrapper);
        return ocList;
    }

    /**
     * 通过类型查询当前组织的网点
     * @param oc
     * @return
     */
    @Override
    public List<OptionVo> findOptionVoByClient(SysOrganizationOc oc){
        QueryWrapper<SysOrganizationOc> queryWrapper = new QueryWrapper<SysOrganizationOc>();
        if(oc.getOrgId() != null && oc.getOrgId() != 0){
            queryWrapper.lambda().eq(SysOrganizationOc::getOrgId,oc.getOrgId());
        }

        queryWrapper.lambda().eq(SysOrganizationOc::getOcType,OcType.ORG_3TH);

        List<OptionVo> result = new ArrayList<OptionVo>();
        List<SysOrganizationOc> clients = this.baseMapper.selectList(queryWrapper);
        clients.forEach(client ->{
            OptionVo optionVo = new OptionVo();
            optionVo.setValue(client.getOcCode());
            optionVo.setText(client.getOcName());
            result.add(optionVo);
        });
        return result;
    }

    /**
     * 通过类型查询网点
     * @param ocTypes
     * @return
     */
    @Override
    public List<OptionVo> findOptionVoByOcType(Set<String> ocTypes){
        QueryWrapper<SysOrganizationOc> queryWrapper = new QueryWrapper<SysOrganizationOc>();
        if(ocTypes != null && ocTypes.size() > 0){
            queryWrapper.lambda().in(SysOrganizationOc::getOcType,ocTypes);
        }
        List<OptionVo> result = new ArrayList<OptionVo>();
        List<SysOrganizationOc> clients = this.baseMapper.selectList(queryWrapper);
        clients.forEach(client ->{
            OptionVo optionVo = new OptionVo();
            optionVo.setValue(client.getOcCode());
            optionVo.setText(client.getOcName());
            result.add(optionVo);
        });
        return result;
    }

    /**
     * 通过类型查询网点
     * @param ocTypes
     * @return
     */
    @Override
    public List<OptionVo> findOptionVoByOcType(Set<String> ocTypes,Long orgId){
        QueryWrapper<SysOrganizationOc> queryWrapper = new QueryWrapper<SysOrganizationOc>();
        queryWrapper.lambda().eq(SysOrganizationOc::getOrgId,orgId);
        if(ocTypes != null && ocTypes.size() > 0){
            queryWrapper.lambda().in(SysOrganizationOc::getOcType,ocTypes);
        }
        List<OptionVo> result = new ArrayList<OptionVo>();
        List<SysOrganizationOc> clients = this.baseMapper.selectList(queryWrapper);
        clients.forEach(client ->{
            OptionVo optionVo = new OptionVo();
            optionVo.setValue(client.getOcCode());
            optionVo.setText(client.getOcName());
            result.add(optionVo);
        });
        return result;
    }

    /**
     * 通过客户查询仓储
     * @param clientCode
     * @return
     */
    @Override
    public List<OptionVo> findWarehouseOptionVoByClient(String clientCode){
        List<SysOrganizationOc> warehouses = this.baseMapper.getWarehouseOptionVoByClient(clientCode);
        List<OptionVo> result = new ArrayList<OptionVo>();
        warehouses.forEach(warehouse ->{
            OptionVo optionVo = new OptionVo();
            optionVo.setValue(warehouse.getOcCode());
            optionVo.setText(warehouse.getOcName());
            result.add(optionVo);
        });
        return result;
    }

    /**
     * 通过类型查询当前组织的网点
     * @param warehouseCode
     * @return
     */
    /**
     * 通过客户查询仓储
     * @param warehouseCode
     * @return
     */
    @Override
    public List<OptionVo> findClientOptionVoByWarehouse(String warehouseCode){
        List<SysOrganizationOc> clients = this.baseMapper.getClientOptionVoByWarehouse(warehouseCode);
        List<OptionVo> result = new ArrayList<OptionVo>();
        clients.forEach(client ->{
            OptionVo optionVo = new OptionVo();
            optionVo.setValue(client.getOcCode());
            optionVo.setText(client.getOcName());
            result.add(optionVo);
        });
        return result;

    }

    /**
     * 通过客户查询仓储
     * @param clientCode
     * @return
     */
    @Override
    public Map<String,SysOrganizationOc> findWarehouseByClient(String clientCode){
        List<SysOrganizationOc> warehouses = this.baseMapper.getWarehouseOptionVoByClient(clientCode);
        Map<String,SysOrganizationOc>  result = new HashMap<>();
        warehouses.forEach(warehouse ->{
            result.put(warehouse.getOcCode(),warehouse);
        });
        return result;
    }

    /**
     * 通过类型查询当前组织的网点
     * @param warehouseCode
     * @return
     */
    @Override
    public Map<String,SysOrganizationOc> findClientByWarehouse(String warehouseCode){
        List<SysOrganizationOc> clients = this.baseMapper.getClientOptionVoByWarehouse(warehouseCode);
        Map<String,SysOrganizationOc>  result = new HashMap<>();
        clients.forEach(client ->{
            result.put(client.getOcCode(),client);
        });
        return result;
    }
}
