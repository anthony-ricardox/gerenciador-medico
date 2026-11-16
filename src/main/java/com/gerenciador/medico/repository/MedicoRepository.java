package com.gerenciador.medico.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gerenciador.medico.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @Override
    @EntityGraph(attributePaths = "especialidade")
    Page<Medico> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "especialidade")
    Page<Medico> findByEspecialidadeId(Long especialidadeId, Pageable pageable);

    boolean existsByCrmAndCrmUf(String crm, String crmUf);

    boolean existsByCrmAndCrmUfAndIdNot(String crm, String crmUf, Long id);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByEspecialidadeId(Long especialidadeId);
}