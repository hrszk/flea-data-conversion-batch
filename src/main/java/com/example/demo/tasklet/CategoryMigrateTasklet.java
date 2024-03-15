package com.example.demo.tasklet;

import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Category;
import com.example.demo.domain.Original;
import com.example.demo.repository.CategoryRepositoty;
import com.example.demo.repository.OriginalRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class CategoryMigrateTasklet implements Tasklet{

    @Autowired
    private CategoryRepositoty categoryRepositoty;

    @Autowired
    private OriginalRepository originalRepository;
    
    @Override
    public RepeatStatus execute(StepContribution contribution,ChunkContext chunkContext)throws Exception{

        List<Original> originalList=originalRepository.findAll();

        for(Original original:originalList){

            // category_nameがnullのものはスキップ
            if(original.getCategoryName()==null){
                log.info("カテゴリ名がnullのためスキップします");

            }else{

                // category_nameを分割
                original.divideCategoryName();
                
                // nameAllのデータを生成
                String nameAll=original.getCategoryName1()+"/"+original.getCategoryName2()+"/"+original.getCategoryName3();
        
                // データの重複がないか確認
                Category category1=categoryRepositoty.findByNameParentCategory(original.getCategoryName1());
                Category category2=categoryRepositoty.findByNameChildCategory(original.getCategoryName2());
                Category category3=categoryRepositoty.findByNameGrandChild(original.getCategoryName3());

                // 重複がない場合は全てのデータを挿入
                if(category1==null && category2==null && category3==null){
                    
                    Category parentCategory=new Category();
                    parentCategory.setName(original.getCategoryName1());
                    parentCategory =categoryRepositoty.insertCategory(parentCategory); // 親カテゴリーを追加し、追加したカテゴリ情報を取得
                
                    Category childCategory=new Category();
                    childCategory.setName(original.getCategoryName2());
                    childCategory.setParentId(parentCategory.getId());
                    childCategory =categoryRepositoty.insertCategory(childCategory); // 子カテゴリーを追加し、追加したカテゴリ情報を取得
                
                    Category grandChildCategory=new Category();
                    grandChildCategory.setName(original.getCategoryName3());
                    grandChildCategory.setParentId(childCategory.getId());
                    grandChildCategory.setNameAll(nameAll);
                    grandChildCategory =categoryRepositoty.insertCategory(grandChildCategory); // 孫カテゴリーを追加し、追加したカテゴリ情報を取得
                
                // 重複がある場合はデータの挿入をスキップ
                }else if(category1!=null && category2==null && category3==null){
                    
                    Category childCategory=new Category();
                    childCategory.setName(original.getCategoryName2());
                    childCategory.setParentId(category1.getId());
                    childCategory =categoryRepositoty.insertCategory(childCategory); 
                
                    Category grandChildCategory=new Category();
                    grandChildCategory.setName(original.getCategoryName3());
                    grandChildCategory.setParentId(childCategory.getId());
                    grandChildCategory.setNameAll(nameAll);
                    grandChildCategory =categoryRepositoty.insertCategory(grandChildCategory); 
                
                }else if(category1!=null && category2!=null && category3==null){

                    Category grandChildCategory=new Category();
                    grandChildCategory.setName(original.getCategoryName3());
                    grandChildCategory.setParentId(category2.getId());
                    grandChildCategory.setNameAll(nameAll);
                    grandChildCategory =categoryRepositoty.insertCategory(grandChildCategory); 

                }
            }
        }
        
        return RepeatStatus.FINISHED;
    }
}
