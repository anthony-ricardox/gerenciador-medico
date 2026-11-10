package com.gereciador.medico.service;

import com.gereciador.medico.dto.EspecialidadeRequest;
import com.gereciador.medico.dto.EspecialidadeResponse;
import com.gereciador.medico.exception.RecursoNaoEncontradoException;
import com.gereciador.medico.exception.RegraDeNegocioException;
import com.gereciador.medico.model.Especialidade;
import com.gereciador.medico.repository.EspecialidadeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository repository;

    public EspecialidadeService(EspecialidadeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public EspecialidadeResponse criar(EspecialidadeRequest request) {
        String nome = request.nome().trim();
        if (repository.existsByNomeIgnoreCase(nome)) {
            throw new RegraDeNegocioException("Já existe uma especialidade com o nome: " + nome);
        }
        Especialidade nova = new Especialidade(null, nome, request.descricao());
        return EspecialidadeResponse.de(repository.save(nova));
    }

    @Transactional(readOnly = true)
    public List<EspecialidadeResponse> listar() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome")).stream()
                .map(EspecialidadeResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public EspecialidadeResponse buscarPorId(Long id) {
        return EspecialidadeResponse.de(buscarEntidade(id));
    }

    @Transactional
    public EspecialidadeResponse atualizar(Long id, EspecialidadeRequest request) {
        Especialidade especialidade = buscarEntidade(id);
        String nome = request.nome().trim();

        if (repository.existsByNomeIgnoreCaseAndIdNot(nome, id)) {
            throw new RegraDeNegocioException("Já existe uma especialidade com o nome: " + nome);
        }
        especialidade.setNome(nome);
        especialidade.setDescricao(request.descricao());
        return EspecialidadeResponse.de(especialidade);
    }

    @Transactional
    public void deletar(Long id) {
        Especialidade especialidade = buscarEntidade(id);
        repository.delete(especialidade);
    }

    private Especialidade buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada: " + id));
    }
}