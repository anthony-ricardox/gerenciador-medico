package com.gereciador.medico.dto;

import com.gereciador.medico.model.Especialidade;

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
