package com.ifms.lp3spring.Service;

import com.ifms.lp3spring.Repository.AlunoRepository;
import com.ifms.lp3spring.Repository.MatriculaRepository;
import com.ifms.lp3spring.Repository.TurmaRepository;
import com.ifms.lp3spring.model.AlunoModel;
import com.ifms.lp3spring.model.MatriculaModel;
import com.ifms.lp3spring.model.TurmaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private TurmaService turmaService;

    public MatriculaModel matricular(MatriculaModel matricula) {

        // 1. Validação de presença do Aluno e Turma
        if (matricula.getAluno() == null || matricula.getAluno().getIdPessoa() == null) {
            throw new IllegalArgumentException("Selecione um aluno válido para realizar a matrícula!");
        }
        if (matricula.getTurma() == null || matricula.getTurma().getIdTurma() == null) {
            throw new IllegalArgumentException("Selecione uma turma válida!");
        }

        // 2. Busca entidades completas do banco
        AlunoModel alunoBanco = alunoRepository.findById(matricula.getAluno().getIdPessoa())
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado no banco de dados!"));

        TurmaModel turmaBanco = turmaRepository.findById(matricula.getTurma().getIdTurma())
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada no banco de dados!"));

        // 3. Validação: O aluno precisa estar com cadastro ATIVO
        if (!alunoBanco.isAtivo()) {
            throw new IllegalArgumentException("Não é possível matricular: O cadastro do aluno está inativo!");
        }

        // 4. TRAVA DE DUPLICIDADE: Verifica se a criança já está matriculada nesta turma
        boolean jaMatriculado = matriculaRepository.existsByAlunoETurmaAtiva(
                alunoBanco.getIdPessoa(), 
                turmaBanco.getIdTurma());

        if (jaMatriculado) {
            throw new IllegalArgumentException("O aluno " + alunoBanco.getNome() + " já possui matrícula ativa nesta turma!");
        }

        // 5. TRAVA DE LOTAÇÃO: Verifica se a turma tem vagas disponíveis
        if (turmaService.isTurmaLotada(turmaBanco.getIdTurma())) {
            throw new IllegalArgumentException("Não há mais vagas disponíveis na turma " + turmaBanco.getNome() + "!");
        }

        // Configurações finais da matrícula
        matricula.setAluno(alunoBanco);
        matricula.setTurma(turmaBanco);
        if (matricula.getDataMatricula() == null) {
            matricula.setDataMatricula(LocalDate.now());
        }
        matricula.setStatus("ATIVA");

        return matriculaRepository.save(matricula);
    }

    public List<MatriculaModel> listarTodas() {
        return matriculaRepository.findAll();
    }

    public MatriculaModel buscarPorId(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Matrícula não encontrada com o ID: " + id));
    }

    // Trancar/Cancelar matrícula (libera a vaga da turma imediatamente)
    public void alterarStatusMatricula(Long idMatricula, String novoStatus) {
        MatriculaModel matricula = buscarPorId(idMatricula);
        matricula.setStatus(novoStatus); // ex: "TRANCADA", "CANCELADA", "CONCLUÍDA"
        matriculaRepository.save(matricula);
    }

    public void remover(Long id) {
        // Ao excluir a matrícula do banco, a vaga da turma é reaberta automaticamente!
        matriculaRepository.deleteById(id);
    }
}