package org.example.dataStorage;

import org.example.entities.ClientEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientEventRepository extends JpaRepository<ClientEventEntity, Long> {}