package com.ifms.lp3spring.Service;

import com.ifms.lp3spring.Repository.MatriculaRepository;
import com.ifms.lp3spring.Repository.TurmaRepository;
import com.ifms.lp3spring.model.TurmaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    public TurmaModel salvar(TurmaModel turma) {
        if (turma.getVagasTotais() == null || turma.getVagasTotais() <= 0) {
            throw new IllegalArgumentException("O limite de vagas deve ser de no mínimo 1 aluno!");
        }
        
        // Garante valor padrão para status caso venha nulo
        if (turma.getStatus() == null || turma.getStatus().isBlank()) {
            turma.setStatus("Ativa");
        }

        return turmaRepository.save(turma);
    }

    public List<TurmaModel> listarTodas() {
        return turmaRepository.findAll();
    }

    public TurmaModel buscarPorId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada com o ID: " + id));
    }

    // 💡 Retorna quantas matrículas ativas existem na turma (usado no card ex: 2 / 15)
    public long contarMatriculadas(Long idTurma) {
        return matriculaRepository.contarMatriculasAtivasPorTurma(idTurma);
    }

    // 💡 MÉTODO DINÂMICO: Retorna quantas vagas restam na turma em tempo de execução
    public long calcularVagasRestantes(Long idTurma) {
        TurmaModel turma = buscarPorId(idTurma);
        long matriculasAtivas = contarMatriculadas(idTurma);
        
        long vagasRestantes = turma.getVagasTotais() - matriculasAtivas;
        return Math.max(0, vagasRestantes); // Retorna 0 se estiver lotada
    }

    // Retorna true se a turma já atingiu a capacidade máxima de alunos
    public boolean isTurmaLotada(Long idTurma) {
        return calcularVagasRestantes(idTurma) <= 0;
    }

    public void remover(Long id) {
        buscarPorId(id); // Valida se a turma existe no banco

        long matriculasAtivas = contarMatriculadas(id);
        if (matriculasAtivas > 0) {
            throw new IllegalStateException("Não é possível remover uma turma que possui alunos matriculados!");
        }

        turmaRepository.deleteById(id);
    }
}