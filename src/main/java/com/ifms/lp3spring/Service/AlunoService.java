package com.ifms.lp3spring.Service;

import com.ifms.lp3spring.Repository.AlunoRepository;
// import com.ifms.lp3spring.Repository.ResponsavelRepository;
import com.ifms.lp3spring.model.AlunoModel;
// import com.ifms.lp3spring.model.ResponsavelModel;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    // @Autowired
    // private ResponsavelRepository responsavelRepository;

    @Transactional 
    public AlunoModel salvar(AlunoModel alunoForm) {
        // 💡 Se for uma EDIÇÃO (aluno já possui ID no banco)
        if (alunoForm.getIdPessoa() != null) {
            AlunoModel alunoExistente = alunoRepository.findById(alunoForm.getIdPessoa())
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

            // Atualiza os dados básicos da criança diretamente na instância gerenciada
            alunoExistente.setNome(alunoForm.getNome());
            alunoExistente.setDataDeNascimento(alunoForm.getDataDeNascimento());
            alunoExistente.setGraduacao(alunoForm.getGraduacao());
            alunoExistente.setTamanhoQuimono(alunoForm.getTamanhoQuimono());
            alunoExistente.setObservacoesMedicas(alunoForm.getObservacoesMedicas());
            alunoExistente.setAtivo(alunoForm.isAtivo());

            // Se o responsável foi informado/alterado
            if (alunoForm.getResponsavel() != null) {
                alunoExistente.setResponsavel(alunoForm.getResponsavel());
            }

            // O @Transactional sincroniza as alterações automaticamente no banco
            return alunoRepository.save(alunoExistente);
        }

        // 💡 Se for um NOVO CADASTRO
        return alunoRepository.save(alunoForm);
    }

    public List<AlunoModel> listarTodos() {
        return alunoRepository.findAll();
    }

    public List<AlunoModel> listarApenasAtivos() {
        return alunoRepository.findByAtivoTrue();
    }

    public AlunoModel buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado com o ID: " + id));
    }

    public List<AlunoModel> buscarPorResponsavel(Long idResponsavel) {
        return alunoRepository.findByResponsavelIdPessoa(idResponsavel);
    }

    // Método utilitário: Calcula a idade atual do aluno em anos
    public int calcularIdade(AlunoModel aluno) {
        if (aluno.getDataDeNascimento() == null) {
            return 0;
        }
        return Period.between(aluno.getDataDeNascimento(), LocalDate.now()).getYears();
    }

    // Alternar o status de frequência do aluno (Ativo / Inativo)
    public void alterarStatusAtivo(Long idAluno, boolean status) {
        AlunoModel aluno = buscarPorId(idAluno);
        aluno.setAtivo(status);
        alunoRepository.save(aluno);
    }

    public void remover(Long id) {
        alunoRepository.deleteById(id);
    }
}