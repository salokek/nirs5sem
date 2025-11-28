package org.autosalon.controllers;

import org.autosalon.model.Catalog;
import org.autosalon.model.Client;
import org.autosalon.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    //==============================GET======================================

    @GetMapping
    public String getAllClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "clients/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            Client client = clientService.getClientById(id);
            model.addAttribute("client", client);
            return "clients/edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Client not found");
            return "error";
        }
    }

    @GetMapping("/search")
    public String searchClients(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("clients", clientService.searchClients(keyword));
        return "clients/list";
    }

    //==============================POST======================================

    @PostMapping("/add")
    public String addClient(@Valid @ModelAttribute("client") Client client,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "clients/add";
        }
        clientService.saveClient(client);
        return "redirect:/clients";
    }

    @PostMapping("/edit/{id}")
    public String updateClient(@PathVariable("id") Long id,
                               @Valid @ModelAttribute("client") Client client,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "clients/edit";
        }
        client.setClientId(id);
        clientService.saveClient(client);
        return "redirect:/clients";
    }

    //==============================DELETE==================================

    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable("id") Long id, Model model) {
        try {
            clientService.deleteClient(id);
            return "redirect:/clients";
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting client");
            return "error"; // Страница ошибки, если товар не найден
        }

    }


}
