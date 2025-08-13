package org.example.services;

import org.example.operations_dao.OperationClass;

import java.util.List;

public interface OperationService {
    List<OperationClass> filterOperations(int accountId, String operationType);

    OperationClass addOperation(OperationClass operation);
}
