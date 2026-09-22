package com.ifms.lp3spring.controller;

import com.ifms.lp3spring.Service.AlunoService;
import com.ifms.lp3spring.Service.ResponsavelService;
import com.ifms.lp3spring.model.AlunoModel;
import com.ifms.lp3spring.model.ResponsavelModel;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ResponsavelService responsavelService;

    @GetMapping
    public String listarAlunos(Model model) {

        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("alunoService", alunoService);

        // Injeção para o Modal de Novo Aluno funcionar na mesma tela
        model.addAttribute("alunoForm", new AlunoModel());
        model.addAttribute("responsaveis", responsavelService.listarTodos());

        // Se o alunoForm NÃO tiver vindo pelo RedirectAttributes, instancia um novo
        if (!model.containsAttribute("alunoForm")) {
            model.addAttribute("alunoForm", new AlunoModel());
        }

        return "aluno/listar";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("aluno", new AlunoModel());
        model.addAttribute("responsaveis", responsavelService.listarTodos());
        return "aluno/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("alunoForm") AlunoModel aluno,
            BindingResult result,
            @RequestParam(value = "tipoResp", required = false) String tipoResp,
            RedirectAttributes attributes) {

        // 1. Trata o Responsável quando for selecionado "existente"
        if ("existente".equals(tipoResp) && aluno.getResponsavel() != null
                && aluno.getResponsavel().getIdPessoa() != null) {
            ResponsavelModel respBanco = responsavelService.buscarPorId(aluno.getResponsavel().getIdPessoa());
            aluno.setResponsavel(respBanco);
        }

        // 2. Filtra se há erros REAIS (ignora validação do formulário do responsável
        // quando selecionado 'existente')
        boolean errosReais = result.getFieldErrors().stream()
                .anyMatch(error -> !"existente".equals(tipoResp) || !error.getField().startsWith("responsavel."));

        if (errosReais) {
            String primeiraMsg = result.getFieldErrors().stream()
                    .filter(error -> !"existente".equals(tipoResp) || !error.getField().startsWith("responsavel."))
                    .findFirst()
                    .map(error -> error.getDefaultMessage())
                    .orElse("Verifique os campos preenchidos.");

            attributes.addFlashAttribute("mensagemErro", primeiraMsg);
            attributes.addFlashAttribute("alunoForm", aluno);
            attributes.addFlashAttribute("tipoRespDevolvido", tipoResp != null ? tipoResp : "existente");
            attributes.addFlashAttribute("abrirModalErro", true);
            return "redirect:/alunos";
        }

        // 3. Tenta salvar no banco de dados
        try {
            alunoService.salvar(aluno);
            attributes.addFlashAttribute("mensagemSucesso", "Aluno salvo com sucesso!");
            return "redirect:/alunos";
        } catch (Exception e) {
            String mensagemTratada = e.getMessage();
            if (e.getCause() != null && e.getCause().getCause() != null) {
                mensagemTratada = e.getCause().getCause().getMessage();
            }

            attributes.addFlashAttribute("mensagemErro", mensagemTratada);
            attributes.addFlashAttribute("alunoForm", aluno);
            attributes.addFlashAttribute("tipoRespDevolvido", tipoResp);
            attributes.addFlashAttribute("abrirModalErro", true);
            return "redirect:/alunos";
        }
    }

    @GetMapping("/buscar/{id}")
    @ResponseBody
    public ResponseEntity<AlunoModel> buscarAlunoPorId(@PathVariable Long id) {
        AlunoModel aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(aluno);
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable("id") Long id, Model model) {
        model.addAttribute("aluno", alunoService.buscarPorId(id));
        model.addAttribute("responsaveis", responsavelService.listarTodos());
        return "aluno/formulario";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            alunoService.remover(id);
            attributes.addFlashAttribute("mensagemSucesso", "Aluno removido com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/alunos";
    }
}