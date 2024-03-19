package com.example.demo.listener;


import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Original;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReaderListener implements ItemReadListener<Original>{
    
    @Override
    public void beforeRead(){
        // Do nothing
    }

    @Override
    public void afterRead(Original original){
        // Do nothing
    }

    @Override
    public void onReadError(Exception ex){
        log.error("ReadError:errorMessage",ex.getMessage(),ex);
    }
}
