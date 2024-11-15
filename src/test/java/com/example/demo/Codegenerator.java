package com.example.demo;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class Codegenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/diary";
        String username = "root";
        String password = "gxh151";
        String author = "gaoxianhua";
        String outputDir = "E:\\project\\java\\demo1\\src\\main\\java";
        String basePackage = "com.example.demo";
        String moduleName = "system";
        String mapperLocation = "E:\\project\\java\\demo1\\src\\main\\resources\\mapper\\" + moduleName;
        String tableName = "d_users,d_role,d_upload_file,d_diary_text,d_location,d_clock_image,d_user_role," +
                "d_app_version,d_problem_info,d_apply_list,d_mount,d_collect_mount,d_mark_mount,d_mountain_record," +
                "d_mark_reply";
        String tablePrefix = "d_";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(basePackage) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableName) // 设置需要生成的表名
                            .addTablePrefix(tablePrefix); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
