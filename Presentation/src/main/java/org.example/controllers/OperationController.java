package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.services.OperationService;
import org.example.dto.OperationDTO;
import org.example.mappers.OperationMapper;
import org.example.operations_dao.OperationClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/bank_system/operations")
@Tag(name = "Operations", description = "Operation management API")
public class OperationController {

    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @Operation(summary = "Filter operations by account ID and operation type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered operations list"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/filter")
    public ResponseEntity<List<OperationDTO>> filterOperations(
            @RequestParam int accountId,
            @RequestParam String operationType) {

        try {
            List<OperationClass> operations = operationService.filterOperations(accountId, operationType);
            return ResponseEntity.ok(operations.stream().map(OperationMapper::toDTO).toList());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build(); // 400
        }
    }
}
