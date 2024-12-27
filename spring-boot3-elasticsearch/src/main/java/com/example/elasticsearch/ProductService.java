package com.example.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 获取所有产品
    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }

    // 根据ID获取产品
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    // 创建产品
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // 更新产品
    public Product updateProduct(String id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            return productRepository.save(product);
        }
        return null;
    }

    // 删除产品
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}
