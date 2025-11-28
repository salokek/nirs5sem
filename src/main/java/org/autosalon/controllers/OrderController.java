package org.autosalon.controllers;

import org.autosalon.dto.OrderDetailedDTO;
import org.autosalon.model.*;
import org.autosalon.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private EmployeeService employeeService;

    //======================GET================================

    @GetMapping
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("products", catalogService.getAllProducts());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("paymentStatuses", Order.PaymentStatus.values());
        return "orders/add";
    }

    @GetMapping("/detailed/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {
        try {
            OrderDetailedDTO orderDetails = orderService.getOrderDetailed(id);
            model.addAttribute("orderDetails", orderDetails); // Добавляем атрибут в модель
            return "order/detailed"; // Возвращаем имя Thymeleaf-шаблона
        } catch (RuntimeException e) {
            model.addAttribute("error", "Order not found");
            return "error"; // Перенаправляем на страницу ошибки
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            // Пытаемся получить заказ по ID
            Order order = orderService.getOrderById(id);

            // Если заказ найден, добавляем данные в модель
            model.addAttribute("order", order);
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("products", catalogService.getAllProducts());
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("paymentStatuses", Order.PaymentStatus.values());

            // Возвращаем страницу редактирования заказа
            return "orders/edit";
        } catch (RuntimeException e) {
            // Если заказ не найден, перенаправляем на страницу с ошибкой или на список заказов
            model.addAttribute("error", "Order not found");
            return "error";  // Перенаправляем на список заказов
        }
    }

    @GetMapping("/client/{clientId}")
    public String getOrdersByClient(@PathVariable("clientId") Long clientId, Model model) {
        model.addAttribute("orders", orderService.getOrdersByClient(clientId));
        return "orders/list";
    }

    @GetMapping("/status/{status}")
    public String getOrdersByStatus(@PathVariable("status") Order.PaymentStatus status,
                                    Model model) {

        List<Order> orders = orderService.getOrdersByStatus(status);

        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", status);

        return "orders/list";  // твой шаблон списка заказов
    }

    //======================POST================================

    @PostMapping("/add")
    public String addOrder(@Valid @ModelAttribute("order") Order order,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("products", catalogService.getAllProducts());
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("paymentStatuses", Order.PaymentStatus.values());
            return "orders/add";
        }

        // Автоматический расчет общей стоимости
        if (order.getProduct() != null && order.getQuantity() != null) {
            BigDecimal total = order.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(order.getQuantity()));
            order.setTotalAmount(total);
        }

        if (order.getTransactionDate() == null) {
            order.setTransactionDate(LocalDate.now());
        }

        orderService.saveOrder(order);
        return "redirect:/orders";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("order") Order order,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("products", catalogService.getAllProducts());
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("paymentStatuses", Order.PaymentStatus.values());
            return "orders/edit";
        }

        // Пересчет общей стоимости
        if (order.getProduct() != null && order.getQuantity() != null) {
            BigDecimal total = order.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(order.getQuantity()));
            order.setTotalAmount(total);
        }

        order.setOrderId(id);
        orderService.saveOrder(order);
        return "redirect:/orders";
    }

    @PostMapping("/update-status/{id}")
    public String updateOrderStatus(@PathVariable("id") Long id,
                                    @RequestParam("status") Order.PaymentStatus status,
                                    Model model) {
        try {
            // Получаем заказ по ID
            Order order = orderService.getOrderById(id);

            // Обновляем статус оплаты
            order.setPaymentStatus(status);
            orderService.saveOrder(order);

            // Перенаправляем на страницу заказов
            return "redirect:/orders";
        } catch (RuntimeException e) {
            // Если заказ не найден или произошла другая ошибка, показываем ошибку
            model.addAttribute("error", "Order not found or other error occurred");
            return "error"; // Страница с ошибкой
        }
    }

    //======================DELETE================================

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id, Model model) {
        try {
            orderService.deleteOrder(id);
            return "redirect:/orders";
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting order");
            return "error";
        }
    }

}
