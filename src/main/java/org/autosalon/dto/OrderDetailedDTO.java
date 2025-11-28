package org.autosalon.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Setter
@Getter
public class OrderDetailedDTO {
    private Long orderId;
    private LocalDate transactionDate;
    private LocalDate orderDate;
    private String clientFullName;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String productName;
    private String productSku; // Артикул
    private Integer quantity;
    private BigDecimal unitPrice;

    // Конструкторы, геттеры и сеттеры
}
