package com.hampcode.bankingservice.controller;

import com.hampcode.bankingservice.model.dto.TransactionReportDTO;
import com.hampcode.bankingservice.model.dto.TransactionRequestDTO;
import com.hampcode.bankingservice.model.dto.TransactionResponseDTO;
import com.hampcode.bankingservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // http://localhost:8080/api/v1/transactions/accounts/1
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccountNumber(@PathVariable String accountNumber){
        List<TransactionResponseDTO> transactions = transactionService.getTransactionByAccountNumber(accountNumber);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Validated @RequestBody TransactionRequestDTO transactionRequestDTO){
        TransactionResponseDTO createdTransaction = transactionService.createTransaction(transactionRequestDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @PostMapping("/report")
    public ResponseEntity<List<TransactionReportDTO>> generateTransactionReport(@RequestParam("startDate") String startDateStr,
                                                                                @RequestParam("endDate") String endDateStr,
                                                                                @RequestParam("accountNumber") String accountNumber){

        List<TransactionReportDTO> reportDTOs = transactionService.generateTransactionReport(startDateStr, endDateStr, accountNumber);
        return new ResponseEntity<>(reportDTOs, HttpStatus.OK);

    }
}
