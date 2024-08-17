package com.Cart.Service;

import com.Cart.Entity.Cart;
import com.Cart.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private static final String PRODUCT_SERVICE_URL = "http://localhost:8080/api/products/";
    private static final long CART_EXPIRATION_TIME = 7; // 7 days

    public Cart createCart(Cart cart) {
        for (Cart.CartItem item : cart.getItems()) {
            Boolean available = restTemplate.getForObject(PRODUCT_SERVICE_URL + item.getProductId() + "/available", Boolean.class);
            if (available == null || !available) {
                throw new RuntimeException("Product with ID " + item.getProductId() + " is not available.");
            }
        }
        Cart savedCart = cartRepository.save(cart);
        redisTemplate.expire(savedCart.getId(), CART_EXPIRATION_TIME, TimeUnit.DAYS);
        return savedCart;
    }

    public Optional<Cart> getCartById(String id) {
        return cartRepository.findById(id);
    }

    public Cart updateCart(String id, Cart cartDetails) {
        for (Cart.CartItem item : cartDetails.getItems()) {
            Boolean available = restTemplate.getForObject(PRODUCT_SERVICE_URL + item.getProductId() + "/available", Boolean.class);
            if (available == null || !available) {
                throw new RuntimeException("Product with ID " + item.getProductId() + " is not available.");
            }
        }
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.setItems(cartDetails.getItems());
            redisTemplate.expire(cart.getId(), CART_EXPIRATION_TIME, TimeUnit.DAYS);
            return cartRepository.save(cart);
        }
        return null;
    }

    public void deleteCart(String id) {
        cartRepository.deleteById(id);
    }
}

