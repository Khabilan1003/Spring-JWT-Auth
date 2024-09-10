package com.app.catalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.catalog.dao.AuthRequest;
import com.app.catalog.dao.Product;
import com.app.catalog.entity.UserInfo;
import com.app.catalog.service.JwtService;
import com.app.catalog.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}

	@PostMapping("/new")
	public String addNewUser(@RequestBody UserInfo userInfo) {
		return productService.addUser(userInfo);
	}

	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public List<Product> getAllProducts() {
		return productService.getProducts();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Product getProductById(@PathVariable int id) {
		return productService.getProduct(id);
	}

	@GetMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		if (!auth.isAuthenticated())
			throw new UsernameNotFoundException("Invalid Credentials");

		return jwtService.generateToken(authRequest.getUsername());
	}
}
