package org.autosalon.repository;

import org.autosalon.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // Найти поставки по диапазону дат
    List<Delivery> findByDeliveryDateBetween(LocalDate startDate, LocalDate endDate);

    // Найти поставки после указанной даты
    List<Delivery> findByDeliveryDateAfter(LocalDate date);

    // Найти поставки до указанной даты
    List<Delivery> findByDeliveryDateBefore(LocalDate date);

    // Получить все поставки отсортированные по дате (новые сначала)
    @Query("SELECT d FROM Delivery d ORDER BY d.deliveryDate DESC")
    List<Delivery> findAllOrderByDateDesc();

    @Query("""
           SELECT COALESCE(SUM(d.purchaseCost), 0)
           FROM Delivery d
           WHERE d.deliveryDate BETWEEN :startDate AND :endDate
           """)
    BigDecimal getTotalPurchaseCost(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

    // Найти поставки по конкретной дате
    List<Delivery> findByDeliveryDate(LocalDate date);

    List<Delivery> findBySupplierId(Long supplierId);

    List<Delivery> findByDeliveryId(Long deliveryId);

    int countByDeliveryDateBetween(LocalDate start, LocalDate end);
}

