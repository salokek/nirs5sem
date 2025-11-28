package org.autosalon.service;

import lombok.AllArgsConstructor;
import org.autosalon.model.Catalog;
import org.autosalon.model.Delivery;
import org.autosalon.model.Warehouse;
import org.autosalon.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    //================================== GET ============================================

    public List<Warehouse> getAllWarehouseItems() {
        return warehouseRepository.findAll();
    }

    public Warehouse getWarehouseItemById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse record not found"));
    }



    public List<Warehouse> getItemsByProduct(Long productId) {
        return warehouseRepository.findByProductProductId(productId);
    }

    public Integer getTotalQuantityByProduct(Long productId) {
        Integer quantity = warehouseRepository.getTotalQuantityByProductId(productId);
        return quantity != null ? quantity : 0;
    }

    //================================== POST ============================================

    public Warehouse saveWarehouseItem(Warehouse warehouseItem) {
        return warehouseRepository.save(warehouseItem);
    }

    //================================== DELETE ============================================

    public void deleteWarehouseItem(Long id) {
        warehouseRepository.deleteById(id);
    }

    public List<Warehouse> getAvailableItems() {
        return warehouseRepository.findAvailableItems();
    }

    //================================== PUT ============================================

    @Transactional
    public void decreaseStock(Long productId, int quantityNeeded) {

        List<Warehouse> items = warehouseRepository.findByProductProductId(productId);

        int remaining = quantityNeeded;

        for (Warehouse w : items) {
            if (remaining <= 0) break;

            Integer quantity = w.getDelivery().getQuantity();
            if (quantity > 0) {
                int take = Math.min(quantity, remaining);
                quantity -= take;
                remaining -= take;
            }
        }

        if (remaining > 0) {
            throw new RuntimeException("Not enough stock to complete the order");
        }
    }

    public void updateWarehouse(Delivery delivery) {

        Warehouse existingWarehouseItem = warehouseRepository
                .findByProductProductIdAndDeliveryDeliveryId(delivery.getProduct().getProductId(), delivery.getDeliveryId())
                .orElse(null);
        BigDecimal quantityBig = BigDecimal.valueOf(delivery.getQuantity());
        BigDecimal totalAmount = delivery.getProduct().getPrice().multiply(quantityBig);
        if (existingWarehouseItem != null) {
            existingWarehouseItem.setQuantity(existingWarehouseItem.getQuantity() + delivery.getQuantity());

            //existingWarehouseItem.setPurchaseCost(existingWarehouseItem.getPurchaseCost().add(totalAmount));
            warehouseRepository.save(existingWarehouseItem);
        } else {
            Warehouse newWarehouseItem = new Warehouse();
            newWarehouseItem.setProduct(delivery.getProduct());
            newWarehouseItem.setDelivery(delivery);
            newWarehouseItem.setQuantity(delivery.getQuantity());
            //newWarehouseItem.setPurchaseCost(totalAmount);
            warehouseRepository.save(newWarehouseItem);
        }
    }

}
