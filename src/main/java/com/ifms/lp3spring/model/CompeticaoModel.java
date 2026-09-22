package com.ifms.lp3spring.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "competicao")
public class CompeticaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCompeticao;

    @NotBlank(message = "O nome do campeonato é obrigatório.")
    private String nomeCampeonato;

    @NotNull(message = "A data da competição é obrigatória.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataCompeticao;

    @NotBlank(message = "A modalidade é obrigatória (ex: Kata, Kumite).")
    private String modalidade;

    @NotBlank(message = "A colocação é obrigatória.")
    private String colocacao; // Ex: 1º Lugar (Ouro), Participação, etc.

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    // 💡 Previne o erro 500 de serialização cíclica que resolvemos antes!
    @JsonIgnoreProperties({"competicoes", "historicoFaixas", "responsavel"}) 
    private AlunoModel aluno;

    public CompeticaoModel() {}

    // --- GETTERS E SETTERS ---

    public Long getIdCompeticao() { return idCompeticao; }
    public void setIdCompeticao(Long idCompeticao) { this.idCompeticao = idCompeticao; }

    public String getNomeCampeonato() { return nomeCampeonato; }
    public void setNomeCampeonato(String nomeCampeonato) { this.nomeCampeonato = nomeCampeonato; }

    public LocalDate getDataCompeticao() { return dataCompeticao; }
    public void setDataCompeticao(LocalDate dataCompeticao) { this.dataCompeticao = dataCompeticao; }

    public String getModalidade() { return modalidade; }
    public void setModalidade(String modalidade) { this.modalidade = modalidade; }

    public String getColocacao() { return colocacao; }
    public void setColocacao(String colocacao) { this.colocacao = colocacao; }

    public AlunoModel getAluno() { return aluno; }
    public void setAluno(AlunoModel aluno) { this.aluno = aluno; }
}