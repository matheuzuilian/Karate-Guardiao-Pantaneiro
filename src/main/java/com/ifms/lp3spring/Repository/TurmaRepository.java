package com.ifms.lp3spring.Repository;

import com.ifms.lp3spring.model.TurmaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurmaRepository extends JpaRepository<TurmaModel, Long> {

    // Método por nome da turma
    List<TurmaModel> findByNomeContainingIgnoreCase(String nome);

    // Método por status (ex: "Ativa")
    List<TurmaModel> findByStatus(String status);

    // 💡 REMOVIDO: findByFaixaEtaria (propriedade foi substituída por idadeMinima e idadeMaxima)
    
    // Opcional: Busca por faixa etária de um aluno
    List<TurmaModel> findByIdadeMinimaLessThanEqualAndIdadeMaximaGreaterThanEqual(Integer idadeAluno1, Integer idadeAluno2);
}