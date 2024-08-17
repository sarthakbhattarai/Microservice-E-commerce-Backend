package com.Cart;

import com.Cart.Entity.Cart;
import com.Cart.Repository.CartRepository;
import com.Cart.Service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartApplicationTests {
	@InjectMocks
	private CartService cartService;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private RedisTemplate<String, Object> redisTemplate;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateCart() {
		Cart cart = new Cart();
		when(cartRepository.save(cart)).thenReturn(cart);

		Cart result = cartService.createCart(cart);
		assertEquals(cart, result);
		verify(redisTemplate).expire(cart.getId(), 7, TimeUnit.DAYS);
	}

	@Test
	public void testGetCartById() {
		Cart cart = new Cart();
		when(cartRepository.findById("1")).thenReturn(Optional.of(cart));

		Optional<Cart> result = cartService.getCartById("1");
		assertEquals(cart, result.get());
	}

	@Test
	public void testUpdateCart() {
		Cart existingCart = new Cart();
		existingCart.setItems(List.of(new Cart.CartItem(1L, 2)));

		when(cartRepository.findById("1")).thenReturn(Optional.of(existingCart));
		when(cartRepository.save(existingCart)).thenReturn(existingCart);

		Cart updatedDetails = new Cart();
		updatedDetails.setItems(List.of(new Cart.CartItem(1L, 3)));

		Cart result = cartService.updateCart("1", updatedDetails);
		assertEquals(updatedDetails.getItems(), result.getItems());
		verify(redisTemplate).expire(existingCart.getId(), 7, TimeUnit.DAYS);
	}

	@Test
	public void testDeleteCart() {
		cartService.deleteCart("1");
		verify(cartRepository).deleteById("1");
	}
}
