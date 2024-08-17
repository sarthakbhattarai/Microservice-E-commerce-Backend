package com.Product;

import com.Product.Entity.Product;
import com.Product.Repository.ProductRepository;
import com.Product.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductApplicationTests {
	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetAllProducts() {
		List<Product> products = Arrays.asList(new Product(), new Product());
		when(productRepository.findAll()).thenReturn(products);

		List<Product> result = productService.getAllProducts();
		assertEquals(2, result.size());
	}

	@Test
	public void testGetProductById() {
		Product product = new Product();
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		Product result = productService.getProductById(1L);
		assertEquals(product, result);
	}

	@Test
	public void testGetProductsByCategory() {
		List<Product> products = Arrays.asList(new Product(), new Product());
		when(productRepository.findByCategory("electronics")).thenReturn(products);

		List<Product> result = productService.getProductsByCategory("electronics");
		assertEquals(2, result.size());
	}
}

