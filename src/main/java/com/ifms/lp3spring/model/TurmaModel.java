package com.ifms.lp3spring.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "turma")
public class TurmaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTurma;

    @NotBlank(message = "O nome da turma é obrigatório.")
    private String nome;

    private String diasSemana;

    @NotBlank(message = "Informe o horário de início.")
    private String horarioInicio;

    @NotBlank(message = "Informe o horário de término.")
    private String horarioFim;

    @NotNull(message = "Informe a idade mínima.")
    @Min(value = 4, message = "A idade mínima deve ser no mínimo 4 anos.")
    private Integer idadeMinima;

    @NotNull(message = "Informe a idade máxima.")
    private Integer idadeMaxima;

    @NotNull(message = "O limite de vagas é obrigatório.")
    @Min(value = 1, message = "A turma deve ter no mínimo 1 vaga.")
    private Integer vagasTotais;

    @NotBlank(message = "O instrutor responsável é obrigatório.")
    private String instrutor;

    private String status = "Ativa";

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatriculaModel> matriculas = new ArrayList<>();

    public TurmaModel() {
    }

    // --- GETTERS E SETTERS ---

    public Long getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(Long idTurma) {
        this.idTurma = idTurma;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(String horarioFim) {
        this.horarioFim = horarioFim;
    }

    public Integer getIdadeMinima() {
        return idadeMinima;
    }

    public void setIdadeMinima(Integer idadeMinima) {
        this.idadeMinima = idadeMinima;
    }

    public Integer getIdadeMaxima() {
        return idadeMaxima;
    }

    public void setIdadeMaxima(Integer idadeMaxima) {
        this.idadeMaxima = idadeMaxima;
    }

    public Integer getVagasTotais() {
        return vagasTotais;
    }

    public void setVagasTotais(Integer vagasTotais) {
        this.vagasTotais = vagasTotais;
    }

    public String getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(String instrutor) {
        this.instrutor = instrutor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MatriculaModel> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<MatriculaModel> matriculas) {
        this.matriculas = matriculas;
    }
}