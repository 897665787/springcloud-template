package com.company.gateway.knife4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

/**
 * 官方文档：https://doc.xiaominfo.com/docs/action/springcloud-gateway
 */
@RestController
@RequiredArgsConstructor
public class SwaggerHandler {

	private final SecurityConfiguration securityConfiguration;

	private final UiConfiguration uiConfiguration;

	private final SwaggerResourcesProvider swaggerResources;

	public SwaggerHandler(SwaggerResourcesProvider swaggerResources, SecurityConfiguration securityConfiguration, UiConfiguration uiConfiguration) {
		this.swaggerResources = swaggerResources;
		this.securityConfiguration = securityConfiguration;
		this.uiConfiguration = uiConfiguration;
	}

	@GetMapping("/swagger-resources/configuration/security")
	public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
		return Mono.just(new ResponseEntity<>(
				Optional.ofNullable(securityConfiguration).orElse(SecurityConfigurationBuilder.builder().build()),
				HttpStatus.OK));
	}

	@GetMapping("/swagger-resources/configuration/ui")
	public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
		return Mono.just(new ResponseEntity<>(
				Optional.ofNullable(uiConfiguration).orElse(UiConfigurationBuilder.builder().build()), HttpStatus.OK));
	}

	@GetMapping("/swagger-resources")
	public Mono<ResponseEntity> swaggerResources() {
		return Mono.just((new ResponseEntity<>(swaggerResources.get(), HttpStatus.OK)));
	}
}