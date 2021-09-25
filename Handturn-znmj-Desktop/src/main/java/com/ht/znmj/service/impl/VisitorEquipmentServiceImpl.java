package com.ht.znmj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ht.znmj.common.customPage.CustomPage;
import com.ht.znmj.common.customPage.ICustomPage;
import com.ht.znmj.common.entity.FebsConstant;
import com.ht.znmj.common.entity.QueryRequest;
import com.ht.znmj.common.exception.FebsException;
import com.ht.znmj.entity.EquipmentInfo;
import com.ht.znmj.entity.EquipmentInfoCloudFlag;
import com.ht.znmj.entity.VisitorEquipment;
import com.ht.znmj.entity.VisitorInfo;
import com.ht.znmj.mapper.VisitorEquipmentMapper;
import com.ht.znmj.sdk.ZBX_Global;
import com.ht.znmj.sdk.libFaceRecognition;
import com.ht.znmj.sdk.MyFunction;
import com.ht.znmj.service.IEquipmentInfoService;
import com.ht.znmj.service.IVisitorEquipmentService;
import com.ht.znmj.service.IVisitorInfoService;
import com.ht.znmj.utils.DateUtil;
import com.ht.znmj.utils.PicConverUtil;
import com.ht.znmj.utils.SortUtil;
import com.sun.jna.Memory;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 人员信息 Service实现
 *
 * @author Eric
 * @date 2020-01-29 14:34:06
 */
