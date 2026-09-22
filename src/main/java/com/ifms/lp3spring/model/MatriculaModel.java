package com.ifms.lp3spring.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "matricula")
public class MatriculaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMatricula;

    @NotNull(message = "O aluno é obrigatório para realizar a matrícula.")
    @ManyToOne
    @JoinColumn(name = "id_aluno")
    @JsonIgnoreProperties({"historicoFaixas", "responsavel"}) // 💡 Evita recursion no JSON do Aluno
    private AlunoModel aluno;

    @NotNull(message = "A turma é obrigatória para realizar a matrícula.")
    @ManyToOne
    @JoinColumn(name = "id_turma")
    @JsonIgnoreProperties({"matriculas"}) // 💡 CRUCIAL: Impede o loop infinito com a Turma
    private TurmaModel turma;

    @NotNull(message = "A data da matrícula é obrigatória.")
    private LocalDate dataMatricula = LocalDate.now();

    private String status = "ATIVA"; // Valores comuns: "ATIVA", "TRANCADA", "CANCELADA", "CONCLUÍDA"

    public MatriculaModel() {
    }

    public MatriculaModel(AlunoModel aluno, TurmaModel turma, LocalDate dataMatricula, String status) {
        this.aluno = aluno;
        this.turma = turma;
        this.dataMatricula = dataMatricula;
        this.status = status;
    }

    // --- GETTERS E SETTERS ---

    public Long getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(Long idMatricula) {
        this.idMatricula = idMatricula;
    }

    public AlunoModel getAluno() {
        return aluno;
    }

    public void setAluno(AlunoModel aluno) {
        this.aluno = aluno;
    }

    public TurmaModel getTurma() {
        return turma;
    }

    public void setTurma(TurmaModel turma) {
        this.turma = turma;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}