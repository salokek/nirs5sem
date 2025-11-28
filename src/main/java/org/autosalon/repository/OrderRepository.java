package org.autosalon.repository;

import org.autosalon.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientClientId(Long clientId);
    List<Order> findByEmployeeEmployeeId(Long employeeId);
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);
    List<Order> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT o FROM Order o ORDER BY o.transactionDate DESC")
    List<Order> findAllOrderByDateDesc();

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.paymentStatus = 'PAID'")
    Double getTotalRevenue();

    @Query("SELECT SUM(o.totalAmount) FROM Order o " +
            "WHERE o.transactionDate BETWEEN :from AND :to " +
            "AND o.paymentStatus = 'PAID'")
    BigDecimal getRevenueBetween(LocalDate from, LocalDate to);


    @Query("SELECT o.paymentStatus, COUNT(o) FROM Order o " +
            "WHERE o.orderDate BETWEEN :start AND :end " +
            "GROUP BY o.paymentStatus")
    List<Object[]> countGroupByStatus(@Param("start") LocalDate start,
                                      @Param("end") LocalDate end);
}