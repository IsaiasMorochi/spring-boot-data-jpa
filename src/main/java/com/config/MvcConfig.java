package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.LocaleResolver;

import java.nio.file.Paths;
import java.util.Locale;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	// 1er Forma de ver foto, atraves de un metodo handlers
	// Permite guardar archivos en rutas externas al proyecto
	// para registrar un directorio estatico de upload

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

	/**
	 * Registrar un controlador de vista
	 */
	public void addViewControllers(ViewControllerRegistry registry){
		registry.addViewController("/error_403").setViewName("error_403");
	}

	@Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * RESOLVE define donde se va a guardar o almacenar el parametro para el lenguaje
	 * en este caso se guarda en la SESSION
     */
    @Bean
    public LocaleResolver localResolver(){
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("es", "ES"));
		return localeResolver;
	}

	/**
	 * INTERCEPTOR se encarga de modificar o cambiar los textos de la pagina
	 * cada vez que se pase el parametro lang
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor(){
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

    /**
     * Registra el interceptor
     * @param registry
     */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
}
