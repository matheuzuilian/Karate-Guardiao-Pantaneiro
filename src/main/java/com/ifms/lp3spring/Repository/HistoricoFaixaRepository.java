package com.ifms.lp3spring.Repository;

import com.ifms.lp3spring.model.HistoricoFaixaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoFaixaRepository extends JpaRepository<HistoricoFaixaModel, Long> {

    // Lista o histórico de faixas de um aluno específico (ordenado da mais recente para a mais antiga)
    List<HistoricoFaixaModel> findByAlunoIdPessoaOrderByDataGraduacaoDesc(Long idAluno);

    // Busca as últimas graduações registradas no sistema (ideal para alimentar o Dashboard!)
    List<HistoricoFaixaModel> findTop5ByOrderByDataGraduacaoDesc();

    // Método necessário para listar todas as graduações ordenadas por data
    List<HistoricoFaixaModel> findAllByOrderByDataGraduacaoDesc();
}