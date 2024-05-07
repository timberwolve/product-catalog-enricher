package com.bw.pce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ProductCatalogEnricherApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogEnricherApplication.class, args);
    }

}
