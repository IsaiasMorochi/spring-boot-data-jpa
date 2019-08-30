package com.springboot.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	// 1er Forma de ver foto, atraves de un metodo handlers
	/*
    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
		WebMvcConfigurer.super.addResourceHandlers(registry);

        //#1
        //registry.addResourceHandler("/uploads/**")
        //        .addResourceLocations("file:/C/Temp/uploads/");

		//#2 mejorada
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString(); //toUri adjunta file://
		log.info(resourcePath);
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations(resourcePath);
	}*/
}
