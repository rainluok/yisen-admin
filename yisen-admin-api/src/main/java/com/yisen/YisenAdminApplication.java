package com.yisen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yisen.**.mapper")
public class YisenAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(YisenAdminApplication.class, args);
    }

}
