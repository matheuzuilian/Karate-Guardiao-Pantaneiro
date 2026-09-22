package com.ifms.lp3spring.Repository;

import com.ifms.lp3spring.model.CompeticaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompeticaoRepository extends JpaRepository<CompeticaoModel, Long> {
    
    // Busca as competições de um aluno ordenando pela data mais recente
    List<CompeticaoModel> findByAluno_IdPessoaOrderByDataCompeticaoDesc(Long idAluno);
}