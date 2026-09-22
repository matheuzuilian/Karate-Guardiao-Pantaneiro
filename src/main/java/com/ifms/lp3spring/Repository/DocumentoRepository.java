package com.ifms.lp3spring.Repository;

import com.ifms.lp3spring.model.DocumentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<DocumentoModel, Long> {
    
    // Interface de Projeção: Traz só os dados leves para preencher a tabela
    interface DocumentoResumo {
        Long getIdDocumento();
        String getNomeArquivo();
        String getTipoDocumento();
    }

    List<DocumentoResumo> findByAluno_IdPessoa(Long idAluno);
}