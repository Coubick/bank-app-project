package org.example.dataStorage;

import org.example.entities.AccountEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountEventRepository extends JpaRepository<AccountEventEntity, Long> {}