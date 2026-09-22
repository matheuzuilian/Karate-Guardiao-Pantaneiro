package com.ifms.lp3spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "documento")
public class DocumentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocumento;

    private String nomeArquivo;
    private String tipoArquivo; // Ex: application/pdf, image/jpeg
    private String tipoDocumento; // Ex: Atestado Médico, Certificado, RG

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] dados; // 💡 Aqui fica o arquivo real

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    @JsonIgnoreProperties({"documentos", "historicoFaixas", "responsavel", "competicoes"})
    private AlunoModel aluno;

    public DocumentoModel() {}

    // --- GETTERS E SETTERS ---
    public Long getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Long idDocumento) { this.idDocumento = idDocumento; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public byte[] getDados() { return dados; }
    public void setDados(byte[] dados) { this.dados = dados; }

    public AlunoModel getAluno() { return aluno; }
    public void setAluno(AlunoModel aluno) { this.aluno = aluno; }
}