package org.autosalon.controllers;

import org.autosalon.model.Client;
import org.autosalon.model.Warehouse;
import org.autosalon.service.WarehouseService;
import org.autosalon.service.CatalogService;
import org.autosalon.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private DeliveryService deliveryService;

    //==============================GET==================================

    @GetMapping
    public String getAllWarehouseItems(Model model) {
        model.addAttribute("warehouseItems", warehouseService.getAllWarehouseItems());
        return "warehouse/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("warehouseItem", new Warehouse());
        model.addAttribute("products", catalogService.getAllProducts());
        model.addAttribute("deliveries", deliveryService.getAllDeliveries());
        return "warehouse/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        /*if (warehouseItem.isPresent()) {
            model.addAttribute("warehouseItem", warehouseItem.get());
            model.addAttribute("products", catalogService.getAllProducts());
            model.addAttribute("deliveries", deliveryService.getAllDeliveries());
            return "warehouse/edit";
        }*/

        try {
            Warehouse warehouseItem = warehouseService.getWarehouseItemById(id);
            model.addAttribute("warehouseItem", warehouseItem);
            return "warehouse/edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Warehouse item not found");
            return "error";
        }
    }

    @GetMapping("/available")
    public String getAvailableItems(Model model) {
        model.addAttribute("warehouseItems", warehouseService.getAvailableItems());
        model.addAttribute("title", "Доступные автомобили на складе");
        return "warehouse/list";
    }

    //==============================POST==================================

    @PostMapping("/add")
    public String addWarehouseItem(@ModelAttribute("warehouseItem") Warehouse warehouseItem) {
        warehouseService.saveWarehouseItem(warehouseItem);
        return "redirect:/warehouse";
    }


    @PostMapping("/edit/{id}")
    public String updateWarehouseItem(@PathVariable("id") Long id,
                                      @ModelAttribute("warehouseItem") Warehouse warehouseItem) {
        warehouseItem.setRecordId(id);
        warehouseService.saveWarehouseItem(warehouseItem);
        return "redirect:/warehouse";
    }

    //==============================DELETE==================================

    @GetMapping("/delete/{id}")
    public String deleteWarehouseItem(@PathVariable("id") Long id) {
        warehouseService.deleteWarehouseItem(id);
        return "redirect:/warehouse";
    }


}