package org.autosalon.controllers;

import org.autosalon.model.Delivery;
import org.autosalon.model.Employee;
import org.autosalon.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //======================GET================================

    @GetMapping
    public String getAllEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/add";
    }

    @GetMapping("/search")
    public String searchEmployees(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("employees", employeeService.searchEmployees(keyword));
        return "employees/list";
    }

    //======================POST================================

    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute("employee") Employee employee,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "employees/add";
        }
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            model.addAttribute("employee", employee);
            return "employees/edit";

        } catch (RuntimeException e) {
            model.addAttribute("error", "Employee not found");
            return "error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return "employees/edit";
        }
        employee.setEmployeeId(id);
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    //======================DELETE================================

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id, Model model) {
        try {
            employeeService.deleteEmployee(id);
            return "redirect:/employees";
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting employee");
            return "error";
        }
    }

}
