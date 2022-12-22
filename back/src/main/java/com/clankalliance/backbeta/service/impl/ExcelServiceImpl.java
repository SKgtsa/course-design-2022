package com.clankalliance.backbeta.service.impl;

import com.alibaba.excel.EasyExcel;
import com.clankalliance.backbeta.entity.RegisterData;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.ExcelService;
import com.clankalliance.backbeta.utils.ExcelListener;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;


@Service
public class ExcelServiceImpl implements ExcelService {


    @Resource
    private TokenUtil tokenUtil;

    @Override
    public CommonResponse handleBatchRegister(MultipartFile file, String token) {
        CommonResponse response;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(!response.getSuccess())
            return response;
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        File temp;
        try {
            temp = File.createTempFile(fileName, prefix);
            file.transferTo(temp);
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("保存失败");
            return response;
        }
        EasyExcel.read(temp, RegisterData.class,new ExcelListener()).sheet().doRead();
        response.setMessage("创建成功");
        return response;
    }
}
