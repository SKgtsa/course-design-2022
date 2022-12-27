package com.clankalliance.backbeta.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.clankalliance.backbeta.entity.RegisterData;
import com.clankalliance.backbeta.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;



@Component
public class ExcelListener extends AnalysisEventListener<RegisterData> {

    private static ExcelListener listener;
    //获取默认头像及照片
    @Resource
    private UserService userService;

    @PostConstruct
    public void init(){
        listener = this;
    }

    //一行一行读取表格内容 从第二行开始不读取表头内容
    @Override
    public void invoke(RegisterData data, AnalysisContext context) {
        System.out.println("解析到数据: " + data);
        //根据读取的数据来创建账户并保存
        listener.userService.handleBatchRegisterStudent(Long.parseLong(data.getUserNumber()), data.getName(), data.getPassword(), Long.parseLong(data.getPhone()), data.getStudentClass(), data.getIdCardNumber(), data.getGender().equals("男")? false:true, data.getEthnic(), data.getPoliticalAffiliation(), data.getEMail(), "默认昵称",data.getSection());
    }
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        //读取文件，正常获取表头
        System.out.println("表头内容"+headMap);
    }
    //读取之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("所有数据解析完成");
    }
}
