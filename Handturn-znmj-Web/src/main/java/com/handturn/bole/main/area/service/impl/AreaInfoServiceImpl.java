package com.handturn.bole.main.area.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.area.entity.AreaInfo;
import com.handturn.bole.main.area.mapper.AreaInfoMapper;
import com.handturn.bole.main.area.service.IAreaInfoService;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.sysconf.service.SysconfCodeRuleContant;
import com.handturn.bole.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 基础资料-区域管理 Service实现
 *
 * @author Eric
 * @date 2020-09-11 08:20:03
 */
@Service("AreaInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AreaInfoServiceImpl extends ServiceImpl<AreaInfoMapper, AreaInfo> implements IAreaInfoService {

    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;

    @Override
    public ICustomPage<AreaInfo> findAreaInfos(QueryRequest request, AreaInfo areaInfo) {
        CustomPage<AreaInfo> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, areaInfo);
    }

    @Override
    @Transactional
    public AreaInfo saveAreaInfo(AreaInfo areaInfo) {
        if(areaInfo.getId() == null){
            checkByUk(areaInfo);

            //编码生成
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.ZNMJ_AREAINFO_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);
            areaInfo.setAreaCode(code);

            areaInfo.setStatus(BaseStatus.ENABLED);
            this.save(areaInfo);
            return areaInfo;
        }else{
            checkByUk(areaInfo);

            AreaInfo areaInfoOld = this.baseMapper.selectById(areaInfo.getId());
            CopyUtils.copyProperties(areaInfo,areaInfoOld);
            this.updateById(areaInfoOld);
            return areaInfoOld;
        }
    }

    @Override
    @Transactional
    public void enableAreaInfo(String[] areaInfoIds) {
        Arrays.stream(areaInfoIds).forEach(areaInfoId -> {
            AreaInfo areaInfo = this.baseMapper.selectById(areaInfoId);
            areaInfo.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(areaInfo);
        });
	}

    @Override
    @Transactional
    public void disableAreaInfo(String[] areaInfoIds) {
        Arrays.stream(areaInfoIds).forEach(areaInfoId -> {
            AreaInfo areaInfo = this.baseMapper.selectById(areaInfoId);
            areaInfo.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(areaInfo);
        });
    }

    @Override
    public AreaInfo findAreaInfoById(Long areaInfoId){
        return this.baseMapper.selectOne(new QueryWrapper<AreaInfo>().lambda().eq(AreaInfo::getId, areaInfoId));
    }

    @Override
    public List<OptionVo> findOptionVo4IdKeyByOcCode(String orgCode,String ocCode){
        QueryWrapper<AreaInfo> queryWrapper = new QueryWrapper<AreaInfo>();
        queryWrapper.lambda().eq(AreaInfo::getOrgCode,orgCode);
        queryWrapper.lambda().eq(AreaInfo::getOcCode,ocCode);
        List<AreaInfo> areaInfos = this.baseMapper.selectList(queryWrapper);

        List<OptionVo> result = new ArrayList<OptionVo>();
        areaInfos.forEach(areaInfo ->{
            OptionVo optionVo = new OptionVo();
            optionVo.setValue(areaInfo.getId().toString());
            optionVo.setText(areaInfo.getAreaClientCode()+"-"+areaInfo.getAreaName());
            result.add(optionVo);
        });

        return result;
    }

    @Override
    public List<OptionVo> findOptionVo4CodeKeyByOcCode(String orgCode,String ocCode){
        QueryWrapper<AreaInfo> queryWrapper = new QueryWrapper<AreaInfo>();
        queryWrapper.lambda().eq(AreaInfo::getOrgCode,orgCode);
        queryWrapper.lambda().eq(AreaInfo::getOcCode,ocCode);
        List<AreaInfo> areaInfos = this.baseMapper.selectList(queryWrapper);

        List<OptionVo> result = new ArrayList<OptionVo>();
        areaInfos.forEach(areaInfo ->{
            OptionVo optionVo = new OptionVo();
            optionVo.setValue(areaInfo.getAreaCode().toString());
            optionVo.setText(areaInfo.getAreaClientCode()+"-"+areaInfo.getAreaName());
            result.add(optionVo);
        });

        return result;
    }

    @Override
    public Map<String,String> findMap4CodeKeyByOcCode(String ocCode){
        QueryWrapper<AreaInfo> queryWrapper = new QueryWrapper<AreaInfo>();
        queryWrapper.lambda().eq(AreaInfo::getOcCode,ocCode);
        List<AreaInfo> areaInfos = this.baseMapper.selectList(queryWrapper);

        Map<String,String> result = new HashMap<>();
        areaInfos.forEach(areaInfo ->{
            result.put(areaInfo.getAreaCode(),areaInfo.getAreaClientCode()+"-"+areaInfo.getAreaName());
        });

        return result;
    }

    public void checkByUk(AreaInfo areaInfo){
        QueryWrapper<AreaInfo> queryWrapper = new QueryWrapper<AreaInfo>();
        queryWrapper.lambda().eq(AreaInfo::getAreaClientCode, areaInfo.getAreaClientCode());
        queryWrapper.lambda().eq(AreaInfo::getOrgCode, areaInfo.getOrgCode());
        queryWrapper.lambda().eq(AreaInfo::getOcCode, areaInfo.getOcCode());
        if(areaInfo.getId() != null && areaInfo.getId() != 0){
            queryWrapper.lambda().ne(AreaInfo::getId, areaInfo.getId());
        }

        AreaInfo temp = this.baseMapper.selectOne(queryWrapper);

        if(temp != null){
            throw new FebsException("记录已经存在!请确认!");
        }
    }
}
