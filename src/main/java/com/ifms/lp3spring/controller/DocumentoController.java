package com.ifms.lp3spring.controller;

import com.ifms.lp3spring.model.AlunoModel;
import com.ifms.lp3spring.model.DocumentoModel;
import com.ifms.lp3spring.Repository.AlunoRepository;
import com.ifms.lp3spring.Repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    // 1. UPLOAD DE DOCUMENTO
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocumento(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tipoDocumento") String tipoDocumento,
            @RequestParam("idAluno") Long idAluno) {
        
        try {
            AlunoModel aluno = alunoRepository.findById(idAluno)
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

            DocumentoModel doc = new DocumentoModel();
            doc.setNomeArquivo(file.getOriginalFilename());
            doc.setTipoArquivo(file.getContentType());
            doc.setTipoDocumento(tipoDocumento);
            doc.setDados(file.getBytes()); // Salva o arquivo em BLOB
            doc.setAluno(aluno);

            documentoRepository.save(doc);
            return ResponseEntity.ok("Documento salvo com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro no upload: " + e.getMessage());
        }
    }

    // 2. LISTAR DOCUMENTOS (SOMENTE RESUMO)
    @GetMapping("/aluno/{idAluno}")
    public ResponseEntity<List<DocumentoRepository.DocumentoResumo>> listarPorAluno(@PathVariable Long idAluno) {
        return ResponseEntity.ok(documentoRepository.findByAluno_IdPessoa(idAluno));
    }

    // 3. DOWNLOAD / VISUALIZAR ARQUIVO
    @GetMapping("/download/{idDocumento}")
    public ResponseEntity<byte[]> downloadDocumento(@PathVariable Long idDocumento) {
        DocumentoModel doc = documentoRepository.findById(idDocumento)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getNomeArquivo() + "\"")
                .contentType(MediaType.parseMediaType(doc.getTipoArquivo()))
                .body(doc.getDados());
    }
    
    // 4. DELETAR
    @DeleteMapping("/deletar/{idDocumento}")
    public ResponseEntity<?> deletar(@PathVariable Long idDocumento) {
        documentoRepository.deleteById(idDocumento);
        return ResponseEntity.ok().build();
    }
}