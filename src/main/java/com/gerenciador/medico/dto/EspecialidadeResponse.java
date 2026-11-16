package com.gerenciador.medico.dto;

import com.gerenciador.medico.model.Especialidade;

public record EspecialidadeResponse(
    Long id,
    String nome,
    String descricao
) {
    public static EspecialidadeResponse de(Especialidade especialidade) {
        return new EspecialidadeResponse(
                especialidade.getId(),
                especialidade.getNome(),
                especialidade.getDescricao()
        );
    }
}

