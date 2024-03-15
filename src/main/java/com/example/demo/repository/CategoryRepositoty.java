package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Category;

@Repository
public class CategoryRepositoty {
    
    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Category> CATEGORY_ROW_MAPPER=(rs,i)->{
        Category category=new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setParentId(rs.getInt("parent_id"));
        category.setNameAll(rs.getString("name_all"));
        return category;
    };

    public Category insertCategory(Category category){
        String sql="INSERT INTO category (name, parent_id, name_all) VALUES (:name,:parentId,:nameAll)";
        SqlParameterSource param=new BeanPropertySqlParameterSource(category);

        KeyHolder keyHolder=new GeneratedKeyHolder();
        String[] keyColumnNames={"id"};
        template.update(sql, param,keyHolder,keyColumnNames);
        category.setId(keyHolder.getKey().intValue());
        return category;
    }

    // 孫カテゴリの検索
    public Category findByNameGrandChild(String name){
        String sql="SELECT * FROM category WHERE name=:name AND parent_id IS NOT NULL AND name_all IS NOT NULL;";
        SqlParameterSource param=new MapSqlParameterSource().addValue("name", name);
        List<Category> categoryList=template.query(sql,param,CATEGORY_ROW_MAPPER);

        return categoryList.isEmpty() ? null : categoryList.get(0);
    }

    // 子カテゴリの検索
    public Category findByNameChildCategory(String name){
        String sql="SELECT * FROM category WHERE name=:name AND parent_id IS NOT NULL AND name_all IS NULL;";
        SqlParameterSource param=new MapSqlParameterSource().addValue("name", name);
        List<Category> categoryList=template.query(sql,param,CATEGORY_ROW_MAPPER);

        return categoryList.isEmpty() ? null : categoryList.get(0);
    }

    // 親カテゴリの検索
    public Category findByNameParentCategory(String name){
        String sql="SELECT * FROM category WHERE name=:name AND parent_id IS NULL;";
        SqlParameterSource param=new MapSqlParameterSource().addValue("name", name);
        List<Category> categoryList=template.query(sql,param,CATEGORY_ROW_MAPPER);

        return categoryList.isEmpty() ? null : categoryList.get(0);
    }
}
