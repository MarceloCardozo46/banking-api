package com.hampcode.bankingservice.service;

import com.hampcode.bankingservice.exception.BadRequestException;
import com.hampcode.bankingservice.exception.ResourceNotFoundException;
import com.hampcode.bankingservice.mapper.TransactionMapper;
import com.hampcode.bankingservice.model.dto.TransactionReportDTO;
import com.hampcode.bankingservice.model.dto.TransactionRequestDTO;
import com.hampcode.bankingservice.model.dto.TransactionResponseDTO;
import com.hampcode.bankingservice.model.entity.Account;
import com.hampcode.bankingservice.model.entity.Transaction;
import com.hampcode.bankingservice.repository.AccountRepository;
import com.hampcode.bankingservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionByAccountNumber(String accountNumber){
        List<Transaction> transactions = transactionRepository.findBySourceOrTargetAccountNumber(accountNumber);
        return transactionMapper.convertToListDTO(transactions);
    }

    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO){
        // Obtener las cuentas involucradas en la transacción y // Verificar si las cuentas existen
        Account sourceAccount = accountRepository.findByAccountNumber(transactionRequestDTO.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("La cuenta de origen no existe"));

        Account targetAccount = accountRepository.findByAccountNumber(transactionRequestDTO.getTargetAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("La cuenta de destino no existe"));

        // Verificar si el saldo de la cuenta de origen suficiente para realizar la transacción
        BigDecimal amount = transactionRequestDTO.getAmount();
        BigDecimal sourceAccountBalance = sourceAccount.getBalance();

        if(sourceAccountBalance.compareTo(amount) < 0){
            throw new BadRequestException("Saldo insuficiente en la cuenta origen");
        }

        // Realizar la transacción
        Transaction transaction = transactionMapper.convertToEntity(transactionRequestDTO);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction = transactionRepository.save(transaction);

        // Actualizar los saldos de las cuentas
        BigDecimal newSourceAccountBalance = sourceAccountBalance.subtract(amount);
        BigDecimal targetAccountBalance = targetAccount.getBalance().add(amount);

        sourceAccount.setBalance(newSourceAccountBalance);
        targetAccount.setBalance(targetAccountBalance);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        return transactionMapper.convertToDTO(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionReportDTO> generateTransactionReport(String startDateStr, String endDateStr, String accountNumber){
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        List<Object[]> transactionCounts = transactionRepository.
                getTransactionCountByDateRangeAndAccountNumber(startDate, endDate, accountNumber);

        return transactionCounts.stream()
                .map(transactionMapper::convertTransactionReportDTO)
                .toList();
    }




}
