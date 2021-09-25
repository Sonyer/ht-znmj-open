package com.handturn.bole.sitCommon.common.controller;

import com.handturn.bole.common.controller.BaseController;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.sitCommon.common.service.ICommonImgShowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

/**
 * 系统-组织 Controller
 *
 * @author MrBird
 * @date 2019-12-08 10:00:07
 */
@Slf4j
@Validated
@RestController
@RequestMapping("sitapi/fileShow")
public class CommonFileShowController extends BaseController {

    @Autowired
    private ICommonImgShowService commonImgShowService;

    @RequestMapping(value="/{typeName}/{dateStr}/{fileName}")
    public void logoImgShow(@NotBlank(message = "{required}") @PathVariable String typeName,
                            @NotBlank(message = "{required}") @PathVariable String dateStr,
                            @NotBlank(message = "{required}") @PathVariable  String fileName,
                            HttpServletRequest request, HttpServletResponse response){
        try {
            commonImgShowService.fileShow(typeName,dateStr,fileName,request, response);
        }catch (Exception e){
            throw new FebsException(e.getMessage());
        }
    }
}