@Slf4j
@Service("VisitorEquipmentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VisitorEquipmentServiceImpl extends ServiceImpl<VisitorEquipmentMapper, VisitorEquipment> implements IVisitorEquipmentService {
    @Autowired
    private IVisitorInfoService visitorInfoService;
    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    @Override
    public ICustomPage<VisitorEquipment> findVisitorEquipments(QueryRequest request, VisitorEquipment visitorEquipment) {
        CustomPage<VisitorEquipment> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findForPage(page, visitorEquipment);
    }

    /**
     * 通过ID查询
     */
    @Override
    public VisitorEquipment findVisitorEquipmentById(String id){
        QueryWrapper<VisitorEquipment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VisitorEquipment::getId,id);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 通过姓名、手机号查询
     */
    @Override
    public VisitorEquipment findVisitorEquipmentByUK(String visitorId,String equipmentId){
        QueryWrapper<VisitorEquipment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VisitorEquipment::getVisitorId,visitorId);
        queryWrapper.lambda().eq(VisitorEquipment::getEquipmentId,equipmentId);
        List<VisitorEquipment> visitorEquipments = this.baseMapper.selectList(queryWrapper);
        return visitorEquipments == null || visitorEquipments.size() <=0 ?null:visitorEquipments.get(0);
    }

    /**
     * 保存信息
     * @param visitorId
     */
    @Override
    @Transactional
    public VisitorEquipment saveVisitorEquipment(String visitorId,String equipmentId){
        VisitorEquipment visitorEquipment = findVisitorEquipmentByUK(visitorId,equipmentId);
        Boolean equipAsy = false;
        if(visitorEquipment == null){
            equipAsy = false;
            visitorEquipment = new VisitorEquipment();
        }else{
            if(visitorEquipment.getEquipmentFlag().equals(EquipmentInfoCloudFlag.ASYED.getCode())){
                equipAsy = true;
            }else{
                equipAsy = false;
            }
        }

        VisitorInfo visitorInfo = visitorInfoService.findVisitorInfoById(visitorId);
        if(visitorInfo == null){
            throw new FebsException("人员信息未找到!");
        }
        EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(equipmentId);
        if(equipmentInfo == null){
            throw new FebsException("设备信息未找到!");
        }

        //设备同步
        try {
            IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(equipmentInfo.getSn());

            if (MyFunction.m_FaceRecognition.ZBX_Connected(cameraPoint) != 1) {
                //visitorEquipment.setEquipmentFlag(EquipmentInfoCloudFlag.NO_ASY.getCode());
                throw new FebsException("设备未全部连接,请先在首页刷新设备列表");
            }else {
                libFaceRecognition.FaceFlags.ByValue flag = new libFaceRecognition.FaceFlags.ByValue();
                libFaceRecognition.FaceImage.ByValue faceimg = new libFaceRecognition.FaceImage.ByValue();
                libFaceRecognition.FaceImage.ByValue[] faceimgarray = (libFaceRecognition.FaceImage.ByValue[]) faceimg.toArray(1);
                libFaceRecognition.FaceFlags.ByValue[] flagarray = (libFaceRecognition.FaceFlags.ByValue[]) flag.toArray(1);
                flagarray[0].faceName = visitorInfo.getName().getBytes(StandardCharsets.UTF_8);
                flagarray[0].faceID = visitorInfo.getId().getBytes(StandardCharsets.UTF_8);

                flagarray[0].usr_type = 0;
                if(!StringUtils.isEmpty(visitorInfo.getWigan())){
                    flagarray[0].wgCardNO = Integer.valueOf(visitorInfo.getWigan());
                }

                Long effectTimeLong = visitorInfo.getEndTime().getTime() - DateUtil.formatStr2Date("1970-01-01 00:00:00", DateUtil.FULL_TIME_SPLIT_PATTERN).getTime();
                Long effectStartTimeLong = visitorInfo.getStartTime().getTime() - DateUtil.formatStr2Date("1970-01-01 00:00:00",DateUtil.FULL_TIME_SPLIT_PATTERN).getTime();
                flagarray[0].effectStartTime = Long.valueOf(effectStartTimeLong/1000).intValue();
                flagarray[0].effectTime = Long.valueOf(effectTimeLong/1000).intValue();
                //flagarray[0].resv = "";
                String filepath = visitorInfo.getFaceFilePath();
                if (filepath.isEmpty()) {
                    throw new FebsException("图片信息为空!");
                } else {
                    byte[] imagedata = PicConverUtil.imageToByteArray(filepath);
                    faceimgarray[0].img_len = imagedata.length;
                    Memory a = new Memory(imagedata.length);
                    a.write(0, imagedata, 0, imagedata.length);
                    faceimgarray[0].img.setPointer(a);
                    faceimgarray[0].img_fmt = 0;


                    int k = -1;
                    if(equipAsy){
                        k = MyFunction.m_FaceRecognition.ZBX_ModifyFaces(cameraPoint, flagarray,
                                faceimgarray);
                    }else{
                        k = MyFunction.m_FaceRecognition.ZBX_AddJpgFaces(cameraPoint, flagarray,
                                faceimgarray, 1, 0);
                    }

                    if (k == 0) {
                        log.info("调用设备-人员同步成功:(MAC="+equipmentInfo.getSn()+";)");
                        visitorEquipment.setEquipmentFlag(EquipmentInfoCloudFlag.ASYED.getCode());
                    } else {
                        log.info("调用设备-人员同步失败:(MAC="+equipmentInfo.getSn()+";)");
                        if(!equipAsy){
                            visitorEquipment.setEquipmentFlag(EquipmentInfoCloudFlag.NO_ASY.getCode());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            visitorEquipment.setEquipmentFlag(EquipmentInfoCloudFlag.NO_ASY.getCode());
        }

        visitorEquipment.setVisitorId(visitorId);
        visitorEquipment.setEquipmentId(equipmentId);

        if(StringUtils.isEmpty(visitorEquipment.getId())){
            visitorEquipment.setCloudFlag(EquipmentInfoCloudFlag.NO_ASY.getCode());
            this.getBaseMapper().insert(visitorEquipment);
        }else{
            this.getBaseMapper().updateById(visitorEquipment);
        }
        return visitorEquipment;
    }

    /**
     * 删除信息
     * @param visitorIds
     */
    @Override
    @Transactional
    public void deleteVisitorEquipments(Set<String> visitorIds, String equipmentId){
        EquipmentInfo equipmentInfo = equipmentInfoService.findEquipmentInfoById(equipmentId);
        if(equipmentInfo == null){
            throw new FebsException("设备信息未找到!");
        }
        visitorIds.forEach(visitorId ->{
            IntByReference cameraPoint = ZBX_Global.EQUIPMENT_CONNECT_REFERENCE_MAP.get(equipmentInfo.getSn());
            int k = MyFunction.m_FaceRecognition.ZBX_DeleteFaceDataByPersonID(cameraPoint,
                    String.valueOf(visitorId));
            if (k == 0) {
                log.info("调用设备-取消人员授权成功!"+equipmentInfo.getSn());
                QueryWrapper<VisitorEquipment> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(VisitorEquipment::getVisitorId,visitorId);
                queryWrapper.lambda().eq(VisitorEquipment::getEquipmentId,equipmentId);
                this.getBaseMapper().delete(queryWrapper);
            } else {
                log.info("调用设备-取消人员授权失败!"+equipmentInfo.getSn());
            }
        });
    }

    /**
     * 授权所有人员
     */
    @Override
    @Transactional
    public void authAllVisitorEquipments(Set<String> equipmentIds){
        QueryWrapper<VisitorInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(VisitorInfo::getId);
        List<VisitorInfo> visitorInfos = visitorInfoService.getBaseMapper().selectList(queryWrapper);
        Set<String> visitorIds = new HashSet<>();
        visitorInfos.forEach(visitorInfo ->{
            visitorIds.add(visitorInfo.getId());
        });

        if(visitorIds.size() > 0){
            authVisitorIdEquipments(visitorIds,equipmentIds);
        }
    }

    /**
     * 查询授权
     */
    @Override
    @Transactional
    public void authQueryVisitorEquipments(VisitorInfo queryVisitorInfo, Set<String> equipmentIds){
        Integer pageNum = 1;
        Integer pageSize = 200;
        while (true) {
            QueryRequest request = new QueryRequest();
            request.setPageNum(pageNum);
            request.setPageSize(pageSize);
            IPage<VisitorInfo> page = visitorInfoService.findVisitorInfos(request,queryVisitorInfo);
            if(page.getRecords().size() <= 0){
                break;
            }else{
                Set<String> visitorIds = new HashSet<>();
                page.getRecords().forEach(visitorInfo ->{
                    visitorIds.add(visitorInfo.getId());
                });

                authVisitorIdEquipments(visitorIds,equipmentIds);
            }
            pageNum ++;
        }
    }

    /**
     * 选择授权
     */
    @Override
    @Transactional
    public void authVisitorIdEquipments(Set<String> visitorIds, Set<String> equipmentIds){
        equipmentIds.forEach(equipmentId ->{
            visitorIds.forEach(visitorId ->{
                saveVisitorEquipment(visitorId,equipmentId);
            });
        });
    }

    /**
     * 取消授权所有人员
     */
    @Override
    @Transactional
    public void authCancelAllVisitorEquipments(Set<String> equipmentIds){
        QueryWrapper<VisitorInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(VisitorInfo::getId);
        List<VisitorInfo> visitorInfos = visitorInfoService.getBaseMapper().selectList(queryWrapper);
        Set<String> visitorIds = new HashSet<>();
        visitorInfos.forEach(visitorInfo ->{
            visitorIds.add(visitorInfo.getId());
        });

        if(visitorIds.size() > 0){
            authCancelVisitorIdEquipments(visitorIds,equipmentIds);
        }
    }

    /**
     * 取消查询授权
     */
    @Override
    @Transactional
    public void authCancelQueryVisitorEquipments(VisitorInfo queryVisitorInfo, Set<String> equipmentIds){
        Integer pageNum = 1;
        Integer pageSize = 200;
        while (true) {
            QueryRequest request = new QueryRequest();
            request.setPageNum(pageNum);
            request.setPageSize(pageSize);
            IPage<VisitorInfo> page = visitorInfoService.findVisitorInfos(request,queryVisitorInfo);
            if(page.getRecords().size() <= 0){
                break;
            }else{
                Set<String> visitorIds = new HashSet<>();
                page.getRecords().forEach(visitorInfo ->{
                    visitorIds.add(visitorInfo.getId());
                });

                authCancelVisitorIdEquipments(visitorIds,equipmentIds);
            }
            pageNum ++;
        }
    }

    /**
     * 取消选择授权
     */
    @Override
    @Transactional
    public void authCancelVisitorIdEquipments(Set<String> visitorIds, Set<String> equipmentIds){
        equipmentIds.forEach(equipmentId ->{
            deleteVisitorEquipments(visitorIds,equipmentId);
        });
    }

}
