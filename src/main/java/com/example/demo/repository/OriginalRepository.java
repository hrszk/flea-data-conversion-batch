package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Original;

@Repository
public class OriginalRepository {
    
    @Autowired
    private JdbcTemplate template;

    private static final RowMapper<Original> ORIGINAL_ROW_MAPPER=(rs,i)->{
        Original original=new Original();
        original.setId(rs.getInt("id"));
        original.setName(rs.getString("name"));
        original.setConditionId(rs.getInt("condition_id"));
        original.setCategoryName(rs.getString("category_name"));
        original.setBrand(rs.getString("brand"));
        original.setPrice(rs.getDouble("price"));
        original.setShipping(rs.getInt("shipping"));
        original.setDescription(rs.getString("description"));
        return original;
    } ;

    private static final String EXISTS_SQL="select exists(select * from original where id = ?)";

    // SQL実行
    public boolean exists(Integer id){
        boolean result=template.queryForObject(EXISTS_SQL,Boolean.class,id);
        return result;
    }

    // originalのデータを取得
    public List<Original> findAll(){
        String sql="SELECT * FROM original ORDER by id;";
        List<Original> originalList=template.query(sql,ORIGINAL_ROW_MAPPER);
        return originalList;
    }
}
