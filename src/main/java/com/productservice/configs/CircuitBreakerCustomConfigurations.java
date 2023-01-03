package com.productservice.configs;

import java.time.Duration;
import java.util.function.Consumer;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class CircuitBreakerCustomConfigurations {
	
	/*
	 * How to create custom configurations?
	 */
	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> slowCustomizer(){
		return new Customizer<Resilience4JCircuitBreakerFactory>() {
			@Override
			public void customize(Resilience4JCircuitBreakerFactory factory) {
				factory.configure(new Consumer<Resilience4JConfigBuilder>() {
					@Override
					public void accept(Resilience4JConfigBuilder builder) {
						builder.timeLimiterConfig(
								TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(6)).build())
								.circuitBreakerConfig(
										CircuitBreakerConfig.custom().failureRateThreshold(50).slidingWindowSize(2).waitDurationInOpenState(Duration.ofSeconds(20)).build())
								.build();
					}
				}, "eureka-client-slow-cb");
			}
		};
	}
}
