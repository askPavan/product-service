package com.productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class ProductController {
	
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/products/{productNo}")
	public ResponseEntity<String> getProducts(@PathVariable("productNo") int productNo){
		System.out.println("Fetching products....");
		return restTemplate.getForEntity("http://eureka-discovery-client-app/api/products/"+productNo, String.class);
	}
}
