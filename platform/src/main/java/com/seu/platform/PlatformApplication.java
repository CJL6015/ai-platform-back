package com.seu.platform;

import cn.hutool.core.date.DateUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 陈小黑
 */
@EnableCaching
@EnableScheduling
@SpringBootApplication
@MapperScan("com.seu.platform.dao.mapper")
public class PlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformApplication.class, args);
    }

}
