package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.listener.CategoryMigrateListener;
import com.example.demo.tasklet.CategoryMigrateTasklet;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private CategoryMigrateTasklet categoryMigrateTasklet;

    @Autowired
    private CategoryMigrateListener categoryMigrateListener;

    @Bean
    public Step categoryMigrateStep(){
        return stepBuilderFactory.get("categoryMigrateStep")
            .tasklet(categoryMigrateTasklet)
            .build();
    }

    @Bean
    public Job categoryMigrateJob() {
        return jobBuilderFactory.get("CategoryMigrateJob")
                .incrementer(new RunIdIncrementer())
                .start(categoryMigrateStep()).listener(categoryMigrateListener)
                .build();
    }
}
