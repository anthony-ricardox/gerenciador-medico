package com.gerenciador.medico.dto;

import com.gerenciador.medico.model.Medico;

public record MedicoResponse(
        Long id,
        String nome,
        String crm,
        String crmUf,
        String email,
        String telefone,
        boolean ativo,
        EspecialidadeResponse especialidade
) {

    public static MedicoResponse de(Medico medico) {
        return new MedicoResponse(
                medico.getId(),
                medico.getNome(),
                medico.getCrm(),
                medico.getCrmUf(),
                medico.getEmail(),
                medico.getTelefone(),
                medico.isAtivo(),
                EspecialidadeResponse.de(medico.getEspecialidade())
        );
    }
}