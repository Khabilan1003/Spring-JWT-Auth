package com.app.catalog.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.catalog.dao.Product;
import com.app.catalog.entity.UserInfo;
import com.app.catalog.repository.UserInfoRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ProductService {
	List<Product> products = null;
	
	@Autowired
	private UserInfoRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void loadProductsFromDB() {
		products = IntStream.rangeClosed(1, 100)
				.mapToObj(i -> new Product(i, "product " + i, new Random().nextInt(10), new Random().nextInt(4000)))
				.collect(Collectors.toList());
	}

	public List<Product> getProducts() {
		return products;
	}

	public Product getProduct(int id) {
		return products.stream().filter(product -> product.getProductId() == id).findAny()
				.orElseThrow(() -> new RuntimeException("product " + id + " not found"));
	}
	
	public String addUser(UserInfo userInfo) {
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		repository.save(userInfo);
		return "user added to system";
	}
}
