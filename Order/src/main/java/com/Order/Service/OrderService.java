package com.Order.Service;


import com.Order.Entity.*;
import com.Order.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String CART_SERVICE_URL = "http://localhost:8081/api/carts/";

    public Order createOrder(String cartId, String userId) {
        Cart cart = restTemplate.getForObject(CART_SERVICE_URL + cartId, Cart.class);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        Order order = new Order();
        order.setUserId(userId);
        order.setItems(cart.getItems());
        order.setStatus("CREATED");
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(String id, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }


    // Cart class to map the response from Cart Service
    public static class Cart {
        private List<OrderItem> items;

        public List<OrderItem> getItems() {
            return items;
        }

        public void setItems(List<OrderItem> items) {
            this.items = items;
        }

    }
}

