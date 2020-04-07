package com.xchgx.cloud.sso8.assetmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan //Servlet组件扫描
public class AssetmanagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(AssetmanagerApplication.class, args);
	}
}
