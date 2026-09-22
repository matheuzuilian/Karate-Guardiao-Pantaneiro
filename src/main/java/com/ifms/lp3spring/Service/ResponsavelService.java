package com.ifms.lp3spring.Service;

import com.ifms.lp3spring.Repository.ResponsavelRepository;
import com.ifms.lp3spring.model.ResponsavelModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponsavelService {

    @Autowired
    private ResponsavelRepository responsavelRepository;

    public ResponsavelModel salvar(ResponsavelModel responsavel) {
        // Validação de CPF Único antes de cadastrar
        if (responsavel.getCpf() != null) {
            Optional<ResponsavelModel> existente = responsavelRepository.findByCpf(responsavel.getCpf());
            
            // Se já existe e não é o mesmo registro em edição, bloqueia
            if (existente.isPresent() && !existente.get().getIdPessoa().equals(responsavel.getIdPessoa())) {
                throw new IllegalArgumentException("Já existe um responsável cadastrado com o CPF informado!");
            }
        }

        return responsavelRepository.save(responsavel);
    }

    public List<ResponsavelModel> listarTodos() {
        return responsavelRepository.findAll();
    }

    public ResponsavelModel buscarPorId(Long id) {
        return responsavelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado com o ID: " + id));
    }

    public List<ResponsavelModel> buscarPorNome(String nome) {
        return responsavelRepository.findByNomeContainingIgnoreCase(nome);
    }

    public void remover(Long id) {
        ResponsavelModel responsavel = buscarPorId(id);
        
        // Regra de segurança: Não permite excluir responsável se houver alunos vinculados
        if (responsavel.getAlunos() != null && !responsavel.getAlunos().isEmpty()) {
            throw new IllegalStateException("Não é possível remover o responsável pois existem alunos/dependentes vinculados a ele!");
        }

        responsavelRepository.deleteById(id);
    }
}