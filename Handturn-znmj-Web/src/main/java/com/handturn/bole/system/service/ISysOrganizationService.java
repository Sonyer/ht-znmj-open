package com.handturn.bole.system.service;

import com.handturn.bole.common.customPage.ICustomPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.entity.ImportResultBean;
import com.handturn.bole.common.entity.OptionVo;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.system.entity.SysOrganization;
import com.handturn.bole.system.entity.SysUser;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 系统-组织 Service接口
 *
 * @author MrBird
 * @date 2019-12-08 10:00:07
 */
public interface ISysOrganizationService extends IService<SysOrganization> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param sysOrganization sysOrganization
     * @return ICustomPage<SysOrganization>
     */
    ICustomPage<SysOrganization> findSysOrganizations(QueryRequest request, SysOrganization sysOrganization);

    /**
     * 按组织类型查询
     * @param orgType
     * @return
     */
    List <SysOrganization> findSysOrganizationsByOrgType(String orgType,Integer pageIndex);

    /**
     * 修改
     *
     * @param sysOrganization sysOrganization
     */
    void saveSysOrganization(SysOrganization sysOrganization);

    /**
     * 修改 无其他信息
     *
     * @param sysOrganization sysOrganization
     */
    void saveSysOrganizationNoneExtend(SysOrganization sysOrganization);

    /**
     * 启用
     *
     * @param sysOrganizationIds sysOrganizationIds
     */
    void enableSysOrganization(String[] sysOrganizationIds);

    /**
    * 禁用
    *
    * @param sysOrganizationIds sysOrganizationIds
    */
    void disableSysOrganization(String[] sysOrganizationIds);


    /**
    * 通过Id获取信息
    * @param sysOrganizationId
    * @return
    */
    SysOrganization findSysOrganizationById(Long sysOrganizationId);

    /**
     * 通过Id获取信息
     * @param orgCode
     * @return
     */
    SysOrganization findSysOrganizationByCode(String orgCode);


    /**
     * 获取当前用户的组织选项
     * @param sysUser
     * @return
     */
    List<OptionVo> getOrgOptionVoByCurrentUser(SysUser sysUser);

    /**
     * 获取
     * @return
     */
    List<OptionVo> getOrgCodeKeyOptionVoForAll();

    /**
     * 导入
     * @param uploadFile
     * @return
     */
    ImportResultBean importUpload(MultipartFile uploadFile);

    /**
     * 图片展现
     * @return
     */
    void logoImgShow(String logoFileName,HttpServletRequest request, HttpServletResponse response) throws IOException;
}
