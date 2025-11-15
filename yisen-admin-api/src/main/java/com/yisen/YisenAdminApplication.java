package com.yisen;

import com.yisen.common.util.IpUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.yisen.**.mapper")
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableCaching
public class YisenAdminApplication {

    public static void main(String[] args) {
        // 构建一个data/ip2region_v4.xdb文件 的绝对路径
        String dbPath = System.getProperty("user.dir") + "/src/main/resources/data/ip2region_v4.xdb";
        IpUtil.init(dbPath);
        SpringApplication.run(YisenAdminApplication.class, args);
    }

}
