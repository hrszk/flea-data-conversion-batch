package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.example.demo.domain.Item;
import com.example.demo.domain.Original;
import com.example.demo.listener.CategoryMigrateListener;
import com.example.demo.listener.ItemMigrateJobListener;
import com.example.demo.tasklet.CategoryMigrateTasklet;
import com.example.demo.tasklet.CategoryMigrateTasklet2;

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
    private CategoryMigrateTasklet2 categoryMigrateTasklet2;

    @Autowired
    private CategoryMigrateListener categoryMigrateListener;

    @Autowired
    private ItemMigrateJobListener itemMigrateJobListener;

    @Autowired
    private ItemProcessor<Original,Item> itemMigrateProcessor;

    @Autowired
    protected ItemReadListener<Original> readListener;

    @Autowired
    protected ItemWriteListener<Item> writeListener;

    // DataSource(JDBCで必要)
    @Autowired
    private DataSource dataSource;

    // INSERT用のSQL
    private static final String INSERT_ITEM_SQL="""
        INSERT INTO items(name,condition,category,brand,price,shipping,description) 
        VALUES(:name,:condition,:category,:brand,:price,:shipping,:description);
            """;
    
    @Bean
    public Job categoryMigrateJob() {
        return this.jobBuilderFactory.get("CategoryMigrateJob")
        .incrementer(new RunIdIncrementer())
        .start(categoryMigrateStep2()).listener(categoryMigrateListener)
        .next(ItemMigrateStep()).listener(itemMigrateJobListener)
        .build();
    }
    
    @Bean
    public Step categoryMigrateStep(){
        return stepBuilderFactory.get("categoryMigrateStep")
            .tasklet(categoryMigrateTasklet)
            .build();
    }

    @Bean
    public Step categoryMigrateStep2(){
        return stepBuilderFactory.get("categoryMigrateStep2")
            .tasklet(categoryMigrateTasklet2)
            .build();
    }

    @Bean
    public Step ItemMigrateStep(){
        return this.stepBuilderFactory.get("ItemMigrateStep")
            .<Original,Item>chunk(10000)
            .reader(jdbcCursorReader()).listener(readListener)
            .processor(itemMigrateProcessor)
            .writer(jdbcWriter()).listener(writeListener)
            .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Original> jdbcCursorReader(){

        // RowMapper
        RowMapper<Original> rowMapper=new BeanPropertyRowMapper<>(Original.class);

        return new JdbcCursorItemReaderBuilder<Original>()
            .dataSource(this.dataSource)
            .name("jdbcCursorItemReader")
            .sql("SELECT * FROM original order by id")
            .rowMapper(rowMapper)
            .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Item> jdbcWriter(){

        // provider生成
        BeanPropertyItemSqlParameterSourceProvider<Item> provider=
            new BeanPropertyItemSqlParameterSourceProvider<>();

        return new JdbcBatchItemWriterBuilder<Item>()
            .itemSqlParameterSourceProvider(provider)
            .sql(INSERT_ITEM_SQL)
            .dataSource(this.dataSource)
            .build();
    }

}
