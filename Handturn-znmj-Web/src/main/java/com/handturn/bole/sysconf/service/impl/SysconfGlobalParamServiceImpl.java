package com.handturn.bole.sysconf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.entity.BaseBoolean;
import com.handturn.bole.common.entity.BaseStatus;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.constant.SysconfGlobalConstant;
import com.handturn.bole.sysconf.entity.SysconfGlobalGroup;
import com.handturn.bole.sysconf.entity.SysconfGlobalParam;
import com.handturn.bole.sysconf.mapper.SysconfGlobalParamMapper;
import com.handturn.bole.sysconf.service.ISysconfGlobalGroupService;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 系统-系统配置参数 Service实现
 *
 * @author Eric
 * @date 2019-12-18 20:31:06
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysconfGlobalParamServiceImpl extends ServiceImpl<SysconfGlobalParamMapper, SysconfGlobalParam> implements ISysconfGlobalParamService {

    @Autowired
    private ISysconfGlobalGroupService sysconfGlobalGroupService;

    @Override
    public ICustomPage<SysconfGlobalParam> findSysconfGlobalParams(QueryRequest request, SysconfGlobalParam sysconfGlobalParam) {
        CustomPage<SysconfGlobalParam> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysconfGlobalParam);
    }

    @Override
    @Transactional
    public SysconfGlobalParam saveSysconfGlobalParam(SysconfGlobalParam sysconfGlobalParam) {
        if(sysconfGlobalParam.getId() == null){
            SysconfGlobalGroup sysconfGlobalGroup = sysconfGlobalGroupService.findSysconfGlobalGroupByGroupCode(sysconfGlobalParam.getGroupCode());
            if(sysconfGlobalGroup == null){
                throw new FebsException("全局配置未找到!");
            }
            sysconfGlobalParam.setGroupName(sysconfGlobalGroup.getGroupName());
            sysconfGlobalParam.setStatus(BaseStatus.ENABLED);
            this.save(sysconfGlobalParam);
            return sysconfGlobalParam;
        }else{
            SysconfGlobalParam sysconfGlobalParamOld = this.baseMapper.selectById(sysconfGlobalParam.getId());
            CopyUtils.copyProperties(sysconfGlobalParam,sysconfGlobalParamOld);
            this.updateById(sysconfGlobalParamOld);
            return sysconfGlobalParamOld;
        }
    }


    /**
     * 同步
     *
     * @param sysconfGlobalParamIds sysconfGlobalParamIds
     */
    @Override
    @Transactional
    public void synchroSysconfGlobalParam(String[] sysconfGlobalParamIds){
        Arrays.stream(sysconfGlobalParamIds).forEach(sysconfGlobalParamId -> {
            SysconfGlobalParam sysconfGlobalParam = this.baseMapper.selectById(sysconfGlobalParamId);
            SysconfGlobalParam sysconfGlobalParamNew = new SysconfGlobalParam();
            CopyUtils.copyProperties(sysconfGlobalParam,sysconfGlobalParamNew);

            sysconfGlobalParamNew.setId(null);
            sysconfGlobalParamNew.setIsSysCreated(BaseBoolean.FALSE);
            sysconfGlobalParamNew.setOrgCode(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());
            sysconfGlobalParamNew.setOcCode(UserInfoHolder.getUserInfo().getCurrentOc().getOcCode());

            sysconfGlobalParamNew.setStatus(BaseStatus.ENABLED);
            this.baseMapper.insert(sysconfGlobalParamNew);
        });
    }

    @Override
    @Transactional
    public void enableSysconfGlobalParam(String[] sysconfGlobalParamIds) {
        Arrays.stream(sysconfGlobalParamIds).forEach(sysconfGlobalParamId -> {
            SysconfGlobalParam sysconfGlobalParam = this.baseMapper.selectById(sysconfGlobalParamId);
            sysconfGlobalParam.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysconfGlobalParam);
        });
	}

    @Override
    @Transactional
    public void disableSysconfGlobalParam(String[] sysconfGlobalParamIds) {
        Arrays.stream(sysconfGlobalParamIds).forEach(sysconfGlobalParamId -> {
            SysconfGlobalParam sysconfGlobalParam = this.baseMapper.selectById(sysconfGlobalParamId);
            sysconfGlobalParam.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysconfGlobalParam);
        });
    }

    @Override
    public SysconfGlobalParam findSysconfGlobalParamById(Long sysconfGlobalParamId){
        return this.baseMapper.selectOne(new QueryWrapper<SysconfGlobalParam>().lambda().eq(SysconfGlobalParam::getId, sysconfGlobalParamId));
    }


    /**
     * 根据不同系统获取配置值
     * @param groupCode
     * @return
     */
    public String getParamValueWithDiffSystem(String groupCode,String ocCode,String orgCode){
        String system = "";
        if(System.getProperty("os.name").toLowerCase().indexOf(SysconfGlobalConstant.TempletFilePathKey.linux) >= 0){  //linux系统运行
            system = SysconfGlobalConstant.TempletFilePathKey.linux;
        }else {                                 //window
            system = SysconfGlobalConstant.TempletFilePathKey.window;
        }
        String paramValue = getParamValueByGroupCode(groupCode,system,ocCode,orgCode);
        return paramValue;
    }


    /**
     * 获取配置文件
     * @param groupCode
     * @param paramKey
     * @return
     */
    public String getParamValueByGroupCode(String groupCode,String paramKey,String ocCode,String orgCode){
        QueryWrapper<SysconfGlobalParam> queryWrapper = new QueryWrapper<SysconfGlobalParam>();
        queryWrapper.lambda().eq(SysconfGlobalParam::getGroupCode,groupCode)
                .eq(SysconfGlobalParam::getParamKey,paramKey);
        if(!StringUtils.isEmpty(ocCode)){
            queryWrapper.lambda().eq(SysconfGlobalParam::getOcCode,ocCode);
        }
        if(!StringUtils.isEmpty(orgCode)){
            queryWrapper.lambda().eq(SysconfGlobalParam::getOrgCode,orgCode);
        }

        List<SysconfGlobalParam> confGlobalParams = this.baseMapper.selectList(queryWrapper);
        if(confGlobalParams == null || confGlobalParams.size() <= 0){

            QueryWrapper<SysconfGlobalParam> queryWrapper1 = new QueryWrapper<SysconfGlobalParam>();
            queryWrapper1.lambda().eq(SysconfGlobalParam::getGroupCode,groupCode)
                    .eq(SysconfGlobalParam::getParamKey,paramKey);
            confGlobalParams = this.baseMapper.selectList(queryWrapper1);
            return confGlobalParams == null || confGlobalParams.size() <= 0 ? "":confGlobalParams.get(0).getParamValue();

        }else{
            return confGlobalParams == null || confGlobalParams.size() <= 0 ? "":confGlobalParams.get(0).getParamValue();
        }
    }

    /**
     * 获取配置文件
     * @param groupCode
     * @param paramKey
     * @return
     */
    public String getParamValueByGroupCodeAndOrg(String groupCode,String paramKey,String ocCode,String orgCode){
        QueryWrapper<SysconfGlobalParam> queryWrapper = new QueryWrapper<SysconfGlobalParam>();
        queryWrapper.lambda().eq(SysconfGlobalParam::getGroupCode,groupCode)
                .eq(SysconfGlobalParam::getParamKey,paramKey)
                .eq(SysconfGlobalParam::getOcCode,ocCode)
                .eq(SysconfGlobalParam::getOrgCode,orgCode);
        List<SysconfGlobalParam> confGlobalParams = this.baseMapper.selectList(queryWrapper);
        if(confGlobalParams == null || confGlobalParams.size() <= 0){
            return confGlobalParams == null || confGlobalParams.size() <= 0 ? "":confGlobalParams.get(0).getParamValue();
        }else{
            return confGlobalParams == null || confGlobalParams.size() <= 0 ? "":confGlobalParams.get(0).getParamValue();
        }
    }

    /**
     * 获取配置文件
     * @param groupCode
     * @param paramKey
     * @return
     */
    public SysconfGlobalParam getParamByGroupCodeByOrg(String groupCode,String paramKey,String ocCode,String orgCode){
        QueryWrapper<SysconfGlobalParam> queryWrapper = new QueryWrapper<SysconfGlobalParam>();
        queryWrapper.lambda().eq(SysconfGlobalParam::getGroupCode,groupCode)
                .eq(SysconfGlobalParam::getParamKey,paramKey)
                .eq(SysconfGlobalParam::getOcCode,ocCode)
                .eq(SysconfGlobalParam::getOrgCode,orgCode);
        List<SysconfGlobalParam> confGlobalParams = this.baseMapper.selectList(queryWrapper);
        if(confGlobalParams == null || confGlobalParams.size() <= 0){
            return confGlobalParams == null || confGlobalParams.size() <= 0 ? null :confGlobalParams.get(0);
        }else{
            return confGlobalParams == null || confGlobalParams.size() <= 0 ? null :confGlobalParams.get(0);
        }
    }

}
