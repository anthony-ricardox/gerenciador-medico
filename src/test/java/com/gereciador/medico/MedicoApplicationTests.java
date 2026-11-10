package com.gereciador.medico;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gereciador.medico.repository.EspecialidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MedicoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EspecialidadeRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void deveExecutarCrudDeEspecialidades() throws Exception {
        MvcResult resultadoCriacao = mockMvc.perform(post("/api/especialidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("  Cardiologia  ", "Cuida do coração")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/especialidades/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Cardiologia"))
                .andReturn();
        long id = objectMapper.readTree(resultadoCriacao.getResponse().getContentAsString())
                .get("id")
                .asLong();
        String recurso = "/api/especialidades/" + id;

        mockMvc.perform(get("/api/especialidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Cardiologia"));

        mockMvc.perform(put(recurso)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("Neurologia", null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Neurologia"));

        mockMvc.perform(get(recurso))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value(nullValue()));

        mockMvc.perform(delete(recurso))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(recurso))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRejeitarNomeEmBranco() throws Exception {
        mockMvc.perform(post("/api/especialidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("   ", null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.nome").value("é obrigatório"));
    }

    @Test
    void deveRejeitarNomeDuplicadoIgnorandoMaiusculasEspacos() throws Exception {
        mockMvc.perform(post("/api/especialidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("Cardiologia", null)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/especialidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("  CARDIOLOGIA  ", null)))
                .andExpect(status().isConflict());
    }

    @Test
    void deveListarEspecialidadesEmOrdemAlfabetica() throws Exception {
        mockMvc.perform(post("/api/especialidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("Neurologia", null)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/especialidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("Cardiologia", null)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/especialidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Cardiologia"))
                .andExpect(jsonPath("$[1].nome").value("Neurologia"));
    }

    @Test
    void deveRejeitarAtualizacaoComNomeDuplicado() throws Exception {
        criar("Cardiologia", null);
        long id = criar("Neurologia", null);

        mockMvc.perform(put("/api/especialidades/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("  CARDIOLOGIA ", null)))
                .andExpect(status().isConflict());
    }

    private long criar(String nome, String descricao) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/especialidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(nome, descricao)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    private String json(String nome, String descricao) throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("nome", nome);
        request.put("descricao", descricao);
        return objectMapper.writeValueAsString(request);
    }
}
