package com.ssafy.hoshinohome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ssafy.hoshinohome.model.dao")
@SpringBootApplication
public class HoshinohomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoshinohomeApplication.class, args);
	}

}
