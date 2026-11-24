package com.gerenciador.medico.dto;

import java.time.LocalDate;

import com.gerenciador.medico.model.TipoConvenio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ConvenioRequest(

        @NotBlank(message = "é obrigatório")
        @Size(max = 100, message = "deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "é obrigatório")
        @Pattern(regexp = "\\d{6}", message = "deve ter exatamente 6 dígitos")
        String registroAns,

        @NotNull(message = "é obrigatório")
        TipoConvenio tipo,

        @Size(max = 20, message = "deve ter no máximo 20 caracteres")
        String telefone,

        @NotNull(message = "é obrigatório")
        LocalDate inicioVigencia,

        LocalDate fimVigencia
) {}