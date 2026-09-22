package com.ifms.lp3spring.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@PrimaryKeyJoinColumn(name = "idPessoa")
@Table(name = "responsavel")
public class ResponsavelModel extends PessoaModel {

    @NotBlank(message = "O grau de parentesco é obrigatório (ex: Pai, Mãe, Tutor).")
    private String parentesco;

    // Relacionamento 1 Responsável para N Alunos (Filhos/Dependentes)
    @OneToMany(mappedBy = "responsavel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AlunoModel> alunos = new ArrayList<>();

    public ResponsavelModel() {
        super();
    }

    public ResponsavelModel(String parentesco) {
        this.parentesco = parentesco;
    }

    // --- VALIDAÇÕES SOBREPOSTAS PARA O RESPONSÁVEL ---

    @Override
    @NotBlank(message = "O CPF do responsável é obrigatório.")
    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$", message = "CPF inválido. Digite 11 números ou no formato 000.000.000-00.")
    public String getCpf() {
        return super.getCpf();
    }

    @Override
    @NotBlank(message = "O telefone de contato é obrigatório.")
    @Pattern(regexp = "^(\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}|\\d{10,11})$", message = "Informe um telefone válido no formato (00) 90000-0000.")
    public String getTelefone() {
        return super.getTelefone();
    }

    // --- GETTERS E SETTERS ESPECÍFICOS ---

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public List<AlunoModel> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<AlunoModel> alunos) {
        this.alunos = alunos;
    }
}