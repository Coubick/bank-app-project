package org.example.services;

import org.example.dataStorage.AccountEventRepository;
import org.example.dataStorage.ClientEventRepository;
import org.example.entities.AccountEventEntity;
import org.example.entities.ClientEventEntity;
import org.springframework.stereotype.Repository;

@Repository
public class Service {
    private final AccountEventRepository accountEventRepository;
    private final ClientEventRepository clientEventRepository;

    public Service(AccountEventRepository accountEventRepository, ClientEventRepository clientEventRepository) {
        this.accountEventRepository = accountEventRepository;
        this.clientEventRepository = clientEventRepository;
    }

    public void saveAccount(AccountEventEntity account) {
        accountEventRepository.save(account);
    }

    public void saveClient(ClientEventEntity client) {
        clientEventRepository.save(client);
    }
}