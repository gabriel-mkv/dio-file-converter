package com.gabrielmkv.file_converter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuração do OpenAPI (Swagger) para documentação da API.
 */
@Configuration
public class OpenApiConfig {
    
    /**
     * Define as informações personalizadas da API.
     * 
     * @return Instância de OpenAPI configurada.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("File Converter API")
                .version("1.0")
                .description("Serviço para geração de relatórios de transações em formatos PDF, CSV e JSON.")
                .contact(new Contact()
                        .name("gabriel.mkv")
                        .url("https://github.com/gabriel-mkv")));
    }
}
