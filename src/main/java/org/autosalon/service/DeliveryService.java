package org.autosalon.service;

import lombok.RequiredArgsConstructor;
import org.autosalon.model.Delivery;
import org.autosalon.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;
    private final WarehouseService warehouseService;
    private final CatalogService catalogService;

    //================================== GET ============================================

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAllOrderByDateDesc();
    }

    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
    }

    public List<Delivery> getDeliveriesByDateRange(LocalDate startDate, LocalDate endDate) {
        return deliveryRepository.findByDeliveryDateBetween(startDate, endDate);
    }

    public List<Delivery> searchDeliveriesByDate(LocalDate date) {
        return deliveryRepository.findByDeliveryDate(date);
    }

    public List<Delivery> getRecentDeliveries() {
        LocalDate monthAgo = LocalDate.now().minusMonths(1);
        return deliveryRepository.findByDeliveryDateAfter(monthAgo);
    }

    public int countDeliveries(LocalDate start, LocalDate end) {
        return deliveryRepository.countByDeliveryDateBetween(start, end);
    }

    public List<Delivery> getDeliveryItems(Long deliveryId) {
        return deliveryRepository.findByDeliveryId(deliveryId);
    }

    public List<Delivery> getDeliveriesBySupplierId(Long supplierId) {
        // Возвращаем список поставок, отфильтрованных по supplierId
        return deliveryRepository.findBySupplierId(supplierId);
    }

    public BigDecimal getTotalExpenses(LocalDate from, LocalDate to) {
        return deliveryRepository.getTotalPurchaseCost(from, to);  // Метод для получения суммы закупок
    }
    //================================== POST ============================================

    public Delivery saveDelivery(Delivery delivery) {

        Delivery saved = deliveryRepository.save(delivery);

        warehouseService.updateWarehouse(delivery);

        return saved;
    }

    //public Delivery saveDelivery(Delivery delivery) {return deliveryRepository.save(delivery);}

    //================================== DELETE ============================================

    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
    }


}
