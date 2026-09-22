package com.ifms.lp3spring.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Entity
@PrimaryKeyJoinColumn(name = "idPessoa")
@Table(name = "aluno")
public class AlunoModel extends PessoaModel {

    @NotNull(message = "A data de nascimento do aluno é obrigatória.")
    @Past(message = "A data de nascimento deve ser uma data no passado.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDeNascimento;

    @NotBlank(message = "A graduação (faixa) é obrigatória.")
    private String graduacao;

    @NotBlank(message = "O tamanho do quimono é obrigatório.")
    private String tamanhoQuimono;

    @Column(columnDefinition = "TEXT")
    private String observacoesMedicas;

    private boolean ativo = true;

    // 💡 AJUSTE CRUCIAL: Adicionado Cascade PERSIST/MERGE para salvar o novo responsável em cadeia
    @NotNull(message = "O aluno deve ter um responsável legal!")
    @Valid
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("alunos")
    @JoinColumn(name = "id_responsavel")
    private ResponsavelModel responsavel;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HistoricoFaixaModel> historicoFaixas = new ArrayList<>();

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CompeticaoModel> competicoes = new ArrayList<>();

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DocumentoModel> documentos = new ArrayList<>();

    public AlunoModel() {
        super();
    }

    public AlunoModel(LocalDate dataDeNascimento, String graduacao, String tamanhoQuimono, boolean ativo, ResponsavelModel responsavel) {
        this.dataDeNascimento = dataDeNascimento;
        this.graduacao = graduacao;
        this.tamanhoQuimono = tamanhoQuimono;
        this.ativo = ativo;
        this.responsavel = responsavel;
    }

    // --- GETTERS E SETTERS ---

    public String getGraduacao() {
        return graduacao;
    }

    public void setGraduacao(String graduacao) {
        this.graduacao = graduacao;
    }

    public String getTamanhoQuimono() {
        return tamanhoQuimono;
    }

    public void setTamanhoQuimono(String tamanhoQuimono) {
        this.tamanhoQuimono = tamanhoQuimono;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public ResponsavelModel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(ResponsavelModel responsavel) {
        this.responsavel = responsavel;
    }

    public List<HistoricoFaixaModel> getHistoricoFaixas() {
        return historicoFaixas;
    }

    public void setHistoricoFaixas(List<HistoricoFaixaModel> historicoFaixas) {
        this.historicoFaixas = historicoFaixas;
    }

    public String getObservacoesMedicas() {
        return observacoesMedicas;
    }

    public void setObservacoesMedicas(String observacoesMedicas) {
        this.observacoesMedicas = observacoesMedicas;
    }

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public List<CompeticaoModel> getCompeticoes() {
        return competicoes;
    }

    public void setCompeticoes(List<CompeticaoModel> competicoes) {
        this.competicoes = competicoes;
    }
    
}