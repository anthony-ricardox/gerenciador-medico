CREATE TABLE medico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    crm VARCHAR(10) NOT NULL,
    crm_uf VARCHAR(2) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    especialidade_id BIGINT NOT NULL,
    CONSTRAINT uk_medico_crm UNIQUE (crm, crm_uf),
    CONSTRAINT fk_medico_especialidade
        FOREIGN KEY (especialidade_id) REFERENCES especialidade (id)
);