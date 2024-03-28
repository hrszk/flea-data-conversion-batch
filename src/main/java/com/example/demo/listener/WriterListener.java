package com.example.demo.listener;


import java.util.List;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Item;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WriterListener implements ItemWriteListener<Item>{

    int count=0;
    
    @Override
    public void beforeWrite(List<? extends Item>items){
        // Do nothing
    }

    @Override
    public void afterWrite(List<? extends Item>items){
        count = count + 10000;
        log.info(count+"件処理が完了しました");
    }

    @Override
    public void onWriteError(Exception exception,List<? extends Item>items){
        log.error("WriteError: errorMessage={}",exception.getMessage(),exception);
    }
}
