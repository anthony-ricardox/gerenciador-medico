package com.gerenciador.medico.dto;

import java.time.LocalDate;

import com.gerenciador.medico.model.Convenio;
import com.gerenciador.medico.model.TipoConvenio;

public record ConvenioResponse(
        Long id,
        String nome,
        String registroAns,
        TipoConvenio tipo,
        String telefone,
        LocalDate inicioVigencia,
        LocalDate fimVigencia,
        boolean ativo,
        boolean vigente
) {

    public static ConvenioResponse de(Convenio c) {
        return new ConvenioResponse(
                c.getId(),
                c.getNome(),
                c.getRegistroAns(),
                c.getTipo(),
                c.getTelefone(),
                c.getInicioVigencia(),
                c.getFimVigencia(),
                c.isAtivo(),
                c.estaVigente()
        );
    }
}