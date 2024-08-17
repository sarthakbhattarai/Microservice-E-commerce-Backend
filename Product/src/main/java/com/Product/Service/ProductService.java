package com.Product.Service;



import com.Product.Entity.Product;
import com.Product.Repository.ProductRepository;
import com.Product.Repository.ProductSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Scheduled(fixedRate = 86400000) // Run once a day
    public void syncProducts() {
        RestTemplate restTemplate = new RestTemplate();
        Product[] products = restTemplate.getForObject("https://fakestoreapi.com/products", Product[].class);
        if (products != null) {
            for (Product product : products) {
                productRepository.save(product);
            }
        }
    }

    @Cacheable("products")
    public Page<Product> getProducts(Double minPrice, Double maxPrice, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findAllByPriceRange(minPrice, maxPrice, pageRequest);
    }
    
    @Cacheable("products")
    public List<Product> searchProducts(String query) {
        return productSearchRepository.findByNameContainingOrDescriptionContaining(query, query);
    }

    @Cacheable("products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Cacheable("products")
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Cacheable("products")
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
}

