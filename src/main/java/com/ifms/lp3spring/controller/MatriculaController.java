package com.ifms.lp3spring.controller;

import com.ifms.lp3spring.Service.AlunoService;
import com.ifms.lp3spring.Service.MatriculaService;
import com.ifms.lp3spring.Service.TurmaService;
import com.ifms.lp3spring.model.MatriculaModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private TurmaService turmaService;

    @GetMapping
    public String listarMatriculas(Model model) {
        model.addAttribute("matriculas", matriculaService.listarTodas());
        model.addAttribute("alunos", alunoService.listarApenasAtivos());
        model.addAttribute("turmas", turmaService.listarTodas());
        model.addAttribute("alunoService", alunoService); // Usado para calcular a idade na tabela

        // Garante que o objeto do formulário do modal exista
        if (!model.containsAttribute("matriculaForm")) {
            MatriculaModel nova = new MatriculaModel();
            nova.setDataMatricula(LocalDate.now());
            nova.setStatus("Ativa");
            model.addAttribute("matriculaForm", nova);
        }

        return "matricula/listar";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("matriculaForm") MatriculaModel matricula,
                         BindingResult result,
                         RedirectAttributes attributes) {

        // 1. Tratamento de erro de validação do Spring (@Valid)
        if (result.hasErrors()) {
            String primeiraMsg = result.getAllErrors().isEmpty() 
                ? "Verifique os campos preenchidos." 
                : result.getAllErrors().get(0).getDefaultMessage();

            attributes.addFlashAttribute("mensagemErroModal", primeiraMsg);
            attributes.addFlashAttribute("matriculaForm", matricula);
            attributes.addFlashAttribute("abrirModalErro", true);
            return "redirect:/matriculas";
        }

        // 2. Tenta salvar via Service utilizando a regra de negócio existente (matricular)
        try {
            matriculaService.matricular(matricula);
            attributes.addFlashAttribute("mensagemSucesso", "Matrícula realizada com sucesso!");
            return "redirect:/matriculas";
        } catch (Exception e) {
            String mensagemTratada = e.getMessage();
            if (e.getCause() != null && e.getCause().getCause() != null) {
                mensagemTratada = e.getCause().getCause().getMessage();
            }

            attributes.addFlashAttribute("mensagemErroModal", mensagemTratada);
            attributes.addFlashAttribute("matriculaForm", matricula);
            attributes.addFlashAttribute("abrirModalErro", true);
            return "redirect:/matriculas";
        }
    }

    @GetMapping("/alterar-status/{id}")
    public String alterarStatus(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            MatriculaModel mat = matriculaService.buscarPorId(id);
            String novoStatus = ("Ativa".equalsIgnoreCase(mat.getStatus())) ? "Inativa" : "Ativa";
            
            // Chama o método do service que altera o status
            matriculaService.alterarStatusMatricula(id, novoStatus);
            
            attributes.addFlashAttribute("mensagemSucesso", "Status da matrícula atualizado para: " + novoStatus);
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/matriculas";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            matriculaService.remover(id);
            attributes.addFlashAttribute("mensagemSucesso", "Matrícula removida e vaga reaberta com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/matriculas";
    }
}