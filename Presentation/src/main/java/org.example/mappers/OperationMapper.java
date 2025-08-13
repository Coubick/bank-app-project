package org.example.mappers;

import org.example.dto.OperationDTO;
import org.example.operations_dao.OperationClass;

public class OperationMapper {

    public static OperationDTO toDTO(OperationClass operation) {
        OperationDTO dto = new OperationDTO();
        dto.setOperationId(operation.getOperationId());
        dto.setAccountId(operation.getAccountId());
        dto.setMoneyAmount(operation.getMoneyAmount());
        dto.setOperationType(operation.getOperationType());
        return dto;
    }

    public static OperationClass toEntity(OperationDTO dto) {
        OperationClass operation = new OperationClass();
        operation.setAccountId(dto.getAccountId());
        operation.setMoneyAmount(dto.getMoneyAmount());
        operation.setOperationType(dto.getOperationType());
        return operation;
    }
}
