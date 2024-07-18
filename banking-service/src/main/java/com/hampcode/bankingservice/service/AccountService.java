package com.hampcode.bankingservice.service;

import com.hampcode.bankingservice.exception.ResourceNotFoundException;
import com.hampcode.bankingservice.mapper.AccountMapper;
import com.hampcode.bankingservice.model.dto.AccountRequestDTO;
import com.hampcode.bankingservice.model.dto.AccountResponseDTO;
import com.hampcode.bankingservice.model.entity.Account;
import com.hampcode.bankingservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();
        return accountMapper.convertToListDTO(accounts);
    }

    @Transactional(readOnly = true)
    public AccountResponseDTO getAccountById(Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con el número:" + id));
        return accountMapper.convertToDTO(account);
    }

    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO){
        Account account = accountMapper.convertToEntity(accountRequestDTO);
        account.setCreatedAt(LocalDate.now());

        accountRepository.save(account);
        return accountMapper.convertToDTO(account);
    }

    @Transactional
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO accountRequestDTO){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con el número:" + id));

        account.setAccountNumber(accountRequestDTO.getAccountNumber());
        account.setBalance(accountRequestDTO.getBalance());
        account.setOwnerName(accountRequestDTO.getOwnerName());
        account.setOwnerEmail(accountRequestDTO.getOwnerEmail());
        account.setUpdateAt(LocalDate.now());

        account = accountRepository.save(account);

        return accountMapper.convertToDTO(account);
    }

    @Transactional
    public void deleteAccount(Long id){
        accountRepository.deleteById(id);
    }
}
