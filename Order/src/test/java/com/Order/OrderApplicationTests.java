package com.Order;

import com.Order.Entity.Order;
import com.Order.Repository.OrderRepository;
import com.Order.Service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderApplicationTests {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private RestTemplate restTemplate;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateOrder() {
		// Mock Cart from another service
		OrderService.Cart cart = new OrderService.Cart();
		when(restTemplate.getForObject("http://localhost:8081/api/carts/1", OrderService.Cart.class)).thenReturn(cart);

		// Mock Order entity
		Order order = new Order();
		order.setUserId("user1");
		order.setStatus("CREATED");

		when(orderRepository.save(order)).thenReturn(order);

		// Call the service method
		Order result = orderService.createOrder("1", "user1");

		// Assertions
		assertNotNull(result);
		assertEquals("user1", result.getUserId());
		assertEquals("CREATED", result.getStatus());
	}

	@Test
	public void testGetOrderById() {
		// Mock Order entity
		Order order = new Order();
		order.setId("1");

		when(orderRepository.findById("1")).thenReturn(Optional.of(order));

		// Call the service method
		Optional<Order> result = orderService.getOrderById("1");

		// Assertions
		assertNotNull(result);
		assertEquals(order, result.get());
	}

	@Test
	public void testGetOrdersByUserId() {
		// Mock a list of orders
		List<Order> orders = List.of(new Order(), new Order());
		when(orderRepository.findByUserId("user1")).thenReturn(orders);

		// Call the service method
		List<Order> result = orderService.getOrdersByUserId("user1");

		// Assertions
		assertNotNull(result);
		assertEquals(orders.size(), result.size());
	}

	@Test
	public void testUpdateOrderStatus() {
		// Mock Order entity
		Order order = new Order();
		order.setId("1");
		order.setStatus("CREATED");

		when(orderRepository.findById("1")).thenReturn(Optional.of(order));
		when(orderRepository.save(order)).thenReturn(order);

		// Call the service method
		Order result = orderService.updateOrderStatus("1", "SHIPPED");

		// Assertions
		assertNotNull(result);
		assertEquals("SHIPPED", result.getStatus());
	}
}
