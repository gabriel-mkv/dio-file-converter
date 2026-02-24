package com.gabrielmkv.file_converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação File Converter.
 * Responsável pela inicialização e configuração do contexto Spring Boot.
 */
@SpringBootApplication
public class FileConverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileConverterApplication.class, args);
	}

}
