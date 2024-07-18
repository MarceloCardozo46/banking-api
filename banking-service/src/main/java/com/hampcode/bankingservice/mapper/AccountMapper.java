package com.hampcode.bankingservice.mapper;

import com.hampcode.bankingservice.model.dto.AccountRequestDTO;
import com.hampcode.bankingservice.model.dto.AccountResponseDTO;
import com.hampcode.bankingservice.model.entity.Account;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
//@AllArgsConstructor
public class AccountMapper {
    // Si no funciona algo probar :
    // private final ModelMapper modelMapper;
    @Autowired
    private ModelMapper modelMapper;

    public Account convertToEntity(AccountRequestDTO accountRequestDTO){
        return modelMapper.map(accountRequestDTO, Account.class);
    }

    public AccountResponseDTO convertToDTO(Account account){
        return modelMapper.map(account, AccountResponseDTO.class);
    }

    public List<AccountResponseDTO> convertToListDTO(List<Account> accounts){
        /* Mi intento (Probar si funciona después)
        List<AccountResponseDTO> listResponseDTO = new ArrayList<>();
        accounts.forEach(account ->{
            listResponseDTO.add(converToDTO(account));
        });
        return listResponseDTO;
        */
        return accounts.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
