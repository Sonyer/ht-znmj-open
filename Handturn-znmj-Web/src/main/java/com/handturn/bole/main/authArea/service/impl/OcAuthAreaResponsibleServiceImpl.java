package com.handturn.bole.main.authArea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.main.authArea.entity.OcAuthAreaResponsible;
import com.handturn.bole.main.authArea.mapper.OcAuthAreaResponsibleMapper;
import com.handturn.bole.main.authArea.service.IOcAuthAreaResponsibleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 网点-访客区域责任人 Service实现
 *
 * @author Eric
 * @date 2020-09-30 08:32:35
 */
@Service("OcAuthAreaResponsibleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OcAuthAreaResponsibleServiceImpl extends ServiceImpl<OcAuthAreaResponsibleMapper, OcAuthAreaResponsible> implements IOcAuthAreaResponsibleService {

    @Override
    public ICustomPage<OcAuthAreaResponsible> findOcAuthAreaResponsibles(QueryRequest request, OcAuthAreaResponsible ocAuthAreaResponsible) {
        CustomPage<OcAuthAreaResponsible> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, ocAuthAreaResponsible);
    }

    @Override
    @Transactional
    public OcAuthAreaResponsible saveOcAuthAreaResponsible(OcAuthAreaResponsible ocAuthAreaResponsible) {
        if(ocAuthAreaResponsible.getId() == null){
            checkByUk(ocAuthAreaResponsible);

            this.save(ocAuthAreaResponsible);
            return ocAuthAreaResponsible;
        }else{
            checkByUk(ocAuthAreaResponsible);

            OcAuthAreaResponsible ocAuthAreaResponsibleOld = this.baseMapper.selectById(ocAuthAreaResponsible.getId());
            CopyUtils.copyProperties(ocAuthAreaResponsible,ocAuthAreaResponsibleOld);
            this.updateById(ocAuthAreaResponsibleOld);
            return ocAuthAreaResponsibleOld;
        }
    }

    @Override
    @Transactional
    public void enableOcAuthAreaResponsible(String[] ocAuthAreaResponsibleIds) {
        Arrays.stream(ocAuthAreaResponsibleIds).forEach(ocAuthAreaResponsibleId -> {
            OcAuthAreaResponsible ocAuthAreaResponsible = this.baseMapper.selectById(ocAuthAreaResponsibleId);
            //ocAuthAreaResponsible.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(ocAuthAreaResponsible);
        });
	}

    @Override
    @Transactional
    public void disableOcAuthAreaResponsible(String[] ocAuthAreaResponsibleIds) {
        Arrays.stream(ocAuthAreaResponsibleIds).forEach(ocAuthAreaResponsibleId -> {
            OcAuthAreaResponsible ocAuthAreaResponsible = this.baseMapper.selectById(ocAuthAreaResponsibleId);
            //ocAuthAreaResponsible.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(ocAuthAreaResponsible);
        });
    }

    @Override
    public OcAuthAreaResponsible findOcAuthAreaResponsibleById(Long ocAuthAreaResponsibleId){
        return this.baseMapper.selectOne(new QueryWrapper<OcAuthAreaResponsible>().lambda().eq(OcAuthAreaResponsible::getId, ocAuthAreaResponsibleId));
    }

    /**
     * 移除责任人
     */
    @Override
    @Transactional
    public void deleteResponsible(String[] responsibleIdsArr,String orgCode,String ocCode){
        QueryWrapper<OcAuthAreaResponsible> queryWrapper = new QueryWrapper<OcAuthAreaResponsible>();
        queryWrapper.lambda().in(OcAuthAreaResponsible::getId, responsibleIdsArr);
        queryWrapper.lambda().eq(OcAuthAreaResponsible::getOrgCode, orgCode);
        queryWrapper.lambda().eq(OcAuthAreaResponsible::getOcCode, ocCode);
        this.baseMapper.delete(queryWrapper);
    }

    public void checkByUk(OcAuthAreaResponsible ocAuthAreaResponsible){
        QueryWrapper<OcAuthAreaResponsible> queryWrapper = new QueryWrapper<OcAuthAreaResponsible>();
        queryWrapper.lambda().eq(OcAuthAreaResponsible::getAuthAreaId, ocAuthAreaResponsible.getAuthAreaId());
        queryWrapper.lambda().eq(OcAuthAreaResponsible::getUserId, ocAuthAreaResponsible.getUserId());
        queryWrapper.lambda().eq(OcAuthAreaResponsible::getOrgCode, ocAuthAreaResponsible.getOrgCode());
        queryWrapper.lambda().eq(OcAuthAreaResponsible::getOcCode, ocAuthAreaResponsible.getOcCode());
        if(ocAuthAreaResponsible.getId() != null && ocAuthAreaResponsible.getId() != 0){
            queryWrapper.lambda().ne(OcAuthAreaResponsible::getId, ocAuthAreaResponsible.getId());
        }

        OcAuthAreaResponsible temp = this.baseMapper.selectOne(queryWrapper);

        if(temp != null){
            throw new FebsException("记录已经存在!请确认!");
        }
    }
}
