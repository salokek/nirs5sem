package org.autosalon.service;

import lombok.AllArgsConstructor;
import org.autosalon.dto.OrderDetailedDTO;
import org.autosalon.model.Catalog;
import org.autosalon.model.Client;
import org.autosalon.model.Order;
import org.autosalon.repository.CatalogRepository;
import org.autosalon.repository.ClientRepository;
import org.autosalon.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final CatalogRepository catalogRepository;

    //================================== GET ============================================
    public List<Order> getAllOrders() {
        return orderRepository.findAllOrderByDateDesc();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public OrderDetailedDTO getOrderDetailed(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Client client = order.getClient();

        Catalog product = order.getProduct();

        OrderDetailedDTO orderDTO = new OrderDetailedDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setTransactionDate(order.getTransactionDate());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setClientFullName(client.getFullName());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setPaymentStatus(order.getPaymentStatus().toString());
        orderDTO.setProductName(product.getName());
        orderDTO.setProductSku(product.getProductId().toString()); // Артикул = productId
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setUnitPrice(product.getPrice());

        return orderDTO;
    }

    public BigDecimal getTotalRevenue(LocalDate from, LocalDate to) {
        return orderRepository.getRevenueBetween(from, to);  // Метод для получения суммы доходов
    }

    public List<Order> getOrdersByClient(Long clientId) {
        return orderRepository.findByClientClientId(clientId);
    }

    public List<Order> getOrdersByStatus(Order.PaymentStatus status) {
        return orderRepository.findByPaymentStatus(status);
    }
    public Map<Order.PaymentStatus, Long> getStatusStatistics(LocalDate start, LocalDate end) {
        List<Object[]> raw = orderRepository.countGroupByStatus(start, end);

        Map<Order.PaymentStatus, Long> result = new EnumMap<>(Order.PaymentStatus.class);

        for (Object[] row : raw) {

            // row[0] → ENUM
            // row[1] → count (Long)
            Order.PaymentStatus status = (Order.PaymentStatus) row[0];
            Long count = (Long) row[1];

            result.put(status, count);
        }

        // Чтобы не было null для отсутствующих статусов:
        for (Order.PaymentStatus s : Order.PaymentStatus.values()) {
            result.putIfAbsent(s, 0L);
        }

        return result;
    }

    public Double getTotalRevenue() {
        Double revenue = orderRepository.getTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    public BigDecimal getTotalRevenueBetween(LocalDate from, LocalDate to) {
        BigDecimal result = orderRepository.getRevenueBetween(from, to);
        return result != null ? result : BigDecimal.ZERO;
    }

    //================================== PUT ============================================

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    //================================== DELETE ============================================

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}
