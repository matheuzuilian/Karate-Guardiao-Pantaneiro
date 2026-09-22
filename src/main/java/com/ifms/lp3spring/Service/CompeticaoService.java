package com.ifms.lp3spring.Service;

import com.ifms.lp3spring.model.CompeticaoModel;
import com.ifms.lp3spring.Repository.CompeticaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompeticaoService {

    @Autowired
    private CompeticaoRepository competicaoRepository;

    public CompeticaoModel salvar(CompeticaoModel competicao) {
        return competicaoRepository.save(competicao);
    }

    public List<CompeticaoModel> buscarPorAluno(Long idAluno) {
        return competicaoRepository.findByAluno_IdPessoaOrderByDataCompeticaoDesc(idAluno);
    }

    public void deletar(Long idCompeticao) {
        competicaoRepository.deleteById(idCompeticao);
    }
}