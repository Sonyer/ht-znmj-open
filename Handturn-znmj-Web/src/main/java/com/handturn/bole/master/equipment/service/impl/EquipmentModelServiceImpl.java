package com.handturn.bole.master.equipment.service.impl;

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
import com.handturn.bole.master.equipment.entity.EquipmentModel;
import com.handturn.bole.master.equipment.mapper.EquipmentModelMapper;
import com.handturn.bole.master.equipment.service.IEquipmentModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 基础资料-设备型号管理 Service实现
 *
 * @author Eric
 * @date 2020-09-11 08:20:08
 */
@Service("EquipmentModelService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EquipmentModelServiceImpl extends ServiceImpl<EquipmentModelMapper, EquipmentModel> implements IEquipmentModelService {

    @Override
    public ICustomPage<EquipmentModel> findEquipmentModels(QueryRequest request, EquipmentModel equipmentModel) {
        CustomPage<EquipmentModel> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, equipmentModel);
    }

    @Override
    @Transactional
    public EquipmentModel saveEquipmentModel(EquipmentModel equipmentModel) {
        if(equipmentModel.getId() == null){
            checkByUk(equipmentModel);

            equipmentModel.setStatus(BaseStatus.ENABLED);
            this.save(equipmentModel);
            return equipmentModel;
        }else{
            checkByUk(equipmentModel);

            EquipmentModel equipmentModelOld = this.baseMapper.selectById(equipmentModel.getId());
            CopyUtils.copyProperties(equipmentModel,equipmentModelOld);
            this.updateById(equipmentModelOld);
            return equipmentModelOld;
        }
    }

    @Override
    @Transactional
    public void enableEquipmentModel(String[] equipmentModelIds) {
        Arrays.stream(equipmentModelIds).forEach(equipmentModelId -> {
            EquipmentModel equipmentModel = this.baseMapper.selectById(equipmentModelId);
            equipmentModel.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(equipmentModel);
        });
	}

    @Override
    @Transactional
    public void disableEquipmentModel(String[] equipmentModelIds) {
        Arrays.stream(equipmentModelIds).forEach(equipmentModelId -> {
            EquipmentModel equipmentModel = this.baseMapper.selectById(equipmentModelId);
            equipmentModel.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(equipmentModel);
        });
    }

    @Override
    public EquipmentModel findEquipmentModelById(Long equipmentModelId){
        return this.baseMapper.selectOne(new QueryWrapper<EquipmentModel>().lambda().eq(EquipmentModel::getId, equipmentModelId));
    }

    public void checkByUk(EquipmentModel equipmentModel){
        QueryWrapper<EquipmentModel> queryWrapper = new QueryWrapper<EquipmentModel>();
        queryWrapper.lambda().eq(EquipmentModel::getEquipmentModelCode, equipmentModel.getEquipmentModelCode());
        if(equipmentModel.getId() != null && equipmentModel.getId() != 0){
            queryWrapper.lambda().ne(EquipmentModel::getId, equipmentModel.getId());
        }

        EquipmentModel temp = this.baseMapper.selectOne(queryWrapper);

        if(temp != null){
            throw new FebsException("记录已经存在!请确认!");
        }
    }

    /**
     * @param statuses
     * @return
     */
    public List<OptionVo> findOptionVo(Set<String> statuses){
        QueryWrapper<EquipmentModel> queryWrapper = new QueryWrapper<EquipmentModel>();
        if(statuses != null && statuses.size() > 0){
            queryWrapper.lambda().in(EquipmentModel::getStatus,statuses);
        }

        List<EquipmentModel> equipmentModels = this.baseMapper.selectList(queryWrapper);

        List<OptionVo> result = new ArrayList<>();
        equipmentModels.forEach(equipmentModel ->{
            OptionVo optionVo = new OptionVo();
            optionVo.setValue(equipmentModel.getEquipmentModelCode());
            optionVo.setText(equipmentModel.getEquipmentModelCode()+"-"+equipmentModel.getEquipmentModelName());
            result.add(optionVo);
        });
        return result;
    }
}
