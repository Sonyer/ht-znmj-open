package com.handturn.bole;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Eric
 */
@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@MapperScan("com.handturn.bole.**.mapper")
public class WebZnmjApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebZnmjApplication.class).run(args);
    }

}
