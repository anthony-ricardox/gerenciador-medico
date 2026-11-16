package com.gerenciador.medico.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MedicoRequest(

        @NotBlank(message = "é obrigatório")
        @Size(max = 150, message = "deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "é obrigatório")
        @Pattern(regexp = "\\d{4,10}", message = "deve conter de 4 a 10 dígitos")
        String crm,

        @NotBlank(message = "é obrigatório")
        @Pattern(regexp = "[A-Za-z]{2}", message = "deve ser a sigla do estado com 2 letras")
        String crmUf,

        @NotBlank(message = "é obrigatório")
        @Email(message = "formato inválido")
        @Size(max = 150, message = "deve ter no máximo 150 caracteres")
        String email,

        @Size(max = 20, message = "deve ter no máximo 20 caracteres")
        String telefone,

        @NotNull(message = "é obrigatório")
        Long especialidadeId
) {}