package com.gerenciador.medico.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "convenio")
public class Convenio {



@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false, unique =true, length =100)
private String nome;

@Column(name = "registro_ans", nullable = false, unique = true, length = 6)
private String registroAns;

@Enumerated(EnumType.STRING)
@Column (nullable = false, length = 30)
private TipoConvenio tipo;

@Column (length = 20)
private String telefone;

@Column (name = "inicio_vigencia", nullable = false)
private LocalDate inicioVigencia;

@Column (name = "fim_vigencia")
private LocalDate fimVigencia;

@Column (nullable = false)
private boolean ativo = true;

protected Convenio(){
    
}

public Convenio(String nome, String registroAns, TipoConvenio tipo, String telefone, LocalDate inicioVigencia, LocalDate fimVigencia) {

    this.nome = nome;
    this.registroAns = registroAns;
    this.tipo = tipo;
    this.telefone = telefone;
    this.inicioVigencia = inicioVigencia;
    this.fimVigencia = fimVigencia;

}

    public boolean estaVigente() {

        LocalDate hoje = LocalDate.now();
        boolean iniciou = !hoje.isBefore(inicioVigencia);
        boolean naoExpirou = fimVigencia == null || !hoje.isAfter(fimVigencia);
        return ativo && iniciou && naoExpirou;

    }

    public Long getId() { return id;}

    public String getNome() { return nome;}
    public void setNome(String nome) { this.nome = nome;}

    public String getRegistroAns() { return registroAns;}
    public void setRegistroAns(String registroAns) { this.registroAns = registroAns;}

    public TipoConvenio getTipo() { return tipo; }
    public void setTipo(TipoConvenio tipo) { this.tipo = tipo;}

    public String getTelefone() { return telefone;}
    public void setTelefone(String telefone) { this.telefone = telefone;}

    public LocalDate getInicioVigencia() { return inicioVigencia;}
    public void setInicioVigencia(LocalDate inicioVigencia) { this.inicioVigencia = inicioVigencia;}

    public LocalDate getFimVigencia() {return fimVigencia;}
    public void setFimVigecia(LocalDate fimVigencia) { this.fimVigencia = fimVigencia;}

    public boolean isAtivo() { return ativo;}
    public void setAtivo(boolean ativo) { this.ativo = ativo;}
    




    

}
