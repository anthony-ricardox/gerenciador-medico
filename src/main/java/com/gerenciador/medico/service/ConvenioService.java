package com.gerenciador.medico.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerenciador.medico.dto.ConvenioRequest;
import com.gerenciador.medico.dto.ConvenioResponse;
import com.gerenciador.medico.exception.RecursoNaoEncontradoException;
import com.gerenciador.medico.exception.RegraDeNegocioException;
import com.gerenciador.medico.model.Convenio;
import com.gerenciador.medico.repository.ConvenioRepository;

@Service
public class ConvenioService {

    private final ConvenioRepository repository;

    public ConvenioService(ConvenioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ConvenioResponse criar(ConvenioRequest request) {
        validarVigencia(request.inicioVigencia(), request.fimVigencia());

        String nome = request.nome().trim();
        if (repository.existsByNomeIgnoreCase(nome)) {
            throw new RegraDeNegocioException("Já existe um convênio com o nome: " + nome);
        }
        if (repository.existsByRegistroAns(request.registroAns())) {
            throw new RegraDeNegocioException("Já existe um convênio com o registro ANS " + request.registroAns());
        }

        Convenio convenio = new Convenio(
                nome,
                request.registroAns(),
                request.tipo(),
                request.telefone(),
                request.inicioVigencia(),
                request.fimVigencia()
        );

        return ConvenioResponse.de(repository.save(convenio));
    }

    @Transactional(readOnly = true)
    public Page<ConvenioResponse> listar(Boolean ativo, Pageable pageable) {
        Page<Convenio> pagina = (ativo == null)
                ? repository.findAll(pageable)
                : repository.findByAtivo(ativo, pageable);

        return pagina.map(ConvenioResponse::de);
    }

    @Transactional(readOnly = true)
    public ConvenioResponse buscarPorId(Long id) {
        return ConvenioResponse.de(buscarEntidade(id));
    }

    @Transactional
    public ConvenioResponse atualizar(Long id, ConvenioRequest request) {
        Convenio convenio = buscarEntidade(id);

        validarVigencia(request.inicioVigencia(), request.fimVigencia());

        String nome = request.nome().trim();
        if (repository.existsByNomeIgnoreCaseAndIdNot(nome, id)) {
            throw new RegraDeNegocioException("Já existe um convênio com o nome: " + nome);
        }
        if (repository.existsByRegistroAnsAndIdNot(request.registroAns(), id)) {
            throw new RegraDeNegocioException("Já existe um convênio com o registro ANS " + request.registroAns());
        }

        convenio.setNome(nome);
        convenio.setRegistroAns(request.registroAns());
        convenio.setTipo(request.tipo());
        convenio.setTelefone(request.telefone());
        convenio.setInicioVigencia(request.inicioVigencia());
        convenio.setFimVigencia(request.fimVigencia());

        return ConvenioResponse.de(convenio);
    }

    @Transactional
    public void inativar(Long id) {
        buscarEntidade(id).setAtivo(false);
    }

    @Transactional
    public void ativar(Long id) {
        buscarEntidade(id).setAtivo(true);
    }

    private void validarVigencia(LocalDate inicio, LocalDate fim) {
        if (fim != null && fim.isBefore(inicio)) {
            throw new RegraDeNegocioException("A data final da vigência não pode ser anterior à data inicial");
        }
    }

    private Convenio buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Convênio não encontrado: " + id));
    }
}