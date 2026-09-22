package com.ifms.lp3spring.Repository;

import com.ifms.lp3spring.model.MatriculaModel;
import com.ifms.lp3spring.model.AlunoModel;
import com.ifms.lp3spring.model.TurmaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<MatriculaModel, Long> {

    // Lista matrículas ativas de um aluno
    List<MatriculaModel> findByAlunoAndStatus(AlunoModel aluno, String status);

    // Lista todas as matrículas ativas de uma determinada turma
    List<MatriculaModel> findByTurmaAndStatus(TurmaModel turma, String status);

    // 💡 CONTROLE DINÂMICO DE VAGAS: Conta quantas crianças ativas existem na turma
    @Query("SELECT COUNT(m) FROM MatriculaModel m WHERE m.turma.idTurma = :idTurma AND m.status = 'ATIVA'")
    long contarMatriculasAtivasPorTurma(@Param("idTurma") Long idTurma);

    // 💡 TRAVA DE DUPLICIDADE: Verifica se o aluno já possui matrícula ativa na turma
    @Query("SELECT COUNT(m) > 0 FROM MatriculaModel m WHERE m.aluno.idPessoa = :idAluno AND m.turma.idTurma = :idTurma AND m.status = 'ATIVA'")
    boolean existsByAlunoETurmaAtiva(@Param("idAluno") Long idAluno, @Param("idTurma") Long idTurma);
}