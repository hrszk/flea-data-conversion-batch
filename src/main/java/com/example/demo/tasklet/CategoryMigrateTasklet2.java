package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class CategoryMigrateTasklet2 implements Tasklet{
    
    private final JdbcTemplate jdbcTemplate;

    public CategoryMigrateTasklet2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution,ChunkContext chunkContext)throws Exception{

        jdbcTemplate.execute("""
            -- 各階層（親、子、孫）のカテゴリを出力し、その場合は「-未分類-」にする
                    CREATE TEMP TABLE temp_category AS
                    SELECT split_part(category_name, '/', 1) AS parent,
                        split_part(category_name, '/', 2) AS child,
                        split_part(category_name, '/', 3) AS grandchild
                    FROM original;
                    -- 1階層目（親）のカテゴリを挿入
                    INSERT INTO category (name)
                    SELECT DISTINCT parent
                    FROM temp_category
                    WHERE parent IS NOT NULL;
                    -- 2階層目（子）のカテゴリを挿入
                    INSERT INTO category (name, parent_id)
                    SELECT DISTINCT child, c.id
                    FROM temp_category AS tc
                    JOIN category AS c ON tc.parent = c.name
                    WHERE child IS NOT NULL;
                    -- 3階層目（孫）のカテゴリを挿入
                    INSERT INTO category (name, parent_id, name_all)
                    SELECT DISTINCT tc.grandchild, c1.id,
                        tc.parent || '/' || tc.child || '/' || tc.grandchild
                    FROM temp_category AS tc
                    JOIN category AS c1 ON tc.child = c1.name
                    JOIN category AS c2 ON tc.parent = c2.name
                    AND c1.parent_id = c2.id
                    WHERE tc.grandchild IS NOT NULL;
                    -- 一時テーブルの削除
                    DROP TABLE temp_category;
                """);

        return RepeatStatus.FINISHED;
    }
}
