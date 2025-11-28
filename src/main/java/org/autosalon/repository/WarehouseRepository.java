package org.autosalon.repository;

import org.autosalon.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByProductProductId(Long productId);

    @Query("SELECT w FROM Warehouse w WHERE w.delivery.quantity > 0")
    List<Warehouse> findAvailableItems();

    @Query("SELECT SUM(w.delivery.quantity) FROM Warehouse w WHERE w.product.productId = :productId")
    Integer getTotalQuantityByProductId(@Param("productId") Long productId);

    Optional<Warehouse> findByProductProductIdAndDeliveryDeliveryId(Long productId, Long deliveryId);

}