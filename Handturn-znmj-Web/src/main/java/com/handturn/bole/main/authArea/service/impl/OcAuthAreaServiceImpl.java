package com.handturn.bole.main.authArea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.area.entity.AreaInfo;
import com.handturn.bole.main.authArea.entity.OcAuthArea;
import com.handturn.bole.main.authArea.mapper.OcAuthAreaMapper;
import com.handturn.bole.main.authArea.service.IOcAuthAreaService;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.sysconf.service.SysconfCodeRuleContant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 网点-权限区域 Service实现
 *
 * @author Eric
 * @date 2020-09-22 08:57:47
 */
@Service("OcAuthAreaService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OcAuthAreaServiceImpl extends ServiceImpl<OcAuthAreaMapper, OcAuthArea> implements IOcAuthAreaService {

    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;

    @Override
    public ICustomPage<OcAuthArea> findOcAuthAreas(QueryRequest request, OcAuthArea ocAuthArea) {
        CustomPage<OcAuthArea> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, ocAuthArea);
    }

    @Override
    @Transactional
    public OcAuthArea saveOcAuthArea(OcAuthArea ocAuthArea) {
        if(ocAuthArea.getId() == null){
            checkByUk(ocAuthArea);

            //编码生成
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.ZNMJ_AUTH_AREA_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);
            ocAuthArea.setAreaCode(code);

            this.save(ocAuthArea);
            return ocAuthArea;
        }else{
            checkByUk(ocAuthArea);

            OcAuthArea ocAuthAreaOld = this.baseMapper.selectById(ocAuthArea.getId());
            CopyUtils.copyProperties(ocAuthArea,ocAuthAreaOld);
            this.updateById(ocAuthAreaOld);
            return ocAuthAreaOld;
        }
    }

    @Override
    @Transactional
    public void enableOcAuthArea(String[] ocAuthAreaIds) {
        Arrays.stream(ocAuthAreaIds).forEach(ocAuthAreaId -> {
            OcAuthArea ocAuthArea = this.baseMapper.selectById(ocAuthAreaId);
            ocAuthArea.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(ocAuthArea);
        });
	}

    @Override
    @Transactional
    public void disableOcAuthArea(String[] ocAuthAreaIds) {
        Arrays.stream(ocAuthAreaIds).forEach(ocAuthAreaId -> {
            OcAuthArea ocAuthArea = this.baseMapper.selectById(ocAuthAreaId);
            ocAuthArea.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(ocAuthArea);
        });
    }

    @Override
    public OcAuthArea findOcAuthAreaById(Long ocAuthAreaId){
        return this.baseMapper.selectOne(new QueryWrapper<OcAuthArea>().lambda().eq(OcAuthArea::getId, ocAuthAreaId));
    }

    /**
     * 通过Id获取信息
     * @param ocAuthAreaCode
     * @return
     */
    @Override
    public OcAuthArea findOcAuthAreaByCode(String ocAuthAreaCode,String orgCode,String ocCode){
        QueryWrapper<OcAuthArea> queryWrapper = new QueryWrapper<OcAuthArea>();
        queryWrapper.lambda().eq(OcAuthArea::getAreaCode,ocAuthAreaCode);
        queryWrapper.lambda().eq(OcAuthArea::getOrgCode,orgCode);
        queryWrapper.lambda().eq(OcAuthArea::getOcCode,ocCode);
        return this.baseMapper.selectOne(queryWrapper);
    }

    public void checkByUk(OcAuthArea areaInfo){
        QueryWrapper<OcAuthArea> queryWrapper = new QueryWrapper<OcAuthArea>();
        queryWrapper.lambda().eq(OcAuthArea::getAreaName, areaInfo.getAreaName());
        queryWrapper.lambda().eq(OcAuthArea::getOrgCode, areaInfo.getOrgCode());
        queryWrapper.lambda().eq(OcAuthArea::getOcCode, areaInfo.getOcCode());
        if(areaInfo.getId() != null && areaInfo.getId() != 0){
            queryWrapper.lambda().ne(OcAuthArea::getId, areaInfo.getId());
        }

        OcAuthArea temp = this.baseMapper.selectOne(queryWrapper);

        if(temp != null){
            throw new FebsException("记录已经存在!请确认!");
        }
    }

    /**
     * 前端分页查询
     * @param searchValue
     * @param authAreaIds
     * @return
     */
    @Override
    public List<OcAuthArea> authAreaPageQuery(Integer pageIndex, String searchValue, Set<Long> authAreaIds){
        CustomPage<OcAuthArea> publishPage = new CustomPage<>(Integer.valueOf(pageIndex), 10);
        return this.baseMapper.authAreaPageQuery(publishPage,searchValue,authAreaIds);
    }

    /**
     * 前端分页查询
     * @param authAreaCode
     * @param orgCode
     * @param ocCode
     * @return
     */
    @Override
    public OcAuthArea findByAuthAreaCode(String authAreaCode,String orgCode,String ocCode){
        QueryWrapper<OcAuthArea> queryWrapper = new QueryWrapper<OcAuthArea>();
        queryWrapper.lambda().eq(OcAuthArea::getAreaCode, authAreaCode);
        queryWrapper.lambda().eq(OcAuthArea::getOrgCode, orgCode);
        queryWrapper.lambda().eq(OcAuthArea::getOcCode, ocCode);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
