package org.example.services;
import org.example.operations_dao.OperationClass;
import org.example.operations_dao.OperationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;

    public OperationServiceImpl(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<OperationClass> filterOperations(int accountId, String operationType) {
        if (operationType == null) {
            return List.of();
        }

        List<OperationClass> allOperations = operationRepository.findAll();
        if (allOperations == null) {
            return List.of();
        }

        return allOperations.stream()
                .filter(Objects::nonNull)
                .filter(operation -> operationType.equals(operation.getOperationType()))
                .filter(operation -> accountId == operation.getAccountId())
                .toList();
    }


    @Override
    public OperationClass addOperation(OperationClass operation) {
        return operationRepository.save(operation);
    }
}
