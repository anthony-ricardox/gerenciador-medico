CREATE TABLE especialidade (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    CONSTRAINT pk_especialidade PRIMARY KEY (id),
    CONSTRAINT uk_especialidade_nome UNIQUE (nome)
);
