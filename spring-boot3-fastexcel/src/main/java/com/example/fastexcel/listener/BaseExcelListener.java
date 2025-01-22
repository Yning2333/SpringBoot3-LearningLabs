package com.example.fastexcel.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;


public class BaseExcelListener<T> extends AnalysisEventListener<T> {

    // 定义一个数据列表，用于存储读取到的每一行数据
    private List<T> dataList = new ArrayList<>();

    //定义一个计数器
    private int count = 0;
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        // 添加读取到的每一行数据到数据列表
        dataList.add(t);

        //计数器自增
        count++;
        if (count % 10000 == 0) {
            System.out.println("已读取 " + count + " 条数据");
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 读取完成后，打印数据列表的大小
        System.out.println("读取完成，共读取了 " + dataList.size() + " 条数据");
    }

    public List<T> getDataList() {
        return dataList;
    }

}