package org.autosalon.repository;

import org.autosalon.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    // Найти все товары по категории
    List<Catalog> findByCategory(String category);

    // Найти товары по названию (поиск)
    List<Catalog> findByNameContainingIgnoreCase(String name);

    // Найти товары в ценовом диапазоне
    List<Catalog> findByPriceBetween(Double minPrice, Double maxPrice);

    // Получить все уникальные категории
    @Query("SELECT DISTINCT c.category FROM Catalog c")
    List<String> findAllCategories();

    // Найти товары дороже указанной цены
    List<Catalog> findByPriceGreaterThan(Double price);
}
