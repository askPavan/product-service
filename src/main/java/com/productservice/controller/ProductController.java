package com.productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;




@RestController
@RequestMapping("/api")
public class ProductController {
	//Without Circuit Breaker
	/*
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/products/{productNo}")
	public ResponseEntity<String> getProducts(@PathVariable("productNo") int productNo){
		System.out.println("Fetching products.... Eureka client app...");
		return restTemplate.getForEntity("http://eureka-discovery-client-app/api/products/"+productNo, String.class);
	} 
	*/
	
	//With Criuit Brekaer
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@GetMapping(value = "/products/{productNo}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> getProducts(@PathVariable("productNo") int productNo) {
		CircuitBreaker cb = null;
		cb = cbFactory.create("eureka-discovery-client-app-cb");

		return cb.run(() -> {
			System.out.println("Fetching Products from Eureka client app...");
			//here no need to send the request to eureka client. instead hit the api GW.
			//return restTemplate.getForEntity("http://eureka-discovery-client-app/api/products/" + productNo, String.class);
			return restTemplate.getForEntity("http://"+"PRODUCT-SERVICE-APIGW"+"/api/products/" + productNo, String.class);
		}, Throwable ->{
			return new ResponseEntity<>("Server is down. please try after sometime.",HttpStatus.OK);
		});
	}

}
