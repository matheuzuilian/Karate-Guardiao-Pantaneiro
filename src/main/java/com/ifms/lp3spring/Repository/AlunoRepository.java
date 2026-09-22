package com.ifms.lp3spring.Repository;

import com.ifms.lp3spring.model.AlunoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoModel, Long> {

    // Lista apenas os alunos com matrícula/situação ativa
    List<AlunoModel> findByAtivoTrue();

    // Busca alunos por graduação/faixa (útil para organizar campeonatos/exames de faixa)
    List<AlunoModel> findByGraduacao(String graduacao);

    // Lista todos os alunos cadastrados para um determinado responsável
    List<AlunoModel> findByResponsavelIdPessoa(Long idResponsavel);

    // Busca pelo nome da criança
    List<AlunoModel> findByNomeContainingIgnoreCase(String nome);
}