package com.clankalliance.backbeta.service.impl;

import com.alibaba.excel.EasyExcel;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.ExcelService;
import com.clankalliance.backbeta.utils.ExcelListener.RegisterListener.BatchRegisterData;
import com.clankalliance.backbeta.utils.ExcelListener.RegisterListener.RegisterListener;
import com.clankalliance.backbeta.utils.ExcelListener.ScoreListener.ScoreListener;
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
        CommonResponse response = tokenUtil.tokenCheck(token);
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
        try{
            EasyExcel.read(temp, BatchRegisterData.class, new RegisterListener()).sheet().doRead();
        }catch(Exception e){
            response.setMessage("创建失败 请检查表格");
            return response;
        }
        response.setMessage("创建成功");
        return response;
    }

    public CommonResponse handleBatchScore(MultipartFile file, String token, Long courseId){
        CommonResponse response = tokenUtil.tokenCheck(token);
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
        ScoreListener scoreListener = new ScoreListener();
        scoreListener.setCourseId(courseId);
        try{
            EasyExcel.read(temp, BatchRegisterData.class,scoreListener).sheet().doRead();
        }catch(Exception e){
            response.setMessage("创建失败 请检查表格");
            return response;
        }
        response.setMessage("创建成功");
        return response;
    }
}
