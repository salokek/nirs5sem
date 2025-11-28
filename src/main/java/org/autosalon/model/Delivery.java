package org.autosalon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "deliveries")
public class Delivery {

    @Setter
    @Column(name = "record_id")
    private Long recordId;

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @Setter
    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Setter
    @Column(nullable = false)
    private Integer quantity;

    @Setter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Setter
    @Column(name = "supplier_id")
    private Long supplierId;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Warehouse> warehouseItems = new ArrayList<>();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Catalog product;

    @Setter
    @Column(name = "purchase_cost", nullable = false)
    private BigDecimal purchaseCost;

    public Delivery() {}

    public Delivery(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры

    /*public Catalog getProduct() { return product; }

    public Long getDeliveryId() { return deliveryId; }

    public LocalDate getDeliveryDate() { return deliveryDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Warehouse> getWarehouseItems() { return warehouseItems; }
    public void setWarehouseItems(List<Warehouse> warehouseItems) { this.warehouseItems = warehouseItems; }*/
}
