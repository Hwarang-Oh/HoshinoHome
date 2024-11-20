package com.ssafy.hoshinohome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@MapperScan("com.ssafy.hoshinohome.model.dao")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HoshinohomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoshinohomeApplication.class, args);
	}

}
