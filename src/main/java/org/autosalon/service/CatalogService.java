package org.autosalon.service;

import org.autosalon.model.Catalog;
import org.autosalon.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    //================================== GET ============================================

    public List<Catalog> getAllProducts() {
        return catalogRepository.findAll();
    }

    public Catalog getProductById(Long id) {
        return catalogRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public List<Catalog> getProductsByCategory(String category) {
        return catalogRepository.findByCategory(category);
    }

    public List<Catalog> searchProducts(String keyword) {
        return catalogRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<String> getAllCategories() {
        return catalogRepository.findAll()
                .stream()
                .map(Catalog::getCategory)
                .distinct()
                .toList();
    }

    //================================== PUT ============================================

    public Catalog saveProduct(Catalog product) {
        return catalogRepository.save(product);
    }

    //================================== DELETE ============================================

    public void deleteProduct(Long id) {
        catalogRepository.deleteById(id);
    }

}
