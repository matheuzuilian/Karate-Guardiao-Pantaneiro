package com.ifms.lp3spring.controller;

import com.ifms.lp3spring.Service.AlunoService;
import com.ifms.lp3spring.Service.HistoricoFaixaService;
import com.ifms.lp3spring.model.AlunoModel;
import com.ifms.lp3spring.model.HistoricoFaixaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/graduacoes")
public class HistoricoFaixaController {

    @Autowired
    private HistoricoFaixaService historicoFaixaService;

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public String listarGraduacoes(@RequestParam(name = "alunoId", required = false) Long alunoId, Model model) {
        List<AlunoModel> alunos = alunoService.listarTodos();
        model.addAttribute("alunos", alunos);

        AlunoModel alunoSelecionado = null;
        List<HistoricoFaixaModel> historico = List.of();

        if (alunoId != null) {
            alunoSelecionado = alunoService.buscarPorId(alunoId);
            historico = historicoFaixaService.listarHistoricoDoAluno(alunoId);
        } else if (!alunos.isEmpty()) {
            alunoSelecionado = alunos.get(0);
            historico = historicoFaixaService.listarHistoricoDoAluno(alunoSelecionado.getIdPessoa());
        }

        model.addAttribute("alunoSelecionado", alunoSelecionado);
        model.addAttribute("historico", historico);
        model.addAttribute("alunoService", alunoService);

        // Sugere a próxima faixa do aluno selecionado
        String proximaFaixa = "Amarela";
        if (alunoSelecionado != null) {
            proximaFaixa = historicoFaixaService.obterProximaFaixa(alunoSelecionado.getGraduacao());
        }

        HistoricoFaixaModel novaGraduacao = new HistoricoFaixaModel();
        novaGraduacao.setFaixa(proximaFaixa); // Pré-seleciona a faixa no modal
        model.addAttribute("novaGraduacao", novaGraduacao);

        return "graduacao/listar";
    }

    @PostMapping("/salvar")
    public String salvarGraduacao(@ModelAttribute("novaGraduacao") HistoricoFaixaModel historico) {
        historicoFaixaService.registrarGraduacao(historico);
        return "redirect:/graduacoes?alunoId=" + historico.getAluno().getIdPessoa();
    }

    @GetMapping("/deletar/{id}")
    public String deletarGraduacao(@PathVariable Long id) {
        HistoricoFaixaModel historico = historicoFaixaService.buscarPorId(id);
        Long alunoId = historico.getAluno().getIdPessoa();

        historicoFaixaService.excluirGraduacao(id);

        return "redirect:/graduacoes?alunoId=" + alunoId;
    }
}