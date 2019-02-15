package com.springboot.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	
	/*private final Logger log = LoggerFactory.getLogger(getClass());

	// 1er Forma de ver foto, atraves de un metodo handlers
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString(); //toUri adjunta file://
		
		log.info(resourcePath);
		
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations(resourcePath);
	}*/
}
