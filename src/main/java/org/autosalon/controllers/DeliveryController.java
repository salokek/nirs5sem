package org.autosalon.controllers;

import org.autosalon.model.Delivery;
import org.autosalon.service.CatalogService;
import org.autosalon.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private CatalogService catalogService;

    //======================GET================================

    @GetMapping
    public String getAllDeliveries(Model model) {
        model.addAttribute("deliveries", deliveryService.getAllDeliveries());
        return "deliveries/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("delivery", new Delivery());
        model.addAttribute("products", catalogService.getAllProducts());
        return "deliveries/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            Delivery delivery = deliveryService.getDeliveryById(id);
            model.addAttribute("delivery", delivery);
            return "deliveries/edit";

        } catch (RuntimeException e) {
            model.addAttribute("error", "Product not found");
            return "error";
        }
    }

    @GetMapping("/search")
    public String searchDeliveries(@RequestParam(value = "date", required = false) String dateString,
                                   Model model) {
        if (dateString != null && !dateString.isEmpty()) {
            LocalDate date = LocalDate.parse(dateString);
            List<Delivery> deliveries = deliveryService.searchDeliveriesByDate(date);
            model.addAttribute("deliveries", deliveries);
            model.addAttribute("searchDate", dateString);
        } else {
            model.addAttribute("deliveries", deliveryService.getAllDeliveries());
        }

        return "deliveries/list";
    }

    @GetMapping("/supplier/{supplierId}")
    public List<Delivery> getDeliveriesBySupplier(@PathVariable Long supplierId) {
        return deliveryService.getDeliveriesBySupplierId(supplierId);
    }

    @GetMapping("/recent")
    public String getRecentDeliveries(Model model) {
        model.addAttribute("deliveries", deliveryService.getRecentDeliveries());
        model.addAttribute("title", "Последние поставки (за последний месяц)");
        return "deliveries/list";
    }

    @GetMapping("/details/{id}")
    public String getDeliveryDetails(@PathVariable("id") Long id, Model model) {
        Delivery delivery = deliveryService.getDeliveryById(id);
        model.addAttribute("delivery", delivery);
        return "deliveries/details";
    }

    //======================POST================================

    @PostMapping("/add")
    public String addDelivery(@Valid @ModelAttribute("delivery") Delivery delivery,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "deliveries/add";
        }
        if (delivery.getDeliveryDate() == null) {
            delivery.setDeliveryDate(LocalDate.now());
        }
        deliveryService.saveDelivery(delivery);
        return "redirect:/deliveries";
    }

    @PostMapping("/edit/{id}")
    public String updateDelivery(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("delivery") Delivery delivery,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return "deliveries/edit";
        }

        delivery.setRecordId(id);
        deliveryService.saveDelivery(delivery);
        return "redirect:/deliveries";
    }

    //=====================DELETE==============================

    @GetMapping("/delete/{id}")
    public String deleteDelivery(@PathVariable("id") Long id, Model model) {
        try {
            deliveryService.deleteDelivery(id);
            return "redirect:/deliveries"; // Перенаправляем на страницу поставок
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting delivery");
            return "error"; // Страница ошибки, если поставка не найдена
        }
    }

}