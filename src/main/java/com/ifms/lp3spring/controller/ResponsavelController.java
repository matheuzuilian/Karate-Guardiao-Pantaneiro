package com.ifms.lp3spring.controller;

import com.ifms.lp3spring.Service.ResponsavelService;
import com.ifms.lp3spring.model.ResponsavelModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/responsaveis")
public class ResponsavelController {

    @Autowired
    private ResponsavelService responsavelService;

    @GetMapping
    public String listarResponsaveis(Model model) {
        model.addAttribute("responsaveis", responsavelService.listarTodos());
        return "responsavel/listar"; // Caminho da página HTML
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("responsavel", new ResponsavelModel());
        return "responsavel/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("responsavel") ResponsavelModel responsavel,
                         BindingResult result,
                         RedirectAttributes attributes) {
        
        if (result.hasErrors()) {
            return "responsavel/formulario";
        }

        try {
            responsavelService.salvar(responsavel);
            attributes.addFlashAttribute("mensagemSucesso", "Responsável salvo com sucesso!");
            return "redirect:/responsaveis";
        } catch (IllegalArgumentException e) {
            result.rejectValue("cpf", "error.responsavel", e.getMessage());
            return "responsavel/formulario";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable("id") Long id, Model model) {
        model.addAttribute("responsavel", responsavelService.buscarPorId(id));
        return "responsavel/formulario";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            responsavelService.remover(id);
            attributes.addFlashAttribute("mensagemSucesso", "Responsável removido com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/responsaveis";
    }
}