package com.ifms.lp3spring.controller;

import com.ifms.lp3spring.Service.AlunoService;
import com.ifms.lp3spring.Service.HistoricoFaixaService;
import com.ifms.lp3spring.Service.MatriculaService;
import com.ifms.lp3spring.model.AlunoModel;
import com.ifms.lp3spring.model.HistoricoFaixaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private HistoricoFaixaService historicoFaixaService;

    @Autowired
    private MatriculaService matriculaService;

    @GetMapping("/")
    public String index(Model model) {
        List<AlunoModel> todosAlunos = alunoService.listarTodos();
        List<HistoricoFaixaModel> todasGraduacoes = historicoFaixaService.listarTodas();

        // 1. Total de Alunos e Graduações
        long totalAlunos = todosAlunos.size();
        long totalGraduacoes = todasGraduacoes.size();

        // 2. Contagem de Matrículas Ativas
        long totalMatriculasAtivas = matriculaService.listarTodas().stream()
                .filter(m -> "Ativa".equalsIgnoreCase(m.getStatus()) || m.getStatus() == null)
                .count();

        // 3. Última Graduação Realizada
        HistoricoFaixaModel ultimaGraduacao = todasGraduacoes.isEmpty() ? null : todasGraduacoes.get(0);

        // 4. Contagem por Faixas para o Gráfico de Barras
        Map<String, Long> contagemPorFaixa = new HashMap<>();
        String[] faixas = {"Branca", "Amarela", "Laranja", "Verde", "Roxa", "Marrom", "Preta"};
        for (String faixa : faixas) {
            long count = todosAlunos.stream()
                    .filter(a -> a.getGraduacao() != null && a.getGraduacao().equalsIgnoreCase(faixa))
                    .count();
            contagemPorFaixa.put(faixa, count);
        }

        // 5. Listas Recentes (Limitando a 5 itens)
        List<HistoricoFaixaModel> ultimasGraduacoes = todasGraduacoes.stream().limit(5).toList();
        List<AlunoModel> alunosRecentes = todosAlunos.stream().limit(5).toList();

        // Alimenta o Model para o Thymeleaf
        model.addAttribute("totalAlunos", totalAlunos);
        model.addAttribute("totalMatriculasAtivas", totalMatriculasAtivas);
        model.addAttribute("totalGraduacoes", totalGraduacoes);
        model.addAttribute("ultimaGraduacao", ultimaGraduacao);
        model.addAttribute("contagemPorFaixa", contagemPorFaixa);
        model.addAttribute("ultimasGraduacoes", ultimasGraduacoes);
        model.addAttribute("alunosRecentes", alunosRecentes);
        model.addAttribute("alunoService", alunoService);

        return "dashboard"; // Abre o arquivo templates/dashboard.html
    }
}