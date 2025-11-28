package org.autosalon.controllers;

import org.autosalon.model.Catalog;
import org.autosalon.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    //======================GET================================

    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", catalogService.getAllProducts());
        model.addAttribute("categories", catalogService.getAllCategories());
        return "catalog/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Catalog());
        return "catalog/add";
    }

    @GetMapping("/category/{category}")
    public String getProductsByCategory(@PathVariable("category") String category, Model model) {
        List<Catalog> products = catalogService.getProductsByCategory(category);
        if (products.isEmpty()) {
            model.addAttribute("error", "Нет товаров в данной категории");
        } else {
            model.addAttribute("products", products);
            model.addAttribute("categories", catalogService.getAllCategories());
            model.addAttribute("selectedCategory", category);
        }
        return "catalog/list";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("products", catalogService.searchProducts(keyword));
        model.addAttribute("categories", catalogService.getAllCategories());
        return "catalog/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            Catalog product = catalogService.getProductById(id);

            model.addAttribute("product", product);
            return "catalog/edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Product not found");
            return "error";
        }
    }

    //======================POST================================

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("product") Catalog product,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "catalog/edit";
        }
        product.setProductId(id);
        catalogService.saveProduct(product);
        return "redirect:/catalog";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Catalog product,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "catalog/add";
        }
        catalogService.saveProduct(product);
        return "redirect:/catalog";
    }

    //=====================DELETE===================================

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        try {
            catalogService.deleteProduct(id);
            return "redirect:/catalog"; // Перенаправляем на страницу со списком товаров
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting product");
            return "error"; // Страница ошибки, если товар не найден
        }
    }


}
