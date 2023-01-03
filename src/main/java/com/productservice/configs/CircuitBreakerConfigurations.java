package com.productservice.configs;

import java.time.Duration;
import java.util.function.Function;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class CircuitBreakerConfigurations {

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> customize() {
		return new Customizer<Resilience4JCircuitBreakerFactory>() {
			@Override
			public void customize(Resilience4JCircuitBreakerFactory tocustomize) {
				tocustomize.configureDefault(new Function<String, Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration>() {
					@Override
					public Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration apply(String t) {
						Resilience4JConfigBuilder builder = new Resilience4JConfigBuilder("eureka-client-cb");
						//below we can add other configurations also
						//it will wait for the response from the server for 6 Seconds.
						return builder.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(6)).build()).build();
					}
				});
			}
		};
	}
}
