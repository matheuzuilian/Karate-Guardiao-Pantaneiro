package com.ifms.lp3spring.Service;

import com.ifms.lp3spring.Repository.AlunoRepository;
import com.ifms.lp3spring.Repository.HistoricoFaixaRepository;
import com.ifms.lp3spring.model.AlunoModel;
import com.ifms.lp3spring.model.HistoricoFaixaModel;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistoricoFaixaService {

    @Autowired
    private HistoricoFaixaRepository historicoFaixaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public HistoricoFaixaModel registrarGraduacao(HistoricoFaixaModel historico) {
        if (historico.getAluno() == null || historico.getAluno().getIdPessoa() == null) {
            throw new IllegalArgumentException("Selecione um aluno para registrar a nova faixa.");
        }

        AlunoModel aluno = alunoRepository.findById(historico.getAluno().getIdPessoa())
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        // 💡 Salva a faixa atual como a "faixaAnterior" do histórico
        historico.setFaixaAnterior(aluno.getGraduacao() != null ? aluno.getGraduacao() : "Branca");

        if (historico.getDataGraduacao() == null) {
            historico.setDataGraduacao(LocalDate.now());
        }

        // Atualiza a graduação atual no perfil do aluno
        aluno.setGraduacao(historico.getFaixa());
        alunoRepository.save(aluno);

        historico.setAluno(aluno);
        return historicoFaixaRepository.save(historico);
    }

    // Método auxiliar para descobrir a próxima faixa em ordem lógica
    public String obterProximaFaixa(String faixaAtual) {
        if (faixaAtual == null)
            return "Amarela";
        return switch (faixaAtual.toLowerCase()) {
            case "branca" -> "Amarela";
            case "amarela" -> "Laranja";
            case "laranja" -> "Verde";
            case "verde" -> "Roxa";
            case "roxa" -> "Marrom";
            case "marrom", "preta" -> "Preta";
            default -> "Amarela";
        };
    }

    @Transactional
    public void excluirGraduacao(Long idHistorico) {
        HistoricoFaixaModel historico = historicoFaixaRepository.findById(idHistorico)
                .orElseThrow(() -> new IllegalArgumentException("Registro de graduação não encontrado."));

        AlunoModel aluno = historico.getAluno();

        // Remove o registro
        historicoFaixaRepository.delete(historico);

        // Busca o histórico restante do aluno ordenado por data decrescente
        List<HistoricoFaixaModel> historicoRestante = historicoFaixaRepository
                .findByAlunoIdPessoaOrderByDataGraduacaoDesc(aluno.getIdPessoa());

        // Atualiza a faixa do aluno com a graduação mais recente que sobrou
        if (!historicoRestante.isEmpty()) {
            aluno.setGraduacao(historicoRestante.get(0).getFaixa());
        } else {
            aluno.setGraduacao("Branca"); // Padrão se apagar todas
        }

        alunoRepository.save(aluno);
    }

    // No HistoricoFaixaService.java

    public HistoricoFaixaModel buscarPorId(Long idHistorico) {
        return historicoFaixaRepository.findById(idHistorico)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Registro de graduação não encontrado com o ID: " + idHistorico));
    }

    public List<HistoricoFaixaModel> listarHistoricoDoAluno(Long idAluno) {
        return historicoFaixaRepository.findByAlunoIdPessoaOrderByDataGraduacaoDesc(idAluno);
    }

    public List<HistoricoFaixaModel> listarUltimasGraduacoes() {
        return historicoFaixaRepository.findTop5ByOrderByDataGraduacaoDesc();
    }

    public List<HistoricoFaixaModel> listarTodas() {
        return historicoFaixaRepository.findAllByOrderByDataGraduacaoDesc();
    }
}