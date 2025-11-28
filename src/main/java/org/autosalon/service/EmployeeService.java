package org.autosalon.service;

import org.autosalon.model.Employee;
import org.autosalon.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    //================================== GET ============================================

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public List<Employee> searchEmployees(String keyword) {
        return employeeRepository.findByFullNameContainingIgnoreCase(keyword);
    }

    //================================== POST ============================================

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    //================================== DELETE ============================================

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

}
