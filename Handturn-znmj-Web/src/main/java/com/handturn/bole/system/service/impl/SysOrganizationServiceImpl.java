package com.handturn.bole.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.common.customPage.CustomPage;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.*;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.CopyUtils;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.sysconf.constant.SysconfGlobalConstant;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.sysconf.service.ISysconfGlobalParamService;
import com.handturn.bole.sysconf.service.SysconfCodeRuleContant;
import com.handturn.bole.system.entity.*;
import com.handturn.bole.system.mapper.SysOrganizationMapper;
import com.handturn.bole.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 系统-组织 Service实现
 *
 * @author MrBird
 * @date 2019-12-08 10:00:07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysOrganizationServiceImpl extends ServiceImpl<SysOrganizationMapper, SysOrganization> implements ISysOrganizationService {

    @Autowired
    private ISysconfCodeRuleOptionService sysconfCodeRuleOptionService;


    @Autowired
    private ISysOrganizationOcService sysOrganizationOcService;

    @Autowired
    private ISysOrganizationDepService sysOrganizationDepService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleUserService sysRoleUserService;

    @Autowired
    private ISysRoleOcService sysRoleOcService;

    @Autowired
    private ISysconfGlobalParamService sysconfGlobalParamService;
    @Autowired
    private ISysRoleReportService sysRoleReportService;
    @Autowired
    private ISysRoleResourceService sysRoleResourceService;
    @Autowired
    private ISysOcResourceService sysOcResourceService;
    @Autowired
    private ISysOcReportService sysOcReportService;
    @Autowired
    private ISysGroupService sysGroupService;
    @Autowired
    private ISysGroupResourceService sysGroupResourceService;
    @Autowired
    private ISysGroupReportService sysGroupReportService;

    @Autowired
    private ISysOganizationResourceService sysOganizationResourceService;

    @Autowired
    private ISysOganizationReportService sysOganizationReportService;

    @Override
    public ICustomPage<SysOrganization> findSysOrganizations(QueryRequest request, SysOrganization sysOrganization) {
        CustomPage<SysOrganization> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        if(UserInfoHolder.getUserInfo().getCurrentOrg().getOrgType().equals(OrgType.SYS)){

        }else{
            sysOrganization.setId(UserInfoHolder.getUserInfo().getCurrentOrg().getId());
        }
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, sysOrganization);
    }

    /**
     * 按组织类型查询
     * @param orgType
     * @return
     */
    @Override
    public List <SysOrganization> findSysOrganizationsByOrgType(String orgType,Integer pageIndex){
        CustomPage<SysOrganization> page = new CustomPage<>(pageIndex, 200);
        QueryWrapper<SysOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysOrganization::getOrgType,orgType);

        page.setCurrent(pageIndex);
        page.setSize(200);
        page.setAsc(FebsUtil.camelToUnderscore("id"));
        List <SysOrganization> resultList = this.baseMapper.selectPage(page,queryWrapper).getRecords();
        return resultList;
    }

    @Override
    @Transactional
    public void saveSysOrganization(SysOrganization sysOrganization) {
        if(sysOrganization.getId() == null){
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.SYS_ORGANIZATION_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);
            sysOrganization.setOrgCode(code);
            sysOrganization.setOrgType(OrgType.ORG);
            sysOrganization.setStatus(BaseStatus.ENABLED);
            this.save(sysOrganization);
            saveRelateOtherInfo(sysOrganization);
        }else{
            SysOrganization sysOrganizationOld = this.baseMapper.selectById(sysOrganization.getId());
            CopyUtils.copyProperties(sysOrganization,sysOrganizationOld);
            this.updateById(sysOrganizationOld);
           /* saveRelateOtherInfo(sysOrganizationOld);*/
        }

    }

    /**
     * 修改 无其他信息
     *
     * @param sysOrganization sysOrganization
     */
    @Override
    @Transactional
    public void saveSysOrganizationNoneExtend(SysOrganization sysOrganization){
        if(sysOrganization.getId() == null){
            CodeRuleGenerateDealDTO codeRuleGenerateDealDTO = new CodeRuleGenerateDealDTO();
            codeRuleGenerateDealDTO.setRuleCode(SysconfCodeRuleContant.SYS_ORGANIZATION_CODE);
            codeRuleGenerateDealDTO.setParams(null);
            String code = sysconfCodeRuleOptionService.generateCodeMain(codeRuleGenerateDealDTO);
            sysOrganization.setOrgCode(code);
            sysOrganization.setOrgType(OrgType.ORG);
            sysOrganization.setStatus(BaseStatus.ENABLED);
            this.save(sysOrganization);
        }else{
            SysOrganization sysOrganizationOld = this.baseMapper.selectById(sysOrganization.getId());
            CopyUtils.copyProperties(sysOrganization,sysOrganizationOld);
            this.updateById(sysOrganizationOld);
        }
    }

    private void saveRelateOtherInfo(SysOrganization sysOrganization){
        //保存运营组织
        SysOrganizationOc sysOrganizationOc = null;
        if(sysOrganization.getRelateOcId() == null || sysOrganization.getRelateOcId() == 0){
            sysOrganizationOc = new SysOrganizationOc();
        }else{
            sysOrganizationOc = sysOrganizationOcService.findSysOrganizationOcById(sysOrganization.getRelateOcId());
        }
        sysOrganizationOc.setOcName(sysOrganization.getOrgName());
        sysOrganizationOc.setOcShortName(sysOrganization.getOrgShortName());
        sysOrganizationOc.setOrgId(sysOrganization.getId());
        sysOrganizationOc.setOcType(OcType.ORG_CN);
        sysOrganizationOc.setAddress(sysOrganization.getAddress());
        sysOrganizationOc.setAttentionTo(sysOrganization.getAttentionTo());
        sysOrganizationOc.setCell(sysOrganization.getCell());
        sysOrganizationOc.setCity(sysOrganization.getCity());
        sysOrganizationOc.setCountry(sysOrganization.getCountry());
        sysOrganizationOc.setProvince(sysOrganization.getProvince());
        sysOrganizationOc.setRegion(sysOrganization.getRegion());
        sysOrganizationOc.setEmail(sysOrganization.getEmail());
        sysOrganizationOc.setFax(sysOrganization.getFax());
        sysOrganizationOc.setPostCode(sysOrganization.getPostCode());
        sysOrganizationOc.setProvince(sysOrganization.getProvince());
        sysOrganizationOc.setRegion(sysOrganization.getRegion());
        sysOrganizationOc.setTel(sysOrganization.getTel());

        sysOrganizationOc = sysOrganizationOcService.saveSysOrganizationOc(sysOrganizationOc,true);
        sysOrganization.setRelateOcId(sysOrganizationOc.getId());


        //保存运营部门
        SysOrganizationDep sysOrganizationDep = null;
        if(sysOrganization.getRelateDepId() == null || sysOrganization.getRelateDepId() == 0){
            sysOrganizationDep = new SysOrganizationDep();
        }else{
            sysOrganizationDep = sysOrganizationDepService.findById(sysOrganization.getRelateDepId());
        }
        sysOrganizationDep.setDepName(sysOrganization.getOrgName());
        sysOrganizationDep.setDepShortName(sysOrganization.getOrgShortName());
        sysOrganizationDep.setOrgId(sysOrganization.getId());
        sysOrganizationDep.setOcId(sysOrganizationOc.getId());
        sysOrganizationDep.setAddress(sysOrganization.getAddress());
        sysOrganizationDep.setAttentionTo(sysOrganization.getAttentionTo());
        sysOrganizationDep.setCell(sysOrganization.getCell());
        sysOrganizationDep.setCity(sysOrganization.getCity());
        sysOrganizationDep.setCountry(sysOrganization.getCountry());
        sysOrganizationDep.setProvince(sysOrganization.getProvince());
        sysOrganizationDep.setRegion(sysOrganization.getRegion());
        sysOrganizationDep.setEmail(sysOrganization.getEmail());
        sysOrganizationDep.setFax(sysOrganization.getFax());
        sysOrganizationDep.setPostCode(sysOrganization.getPostCode());
        sysOrganizationDep.setProvince(sysOrganization.getProvince());
        sysOrganizationDep.setRegion(sysOrganization.getRegion());
        sysOrganizationDep.setTel(sysOrganization.getTel());
        sysOrganizationDep = sysOrganizationDepService.saveSysOrganizationDep(sysOrganizationDep,true);
        sysOrganization.setRelateDepId(sysOrganizationDep.getId());

        //保存角色
        SysRole sysRole = null;
        if(sysOrganization.getRelateRoleId() == null || sysOrganization.getRelateRoleId() == 0){
            sysRole = new SysRole();
        }else{
            sysRole = sysRoleService.findSysRoleById(sysOrganization.getRelateRoleId());
        }
        sysRole.setRoleName(sysOrganization.getOrgShortName()+"管理员");
        sysRole.setIsSysCreate(BaseBoolean.TRUE);
        sysRole.setOrgId(sysOrganization.getId());
        sysRole.setOcId(sysOrganizationOc.getId());

        sysRole = sysRoleService.saveSysRole(sysRole,true);
        sysOrganization.setRelateRoleId(sysRole.getId());

        //保存用户
        SysUser sysUser = null;
        if(sysOrganization.getRelateUserId() == null || sysOrganization.getRelateUserId() == 0){
            sysUser = new SysUser();
        }else{
            sysUser = sysUserService.findById(sysOrganization.getRelateUserId());
        }
        sysUser.setUserCode(sysOrganization.getOrgCode());
        sysUser.setUserName(sysOrganization.getOrgShortName());
        sysUser.setFirstLogin(true);
        sysUser.setDepId(sysOrganizationDep.getId());
        sysUser.setOcId(sysOrganizationOc.getId());
        sysUser.setOrgId(sysOrganization.getId());
        sysUser.setSex(BaseSex.FEMALE);
        sysUser.setIsSysCreate(BaseBoolean.TRUE);
        sysUser.setAddress(sysOrganization.getAddress());
        sysUser.setEmail(sysOrganization.getEmail());
        sysUser.setPhone(sysOrganization.getTel());
        sysUser.setMobile(sysOrganization.getCell());
        sysUser = sysUserService.saveSysUser(sysUser,true);
        sysOrganization.setRelateUserId(sysUser.getId());

        //保存角色用户
        sysRoleUserService.saveRoleUser(sysRole.getId(),new String[]{sysUser.getId().toString()});

        //保存角色网点
        sysRoleOcService.saveRoleOc(sysRole.getId(),new String[]{sysOrganizationOc.getId().toString()});

        //资源权限添加
        SysGroup sysGroup = sysGroupService.findSysGroupByCode(sysGroupService.BUSSINESS_GROUP);
        if(sysGroup == null){
            throw new FebsException("分组:"+sysGroupService.BUSSINESS_GROUP+"不存在!");
        }
        Set<Long> resourceIdSet = sysGroupResourceService.findSysResourceIds4Group(sysGroup.getId());
        SysRole finalSysRole = sysRole;
        resourceIdSet.forEach(resourceId->{
            SysRoleResource roleResource = new SysRoleResource();
            roleResource.setRoleId(finalSysRole.getId());
            roleResource.setResourceId(resourceId);
            sysRoleResourceService.createSysRoleResource(roleResource);
        });
        SysOrganizationOc finalSysOrganizationOc = sysOrganizationOc;
        resourceIdSet.forEach(resourceId->{
            SysOcResource ocResource = new SysOcResource();
            ocResource.setOcId(finalSysOrganizationOc.getId());
            ocResource.setResourceId(resourceId);
            sysOcResourceService.saveSysOcResource(ocResource);

            SysOganizationResource sysOganizationResource = new SysOganizationResource();
            sysOganizationResource.setResourceId(resourceId);
            sysOganizationResource.setOrgId(sysOrganization.getId());
            sysOganizationResourceService.saveSysOganizationResource(sysOganizationResource);
        });

        //报表权限添加
        Set<Long> reportIdSet = sysGroupReportService.findReportIds4Group(sysGroup.getId());
        reportIdSet.forEach(reportId->{
            SysRoleReport roleReport = new SysRoleReport();
            roleReport.setRoleId(finalSysRole.getId());
            roleReport.setReportId(reportId);
            sysRoleReportService.saveSysRoleReport(roleReport);
        });
        reportIdSet.forEach(reportId->{
            SysOcReport ocReport = new SysOcReport();
            ocReport.setOcId(finalSysOrganizationOc.getId());
            ocReport.setReportId(reportId);
            sysOcReportService.saveSysOcReport(ocReport);

            SysOganizationReport sysOganizationReport = new SysOganizationReport();
            sysOganizationReport.setOrgId(sysOrganization.getId());
            sysOganizationReport.setReportId(reportId);
            sysOganizationReportService.saveSysOganizationReport(sysOganizationReport);
        });

        this.updateById(sysOrganization);
    }

    @Override
    @Transactional
    public void enableSysOrganization(String[] sysOrganizationIds) {
        Arrays.stream(sysOrganizationIds).forEach(sysOrganizationId -> {
            SysOrganization sysOrganization = this.baseMapper.selectById(sysOrganizationId);
            sysOrganization.setStatus(BaseStatus.ENABLED);
            this.baseMapper.updateById(sysOrganization);
        });
	}

    @Override
    @Transactional
    public void disableSysOrganization(String[] sysOrganizationIds) {
        Arrays.stream(sysOrganizationIds).forEach(sysOrganizationId -> {
            SysOrganization sysOrganization = this.baseMapper.selectById(sysOrganizationId);
            sysOrganization.setStatus(BaseStatus.DISABLED);
            this.baseMapper.updateById(sysOrganization);
        });
    }

    @Override
    public SysOrganization findSysOrganizationById(Long sysOrganizationId){
        return this.baseMapper.selectOne(new QueryWrapper<SysOrganization>().lambda().eq(SysOrganization::getId, sysOrganizationId));
    }

    /**
     * 通过Id获取信息
     * @param orgCode
     * @return
     */
    @Override
    public SysOrganization findSysOrganizationByCode(String orgCode){
        return this.baseMapper.selectOne(new QueryWrapper<SysOrganization>().lambda().eq(SysOrganization::getOrgCode, orgCode));
    }

    /**
     * 获取当前用户的组织选项
     * @param sysUser
     * @return
     */
    @Override
    public List<OptionVo> getOrgOptionVoByCurrentUser(SysUser sysUser){
        SysOrganization sysOrganization = findSysOrganizationById(sysUser.getOrgId());
        List<OptionVo> result = new ArrayList<OptionVo>();

        List<SysOrganization> sysOrganizations = new ArrayList<SysOrganization>();
        if(sysOrganization.getOrgType().equals(OrgType.SYS)){
            sysOrganizations = this.baseMapper.findForAll();
        }else{
            sysOrganizations.add(sysOrganization);
        }
        for(SysOrganization sysOrganizationO : sysOrganizations){
            OptionVo optionvo = new OptionVo();
            optionvo.setValue(sysOrganizationO.getId().toString());
            optionvo.setText(sysOrganizationO.getOrgShortName());
            result.add(optionvo);
        }

        return result;

    }

    /**
     * 获取当前用户的组织选项
     * @return
     */
    @Override
    public List<OptionVo> getOrgCodeKeyOptionVoForAll(){
        List<OptionVo> result = new ArrayList<OptionVo>();
        List<SysOrganization> sysOrganizations = this.baseMapper.findForAll();
        for(SysOrganization sysOrganizationO : sysOrganizations){
            OptionVo optionvo = new OptionVo();
            optionvo.setValue(sysOrganizationO.getOrgCode().toString());
            optionvo.setText(sysOrganizationO.getOrgShortName());
            result.add(optionvo);
        }

        return result;
    }

    /**
     * 导入
     * @param uploadFile
     * @return
     */
    @Override
    public ImportResultBean importUpload(MultipartFile uploadFile){
        ImportResultBean resultBean = new ImportResultBean();
        // 获取临时上传文件目录
        String tempPath = sysconfGlobalParamService.getParamValueWithDiffSystem(SysconfGlobalConstant.SysconfGlobalGroupCode.TEMPLET_LOGO_FILE_PATH,
                UserInfoHolder.getUserInfo().getCurrentOc().getOcCode(),UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());

        // 新建一个文件
        String originalFileName = uploadFile.getOriginalFilename();
        originalFileName.replaceAll("/", "\\");

        String fileName = System.currentTimeMillis()+ "-" + originalFileName.substring(originalFileName.lastIndexOf("\\")+1);
        String filePath = tempPath + fileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            //将上传的文件写入新建的文件中
            uploadFile.transferTo(dest);

            resultBean.setSuccess(true);
            resultBean.setFileName(fileName);
            resultBean.setFilePath(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setSuccess(false);
            resultBean.setReturnInfo("Import Progress exception! "+e.getMessage());
        } finally {

        }
        return resultBean;
    }

    /**
     * 图片展现
     * @return
     */
    @Override
    public void logoImgShow(String logoFileName,HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 获取临时上传文件目录
        String tempPath = sysconfGlobalParamService.getParamValueWithDiffSystem(SysconfGlobalConstant.SysconfGlobalGroupCode.TEMPLET_LOGO_FILE_PATH,
                UserInfoHolder.getUserInfo().getCurrentOc().getOcCode(),UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());

        String filePath = tempPath + logoFileName;
        File file=new File(filePath);//获取图片这个文件
        InputStream is=new FileInputStream(file);
        BufferedImage bi= ImageIO.read(is);
        ImageIO.write(bi, "JPEG", response.getOutputStream());*/


        // 获取临时上传文件目录
        String tempPath = sysconfGlobalParamService.getParamValueWithDiffSystem(SysconfGlobalConstant.SysconfGlobalGroupCode.TEMPLET_LOGO_FILE_PATH,
                UserInfoHolder.getUserInfo().getCurrentOc().getOcCode(),UserInfoHolder.getUserInfo().getCurrentOrg().getOrgCode());

        String filePath = tempPath + logoFileName;
        File file=new File(filePath);//获取图片这个文件

        // 用response获得字节输出流
        ServletOutputStream sos = response.getOutputStream();
        // 获得服务器上的图片
        FileInputStream fis = new FileInputStream(file);

        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = fis.read(buffer)) > 0) {
            sos.write(buffer, 0, len);

        }
        fis.close();
        sos.close();
    }
}
