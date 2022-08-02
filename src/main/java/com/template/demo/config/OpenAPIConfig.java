package com.template.demo.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;

@Slf4j
@Configuration
public class OpenAPIConfig {

	private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
	private final String specPath;

	public OpenAPIConfig(@Value("${open.api.spec.path}") final String specPath) {
		this.specPath = specPath;
	}

	@Bean
	public OpenAPI customOpenAPI() {
		try (InputStream inputStream = getClass().getResourceAsStream(this.specPath)) {
			final String yamlString = Optional.ofNullable(inputStream).map(stream -> {
				try {
					final TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {
					};
					final Map<String, Object> parsedContents = yamlMapper.readValue(stream, typeReference);
					return yamlMapper.writeValueAsString(parsedContents);
				} catch (IOException ex) {
					log.error("There was an error in parsing the OpenAPI", ex);
					throw new RuntimeException(ex);
				}
			}).orElseThrow(() -> new MissingResourceException("The Open API Spec is missing!",
					OpenAPIConfig.class.getSimpleName(), "openApiSpec"));

			return new OpenAPIV3Parser().readContents(yamlString).getOpenAPI();

		} catch (IOException ex) {
			log.error("Failed to close OpenApi specification Resource", ex);
			throw new RuntimeException(ex);
		}
	}
}
