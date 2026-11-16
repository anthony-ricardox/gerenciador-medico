package com.gerenciador.medico.service;

import com.gerenciador.medico.dto.MedicoRequest;
import com.gerenciador.medico.dto.MedicoResponse;
import com.gerenciador.medico.exception.RecursoNaoEncontradoException;
import com.gerenciador.medico.exception.RegraDeNegocioException;
import com.gerenciador.medico.model.Especialidade;
import com.gerenciador.medico.model.Medico;
import com.gerenciador.medico.repository.EspecialidadeRepository;
import com.gerenciador.medico.repository.MedicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

    private final MedicoRepository repository;
    private final EspecialidadeRepository especialidadeRepository;

    public MedicoService(MedicoRepository repository,
                         EspecialidadeRepository especialidadeRepository) {
        this.repository = repository;
        this.especialidadeRepository = especialidadeRepository;
    }

    @Transactional
    public MedicoResponse criar(MedicoRequest request) {
        String crmUf = request.crmUf().toUpperCase();
        String email = request.email().trim().toLowerCase();

        if (repository.existsByCrmAndCrmUf(request.crm(), crmUf)) {
            throw new RegraDeNegocioException(
                    "Já existe um médico com o CRM " + request.crm() + "/" + crmUf);
        }
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new RegraDeNegocioException("Já existe um médico com o e-mail " + email);
        }

        Especialidade especialidade = buscarEspecialidade(request.especialidadeId());

        Medico medico = new Medico(
                request.nome().trim(),
                request.crm(),
                crmUf,
                email,
                request.telefone(),
                especialidade
        );

        return MedicoResponse.de(repository.save(medico));
    }

    @Transactional(readOnly = true)
    public Page<MedicoResponse> listar(Long especialidadeId, Pageable pageable) {
        Page<Medico> pagina = (especialidadeId == null)
                ? repository.findAll(pageable)
                : repository.findByEspecialidadeId(especialidadeId, pageable);

        return pagina.map(MedicoResponse::de);
    }

    @Transactional(readOnly = true)
    public MedicoResponse buscarPorId(Long id) {
        return MedicoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public MedicoResponse atualizar(Long id, MedicoRequest request) {
        Medico medico = buscarEntidade(id);

        String crmUf = request.crmUf().toUpperCase();
        String email = request.email().trim().toLowerCase();

        if (repository.existsByCrmAndCrmUfAndIdNot(request.crm(), crmUf, id)) {
            throw new RegraDeNegocioException(
                    "Já existe um médico com o CRM " + request.crm() + "/" + crmUf);
        }
        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new RegraDeNegocioException("Já existe um médico com o e-mail " + email);
        }

        medico.setNome(request.nome().trim());
        medico.setCrm(request.crm());
        medico.setCrmUf(crmUf);
        medico.setEmail(email);
        medico.setTelefone(request.telefone());
        medico.setEspecialidade(buscarEspecialidade(request.especialidadeId()));

        return MedicoResponse.de(medico);
    }

    @Transactional
    public void inativar(Long id) {
        buscarEntidade(id).setAtivo(false);
    }

    @Transactional
    public void ativar(Long id) {
        buscarEntidade(id).setAtivo(true);
    }

    private Medico buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado: " + id));
    }

    private Especialidade buscarEspecialidade(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada: " + id));
    }
}