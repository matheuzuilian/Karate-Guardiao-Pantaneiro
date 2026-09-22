package com.ifms.lp3spring.controller;

import com.ifms.lp3spring.Service.CompeticaoService;
import com.ifms.lp3spring.model.AlunoModel;
import com.ifms.lp3spring.model.CompeticaoModel;
import com.ifms.lp3spring.Repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/competicoes")
public class CompeticaoController {

    @Autowired
    private CompeticaoService competicaoService;

    // 💡 Usamos diretamente o repositório para garantir o findById
    @Autowired
    private AlunoRepository alunoRepository; 

    @GetMapping("/aluno/{idAluno}")
    public ResponseEntity<List<CompeticaoModel>> listarPorAluno(@PathVariable("idAluno") Long idAluno) {
        List<CompeticaoModel> competicoes = competicaoService.buscarPorAluno(idAluno);
        return ResponseEntity.ok(competicoes);
    }

    // 💡 Recebe um Map simples, ignorando problemas de desserialização de Entidades
    @PostMapping("/salvar")
    public ResponseEntity<?> salvarViaJS(@RequestBody Map<String, String> payload) {
        try {
            CompeticaoModel comp = new CompeticaoModel();
            comp.setNomeCampeonato(payload.get("nomeCampeonato"));
            comp.setDataCompeticao(LocalDate.parse(payload.get("dataCompeticao")));
            comp.setModalidade(payload.get("modalidade"));
            comp.setColocacao(payload.get("colocacao"));

            // Converte o ID recebido e vai buscar o aluno gerido pela sessão
            Long idAluno = Long.parseLong(payload.get("idAluno"));
            AlunoModel alunoBanco = alunoRepository.findById(idAluno)
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

            comp.setAluno(alunoBanco);

            CompeticaoModel salva = competicaoService.salvar(comp);
            return ResponseEntity.ok(salva);
            
        } catch (Exception e) {
            e.printStackTrace(); // Imprime o erro na consola para diagnóstico
            return ResponseEntity.badRequest().body("Erro ao guardar: " + e.getMessage());
        }
    }
}