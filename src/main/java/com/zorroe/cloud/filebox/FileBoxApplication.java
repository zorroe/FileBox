package com.zorroe.cloud.filebox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // 启用异步支持
@SpringBootApplication
public class FileBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileBoxApplication.class, args);
    }

}
