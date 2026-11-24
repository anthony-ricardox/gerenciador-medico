package com.gerenciador.medico.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gerenciador.medico.model.Convenio;

public interface ConvenioRepository extends JpaRepository<Convenio, Long> {

    Page<Convenio> findByAtivo(boolean ativo, Pageable pageable);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    boolean existsByRegistroAns(String registroAns);

    boolean existsByRegistroAnsAndIdNot(String registroAns, Long id);
}