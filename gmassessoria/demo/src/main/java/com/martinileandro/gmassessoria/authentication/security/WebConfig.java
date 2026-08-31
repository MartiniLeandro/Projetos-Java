package com.martinileandro.gmassessoria.authentication.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String diretorioUpload;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String caminhoAbsoluto = Paths.get(diretorioUpload).toAbsolutePath().toUri().toString();

        if (!caminhoAbsoluto.endsWith("/")) {
            caminhoAbsoluto += "/";
        }

        registry.addResourceHandler("/imagens/**")
                .addResourceLocations(caminhoAbsoluto);
    }
}
