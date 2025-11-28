package org.autosalon.service;

import org.autosalon.model.Client;
import org.autosalon.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    //================================== GET ============================================

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public List<Client> searchClients(String keyword) {
        return clientRepository.findByFullNameContainingIgnoreCase(keyword);
    }

    //================================== PUT ============================================

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    //================================== DELETE ============================================

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }


}