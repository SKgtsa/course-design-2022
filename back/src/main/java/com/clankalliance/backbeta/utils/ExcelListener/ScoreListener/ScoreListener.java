package com.clankalliance.backbeta.utils.ExcelListener.ScoreListener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.clankalliance.backbeta.service.ScoreService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Component
public class ScoreListener extends AnalysisEventListener<BatchScoreData> {

    private static ScoreListener listener;
    //获取默认头像及照片

    private Long courseId;

    @Resource
    private ScoreService scoreService;

    @PostConstruct
    public void init(){
        listener = this;
    }

    //一行一行读取表格内容 从第二行开始不读取表头内容
    @Override
    public void invoke(BatchScoreData data, AnalysisContext context) {
        System.out.println("解析到数据: " + data);
        //根据读取的数据来创建账户并保存
        listener.scoreService.handleBatchScore(Integer.parseInt(data.getDailyScore()),Integer.parseInt(data.getEndScore()),Long.parseLong(data.getStudentNumber()),courseId);
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

    public void init(Long courseId){
        this.courseId = courseId;
    }
}
