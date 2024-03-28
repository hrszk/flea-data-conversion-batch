package com.example.demo.chunk;


import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Category;
import com.example.demo.domain.Item;
import com.example.demo.domain.Original;
import com.example.demo.repository.CategoryRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@JobScope
@Slf4j
public class ItemMigrateProcessor implements ItemProcessor<Original,Item>{

    @Autowired
    private CategoryRepository categoryRepository;

    public int count=0;
    
    @Override
    public Item process(Original item) throws Exception{

        if(item.getCategoryName()!=null){

            item.divideCategoryName();
            String nameAll=item.getCategoryName1()+"/"+item.getCategoryName2()+"/"+item.getCategoryName3();
            
            Item item2=new Item();
            item2.setName(item.getName());
            item2.setCondition(item.getConditionId());
            item2.setBrand(item.getBrand());
            item2.setPrice(item.getPrice());
            item2.setShipping(item.getShipping());
            item2.setDescription(item.getDescription());
            
            Category category=categoryRepository.findByNameAll(nameAll);
            
            if (category != null) {
                item2.setCategory(category.getId());   
                return item2;
            } else {
                // スキップ
                return null;
            }
        }else{
            count++;
            return null;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
