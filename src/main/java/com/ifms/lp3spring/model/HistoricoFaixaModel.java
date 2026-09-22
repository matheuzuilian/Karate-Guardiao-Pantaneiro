package com.ifms.lp3spring.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "historico_faixa")
public class HistoricoFaixaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorico;

    @NotNull(message = "O aluno é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private AlunoModel aluno;

    @NotBlank(message = "A faixa é obrigatória.")
    private String faixa; // Branca, Amarela, Laranja, Verde, Roxa, Marrom, Preta...

    @NotNull(message = "A data da graduação é obrigatória.")
    @PastOrPresent(message = "A data da graduação não pode ser no futuro.")
    private LocalDate dataGraduacao = LocalDate.now();

    private String observacao; // Ex: "Exame de faixa do 2º Semestre" ou "Graduação por mérito"
    private String examinador; // Ex: "Sensei Fulano" ou "Policial Instrutor"

    @Column(name = "faixa_anterior")
    private String faixaAnterior;

    @Column(name = "nota")
    private Double nota;

    @Column(name = "examinadores")
    private String examinadores;

    public HistoricoFaixaModel() {
    }

    public HistoricoFaixaModel(AlunoModel aluno, String faixa, LocalDate dataGraduacao, String observacao, Double nota, String examinadores) {
        this.aluno = aluno;
        this.faixa = faixa;
        this.dataGraduacao = dataGraduacao;
        this.observacao = observacao;
    }

    // --- GETTERS E SETTERS ---

    public Long getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(Long idHistorico) {
        this.idHistorico = idHistorico;
    }

    public AlunoModel getAluno() {
        return aluno;
    }

    public void setAluno(AlunoModel aluno) {
        this.aluno = aluno;
    }

    public String getFaixa() {
        return faixa;
    }

    public void setFaixa(String faixa) {
        this.faixa = faixa;
    }

    public LocalDate getDataGraduacao() {
        return dataGraduacao;
    }

    public void setDataGraduacao(LocalDate dataGraduacao) {
        this.dataGraduacao = dataGraduacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getExaminador() {
        return examinador;
    }

    public void setExaminador(String examinador) {
        this.examinador = examinador;
    }

    public String getFaixaAnterior() {
        return faixaAnterior;
    }

    public void setFaixaAnterior(String faixaAnterior) {
        this.faixaAnterior = faixaAnterior;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getExaminadores() {
        return examinadores;
    }

    public void setExaminadores(String examinadores) {
        this.examinadores = examinadores;
    }
}