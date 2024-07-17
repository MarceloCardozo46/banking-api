package com.hampcode.bankingservice.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Anotaciones de lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    @Size(min = 5, max = 20, message = "El número de cuenta debe tener entre 5 y 20 carácteres")
    @Pattern(regexp = "[0-9]+", message = "El número de cuenta debe contener solo dígitos")
    private String accountNumber;

    @NotNull(message = "El saldo no puede ser vacío")
    private BigDecimal balance;

    @NotBlank(message = "El nombre del titular no puede ser vacío")
    private String ownerName;

    @NotBlank(message = "El correo electronico no puede ser vacío")
    @Email
    private String ownerEmail;

}
