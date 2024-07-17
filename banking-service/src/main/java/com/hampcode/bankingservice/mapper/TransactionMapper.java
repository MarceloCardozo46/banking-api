package com.hampcode.bankingservice.mapper;

import com.hampcode.bankingservice.model.dto.TransactionReportDTO;
import com.hampcode.bankingservice.model.dto.TransactionRequestDTO;
import com.hampcode.bankingservice.model.dto.TransactionResponseDTO;
import com.hampcode.bankingservice.model.entity.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TransactionMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Transaction convertToEntity(TransactionRequestDTO transactionRequestDTO){
        return modelMapper.map(transactionRequestDTO, Transaction.class);
    }

    public TransactionResponseDTO convertToDTO(Transaction transaction){
        return modelMapper.map(transaction, TransactionResponseDTO.class);
    }

    public List<TransactionResponseDTO> convertToListDTO(List<Transaction> transactions){
        return transactions.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public TransactionReportDTO convertTransactionReportDTO(Object[] transactionData){
        TransactionReportDTO reportDTO = new TransactionReportDTO();
        reportDTO.setDate((LocalDate) transactionData[0]);
        reportDTO.setTransactionCount((Long) transactionData[1]);

        return reportDTO;
    }


}
