package org.autosalon.controllers;

import org.autosalon.model.Order;
import org.autosalon.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/")
    public String home(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("totalClients", clientService.getAllClients().size());
        model.addAttribute("totalEmployees", employeeService.getAllEmployees().size());
        model.addAttribute("totalProducts", catalogService.getAllProducts().size());
        BigDecimal revenue = orderService.getTotalRevenueBetween(LocalDate.of(1900,1,1), today);
        BigDecimal expenses = deliveryService.getTotalExpenses(LocalDate.of(1900,1,1), today);

        BigDecimal profit = revenue.subtract(expenses);
        model.addAttribute("totalRevenue", profit);
        return "index";
    }

    // Контроллер для получения доходов за период
    @GetMapping("/income")
    public ResponseEntity<BigDecimal> getIncomeReport(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        BigDecimal totalIncome = orderService.getTotalRevenueBetween(startDate, endDate);
        return ResponseEntity.ok(totalIncome);
    }

    // Контроллер для получения расходов за период
    @GetMapping("/expenses")
    public ResponseEntity<BigDecimal> getExpensesReport(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        BigDecimal totalExpenses = deliveryService.getTotalExpenses(startDate, endDate);
        return ResponseEntity.ok(totalExpenses);
    }

    @GetMapping("/statistics")
    public String showStatistics(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model
    ) {
        // Если даты не переданы – берём текущий месяц
        if (startDate == null || endDate == null) {
            YearMonth ym = YearMonth.now();
            startDate = ym.atDay(1);
            endDate = ym.atEndOfMonth();
        }

        BigDecimal revenue = orderService.getTotalRevenueBetween(startDate, endDate);
        BigDecimal expenses = deliveryService.getTotalExpenses(startDate, endDate);

        if (revenue == null) revenue = BigDecimal.ZERO;
        if (expenses == null) expenses = BigDecimal.ZERO;

        BigDecimal profit = revenue.subtract(expenses);

        // Количество товаров
        int productsCount = catalogService.getAllProducts().size();

        // Количество поставок
        int deliveriesCount = deliveryService.countDeliveries(startDate, endDate);

        // Статистика по статусам
        Map<Order.PaymentStatus, Long> statusStats =
                orderService.getStatusStatistics(startDate, endDate);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("revenue", revenue);
        model.addAttribute("expenses", expenses);
        model.addAttribute("profit", profit);

        model.addAttribute("productsCount", productsCount);
        model.addAttribute("deliveriesCount", deliveriesCount);
        model.addAttribute("statusStats", statusStats);


        return "statistics";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Можно добавить больше статистики для дашборда
        return "dashboard";
    }
}
