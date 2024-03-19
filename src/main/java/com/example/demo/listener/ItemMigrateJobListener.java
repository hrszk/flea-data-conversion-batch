package com.example.demo.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.chunk.ItemMigrateProcessor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemMigrateJobListener implements JobExecutionListener {

    @Autowired
    private ItemMigrateProcessor itemMigrateProcessor;
    
    @Override
    public void beforeJob(JobExecution jobExecution){
        log.info("itemsテーブルへのデータ移行を開始します");
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        log.info("itemsテーブルへのデータ移行が完了しました");
        log.info("エラー件数は"+itemMigrateProcessor.getCount()+"件です");
    }
}
