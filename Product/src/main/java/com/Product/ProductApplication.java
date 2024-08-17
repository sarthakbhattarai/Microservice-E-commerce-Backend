package com.Product;

import com.Product.Entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProductApplication {
	@Autowired
	static Category cat;

	public static void main(String[] args) {
		ApplicationContext context=SpringApplication.run(ProductApplication.class, args);
	}

}
