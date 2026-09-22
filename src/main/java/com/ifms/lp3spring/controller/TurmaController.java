package com.ifms.lp3spring.controller;

import com.ifms.lp3spring.Service.MatriculaService;
import com.ifms.lp3spring.Service.TurmaService;
import com.ifms.lp3spring.model.MatriculaModel;
import com.ifms.lp3spring.model.TurmaModel;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private MatriculaService matriculaService;

    @GetMapping
    public String listarTurmas(Model model) {
        model.addAttribute("turmas", turmaService.listarTodas());
        model.addAttribute("turmaService", turmaService); // Consulta o saldo de vagas no Thymeleaf

        // Garante que o objeto do formulário do modal exista
        if (!model.containsAttribute("turmaForm")) {
            model.addAttribute("turmaForm", new TurmaModel());
        }

        return "turma/listar";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("turmaForm") TurmaModel turma,
            BindingResult result,
            RedirectAttributes attributes) {

        // 💡 LOG PARA DIAGNÓSTICO: Mostra os erros no terminal do VS Code/Eclipse
        if (result.hasErrors()) {
            System.out.println("=== ERRO DE VALIDAÇÃO AO SALVAR TURMA ===");
            result.getAllErrors().forEach(error -> System.out.println("-> " + error.getDefaultMessage()));

            String primeiraMsg = result.getAllErrors().get(0).getDefaultMessage();
            attributes.addFlashAttribute("mensagemErro", primeiraMsg);
            attributes.addFlashAttribute("turmaForm", turma);
            attributes.addFlashAttribute("abrirModalErro", true);
            return "redirect:/turmas";
        }

        try {
            turmaService.salvar(turma);
            attributes.addFlashAttribute("mensagemSucesso", "Turma salva com sucesso!");
            return "redirect:/turmas";
        } catch (Exception e) {
            System.out.println("=== EXCEÇÃO AO SALVAR TURMA ===");
            e.printStackTrace();

            attributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
            attributes.addFlashAttribute("turmaForm", turma);
            attributes.addFlashAttribute("abrirModalErro", true);
            return "redirect:/turmas";
        }
    }

    // Endpoint para buscar dados da turma e carregar no Modal ao editar
    @GetMapping("/buscar/{id}")
    @ResponseBody
    public ResponseEntity<TurmaModel> buscarPorId(@PathVariable("id") Long id) {
        try {
            TurmaModel turma = turmaService.buscarPorId(id);
            return ResponseEntity.ok(turma);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            turmaService.remover(id);
            attributes.addFlashAttribute("mensagemSucesso", "Turma removida com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/turmas";
    }

    @GetMapping("/alunos/{idTurma}")
    @ResponseBody
    public ResponseEntity<List<MatriculaModel>> listarAlunosDaTurma(@PathVariable("idTurma") Long idTurma) {
        List<MatriculaModel> matriculas = matriculaService.listarTodas().stream()
                .filter(m -> m.getTurma() != null && m.getTurma().getIdTurma().equals(idTurma))
                .filter(m -> "Ativa".equalsIgnoreCase(m.getStatus()) || m.getStatus() == null)
                .toList();

        return ResponseEntity.ok(matriculas);
    }
}