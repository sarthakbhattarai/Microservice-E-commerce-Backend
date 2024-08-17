package com.Product.Repository;

import com.Product.Entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<Product, Long> {

    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);

}
